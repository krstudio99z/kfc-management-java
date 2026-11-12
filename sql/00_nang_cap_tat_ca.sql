-- TONG HOP 3 SCRIPT NANG CAP KFC. CHI CHAY MOT LAN.
-- CANH BAO: sao luu ql_kfc truoc khi import.
-- Neu da import rieng 3 file tren thi KHONG dung file nay.


-- =================== nang_cap_dang_nhap.sql ===================

-- Chay MOT LAN sau khi import user.sql goc.
USE ql_kfc;
-- Cho phep luu chuoi bam PBKDF2. Mat khau cu se duoc tu dong chuyen thanh ban bam khi dang nhap thanh cong.
ALTER TABLE `user` MODIFY COLUMN `mk` VARCHAR(255) NOT NULL;
ALTER TABLE `user` ADD UNIQUE KEY uq_user_hoten (hoten);


-- =================== ql_kho.sql ===================

-- Chay MOT LAN sau khi da import ban goc monan.sql. Sao luu DB truoc khi nang cap.
-- Cong thuc va kho ben duoi CHI LA DU LIEU MINH HOA, KHONG PHAI DINH LUONG KFC THUC TE.
USE ql_kfc;

ALTER TABLE monan
  ADD COLUMN ChoPhepBan TINYINT(1) NOT NULL DEFAULT 1 AFTER TrangThai;
-- Giu y nguyen trang thai ngung ban do quan ly dat truoc khi nang cap.
UPDATE monan SET ChoPhepBan = TrangThai;

CREATE TABLE IF NOT EXISTS ql_kho (
  MaNL VARCHAR(20) NOT NULL,
  TenNL VARCHAR(100) NOT NULL,
  DonVi VARCHAR(20) NOT NULL,
  SoLuongTon DECIMAL(14,3) NOT NULL DEFAULT 0,
  PRIMARY KEY (MaNL),
  CONSTRAINT ck_kho_soluong CHECK (SoLuongTon >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS cong_thuc_mon (
  MaMon VARCHAR(20) NOT NULL,
  MaNL VARCHAR(20) NOT NULL,
  DinhLuong DECIMAL(14,3) NOT NULL,
  PRIMARY KEY (MaMon, MaNL),
  KEY idx_ct_nl (MaNL),
  CONSTRAINT fk_ct_mon FOREIGN KEY (MaMon) REFERENCES monan (MaMon),
  CONSTRAINT fk_ct_nl FOREIGN KEY (MaNL) REFERENCES ql_kho (MaNL),
  CONSTRAINT ck_ct_dinh_luong CHECK (DinhLuong > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- DEMO: ton kho ban dau (co the sua trong man hinh Quan ly kho).
INSERT IGNORE INTO ql_kho (MaNL, TenNL, DonVi, SoLuongTon) VALUES
('NL001','Ga mieng','mieng',100),
('NL002','Bot chien','kg',15),
('NL003','Dau chien','lit',25),
('NL004','Banh burger','cai',60),
('NL005','Nhan tom','kg',12),
('NL006','Khoai tay','kg',30),
('NL007','Pepsi','lon',100),
('NL008','Kem','kg',15),
('NL009','Sot BBQ','kg',5),
('NL010','Rau an kem','kg',6);

-- DEMO: dinh luong cho 1 phan mon (phai sua lai theo cong thuc thuc te).
INSERT IGNORE INTO cong_thuc_mon (MaMon, MaNL, DinhLuong) VALUES
('M001','NL001',1),('M001','NL002',0.080),('M001','NL003',0.030),
('M002','NL001',1),('M002','NL002',0.090),('M002','NL003',0.035),
('M003','NL001',1),('M003','NL004',1),('M003','NL010',0.050),
('M004','NL005',0.150),('M004','NL004',1),('M004','NL010',0.050),
('M005','NL006',0.200),('M005','NL003',0.025),
('M006','NL007',1),
('M007','NL001',2),('M007','NL002',0.160),('M007','NL003',0.060),('M007','NL007',1),
('M008','NL001',4),('M008','NL002',0.320),('M008','NL003',0.120),('M008','NL006',0.400),('M008','NL007',2),
('M009','NL008',0.150),
('M010','NL001',1),('M010','NL002',0.080),('M010','NL003',0.030),('M010','NL009',0.050);

-- Dong bo trang thai: 1 = quan ly cho ban va co du nguyen lieu cho IT NHAT 1 phan.
-- Mon chua co cong thuc se khong cho dat de tranh tru kho sai.
UPDATE monan AS m
LEFT JOIN (
 SELECT ct.MaMon, COUNT(*) AS SoNguyenLieu,
        SUM(CASE WHEN k.SoLuongTon < ct.DinhLuong THEN 1 ELSE 0 END) AS SoThieu
 FROM cong_thuc_mon AS ct
 JOIN ql_kho AS k ON k.MaNL = ct.MaNL
 GROUP BY ct.MaMon
) AS sl ON sl.MaMon = m.MaMon
SET m.TrangThai = CASE
  WHEN m.ChoPhepBan = 1 AND COALESCE(sl.SoNguyenLieu,0) > 0
       AND COALESCE(sl.SoThieu,0) = 0 THEN 1 ELSE 0 END;


-- =================== hoa_don.sql ===================

-- Chay sau khi import monan.sql. Neu da co hoa_don, kiem tra cau truc bang co trung khop.
USE ql_kfc;

CREATE TABLE IF NOT EXISTS hoa_don (
 MaHD INT NOT NULL AUTO_INCREMENT,
 NgayLap DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
 TongTien DECIMAL(14,2) NOT NULL DEFAULT 0,
 PRIMARY KEY (MaHD),
 INDEX idx_hoadon_ngaylap (NgayLap),
 CONSTRAINT ck_hd_tongtien CHECK (TongTien >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Giu nguyen luoc do chi_tiet_hoa_don.sql da gui: ThanhTien la cot tinh tu dong.
CREATE TABLE IF NOT EXISTS chi_tiet_hoa_don (
 MaHD INT NOT NULL,
 MaMon VARCHAR(20) NOT NULL,
 SoLuong INT NOT NULL,
 DonGia DECIMAL(12,2) NOT NULL,
 ThanhTien DECIMAL(14,2) GENERATED ALWAYS AS (SoLuong * DonGia) STORED,
 PRIMARY KEY (MaHD, MaMon),
 KEY idx_cthd_mon (MaMon),
 CONSTRAINT fk_cthd_hd FOREIGN KEY (MaHD) REFERENCES hoa_don (MaHD) ON DELETE CASCADE,
 CONSTRAINT fk_cthd_mon FOREIGN KEY (MaMon) REFERENCES monan (MaMon),
 CONSTRAINT ck_cthd_soluong CHECK (SoLuong > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Luu ten mon tai thoi diem mua, de in lai khong bi doi ten khi quan ly sua ten mon.
-- Lenh sau can chay 1 lan; tuong thich voi chi_tiet_hoa_don.sql goc.
ALTER TABLE chi_tiet_hoa_don ADD COLUMN IF NOT EXISTS TenMon VARCHAR(100) NULL AFTER MaMon;
UPDATE chi_tiet_hoa_don c JOIN monan m ON c.MaMon=m.MaMon
SET c.TenMon=m.TenMon WHERE c.TenMon IS NULL;

-- Tong hop theo tung ngay, khong can tao bang sao chep hoa don.
CREATE OR REPLACE VIEW v_tong_hop_hoa_don_ngay AS
 SELECT DATE(NgayLap) AS Ngay,
        COUNT(*) AS SoHoaDon,
        COALESCE(SUM(TongTien),0) AS TongDoanhThu
 FROM hoa_don
 GROUP BY DATE(NgayLap);

CREATE OR REPLACE VIEW v_hoa_don_hom_nay AS
 SELECT MaHD, NgayLap, TongTien
 FROM hoa_don
 WHERE NgayLap >= CURRENT_DATE() AND NgayLap < CURRENT_DATE() + INTERVAL 1 DAY;

