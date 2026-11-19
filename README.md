# Ứng dụng quản lý bán hàng KFC

Ứng dụng desktop quản lý hoạt động bán hàng và kho của nhà hàng KFC, được xây dựng bằng Java Swing và kết nối MySQL. Giao diện cung cấp màn hình chọn vai trò khách hàng/quản lý, đăng nhập quản lý, đặt món, quản lý thực đơn, quản lý kho nguyên liệu và xem/xóa hóa đơn.

> Dự án là ứng dụng Java Swing/NetBeans cho máy tính; không phải ứng dụng web.

## Tính năng chính

### Khách hàng
- Chọn món từ thực đơn.
- Thêm món vào giỏ hàng và điều chỉnh số lượng.
- Tạo hóa đơn sau khi kiểm tra giá món và trạng thái còn bán.
- Tự động trừ nguyên liệu theo công thức món ăn trong cùng giao dịch.
- Xem lại nội dung hóa đơn từ dữ liệu lưu trong cơ sở dữ liệu.

### Quản lý
- Đăng nhập bằng tài khoản lưu trong bảng `user`.
- Thêm, sửa, xóa món ăn và bật/tắt cho phép bán.
- Quản lý nguyên liệu trong kho: thêm, sửa, xóa và nhập thêm số lượng.
- Thiết lập công thức món ăn bằng mã nguyên liệu và định lượng.
- Đồng bộ trạng thái món ăn dựa trên tồn kho và công thức.
- Xem danh sách hóa đơn, xem nội dung hóa đơn và xóa hóa đơn.
- Khi xóa hóa đơn, có thể hoàn trả nguyên liệu đã trừ vào kho.

## Công nghệ

- Java Swing (`javax.swing`) cho giao diện desktop.
- Java JDBC kết nối MySQL.
- MySQL Connector/J `lib/mysql-connector-java-8.0.30.jar`.
- Apache Ant/NetBeans Java Project (`build.xml`, `nbproject/`).
- Mã nguồn nằm trong `src/kfc/`.

## Yêu cầu

- JDK tương thích với cấu hình dự án:
  - `nbproject/project.properties` đang đặt `javac.source=25` và `javac.target=25`.
- Apache Ant hoặc NetBeans IDE có hỗ trợ mở và build project Ant.
- Máy chủ MySQL đang chạy trên cổng `3306`.
- Khả năng tạo hoặc truy cập cơ sở dữ liệu MySQL tên `QL_KFC`.
- Desktop có hỗ trợ giao diện Swing.

## Cấu trúc thư mục

```text
src/kfc/
  ui/          Giao diện Swing: màn hình chính, đăng nhập, đặt món, quản lý
  dao/         Truy vấn dữ liệu món ăn, kho, hóa đơn và tài khoản
  service/     Logic giao dịch kho và đồng bộ trạng thái món ăn
  entity/      Các đối tượng dữ liệu, ví dụ mon_an
  database.java  Cấu hình JDBC và hàm mở kết nối SQL

lib/           Thư viện MySQL Connector/J
build.xml      Script build Ant của NetBeans
nbproject/     Cấu hình project NetBeans
dist/          Thư mục JAR đầu ra sau khi build (nếu build thành công)
build/classes/ Kết quả compile class
HoaDon/        Tài nguyên hóa đơn PDF trong repo
```

## Cấu hình cơ sở dữ liệu

Cấu hình kết nối hiện tại nằm trong `src/kfc/database.java`:

```text
JDBC URL: jdbc:mysql://localhost:3306/QL_KFC
Driver:   com.mysql.cj.jdbc.Driver
User:     root
Password: chuỗi rỗng
```

Trước khi chạy ứng dụng, cần:

1. Chạy MySQL Server tại `localhost:3306`.
2. Tạo hoặc cung cấp cơ sở dữ liệu tên `QL_KFC`.
3. Tạo tài khoản/đăng nhập dữ liệu phù hợp với cấu hình trong ứng dụng.
4. Đảm bảo tài khoản `root` với mật khẩu rỗng có quyền truy cập cơ sở dữ liệu nếu muốn giữ cấu hình mặc định.

Để dùng host, tên cơ sở dữ liệu, tài khoản hoặc mật khẩu khác, cập nhật `URL`, `USER` và `PASS` trong `src/kfc/database.java`. Không có cơ chế biến môi trường hoặc file cấu hình riêng trong repo.

### Các bảng và cột được mã nguồn tham chiếu

Các bảng/cột dưới đây được xác nhận trực tiếp từ câu truy vấn trong mã nguồn:

| Bảng | Cột được tham chiếu |
|---|---|
| `user` | `hoten`, `mk` |
| `monan` | `MaMon`, `TenMon`, `Gia`, `TrangThai`, `ChoPhepBan` |
| `ql_kho` | `MaNL`, `TenNL`, `DonVi`, `SoLuongTon` |
| `cong_thuc_mon` | `MaMon`, `MaNL`, `DinhLuong` |
| `hoa_don` | `MaHD`, `NgayLap`, `TongTien` |
| `chi_tiet_hoa_don` | `MaHD`, `MaMon`, `TenMon`, `SoLuong`, `DonGia`, `ThanhTien` |

> **Thiếu dữ liệu setup:** repo hiện không chứa file SQL tạo cơ sở dữ liệu, dump dữ liệu, migration hoặc mẫu dữ liệu. Vì vậy README không thể cung cấp lệnh `CREATE DATABASE`/`CREATE TABLE` đầy đủ mà không bịa đặt schema. Trước khi chạy, người duy trì cần cung cấp schema hoặc tự tạo các bảng/cột trên theo đúng câu truy vấn trong mã nguồn. Ngoài ra, repo không ghi rõ tài khoản và mật khẩu dữ liệu mẫu; không có thông tin đăng nhập mẫu được xác minh.

## Build và chạy

### Cách 1: NetBeans IDE

1. Mở thư mục dự án bằng NetBeans.
2. Kiểm tra JDK đang dùng có tương thích với `javac.source=25` và `javac.target=25`.
3. Đảm bảo `lib/mysql-connector-java-8.0.30.jar` nằm trong classpath; project đã khai báo thư viện này trong `nbproject/project.properties`.
4. Cấu hình MySQL như mục trên.
5. Chạy class khởi động `kfc.ui.kfc_app` hoặc dùng lệnh Run trong NetBeans.

Lưu ý: `nbproject/project.properties` vẫn có giá trị `main.class=test_kfc.Test_kfc`, nhưng repo không tìm thấy package/class `test_kfc.Test_kfc`. Đây là cấu hình main-class cũ/chưa khớp; màn hình khởi động đã được xác minh trong `kfc.ui.kfc_app`.

### Cách 2: Apache Ant

Từ thư mục gốc dự án:

```bat
ant clean jar
```

Sau khi build thành công, có thể chạy class màn hình chính bằng Java trên Windows:

```bat
java -cp "build\classes;lib\mysql-connector-java-8.0.30.jar" kfc.ui.kfc_app
```

Nếu NetBeans/Ant tạo JAR trong `dist/`, kiểm tra manifest hoặc chạy lớp chính tương ứng trong JAR, thay vì giả định tên JAR và tên main-class vì repo không lưu thông tin manifest chính xác trước build.

## Luồng sử dụng

1. Chạy ứng dụng `kfc.ui.kfc_app`.
2. Chọn chế độ khách để mở màn hình đặt món.
3. Chọn chế độ quản lý để mở màn hình đăng nhập.
4. Sau đăng nhập thành công, quản lý có thể vào quản lý món ăn, kho nguyên liệu hoặc hóa đơn.

## Cơ chế dữ liệu quan trọng

- Đặt hàng chạy trong transaction, khóa kho và kiểm tra tồn kho trước khi trừ nguyên liệu.
- Giá món ăn được đọc lại từ MySQL và so sánh với giá hiện tại trong giỏ trước khi tạo hóa đơn.
- Việc trừ kho, tạo hóa đơn và tạo chi tiết hóa đơn được commit hoặc rollback cùng transaction.
- `KhoService` đồng bộ `TrangThai` của món ăn dựa trên công thức và tồn kho.
- Khi xóa hóa đơn, có tùy chọn hoàn trả nguyên liệu theo công thức món đã đặt.
- Mật khẩu lưu trữ hỗ trợ PBKDF2 có tiền tố `pbkdf2$`; nếu gặp mật khẩu plaintext cũ, code sẽ chuyển sang PBKDF2 sau lần đăng nhập thành công.

## Giới hạn và giả định

- Thư mục repo không có README hoặc tài liệu setup cơ sở dữ liệu trước đây.
- Không có file test trong thư mục `test/` được cấu hình bởi project.
- Không có file SQL/schema/migration hoặc dữ liệu mẫu được xác minh.
- Không có cấu hình `.env`/application properties; thông tin DB được hard-code trong mã Java.
- Không thể xác minh toàn bộ dữ liệu hoặc tài khoản mẫu vì chúng không nằm trong repo.
