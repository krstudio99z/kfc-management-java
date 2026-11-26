package kfc.service;
import kfc.database;
import java.sql.*;
import java.math.BigDecimal;
import java.util.*;
public final class DonHangService {
 private DonHangService(){}
 public static void thanhToan(int id, BigDecimal nhan, String maGD) throws SQLException {
  PhienQuanLy.yeuCauVaiTro("ADMIN","THU_NGAN");
  try(Connection c=database.getConnection()) {c.setAutoCommit(false);try {
   BigDecimal tong; String method;
   try(PreparedStatement p=c.prepareStatement("SELECT TongTien,PhuongThuc FROM hoa_don WHERE MaHD=? AND TrangThai='CHO_THANH_TOAN' AND TrangThaiTT='CHUA_THANH_TOAN' FOR UPDATE")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){if(!r.next())throw new SQLException("Đơn không còn chờ thanh toán");tong=r.getBigDecimal(1);method=r.getString(2);}}
   if(nhan==null || nhan.scale()>2 || nhan.compareTo(tong)<0 || nhan.compareTo(new BigDecimal("99999999999999.99"))>0)throw new SQLException("Số tiền nhận không hợp lệ");
   if(method.equals("CHUYEN_KHOAN") && (nhan.compareTo(tong)!=0 || maGD==null || maGD.trim().isEmpty()))throw new SQLException("Cần xác nhận chuyển khoản đúng số tiền và mã giao dịch");
   try(PreparedStatement p=c.prepareStatement("UPDATE hoa_don SET TrangThai='CHO_CHE_BIEN',TrangThaiTT='DA_THANH_TOAN',TienNhan=?,TienThua=?,MaGiaoDich=?,NgayThanhToan=NOW(),NguoiThu=? WHERE MaHD=?")){p.setBigDecimal(1,nhan);p.setBigDecimal(2,nhan.subtract(tong));p.setString(3,maGD);p.setString(4,PhienQuanLy.taiKhoan());p.setInt(5,id);p.executeUpdate();}c.commit();
  }catch(SQLException|RuntimeException e){c.rollback();throw e;}}
 }
 public static void tienTrinh(int id,String next) throws SQLException {
  String previous;
  switch(next){case "DANG_CHE_BIEN":PhienQuanLy.yeuCauVaiTro("ADMIN","BEP");previous="CHO_CHE_BIEN";break;case "SAN_SANG":PhienQuanLy.yeuCauVaiTro("ADMIN","BEP");previous="DANG_CHE_BIEN";break;case "HOAN_THANH":PhienQuanLy.yeuCauVaiTro("ADMIN","THU_NGAN");previous="SAN_SANG";break;default:throw new SQLException("Trạng thái không hợp lệ");}
  if(database.update("UPDATE hoa_don SET TrangThai=? WHERE MaHD=? AND TrangThai=? AND TrangThaiTT='DA_THANH_TOAN'",next,id,previous)!=1)throw new SQLException("Đơn không ở trạng thái phù hợp");
 }
 public static void huy(int id,boolean hoan,boolean refund,String reason) throws SQLException {
  PhienQuanLy.yeuCauVaiTro("ADMIN","THU_NGAN");if(reason==null||reason.trim().isEmpty())throw new SQLException("Cần lý do hủy");
  KhoService.giaoDich(c->{String status,tt;try(PreparedStatement p=c.prepareStatement("SELECT TrangThai,TrangThaiTT FROM hoa_don WHERE MaHD=? FOR UPDATE")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){if(!r.next())throw new SQLException("Không tìm thấy đơn");status=r.getString(1);tt=r.getString(2);}}
   if(status.equals("DA_HUY")||status.equals("HOAN_THANH"))throw new SQLException("Không thể hủy đơn đã hủy/hoàn thành");
   if(tt.equals("DA_THANH_TOAN")){PhienQuanLy.yeuCauVaiTro("ADMIN");if(!refund)throw new SQLException("Quản lý cần xác nhận đã hoàn tiền thực tế");}
   if(hoan){if(!Arrays.asList("CHO_THANH_TOAN","CHO_CHE_BIEN").contains(status))throw new SQLException("Không hoàn kho nguyên liệu đã chế biến");
    try(PreparedStatement p=c.prepareStatement("SET @kfc_reason=?")){p.setString(1,"Hoàn đơn HD"+id);p.execute();}
    try(PreparedStatement p=c.prepareStatement("SELECT MaNL,SoLuong FROM chi_tiet_nguyen_lieu_hoa_don WHERE MaHD=?");PreparedStatement u=c.prepareStatement("UPDATE ql_kho SET SoLuongTon=SoLuongTon+? WHERE MaNL=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){boolean co=false;while(r.next()){co=true;u.setBigDecimal(1,r.getBigDecimal(2));u.setString(2,r.getString(1));if(u.executeUpdate()!=1)throw new SQLException("Thiếu nguyên liệu lịch sử");}if(!co)throw new SQLException("Đơn cũ không có snapshot; không tự hoàn kho");}}
   }
   try(PreparedStatement p=c.prepareStatement("UPDATE hoa_don SET TrangThai='DA_HUY',TrangThaiTT=?,LyDoHuy=?,NgayHuy=NOW(),NguoiHuy=?,HoanKho=? WHERE MaHD=?")){p.setString(1,tt.equals("DA_THANH_TOAN")?"DA_HOAN_TIEN":tt);p.setString(2,reason);p.setString(3,PhienQuanLy.taiKhoan());p.setBoolean(4,hoan);p.setInt(5,id);p.executeUpdate();}
  });
 }
}
