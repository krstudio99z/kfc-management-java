import kfc.service.PhienQuanLy;

public class TestPhienQuanLy {
    public static void main(String[] args) {
        PhienQuanLy.dangXuat();
        boolean blocked = false;
        try { PhienQuanLy.yeuCauDangNhap(); }
        catch (SecurityException ok) { blocked = true; }
        if (!blocked) throw new AssertionError("Chua dang nhap phai bi chan");
        PhienQuanLy.dangNhap("QuanLyDemo");
        PhienQuanLy.yeuCauDangNhap();
        if (!PhienQuanLy.daDangNhap()) throw new AssertionError("Sai trang thai dang nhap");
        PhienQuanLy.dangXuat();
        if (PhienQuanLy.daDangNhap()) throw new AssertionError("Dang xuat khong thanh cong");
        System.out.println("PASS: Quan ly phien dang nhap");
    }
}
