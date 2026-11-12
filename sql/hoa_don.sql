-- Script dung cho MariaDB 10.4 (XAMPP). Chay sau khi import monan.sql.
-- Co the chay lai script nay neu cac bang da duoc tao DUNG cau truc.
-- Neu loi khoa ngoai, kiem tra ENGINE/charset/collation va MaMon/MaHD.
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
