-- Chay MOT LAN sau khi import user.sql goc.
USE ql_kfc;
-- Cho phep luu chuoi bam PBKDF2. Mat khau cu se duoc tu dong chuyen thanh ban bam khi dang nhap thanh cong.
ALTER TABLE `user` MODIFY COLUMN `mk` VARCHAR(255) NOT NULL;
ALTER TABLE `user` ADD UNIQUE KEY uq_user_hoten (hoten);
