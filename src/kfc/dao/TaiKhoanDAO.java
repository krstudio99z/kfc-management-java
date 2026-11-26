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

/** Xác thực tài khoản hoạt động, chỉ chấp nhận mật khẩu PBKDF2. */
public class TaiKhoanDAO {
    private static final int VONG = 600000;
    private String vaiTro;
    public String getVaiTro(){return vaiTro;}
    public static String bamMatKhau(char[] mk) throws Exception { if(mk.length < 10) throw new IllegalArgumentException("Mật khẩu tối thiểu 10 ký tự"); return new TaiKhoanDAO().taoHash(mk); }
    private static final java.util.Map<String, long[]> FAIL = new java.util.HashMap<>();
    public static synchronized void kiemTraKhoa(String ten) { long[] a=FAIL.get(ten); if(a!=null && a[1]>System.currentTimeMillis()) throw new SecurityException("Tạm khóa 60 giây"); }
    public static synchronized void ghiKetQua(String ten, boolean ok) { if(ok){FAIL.remove(ten);return;} long[] a=FAIL.computeIfAbsent(ten,k->new long[2]); if(a[1]>0 && a[1]<=System.currentTimeMillis()){a[0]=0;a[1]=0;} if(++a[0]>=5)a[1]=System.currentTimeMillis()+60000; }

    public boolean dangNhap(String ten, char[] matKhau) throws Exception {
        if (ten == null || ten.trim().isEmpty() || matKhau == null || matKhau.length == 0)
            return false;
        try (Connection c = database.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT mk,VaiTro FROM `user` WHERE hoten=? AND HoatDong=1 LIMIT 1")) {
            p.setString(1, ten.trim());
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) return false;
                String stored = r.getString(1);
                if (stored == null) return false;
                vaiTro = r.getString(2);
                if (stored.startsWith("pbkdf2$")) return kiemTraHash(matKhau, stored);
                return false; // Không chấp nhận mật khẩu plaintext. Reset qua quản trị. 
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
