package kfc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database {
    // Thông tin kết nối MySQL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/QL_KFC"; // Tên CSDL là QL_KFC
    private static final String USER = "root";
    private static final String PASS = ""; // Đổi pass cho phù hợp

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Hàm trả về Connection (Dùng khi cần dùng thư viện bên thứ 3)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
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
            Connection con = getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                stmt.setObject(i + 1, args[i]);
            }
            return stmt.executeQuery();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}