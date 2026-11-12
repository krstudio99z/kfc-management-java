package kfc.dao;

import kfc.database;
import kfc.service.KhoService;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KhoDAO {
    public static class NguyenLieu {
        public final String ma, ten, donVi;
        public final BigDecimal ton;
        public NguyenLieu(String ma, String ten, String donVi, BigDecimal ton) {
            this.ma = ma; this.ten = ten; this.donVi = donVi; this.ton = ton;
        }
    }
    public static class CongThuc {
        public final String maNL, tenNL, donVi;
        public final BigDecimal dinhLuong;
        public CongThuc(String maNL, String tenNL, String donVi, BigDecimal dinhLuong) {
            this.maNL = maNL; this.tenNL = tenNL; this.donVi = donVi; this.dinhLuong = dinhLuong;
        }
    }

    public List<NguyenLieu> danhSachKho() throws SQLException {
        List<NguyenLieu> list = new ArrayList<>();
        try (Connection c = database.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT MaNL, TenNL, DonVi, SoLuongTon FROM ql_kho ORDER BY MaNL");
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                list.add(new NguyenLieu(r.getString(1), r.getString(2), r.getString(3), r.getBigDecimal(4)));
            }
        }
        return list;
    }

    public List<CongThuc> congThuc(String maMon) throws SQLException {
        List<CongThuc> list = new ArrayList<>();
        String sql = "SELECT c.MaNL, k.TenNL, k.DonVi, c.DinhLuong "
                + "FROM cong_thuc_mon c JOIN ql_kho k ON c.MaNL=k.MaNL "
                + "WHERE c.MaMon=? ORDER BY c.MaNL";
        try (Connection con = database.getConnection();
             PreparedStatement p = con.prepareStatement(sql)) {
            p.setString(1, maMon);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) list.add(new CongThuc(r.getString(1), r.getString(2),
                        r.getString(3), r.getBigDecimal(4)));
            }
        }
        return list;
    }

    public void them(NguyenLieu nl) throws SQLException {
        kiemTra(nl);
        KhoService.giaoDich(c -> {
            try (PreparedStatement p = c.prepareStatement(
                    "INSERT INTO ql_kho(MaNL,TenNL,DonVi,SoLuongTon) VALUES(?,?,?,?)")) {
                dien(p, nl);
                p.executeUpdate();
            }
        });
    }

    public void sua(NguyenLieu nl) throws SQLException {
        kiemTra(nl);
        KhoService.giaoDich(c -> {
            try (PreparedStatement p = c.prepareStatement(
                    "UPDATE ql_kho SET TenNL=?,DonVi=?,SoLuongTon=? WHERE MaNL=?")) {
                p.setString(1, nl.ten); p.setString(2, nl.donVi);
                p.setBigDecimal(3, nl.ton); p.setString(4, nl.ma);
                if (p.executeUpdate() == 0) throw new SQLException("Không tìm thấy mã nguyên liệu");
            }
        });
    }

    /** Cong them so luong vao kho theo huong atomic, tranh ghi de ton kho hien tai. */
    public void nhapThem(String maNL, BigDecimal soLuong) throws SQLException {
        if (maNL == null || maNL.trim().isEmpty() || soLuong == null || soLuong.signum() <= 0
                || soLuong.scale() > 3) throw new SQLException("Số lượng nhập phải > 0, tối đa 3 chữ số thập phân");
        KhoService.giaoDich(c -> {
            try (PreparedStatement p = c.prepareStatement(
                    "UPDATE ql_kho SET SoLuongTon=SoLuongTon+? WHERE MaNL=?")) {
                p.setBigDecimal(1, soLuong); p.setString(2, maNL);
                if (p.executeUpdate() != 1) throw new SQLException("Không tìm thấy nguyên liệu: " + maNL);
            }
        });
    }

    public void xoa(String maNL) throws SQLException {
        KhoService.giaoDich(c -> {
            try (PreparedStatement p = c.prepareStatement("DELETE FROM ql_kho WHERE MaNL=?")) {
                p.setString(1, maNL);
                if (p.executeUpdate() == 0) throw new SQLException("Không tìm thấy nguyên liệu");
            }
        });
    }

    public void luuCongThuc(String maMon, String maNL, BigDecimal dinhLuong) throws SQLException {
        if (maMon == null || maNL == null || dinhLuong == null || dinhLuong.signum() <= 0
                || dinhLuong.scale() > 3)
            throw new SQLException("Định lượng phải lớn hơn 0");
        KhoService.giaoDich(c -> {
            try (PreparedStatement p = c.prepareStatement(
                    "INSERT INTO cong_thuc_mon(MaMon,MaNL,DinhLuong) VALUES(?,?,?) "
                    + "ON DUPLICATE KEY UPDATE DinhLuong=VALUES(DinhLuong)")) {
                p.setString(1, maMon); p.setString(2, maNL); p.setBigDecimal(3, dinhLuong);
                p.executeUpdate();
            }
        });
    }

    public void xoaCongThuc(String maMon, String maNL) throws SQLException {
        KhoService.giaoDich(c -> {
            try (PreparedStatement p = c.prepareStatement(
                    "DELETE FROM cong_thuc_mon WHERE MaMon=? AND MaNL=?")) {
                p.setString(1, maMon); p.setString(2, maNL);
                p.executeUpdate();
            }
        });
    }

    private void kiemTra(NguyenLieu nl) throws SQLException {
        if (nl.ma.isEmpty() || nl.ten.isEmpty() || nl.donVi.isEmpty() || nl.ton.signum() < 0
                || nl.ton.scale() > 3)
            throw new SQLException("Mã, tên, đơn vị không được trống; tồn kho phải >= 0");
    }
    private void dien(PreparedStatement p, NguyenLieu nl) throws SQLException {
        p.setString(1, nl.ma); p.setString(2, nl.ten);
        p.setString(3, nl.donVi); p.setBigDecimal(4, nl.ton);
    }
}
