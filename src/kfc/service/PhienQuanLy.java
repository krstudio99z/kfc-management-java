package kfc.service;

/** Trang thai dang nhap cua chuong trinh desktop dang mo. */
public final class PhienQuanLy {
    private static boolean daDangNhap = false;
    private static String taiKhoan = "";
    private PhienQuanLy() {}
    public static synchronized void dangNhap(String ten) { daDangNhap = true; taiKhoan = ten; }
    public static synchronized void dangXuat() { daDangNhap = false; taiKhoan = ""; }
    public static synchronized boolean daDangNhap() { return daDangNhap; }
    public static synchronized String taiKhoan() { return taiKhoan; }
    public static void yeuCauDangNhap() {
        if (!daDangNhap()) throw new SecurityException("Chỉ quản lý đã đăng nhập được sử dụng màn hình này");
    }
}
