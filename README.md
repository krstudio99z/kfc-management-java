# Ứng dụng quản lý bán hàng KFC

Ứng dụng desktop Java Swing quản lý đặt món, thu ngân, bếp, nhân viên, kho và báo cáo doanh thu, sử dụng MySQL. Mã nguồn nghiệp vụ nằm trong `src/kfc/`; SQL cùng tài liệu đồ án được gom trong `project-materials/`.

## Tính năng

- Khách hàng chọn món, tạo đơn chờ thanh toán và nhận hóa đơn từ dữ liệu đã lưu.
- Thu ngân xác nhận thanh toán tiền mặt/chuyển khoản; đơn hàng được chuyển tiếp qua các trạng thái bếp.
- Phân quyền `ADMIN`, `THU_NGAN`, `BEP` và `KHO`.
- Quản lý khách hàng, nhân viên, danh mục và ảnh món ăn.
- Quản lý nguyên liệu, công thức món, phiếu nhập, ngưỡng cảnh báo và lịch sử thay đổi tồn kho.
- Thống kê doanh thu theo ngày/tháng/năm, món bán chạy và xuất XLSX/PDF.

## Yêu cầu

- JDK 22 trở lên (theo `nbproject/project.properties`), Apache Ant hoặc NetBeans.
- MySQL chạy tại `localhost:3306`.
- MySQL Connector/J tại `lib/mysql-connector-j-9.3.0.jar`.
- Máy tính có giao diện desktop để chạy Swing.

## Cấu hình cơ sở dữ liệu

1. Tạo database:

   ```sql
   CREATE DATABASE kfc_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. Chọn database `kfc_management`, sau đó import `project-materials/ql_kfc_nangcap.sql` bằng phpMyAdmin hoặc MySQL client. File SQL tạo bảng và dữ liệu mẫu, nhưng không tự tạo database đích.
3. Sao chép `config.properties.example` thành `config.properties` và chỉnh `db.url`, `db.user`, `db.password` cho máy của bạn. `config.properties` đã được bỏ qua bởi Git; có thể dùng biến môi trường `KFC_DB_PASSWORD` thay cho mật khẩu trong file.

Không có mật khẩu đăng nhập mẫu được xác nhận. Sau khi build, có thể tạo tài khoản quản trị đầu tiên bằng terminal có console:

```bat
java -cp "build\classes;lib\mysql-connector-j-9.3.0.jar" kfc.utils.TaoAdmin
```

Chương trình yêu cầu nhập mật khẩu ít nhất 10 ký tự và lưu dạng PBKDF2. Không sử dụng hoặc commit mật khẩu thật trong `config.properties`.

## Build và chạy

Từ thư mục gốc:

```bat
ant clean jar
java -cp "build\classes;lib\mysql-connector-j-9.3.0.jar" kfc.ui.kfc_app
```

NetBeans có thể mở trực tiếp project Ant; main class đã được đặt là `kfc.ui.kfc_app`.

## Cấu trúc chính

```text
src/kfc/            Giao diện, DAO, entity và nghiệp vụ
lib/                MySQL Connector/J
project-materials/  SQL mới nhất, tài liệu Word/PPTX và SQL cũ lưu tham khảo
HoaDon/             Tài nguyên hóa đơn có sẵn
build.xml           Build Ant
nbproject/          Cấu hình NetBeans
```

Ảnh do người dùng tải lên được lưu trong `data/images/` và không được đưa vào Git. Ảnh món hiện có trong `src/kfc/images/mon/` được giữ nguyên.

## Ghi chú dữ liệu

- Đặt món, lưu chi tiết và trừ nguyên liệu được thực hiện trong cùng transaction; thao tác hỏng sẽ rollback.
- SQL mới có trạng thái đơn/thanh toán, danh mục món, ngưỡng kho, snapshot nguyên liệu theo đơn và trigger ghi lịch sử kho/đơn.
- Hóa đơn mẫu cũ được đặt mặc định ở trạng thái chưa thanh toán. Snapshot nguyên liệu không thể được khôi phục cho đơn cũ nếu trước đây chưa được ghi lại.
- Không có test tự động được cấu hình trong repo; cần kết nối MySQL để kiểm tra các luồng database khi chạy ứng dụng.
