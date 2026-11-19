-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 12, 2026 at 03:42 AM
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
-- Database: `ql_kfc`
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
) ;

--
-- Dumping data for table `chi_tiet_hoa_don`
--

INSERT INTO `chi_tiet_hoa_don` (`MaHD`, `MaMon`, `TenMon`, `SoLuong`, `DonGia`) VALUES
(1, 'M005', 'Khoai tây chiên', 1, 25000.00);

-- --------------------------------------------------------

--
-- Table structure for table `cong_thuc_mon`
--

CREATE TABLE `cong_thuc_mon` (
  `MaMon` varchar(20) NOT NULL,
  `MaNL` varchar(20) NOT NULL,
  `DinhLuong` decimal(14,3) NOT NULL
) ;

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
-- Table structure for table `hoa_don`
--

CREATE TABLE `hoa_don` (
  `MaHD` int(11) NOT NULL,
  `NgayLap` datetime NOT NULL DEFAULT current_timestamp(),
  `TongTien` decimal(14,2) NOT NULL DEFAULT 0.00
) ;

--
-- Dumping data for table `hoa_don`
--

INSERT INTO `hoa_don` (`MaHD`, `NgayLap`, `TongTien`) VALUES
(1, '2026-11-08 11:17:16', 25000.00);

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
) ;

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
-- Table structure for table `ql_kho`
--

CREATE TABLE `ql_kho` (
  `MaNL` varchar(20) NOT NULL,
  `TenNL` varchar(100) NOT NULL,
  `DonVi` varchar(20) NOT NULL,
  `SoLuongTon` decimal(14,3) NOT NULL DEFAULT 0.000
) ;

--
-- Dumping data for table `ql_kho`
--

INSERT INTO `ql_kho` (`MaNL`, `TenNL`, `DonVi`, `SoLuongTon`) VALUES
('NL001', 'Ga mieng', 'mieng', 100.000),
('NL002', 'Bot chien', 'kg', 15.000),
('NL003', 'Dau chien', 'lit', 24.975),
('NL004', 'Banh burger', 'cai', 60.000),
('NL005', 'Nhan tom', 'kg', 12.000),
('NL006', 'Khoai tay', 'kg', 29.800),
('NL007', 'Pepsi', 'lon', 100.000),
('NL008', 'Kem', 'kg', 15.000),
('NL009', 'Sot BBQ', 'kg', 5.000),
('NL010', 'Rau an kem', 'kg', 6.000);

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

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_hoa_don_hom_nay`
-- (See below for the actual view)
--
CREATE TABLE `v_hoa_don_hom_nay` (
`MaHD` int(11)
,`NgayLap` datetime
,`TongTien` decimal(14,2)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `v_tong_hop_hoa_don_ngay`
-- (See below for the actual view)
--
CREATE TABLE `v_tong_hop_hoa_don_ngay` (
`Ngay` date
,`SoHoaDon` bigint(21)
,`TongDoanhThu` decimal(36,2)
);

-- --------------------------------------------------------

--
-- Structure for view `v_hoa_don_hom_nay`
--
DROP TABLE IF EXISTS `v_hoa_don_hom_nay`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_hoa_don_hom_nay`  AS SELECT `hoa_don`.`MaHD` AS `MaHD`, `hoa_don`.`NgayLap` AS `NgayLap`, `hoa_don`.`TongTien` AS `TongTien` FROM `hoa_don` WHERE `hoa_don`.`NgayLap` >= curdate() AND `hoa_don`.`NgayLap` < curdate() + interval 1 day ;

-- --------------------------------------------------------

--
-- Structure for view `v_tong_hop_hoa_don_ngay`
--
DROP TABLE IF EXISTS `v_tong_hop_hoa_don_ngay`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_tong_hop_hoa_don_ngay`  AS SELECT cast(`hoa_don`.`NgayLap` as date) AS `Ngay`, count(0) AS `SoHoaDon`, coalesce(sum(`hoa_don`.`TongTien`),0) AS `TongDoanhThu` FROM `hoa_don` GROUP BY cast(`hoa_don`.`NgayLap` as date) ;

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
-- Indexes for table `cong_thuc_mon`
--
ALTER TABLE `cong_thuc_mon`
  ADD PRIMARY KEY (`MaMon`,`MaNL`),
  ADD KEY `idx_ct_nl` (`MaNL`);

--
-- Indexes for table `hoa_don`
--
ALTER TABLE `hoa_don`
  ADD PRIMARY KEY (`MaHD`),
  ADD KEY `idx_hoadon_ngaylap` (`NgayLap`);

--
-- Indexes for table `monan`
--
ALTER TABLE `monan`
  ADD PRIMARY KEY (`MaMon`);

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
  MODIFY `MaHD` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chi_tiet_hoa_don`
--
ALTER TABLE `chi_tiet_hoa_don`
  ADD CONSTRAINT `chi_tiet_hoa_don_ibfk_1` FOREIGN KEY (`MaHD`) REFERENCES `hoa_don` (`MaHD`) ON DELETE CASCADE,
  ADD CONSTRAINT `chi_tiet_hoa_don_ibfk_2` FOREIGN KEY (`MaMon`) REFERENCES `monan` (`MaMon`);

--
-- Constraints for table `cong_thuc_mon`
--
ALTER TABLE `cong_thuc_mon`
  ADD CONSTRAINT `fk_ct_mon` FOREIGN KEY (`MaMon`) REFERENCES `monan` (`MaMon`),
  ADD CONSTRAINT `fk_ct_nl` FOREIGN KEY (`MaNL`) REFERENCES `ql_kho` (`MaNL`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
