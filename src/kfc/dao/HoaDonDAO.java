package kfc.dao;

import kfc.database;
import kfc.service.KhoService;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HoaDonDAO {
    public static class MatHang {
        public final String maMon;
        public final int soLuong;
        public final BigDecimal giaHienThi;
        public MatHang(String maMon, int soLuong, BigDecimal giaHienThi) {
            this.maMon = maMon; this.soLuong = soLuong; this.giaHienThi = giaHienThi;
        }
    }
    private static class DongDon {
        String ma, ten;
        int soLuong;
        BigDecimal gia;
        DongDon(String ma, String ten, int soLuong, BigDecimal gia) {
            this.ma = ma; this.ten = ten; this.soLuong = soLuong; this.gia = gia;
        }
    }

    /** Tao hoa don, chi tiet va tru kho cung giao dich; tranh ban vuot ton kho. */
    public int datHang(List<MatHang> gio) throws SQLException {
        if (gio == null || gio.isEmpty()) throw new SQLException("Giỏ hàng trống");
        try (Connection c = database.getConnection()) {
            c.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            c.setAutoCommit(false);
            try {
                KhoService.khoaKho(c); // Khoa hang ton kho truoc khi kiem tra.
                List<DongDon> ds = new ArrayList<>();
                Map<String, BigDecimal> canDung = new LinkedHashMap<>();
                BigDecimal tong = BigDecimal.ZERO;
                String sqlMon = "SELECT TenMon,Gia,TrangThai,ChoPhepBan FROM monan WHERE MaMon=? FOR UPDATE";
                String sqlCt = "SELECT MaNL,DinhLuong FROM cong_thuc_mon WHERE MaMon=? ORDER BY MaNL";
                try (PreparedStatement pMon = c.prepareStatement(sqlMon);
                     PreparedStatement pCt = c.prepareStatement(sqlCt)) {
                    for (MatHang h : gio) {
                        if (h.maMon == null || h.soLuong <= 0 || h.giaHienThi == null)
                            throw new SQLException("Dữ liệu món ăn không hợp lệ");
                        pMon.setString(1, h.maMon);
                        String ten;
                        BigDecimal gia;
                        try (ResultSet r = pMon.executeQuery()) {
                            if (!r.next()) throw new SQLException("Mã món không tồn tại: " + h.maMon);
                            ten = r.getString("TenMon");
                            gia = r.getBigDecimal("Gia");
                            if (!r.getBoolean("TrangThai") || !r.getBoolean("ChoPhepBan"))
                                throw new SQLException(ten + " hiện đã hết món hoặc ngừng bán. Hãy làm mới giỏ hàng.");
                        }
                        if (gia.compareTo(h.giaHienThi) != 0)
                            throw new SQLException("Giá " + ten + " đã thay đổi. Hãy làm mới giỏ hàng.");
                        pCt.setString(1, h.maMon);
                        boolean coCongThuc = false;
                        try (ResultSet r = pCt.executeQuery()) {
                            while (r.next()) {
                                coCongThuc = true;
                                BigDecimal can = r.getBigDecimal("DinhLuong")
                                        .multiply(BigDecimal.valueOf(h.soLuong));
                                canDung.merge(r.getString("MaNL"), can, BigDecimal::add);
                            }
                        }
                        if (!coCongThuc) throw new SQLException(ten + " chưa có công thức nguyên liệu");
                        tong = tong.add(gia.multiply(BigDecimal.valueOf(h.soLuong)));
                        ds.add(new DongDon(h.maMon, ten, h.soLuong, gia));
                    }
                }
                // Kiem tra TONG nhu cau cua tat ca cac mon (khong kiem tra rieng tung mon).
                try (PreparedStatement p = c.prepareStatement("SELECT SoLuongTon,TenNL FROM ql_kho WHERE MaNL=?")) {
                    for (Map.Entry<String, BigDecimal> e : canDung.entrySet()) {
                        p.setString(1, e.getKey());
                        try (ResultSet r = p.executeQuery()) {
                            if (!r.next()) throw new SQLException("Thiếu nguyên liệu: " + e.getKey());
                            if (r.getBigDecimal("SoLuongTon").compareTo(e.getValue()) < 0)
                                throw new SQLException("Kho không đủ " + r.getString("TenNL") + " cho đơn này");
                        }
                    }
                }
                try (PreparedStatement p = c.prepareStatement(
                        "UPDATE ql_kho SET SoLuongTon=SoLuongTon-? WHERE MaNL=? AND SoLuongTon>=?")) {
                    for (Map.Entry<String, BigDecimal> e : canDung.entrySet()) {
                        p.setBigDecimal(1, e.getValue());
                        p.setString(2, e.getKey());
                        p.setBigDecimal(3, e.getValue());
                        if (p.executeUpdate() != 1) throw new SQLException("Kho vừa thay đổi, vui lòng thử lại");
                    }
                }
                int maHD;
                try (PreparedStatement p = c.prepareStatement(
                        "INSERT INTO hoa_don(NgayLap,TongTien) VALUES (NOW(),?)", Statement.RETURN_GENERATED_KEYS)) {
                    p.setBigDecimal(1, tong);
                    p.executeUpdate();
                    try (ResultSet r = p.getGeneratedKeys()) {
                        if (!r.next()) throw new SQLException("Không tạo được mã hóa đơn");
                        maHD = r.getInt(1);
                    }
                }
                try (PreparedStatement p = c.prepareStatement(
                        "INSERT INTO chi_tiet_hoa_don(MaHD,MaMon,TenMon,SoLuong,DonGia) VALUES (?,?,?,?,?)")) {
                    for (DongDon h : ds) {
                        p.setInt(1, maHD); p.setString(2, h.ma); p.setString(3, h.ten);
                        p.setInt(4, h.soLuong); p.setBigDecimal(5, h.gia);
                        p.executeUpdate();
                    }
                }
                KhoService.capNhatTrangThai(c);
                c.commit();
                return maHD;
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        }
    }

    /** Tai tu DB, khong lay du lieu tu gio hang (de co the in lai hoa don). */
    public String noiDungHoaDon(int maHD) throws SQLException {
        DecimalFormat fmt = new DecimalFormat("#,##0.##");
        StringBuilder sb = new StringBuilder();
        try (Connection c = database.getConnection()) {
            try (PreparedStatement p = c.prepareStatement(
                    "SELECT MaHD,NgayLap,TongTien FROM hoa_don WHERE MaHD=?")) {
                p.setInt(1, maHD);
                try (ResultSet r = p.executeQuery()) {
                    if (!r.next()) throw new SQLException("Không tìm thấy hóa đơn " + maHD);
                    Timestamp ngay = r.getTimestamp("NgayLap");
                    sb.append("             KFC - HOA DON\n");
                    sb.append("---------------------------------------------\n");
                    sb.append("Ma hoa don: HD").append(String.format("%06d", maHD)).append('\n');
                    sb.append("Thoi gian: ").append(ngay.toLocalDateTime().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append('\n');
                    sb.append("---------------------------------------------\n");
                    sb.append("Mon an  |  So luong  |  Don gia  |  Thanh tien\n");
                }
            }
            try (PreparedStatement p = c.prepareStatement(
                    "SELECT COALESCE(c.TenMon,m.TenMon),c.SoLuong,c.DonGia,c.ThanhTien "
                    + "FROM chi_tiet_hoa_don c JOIN monan m ON c.MaMon=m.MaMon "
                    + "WHERE c.MaHD=? ORDER BY c.MaMon")) {
                p.setInt(1, maHD);
                try (ResultSet r = p.executeQuery()) {
                    while (r.next()) {
                        sb.append(r.getString(1)).append('\n');
                        sb.append("   ").append(r.getInt(2)).append(" x ")
                          .append(fmt.format(r.getBigDecimal(3))).append(" = ")
                          .append(fmt.format(r.getBigDecimal(4))).append(" d\n");
                    }
                }
            }
            try (PreparedStatement p = c.prepareStatement("SELECT TongTien FROM hoa_don WHERE MaHD=?")) {
                p.setInt(1, maHD);
                try (ResultSet r = p.executeQuery()) {
                    r.next();
                    sb.append("---------------------------------------------\n");
                    sb.append("TONG TIEN: ").append(fmt.format(r.getBigDecimal(1))).append(" d\n");
                    sb.append("Cam on quy khach!\n");
                }
            }
        }
        return sb.toString();
    }
}
