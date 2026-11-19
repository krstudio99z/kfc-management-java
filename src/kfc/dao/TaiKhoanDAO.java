package kfc.dao;

import kfc.database;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Doc bang user(hoten,mk). Tu chuyen mat khau plaintext cu -> PBKDF2 khi dang nhap. */
public class TaiKhoanDAO {
    private static final int VONG = 180000;

    public boolean dangNhap(String ten, char[] matKhau) throws Exception {
        if (ten == null || ten.trim().isEmpty() || matKhau == null || matKhau.length == 0)
            return false;
        try (Connection c = database.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT mk FROM `user` WHERE hoten=? LIMIT 1")) {
            p.setString(1, ten.trim());
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) return false;
                String stored = r.getString(1);
                if (stored == null) return false;
                if (stored.startsWith("pbkdf2$")) return kiemTraHash(matKhau, stored);
                // Ho tro user.sql cu co mk dang plaintext; chuyen sang hash sau khi xac minh.
                byte[] a = new String(matKhau).getBytes(StandardCharsets.UTF_8);
                byte[] b = stored.getBytes(StandardCharsets.UTF_8);
                boolean ok = MessageDigest.isEqual(a, b);
                Arrays.fill(a, (byte) 0);
                if (ok) {
                    String hash = taoHash(matKhau);
                    try (PreparedStatement u = c.prepareStatement(
                            "UPDATE `user` SET mk=? WHERE hoten=? AND mk=?")) {
                        u.setString(1, hash); u.setString(2, ten.trim()); u.setString(3, stored);
                        u.executeUpdate();
                    }
                }
                return ok;
            }
        }
    }

    private String taoHash(char[] matKhau) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf(matKhau, salt, VONG);
        return "pbkdf2$" + VONG + "$" + Base64.getEncoder().encodeToString(salt)
                + "$" + Base64.getEncoder().encodeToString(hash);
    }

    private boolean kiemTraHash(char[] mk, String saved) throws Exception {
        try {
            String[] parts = saved.split("\\$");
            if (parts.length != 4 || !parts[0].equals("pbkdf2")) return false;
            int rounds = Integer.parseInt(parts[1]);
            if (rounds < 100000 || rounds > 1000000) return false;
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            byte[] result = pbkdf(mk, salt, rounds);
            boolean ok = MessageDigest.isEqual(expected, result);
            Arrays.fill(result, (byte) 0);
            return ok;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private byte[] pbkdf(char[] mk, byte[] salt, int rounds) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(mk, salt, rounds, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }
}
