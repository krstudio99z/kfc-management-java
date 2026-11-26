-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 26, 2026 at 07:54 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ql_kfc_nangcap`
--

-- --------------------------------------------------------

--
-- Table structure for table `chi_tiet_hoa_don`
--

CREATE TABLE `chi_tiet_hoa_don` (
  `MaHD` int(11) NOT NULL,
  `MaMon` varchar(20) NOT NULL,
  `TenMon` varchar(100) DEFAULT NULL,
  `SoLuong` int(11) NOT NULL,
  `DonGia` decimal(12,2) NOT NULL,
  `ThanhTien` decimal(14,2) GENERATED ALWAYS AS (`SoLuong` * `DonGia`) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `chi_tiet_hoa_don`
--

INSERT INTO `chi_tiet_hoa_don` (`MaHD`, `MaMon`, `TenMon`, `SoLuong`, `DonGia`) VALUES
(1, 'M005', 'Khoai tây chiên', 1, 25000.00),
(2, 'M002', 'Gà giòn cay', 1, 39000.00),
(2, 'M007', 'Combo gà 2 miếng', 1, 89000.00),
(4, 'M001', 'Gà rán truyền thống', 4, 35000.00),
(4, 'M004', 'Burger tôm', 1, 49000.00),
(4, 'M005', 'Khoai tây chiên', 1, 25000.00);

-- --------------------------------------------------------

--
-- Table structure for table `chi_tiet_nguyen_lieu_hoa_don`
--

CREATE TABLE `chi_tiet_nguyen_lieu_hoa_don` (
  `MaHD` int(11) NOT NULL,
  `MaNL` varchar(20) NOT NULL,
  `SoLuong` decimal(16,3) NOT NULL CHECK (`SoLuong` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chi_tiet_phieu_nhap`
--

CREATE TABLE `chi_tiet_phieu_nhap` (
  `MaPN` int(11) NOT NULL,
  `MaNL` varchar(20) NOT NULL,
  `SoLuong` decimal(16,3) NOT NULL CHECK (`SoLuong` > 0),
  `DonGia` decimal(12,2) NOT NULL CHECK (`DonGia` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `cong_thuc_mon`
--

CREATE TABLE `cong_thuc_mon` (
  `MaMon` varchar(20) NOT NULL,
  `MaNL` varchar(20) NOT NULL,
  `DinhLuong` decimal(14,3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `cong_thuc_mon`
--

INSERT INTO `cong_thuc_mon` (`MaMon`, `MaNL`, `DinhLuong`) VALUES
('M001', 'NL001', 1.000),
('M001', 'NL002', 0.080),
('M001', 'NL003', 0.030),
('M002', 'NL001', 1.000),
('M002', 'NL002', 0.090),
('M002', 'NL003', 0.035),
('M003', 'NL001', 1.000),
('M003', 'NL004', 1.000),
('M003', 'NL010', 0.050),
('M004', 'NL004', 1.000),
('M004', 'NL005', 0.150),
('M004', 'NL010', 0.050),
('M005', 'NL003', 0.025),
('M005', 'NL006', 0.200),
('M006', 'NL007', 1.000),
('M007', 'NL001', 2.000),
('M007', 'NL002', 0.160),
('M007', 'NL003', 0.060),
('M007', 'NL007', 1.000),
('M008', 'NL001', 4.000),
('M008', 'NL002', 0.320),
('M008', 'NL003', 0.120),
('M008', 'NL006', 0.400),
('M008', 'NL007', 2.000),
('M009', 'NL008', 0.150),
('M010', 'NL001', 1.000),
('M010', 'NL002', 0.080),
('M010', 'NL003', 0.030),
('M010', 'NL009', 0.050);

-- --------------------------------------------------------

--
-- Table structure for table `danh_muc`
--

CREATE TABLE `danh_muc` (
  `TenDM` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `danh_muc`
--

INSERT INTO `danh_muc` (`TenDM`) VALUES
('Burger'),
('Combo'),
('Gà rán'),
('Món kèm'),
('Nước uống');

-- --------------------------------------------------------

--
-- Table structure for table `hoa_don`
--

CREATE TABLE `hoa_don` (
  `MaHD` int(11) NOT NULL,
  `NgayLap` datetime NOT NULL DEFAULT current_timestamp(),
  `TongTien` decimal(14,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `hoa_don`
--

INSERT INTO `hoa_don` (`MaHD`, `NgayLap`, `TongTien`) VALUES
(1, '2026-11-08 11:17:16', 25000.00),
(2, '2026-11-12 10:23:20', 128000.00),
(4, '2026-11-19 13:42:32', 214000.00);

-- --------------------------------------------------------

--
-- Table structure for table `khach_hang`
--

CREATE TABLE `khach_hang` (
  `MaKH` int(11) NOT NULL,
  `TenKH` varchar(120) NOT NULL,
  `SDT` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `lich_su_don`
--

CREATE TABLE `lich_su_don` (
  `ID` bigint(20) NOT NULL,
  `MaHD` int(11) NOT NULL,
  `ThoiGian` datetime DEFAULT current_timestamp(),
  `HanhDong` varchar(120) NOT NULL,
  `NguoiThucHien` varchar(80) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `lich_su_kho`
--

CREATE TABLE `lich_su_kho` (
  `ID` bigint(20) NOT NULL,
  `MaNL` varchar(20) NOT NULL,
  `ThoiGian` datetime NOT NULL DEFAULT current_timestamp(),
  `Truoc` decimal(16,3) NOT NULL,
  `Sau` decimal(16,3) NOT NULL,
  `NguoiThucHien` varchar(80) DEFAULT NULL,
  `LyDo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `monan`
--

CREATE TABLE `monan` (
  `MaMon` varchar(20) NOT NULL,
  `TenMon` varchar(100) NOT NULL,
  `Gia` decimal(12,2) NOT NULL,
  `TrangThai` tinyint(1) NOT NULL DEFAULT 1,
  `ChoPhepBan` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `monan`
--

INSERT INTO `monan` (`MaMon`, `TenMon`, `Gia`, `TrangThai`, `ChoPhepBan`) VALUES
('M001', 'Gà rán truyền thống', 35000.00, 1, 1),
('M002', 'Gà giòn cay', 39000.00, 1, 1),
('M003', 'Burger Zinger', 55000.00, 0, 0),
('M004', 'Burger tôm', 49000.00, 1, 1),
('M005', 'Khoai tây chiên', 25000.00, 1, 1),
('M006', 'Pepsi', 15000.00, 1, 1),
('M007', 'Combo gà 2 miếng', 89000.00, 1, 1),
('M008', 'Combo gia đình', 199000.00, 1, 1),
('M009', 'Kem Sundae', 22000.00, 1, 1),
('M010', 'Gà sốt BBQ', 45000.00, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `nha_cung_cap`
--

CREATE TABLE `nha_cung_cap` (
  `MaNCC` varchar(20) NOT NULL,
  `TenNCC` varchar(120) NOT NULL,
  `SDT` varchar(20) DEFAULT NULL,
  `DiaChi` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `phieu_nhap`
--

CREATE TABLE `phieu_nhap` (
  `MaPN` int(11) NOT NULL,
  `MaNCC` varchar(20) NOT NULL,
  `NgayNhap` datetime NOT NULL DEFAULT current_timestamp(),
  `NguoiNhap` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ql_kho`
--

CREATE TABLE `ql_kho` (
  `MaNL` varchar(20) NOT NULL,
  `TenNL` varchar(100) NOT NULL,
  `DonVi` varchar(20) NOT NULL,
  `SoLuongTon` decimal(14,3) NOT NULL DEFAULT 0.000
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `ql_kho`
--

INSERT INTO `ql_kho` (`MaNL`, `TenNL`, `DonVi`, `SoLuongTon`) VALUES
('NL001', 'Ga mieng', 'mieng', 93.000),
('NL002', 'Bot chien', 'kg', 14.430),
('NL003', 'Dau chien', 'lit', 24.735),
('NL004', 'Banh burger', 'cai', 59.000),
('NL005', 'Nhan tom', 'kg', 11.850),
('NL006', 'Khoai tay', 'kg', 29.600),
('NL007', 'Pepsi', 'lon', 99.000),
('NL008', 'Kem', 'kg', 15.000),
('NL009', 'Sot BBQ', 'kg', 5.000),
('NL010', 'Rau an kem', 'kg', 5.950);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `hoten` varchar(50) NOT NULL,
  `mk` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`hoten`, `mk`) VALUES
('Dang', 'pbkdf2$180000$1e0go5Rc3xTK0xarHarisA==$aNdlH9zAGFsCXkkNyv93gyr/lSYRuFu/Y1rJy9XyVcQ='),
('Huy', 'pbkdf2$180000$ptuC40lIqReA9GXWaujs4g==$RReRg+j2ci1znNKyoQIhGUgfBJ6pNGj4ibXB2t3nPB4=');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chi_tiet_hoa_don`
--
ALTER TABLE `chi_tiet_hoa_don`
  ADD PRIMARY KEY (`MaHD`,`MaMon`),
  ADD KEY `MaMon` (`MaMon`);

--
-- Indexes for table `chi_tiet_nguyen_lieu_hoa_don`
--
ALTER TABLE `chi_tiet_nguyen_lieu_hoa_don`
  ADD PRIMARY KEY (`MaHD`,`MaNL`),
  ADD KEY `MaNL` (`MaNL`);

--
-- Indexes for table `chi_tiet_phieu_nhap`
--
ALTER TABLE `chi_tiet_phieu_nhap`
  ADD PRIMARY KEY (`MaPN`,`MaNL`),
  ADD KEY `MaNL` (`MaNL`);

--
-- Indexes for table `cong_thuc_mon`
--
ALTER TABLE `cong_thuc_mon`
  ADD PRIMARY KEY (`MaMon`,`MaNL`),
  ADD KEY `idx_ct_nl` (`MaNL`);

--
-- Indexes for table `danh_muc`
--
ALTER TABLE `danh_muc`
  ADD PRIMARY KEY (`TenDM`);

--
-- Indexes for table `hoa_don`
--
ALTER TABLE `hoa_don`
  ADD PRIMARY KEY (`MaHD`),
  ADD KEY `idx_hoadon_ngaylap` (`NgayLap`);

--
-- Indexes for table `khach_hang`
--
ALTER TABLE `khach_hang`
  ADD PRIMARY KEY (`MaKH`),
  ADD UNIQUE KEY `SDT` (`SDT`);

--
-- Indexes for table `lich_su_don`
--
ALTER TABLE `lich_su_don`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `MaHD` (`MaHD`);

--
-- Indexes for table `lich_su_kho`
--
ALTER TABLE `lich_su_kho`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `MaNL` (`MaNL`,`ThoiGian`);

--
-- Indexes for table `monan`
--
ALTER TABLE `monan`
  ADD PRIMARY KEY (`MaMon`);

--
-- Indexes for table `nha_cung_cap`
--
ALTER TABLE `nha_cung_cap`
  ADD PRIMARY KEY (`MaNCC`);

--
-- Indexes for table `phieu_nhap`
--
ALTER TABLE `phieu_nhap`
  ADD PRIMARY KEY (`MaPN`),
  ADD KEY `MaNCC` (`MaNCC`);

--
-- Indexes for table `ql_kho`
--
ALTER TABLE `ql_kho`
  ADD PRIMARY KEY (`MaNL`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD UNIQUE KEY `uq_user_hoten` (`hoten`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `hoa_don`
--
ALTER TABLE `hoa_don`
  MODIFY `MaHD` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `khach_hang`
--
ALTER TABLE `khach_hang`
  MODIFY `MaKH` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lich_su_don`
--
ALTER TABLE `lich_su_don`
  MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lich_su_kho`
--
ALTER TABLE `lich_su_kho`
  MODIFY `ID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `phieu_nhap`
--
ALTER TABLE `phieu_nhap`
  MODIFY `MaPN` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chi_tiet_hoa_don`
--
ALTER TABLE `chi_tiet_hoa_don`
  ADD CONSTRAINT `chi_tiet_hoa_don_ibfk_1` FOREIGN KEY (`MaHD`) REFERENCES `hoa_don` (`MaHD`) ON DELETE CASCADE,
  ADD CONSTRAINT `chi_tiet_hoa_don_ibfk_2` FOREIGN KEY (`MaMon`) REFERENCES `monan` (`MaMon`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
