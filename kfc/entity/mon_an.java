package kfc.entity;

public class mon_an {
    private String maMon;
    private String tenMon;
    private java.math.BigDecimal gia = java.math.BigDecimal.ZERO;
    private String danhMuc = "", hinhAnh = "";
    public String getDanhMuc(){return danhMuc;}
    public void setDanhMuc(String v){danhMuc=v;}
    public String getHinhAnh(){return hinhAnh;}
    public void setHinhAnh(String v){hinhAnh=v;}
    private boolean trangThai; // True = co du nguyen lieu va duoc ban
    private boolean choPhepBan = true; // Quan ly bat / tat mon

    public boolean isChoPhepBan() { return choPhepBan; }
    public void setChoPhepBan(boolean choPhepBan) { this.choPhepBan = choPhepBan; }

    // Constructors, Getters, Setters
    public mon_an() {}

    public mon_an(String maMon, String tenMon, double gia, boolean trangThai) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.gia = java.math.BigDecimal.valueOf(gia);
        this.trangThai = trangThai;
    }

    public String getMaMon() {
        return maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public java.math.BigDecimal getGia() {
        return gia;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public void setGia(double gia) { setGia(java.math.BigDecimal.valueOf(gia)); }
    public void setGia(java.math.BigDecimal gia) {
        this.gia = gia;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
    
}
