KFC - NANG CAP QUAN LY KHO, DANG NHAP, HOA DON
=============================================
Du an: Java Swing + JDBC MySQL/MariaDB (XAMPP), package kfc.
Duoc chinh sua dua tren cac file .java va .sql nguoi dung da cung cap.

1. QUAN TRONG: SAO LUU CSDL TRUOC
--------------------------------
- Sao luu toan bo database ql_kfc tren phpMyAdmin (Export > SQL).
- Neu chua co database: tao database ql_kfc, import monan.sql va user.sql ban goc
  (co ban sao trong thu muc ban_goc). KHONG import lai neu da co du lieu.
- File chi_tiet_hoa_don.sql ban goc tham chieu khoa ngoai den hoa_don. Neu chua
  co bang hoa_don, KHONG import chi_tiet_hoa_don.sql goc truoc; file nang cap
  hoa_don.sql se tu tao dung bang hoa_don + chi_tiet_hoa_don.
- Neu DB da co bang hoa_don khac cot (do khong co file hoa_don.sql goc trong
  cac file da gui), can sao luu/kiem tra cau truc truoc khi chay migration.

2. CAI DAT SQL - CHAY DUNG MOT LAN
---------------------------------
Mở phpMyAdmin > chon ql_kfc > Import > SQL:
Cach A (de nhat): Import sql/00_nang_cap_tat_ca.sql DUY NHAT MOT LAN.
Cach B: Import lan luot 3 file sau DUY NHAT MOT LAN:
   1) sql/nang_cap_dang_nhap.sql
   2) sql/ql_kho.sql
   3) sql/hoa_don.sql
KHONG CHAY CA HAI CACH. KHONG IMPORT LAI CAC SCRIPT DA CHAY.

ql_kho.sql tao:
- ql_kho(MaNL,TenNL,DonVi,SoLuongTon)
- cong_thuc_mon(MaMon,MaNL,DinhLuong)
- them ChoPhepBan vao monan; TrangThai van la 0/1 tren CSDL.
- cong thuc DEMO minh hoa cho M001...M010, KHONG PHAI CONG THUC KFC THUC TE.
  Can mo man hinh ql_kho de sua dinh luong, ten nguyen lieu, don vi va ton kho.
- Mon co ChoPhepBan=0 van ngung ban ke ca khi nhap them nguyen lieu.
- Mon khong co cong thuc tu dong khong cho dat (TrangThai=0).

hoa_don.sql tao:
- hoa_don(MaHD tu tang,NgayLap,TongTien)
- chi_tiet_hoa_don(MaHD,MaMon,SoLuong,DonGia,ThanhTien) theo file goc
  va bo sung TenMon de luu ten mon khi ban, tranh sua ten thi hoa don cu bi doi.
- v_tong_hop_hoa_don_ngay, v_hoa_don_hom_nay (VIEW).

3. THEM FILE JAVA VAO NETBEANS
------------------------------
Copy nguyen thu muc src/kfc vao Source Packages cua du an (dung thu muc/package):
  src/kfc/database.java                      (file goc)
  src/kfc/kt_loi.java                        (file goc)
  src/kfc/entity/mon_an.java                 (da sua)
  src/kfc/dao/truy_cap.java                  (da sua)
  src/kfc/dao/KhoDAO.java                    (moi)
  src/kfc/dao/HoaDonDAO.java                 (moi)
  src/kfc/dao/TaiKhoanDAO.java               (moi)
  src/kfc/service/KhoService.java            (moi)
  src/kfc/service/PhienQuanLy.java           (moi)
  src/kfc/ui/dat_mon.java                    (da sua)
  src/kfc/ui/ql_mon_an.java                  (da sua)
  src/kfc/ui/dang_nhap.java                  (moi)
  src/kfc/ui/menu_quan_ly.java               (moi)
  src/kfc/ui/ql_kho.java                     (moi)
  src/kfc/ui/ql_hoa_don.java                 (moi)
  src/kfc/ui/kfc_app.java                    (moi)

Luu y: dat_mon.java va ql_mon_an.java goc co dau generated code cua NetBeans.
Neu du an cua ban co file .form, mo/sua GUI Builder co the tao lai initComponents()
va ghi de doan da sua. Hay dung SOURCE CODE ban nay, va cap nhat .form neu tiep tuc
thiet ke bang GUI Builder.

4. JDBC MYSQL
-------------
- Bat Apache (neu su dung) va MySQL trong XAMPP.
- NetBeans > Project Properties > Libraries > Add JAR/Folder: mysql-connector-j.
- Kiem tra src/kfc/database.java: localhost:3306/QL_KFC, username root, password "".
  Thay thong tin ket noi neu XAMPP cua ban khac.
- Ma nguon duoc kiem tra BIEN DICH voi javac --release 8.
- Chua ket noi thu duoc MariaDB/XAMPP cua nguoi dung tu moi truong tao file.

5. CHAY CHUONG TRINH
-------------------
Run File: kfc.ui.kfc_app (hoac set Main Class = kfc.ui.kfc_app)
  [KHACH HANG - DAT MON] -> dat_mon
  [QUAN LY - DANG NHAP] -> dang_nhap -> menu_quan_ly
                                 -> Quan ly mon an (ql_mon_an)
                                 -> Quan ly kho + cong thuc (ql_kho)
                                 -> Hoa don/ngay (ql_hoa_don)

Dang nhap dung tai khoan/mat khau dang co trong bang user goc. Khi dang nhap
thanh cong lan dau, mat khau plaintext cu se duoc tu dong chuyen sang PBKDF2.
Nen thay cac mat khau mau yeu bang mat khau manh truoc khi su dung thuc te.

6. CO CHE TU DONG
----------------
- Tinh nhu cau nguyen lieu cho TOAN BO gio hang (ke ca mon dung chung nguyen lieu).
- Kiem tra ton kho, tru kho va luu hoa don + chi tiet cung trong mot transaction.
- Neu khong du: bao loi, KHONG tru kho, KHONG luu hoa don do.
- Sau giao dich: mon khong du lam 1 phan => TrangThai=0 trong monan.
- Sau khi nhap them ton: mon du nguyen lieu, ChoPhepBan=1 => TrangThai=1.
- dat_mon hien "Het mon" hoac "Ngung ban"; khong the cho vao gio mon do.
- Sau khi dat thanh cong: tao MaHD tu dong, hien ID va noi dung; mo dialog in.
  Huy in khong huy don da luu; co the in lai tu ql_hoa_don.
- Trang dat mon tu lam moi sau moi 15 giay va sau khi dat.

7. DOI CHIEU HOA DON / DOANH THU
--------------------------------
-- Hoa don trong ngay (gia su PHP/MySQL timezone cung may):
SELECT * FROM v_hoa_don_hom_nay;
-- Thong ke tung ngay:
SELECT * FROM v_tong_hop_hoa_don_ngay ORDER BY Ngay DESC;
-- Tong doanh thu hom nay:
SELECT COALESCE(SUM(TongTien),0) AS TongHomNay FROM hoa_don
 WHERE NgayLap >= CURDATE() AND NgayLap < CURDATE() + INTERVAL 1 DAY;

MAN HINH ql_hoa_don cho phep nhap ngay yyyy-MM-dd, xem tat ca hoa don trong ngay,
tinh so don va tong tien, xem chi tiet/in lai.

CHU Y: TongTien duoc tinh ngay khi dat hang thanh cong, CHUA co trang thai thanh
toan/hoan tien; neu chua thu tien, tong nay la tong gia tri don, khong phai
so tien thuc te da thu.

8. GOI Y KIEM THU TAI XAMPP
--------------------------
- Dang nhap quan ly > chon Quan ly kho > xem nguyen lieu/cap nhat cong thuc.
- Mua mon co 1 so nguyen lieu dung chung; xem ql_kho da tru dung tong nhu cau.
- Chinh ton nguyen lieu xuong 0: mon dung nguyen lieu do chuyen "Het mon",
  TrangThai=0 (co the kiem tra: SELECT MaMon,TrangThai FROM monan;).
- Dung nut "Nhap them": mon du ton phuc hoi TrangThai=1 neu ChoPhepBan=1.
- M003 ban dau Ngung ban (TrangThai=0 trong file goc), khong tu duoc bat lai.
- Dat mon > in hoa don > mo ql_hoa_don > xem doanh thu > in lai.
- Hoan tat: sao luu lai DB da nang cap.

9. FILE .SQL KHONG PHAI FILE DU LIEU LIVE
----------------------------------------
TrangThai=0/1 duoc chuong trinh UPDATE truc tiep vao BANG monan trong MySQL.
File monan.sql la ban dump tinh, se khong tu doi noi dung sau khi khach dat mon.
Muon luu file SQL moi co trang thai hien tai: phpMyAdmin > Export database.

10. SUA LOI KHI IMPORT hoa_don.sql
--------------------------------
Neu phpMyAdmin bao #1060 Duplicate column name TenMon, file sql/sua_loi_hoa_don.sql
da sua ALTER TABLE ... ADD COLUMN IF NOT EXISTS (MariaDB 10.4).
Neu da import mot phan file 00_nang_cap_tat_ca.sql thi KHONG chay lai ca file
vi cac lenh ALTER TABLE o phan dang nhap/kho co the bao trung cot/index.
Hay import rieng sql/sua_loi_hoa_don.sql de hoan tat phan hoa don.
Neu van loi, can xem dung ma so va noi dung loi (1049/1060/1005/1215/1452...).
