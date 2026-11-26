package kfc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import kfc.service.PhienQuanLy;

public class database {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/kfc_management?characterEncoding=UTF-8&connectionTimeZone=Asia/Ho_Chi_Minh";

    // Không đọc file hoặc nạp JDBC trong static initializer.
    private static Properties loadConfig() throws SQLException {
        Properties cfg = new Properties();
        cfg.setProperty("db.url", DEFAULT_URL);
        cfg.setProperty("db.user", "root");
        cfg.setProperty("db.password", "");
        try {
            String custom = System.getProperty("kfc.config");
            Path path = Paths.get(custom == null ? "config.properties" : custom);
            if (custom == null && !Files.exists(path)) {
                // Tìm bên cạnh JAR hoặc thư mục project (NetBeans/Maven).
                try {
                    Path base = Paths.get(database.class.getProtectionDomain()
                            .getCodeSource().getLocation().toURI());
                    if (!Files.isDirectory(base)) base = base.getParent();
                    for (int i = 0; base != null && i < 3; i++, base = base.getParent()) {
                        Path candidate = base.resolve("config.properties");
                        if (Files.isRegularFile(candidate)) { path = candidate; break; }
                    }
                } catch (Exception ignored) { /* Dùng đường dẫn mặc định. */ }
            }
            if (custom != null || Files.exists(path)) {
                try (InputStream in = Files.newInputStream(path)) { cfg.load(in); }
            }
            return cfg;
        } catch (IOException | RuntimeException ex) {
            throw new SQLException("Không đọc được config.properties: " + ex.getMessage(), ex);
        }
    }

    public static Connection getConnection() throws SQLException {
        Properties cfg = loadConfig();
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException | LinkageError ex) {
            throw new SQLException("Thiếu hoặc lỗi thư viện MySQL Connector/J. Hãy thêm mysql-connector-j vào Libraries hoặc lib.", ex);
        }
        String url = cfg.getProperty("db.url").trim();
        String user = cfg.getProperty("db.user").trim();
        String pass = System.getenv().getOrDefault("KFC_DB_PASSWORD", cfg.getProperty("db.password", ""));
        Connection c = DriverManager.getConnection(url, user, pass);
        try (PreparedStatement p = c.prepareStatement("SET @kfc_actor = ?")) {
            p.setString(1, PhienQuanLy.taiKhoan());
            p.execute();
        } catch (SQLException | RuntimeException ex) {
            try { c.close(); } catch (SQLException closeEx) { ex.addSuppressed(closeEx); }
            throw ex;
        }
        return c;
    }

    public static int update(String sql, Object... args) {
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) stmt.setObject(i + 1, args[i]);
            return stmt.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public static ResultSet query(String sql, Object... args) {
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) stmt.setObject(i + 1, args[i]);
            try (ResultSet rs = stmt.executeQuery()) {
                CachedRowSet cached = RowSetProvider.newFactory().createCachedRowSet();
                cached.populate(rs);
                return cached;
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }
}
