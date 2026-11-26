package kfc.service;

/** Trang thai dang nhap cua chuong trinh desktop dang mo. */
public final class PhienQuanLy {
    private static boolean daDangNhap = false;
    private static String taiKhoan = "";
    private static String vaiTro = "";
    public static synchronized boolean coVaiTro(String... roles) { return daDangNhap && java.util.Arrays.asList(roles).contains(vaiTro); }
    public static void yeuCauVaiTro(String... roles) { if (!coVaiTro(roles)) throw new SecurityException("Không có quyền thực hiện"); }
    public static synchronized String vaiTro(){return vaiTro;}
    private PhienQuanLy() {}
    public static synchronized void dangNhap(String ten, String role) { daDangNhap = true; taiKhoan = ten; vaiTro = role; }
    public static synchronized void dangXuat() { daDangNhap = false; taiKhoan = ""; vaiTro = ""; }
    public static synchronized boolean daDangNhap() { return daDangNhap; }
    public static synchronized String taiKhoan() { return taiKhoan; }
    public static void yeuCauDangNhap() {
        if (!daDangNhap()) throw new SecurityException("Chỉ quản lý đã đăng nhập được sử dụng màn hình này");
    }
}
