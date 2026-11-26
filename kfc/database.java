package kfc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database {
    // Thông tin kết nối MySQL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final java.util.Properties CFG = new java.util.Properties();
    static { try (java.io.InputStream in = java.nio.file.Files.newInputStream(java.nio.file.Paths.get("config.properties"))) { CFG.load(in); } catch (java.io.IOException e) { throw new ExceptionInInitializerError("Thiếu config.properties: " + e.getMessage()); } }
    private static final String URL = CFG.getProperty("db.url", "jdbc:mysql://localhost:3306/kfc_management?connectionTimeZone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8");
    private static final String USER = CFG.getProperty("db.user");
    private static final String PASS = System.getenv().getOrDefault("KFC_DB_PASSWORD", CFG.getProperty("db.password", ""));

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Hàm trả về Connection (Dùng khi cần dùng thư viện bên thứ 3)
    public static Connection getConnection() throws SQLException {
        Connection c = DriverManager.getConnection(URL, USER, PASS);
        try (PreparedStatement p = c.prepareStatement("SET @kfc_actor = ?")) { p.setString(1, kfc.service.PhienQuanLy.taiKhoan()); p.execute(); } catch (SQLException e) { c.close(); throw e; }
        return c;
    }

    // Hàm thực thi các câu lệnh INSERT, UPDATE, DELETE
    public static int update(String sql, Object... args) {
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Hàm thực thi câu lệnh SELECT trả về ResultSet
    public static ResultSet query(String sql, Object... args) {
        try {
            try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
                for (int i = 0; i < args.length; i++) stmt.setObject(i + 1, args[i]);
                try (ResultSet rs = stmt.executeQuery()) { javax.sql.rowset.CachedRowSet cached = javax.sql.rowset.RowSetProvider.newFactory().createCachedRowSet(); cached.populate(rs); return cached; }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}