package kfc.service;

import kfc.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Dong bo ton kho -> trang thai mon an. */
public final class KhoService {
    private KhoService() {}

    @FunctionalInterface
    public interface HanhDongSql {
        void run(Connection con) throws SQLException;
    }

    /** Lay khoa tat ca nguyen lieu de cac giao dich ban hang/nhap kho khong giam ton chong cheo. */
    public static void khoaKho(Connection con) throws SQLException {
        try (PreparedStatement p = con.prepareStatement(
                "SELECT MaNL FROM ql_kho ORDER BY MaNL FOR UPDATE");
             ResultSet rs = p.executeQuery()) {
            while (rs.next()) { /* Giu row lock den COMMIT / ROLLBACK. */ }
        }
    }

    /** Goi trong CUNG giao dich voi thay doi kho, cong thuc hoac mon an. */
    public static void capNhatTrangThai(Connection con) throws SQLException {
        String sql = "UPDATE monan m LEFT JOIN ("
                + " SELECT ct.MaMon, COUNT(*) SoNguyenLieu,"
                + " SUM(CASE WHEN k.SoLuongTon < ct.DinhLuong THEN 1 ELSE 0 END) SoThieu"
                + " FROM cong_thuc_mon ct JOIN ql_kho k ON k.MaNL = ct.MaNL"
                + " GROUP BY ct.MaMon) sl ON m.MaMon = sl.MaMon"
                + " SET m.TrangThai = CASE WHEN m.ChoPhepBan = 1"
                + " AND COALESCE(sl.SoNguyenLieu,0) > 0"
                + " AND COALESCE(sl.SoThieu,0) = 0 THEN 1 ELSE 0 END";
        try (PreparedStatement p = con.prepareStatement(sql)) {
            p.executeUpdate();
        }
    }

    public static void giaoDich(HanhDongSql hanhDong) throws SQLException {
        try (Connection con = database.getConnection()) {
            con.setAutoCommit(false);
            try {
                khoaKho(con);
                hanhDong.run(con);
                capNhatTrangThai(con);
                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            }
        }
    }

    /** Refresh tu dong (ke ca neu ton kho duoc sua ngoai ung dung). */
    public static void dongBo() throws SQLException {
        giaoDich(con -> {});
    }
}
