package kfc.entity;

public class mon_an {
    private String maMon;
    private String tenMon;
    private double gia;
    private boolean trangThai; // True = co du nguyen lieu va duoc ban
    private boolean choPhepBan = true; // Quan ly bat / tat mon

    public boolean isChoPhepBan() { return choPhepBan; }
    public void setChoPhepBan(boolean choPhepBan) { this.choPhepBan = choPhepBan; }

    // Constructors, Getters, Setters
    public mon_an() {}

    public mon_an(String maMon, String tenMon, double gia, boolean trangThai) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.gia = gia;
        this.trangThai = trangThai;
    }

    public String getMaMon() {
        return maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public double getGia() {
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

    public void setGia(double gia) {
        this.gia = gia;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
    
}
