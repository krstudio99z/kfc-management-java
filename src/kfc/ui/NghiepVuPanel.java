package kfc.ui;
import kfc.database;import kfc.service.*;import kfc.dao.TaiKhoanDAO;
import javax.swing.*;import javax.swing.table.DefaultTableModel;import java.awt.*;import java.io.*;import java.math.BigDecimal;import java.nio.file.*;import java.sql.*;import java.util.ArrayList;import java.util.Arrays;import java.util.List;
/** Mở rộng bằng các tab; không thay layout các màn hình cũ. */
public final class NghiepVuPanel extends JPanel {
 private final JPanel navigation = new JPanel(new GridLayout(0, 4, 8, 8));
 private final JPanel workspace = new JPanel(new CardLayout());
 private final java.util.LinkedHashMap<String, View> views = new java.util.LinkedHashMap<>();
 private final java.util.LinkedHashMap<String, JButton> navButtons = new java.util.LinkedHashMap<>();
 private View selected;
 private final JLabel sectionTitle = new JLabel("NGHIỆP VỤ MỞ RỘNG");
 @FunctionalInterface interface Action {void run() throws Exception;}
 private void run(Action a){try{PhienQuanLy.yeuCauDangNhap();a.run();}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Không thực hiện được",JOptionPane.ERROR_MESSAGE);}}
 private JButton button(String name,Action a){JButton b=new JButton(name);b.setFont(new Font("Segoe UI",Font.BOLD,12));b.setFocusPainted(false);b.setOpaque(true);b.setBackground(new Color(238,241,245));b.setForeground(KfcTheme.INK);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));b.addActionListener(e->run(a));return b;}
 private String input(String label,String def){String s=JOptionPane.showInputDialog(this,label,def);if(s==null)throw new IllegalArgumentException("Đã hủy thao tác");return s.trim();}
 private String required(String label,String def){String s=input(label,def);if(s.isEmpty())throw new IllegalArgumentException("Không được bỏ trống: "+label);return s;}
 private String choose(String label,String... values){Object s=JOptionPane.showInputDialog(this,label,"Chọn",JOptionPane.QUESTION_MESSAGE,null,values,values[0]);if(s==null)throw new IllegalArgumentException("Đã hủy");return s.toString();}
 private boolean confirm(String s){return JOptionPane.showConfirmDialog(this,s,"Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;}
 private boolean role(String... roles){return PhienQuanLy.coVaiTro(roles);}
 private int update(String sql,Object... args)throws SQLException{try(Connection c=database.getConnection();PreparedStatement p=c.prepareStatement(sql)){for(int i=0;i<args.length;i++)p.setObject(i+1,args[i]);return p.executeUpdate();}}
 private class View extends JPanel {
  final JTable table = new JTable();
  final JPanel tools = new JPanel(new GridLayout(0, 4, 8, 8));
  final JPanel form = new JPanel(new GridBagLayout());
  final JTextField search = new JTextField(20);
  final JLabel count = new JLabel("Chưa tải dữ liệu");
  final JPanel footer = new JPanel(new BorderLayout(8, 8));
  final JScrollPane scroll = new JScrollPane(table);
  final String sql; final Object[] params;
  private Runnable selectionAction;
  private Runnable loadedAction;
  View(String s, Object... p) {
   super(new BorderLayout(10, 10)); sql=s; params=p;
   table.setAutoCreateRowSorter(true); table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
   table.addPropertyChangeListener("model",e->configureColumns());
   table.getTableHeader().setReorderingAllowed(false);
   JPanel upper=new JPanel(new BorderLayout(8,8));
   JPanel searchRow=new JPanel(new BorderLayout(12,0));
   searchRow.add(new JLabel("Tìm kiếm"),BorderLayout.WEST);searchRow.add(search,BorderLayout.CENTER);
   searchRow.add(count,BorderLayout.EAST);
   upper.add(searchRow,BorderLayout.NORTH);upper.add(form,BorderLayout.CENTER);upper.add(tools,BorderLayout.SOUTH);
   add(upper,BorderLayout.NORTH);add(scroll,BorderLayout.CENTER);add(footer,BorderLayout.SOUTH);
   form.setVisible(false);footer.setVisible(false);
   addBtn("Làm mới",this::load);
   search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
    public void insertUpdate(javax.swing.event.DocumentEvent e){filter();}
    public void removeUpdate(javax.swing.event.DocumentEvent e){filter();}
    public void changedUpdate(javax.swing.event.DocumentEvent e){filter();}
   });
   table.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting() && table.getSelectedRow()>=0 && selectionAction!=null)selectionAction.run();});
  }
  void addBtn(String s,Action a){tools.add(button(s,a));}
  JTextField field(String label,int index){JTextField f=new JTextField(14);field(label,index,f);return f;}
  void field(String label,int index,JComponent f){
   form.setVisible(true);int col=(index%2)*2,row=index/2;
   GridBagConstraints g=new GridBagConstraints();g.gridy=row;g.gridx=col;g.anchor=GridBagConstraints.WEST;g.insets=new Insets(4,4,4,8);
   form.add(new JLabel(label),g);g.gridx=col+1;g.weightx=1;g.fill=GridBagConstraints.HORIZONTAL;form.add(f,g);
  }
  void onSelection(Runnable action){selectionAction=action;}
  void onLoaded(Runnable action){loadedAction=action;}
  void filter(){if(table.getRowSorter() instanceof javax.swing.table.TableRowSorter){
   @SuppressWarnings("unchecked") javax.swing.table.TableRowSorter<javax.swing.table.TableModel> sorter=(javax.swing.table.TableRowSorter<javax.swing.table.TableModel>)table.getRowSorter();
   String text=search.getText().trim();sorter.setRowFilter(text.isEmpty()?null:RowFilter.regexFilter("(?iu)"+java.util.regex.Pattern.quote(text)));
   count.setText(table.getRowCount()+" / "+table.getModel().getRowCount()+" dòng");}}
  void configureColumns(){
   int n=table.getColumnCount();table.setAutoResizeMode(n<=5?JTable.AUTO_RESIZE_ALL_COLUMNS:JTable.AUTO_RESIZE_OFF);
   for(int i=0;i<n;i++){int width=Math.max(108,table.getColumnName(i).length()*8+32);for(int r=0;r<Math.min(table.getModel().getRowCount(),60);r++){Object val=table.getModel().getValueAt(r,i);if(val!=null)width=Math.max(width,Math.min(300,val.toString().length()*8+28));}table.getColumnModel().getColumn(i).setPreferredWidth(width);}
  }
  void load()throws SQLException{
   try(Connection c=database.getConnection();PreparedStatement p=c.prepareStatement(sql)){
    for(int i=0;i<params.length;i++)p.setObject(i+1,params[i]);
    try(ResultSet r=p.executeQuery()){ResultSetMetaData md=r.getMetaData();String[] cols=new String[md.getColumnCount()];
     for(int i=0;i<cols.length;i++)cols[i]=columnTitle(md.getColumnLabel(i+1));
     DefaultTableModel m=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}
      public Class<?> getColumnClass(int col){for(int r=0;r<getRowCount();r++){Object v=getValueAt(r,col);if(v!=null)return v.getClass();}return Object.class;}};
     while(r.next()){Object[] row=new Object[cols.length];for(int i=0;i<cols.length;i++)row[i]=r.getObject(i+1);m.addRow(row);}
     table.setModel(m);
     configureColumns();
     filter();if(loadedAction!=null)loadedAction.run();
    }
   }
  }
  Object value(int col){int r=table.getSelectedRow();if(r<0)throw new IllegalArgumentException("Chọn một dòng trong bảng");return table.getModel().getValueAt(table.convertRowIndexToModel(r),col);}
  String str(int col){Object x=value(col);return x==null?"":x.toString();}
  int id(){return ((Number)value(0)).intValue();}
 }
 private static String columnTitle(String s){switch(s.toLowerCase(java.util.Locale.ROOT)){
  case "mahd":return "Mã HĐ";case "ngaylap":return "Ngày lập";case "tongtien":return "Tổng tiền";case "phuongthuc":return "Phương thức";case "trangthai":return "Trạng thái đơn";case "trangthaitt":return "Thanh toán";case "hinhthuc":return "Nhận món";case "tiennhan":return "Tiền nhận";case "tienthua":return "Tiền thừa";case "magiaodich":return "Mã giao dịch";
  case "makh":return "Mã KH";case "tenkh":return "Tên khách hàng";case "sdt":return "Số điện thoại";case "taikhoan":return "Tài khoản";case "tennv":return "Họ tên";case "vaitro":return "Vai trò";case "hoatdong":return "Hoạt động";case "tendm":return "Tên danh mục";case "mamon":return "Mã món";case "tenmon":return "Tên món";case "danhmuc":return "Danh mục";case "hinhanh":return "Đường dẫn ảnh";
  case "mancc":return "Mã NCC";case "tenncc":return "Nhà cung cấp";case "diachi":return "Địa chỉ";case "mapn":return "Mã phiếu";case "ngaynhap":return "Ngày nhập";case "nguoinhap":return "Người nhập";case "tongchi":return "Tổng chi";case "manl":return "Mã NL";case "tennl":return "Nguyên liệu";case "donvi":return "Đơn vị";case "soluongton":return "Tồn kho";case "tontoithieu":return "Tồn tối thiểu";case "canhbao":return "Cảnh báo";case "soluong":return "Số lượng";case "dongia":return "Đơn giá";case "thanhtien":return "Thành tiền";case "thoigian":return "Thời gian";case "truoc":return "Tồn trước";case "sau":return "Tồn sau";case "chenhlech":return "Chênh lệch";case "nguoithuchien":return "Người thực hiện";case "lydo":return "Lý do";case "ngay":return "Ngày";case "sodon":return "Số đơn";case "doanhthu":return "Doanh thu";case "ky":return "Kỳ";default:return s;}}
 private void add(String title,View v){
  views.put(title,v);workspace.add(v,title);KfcTheme.apply(v);
  javax.swing.table.TableCellRenderer base=v.table.getDefaultRenderer(Object.class);
  v.table.setDefaultRenderer(Object.class,(t,value,selected,focused,row,col)->{
   Object display=value;
   if(value instanceof String){switch((String)value){case "CHO_THANH_TOAN":display="Chờ thanh toán";break;case "CHO_CHE_BIEN":display="Chờ chế biến";break;case "DANG_CHE_BIEN":display="Đang chế biến";break;case "SAN_SANG":display="Sẵn sàng";break;case "HOAN_THANH":display="Hoàn thành";break;case "DA_HUY":display="Đã hủy";break;case "DA_THANH_TOAN":display="Đã thanh toán";break;case "CHUA_THANH_TOAN":display="Chưa thanh toán";break;case "DA_HOAN_TIEN":display="Đã hoàn tiền";break;case "TIEN_MAT":display="Tiền mặt";break;case "CHUYEN_KHOAN":display="Chuyển khoản";break;case "TAI_CHO":display="Tại chỗ";break;case "MANG_DI":display="Mang đi";break;case "THU_NGAN":display="Thu ngân";break;case "BEP":display="Bếp";break;case "KHO":display="Kho";break;default:break;}}
   return base.getTableCellRendererComponent(t,display,selected,focused,row,col);
  });
  JButton b=button(title,()->select(title,true));navButtons.put(title,b);navigation.add(b);
 }
 private void select(String title,boolean load){selected=views.get(title);if(selected==null)return;
  ((CardLayout)workspace.getLayout()).show(workspace,title);sectionTitle.setText(title.toUpperCase(java.util.Locale.ROOT));
  navButtons.forEach((name,b)->{boolean active=name.equals(title);b.setBackground(active?KfcTheme.RED:new Color(238,241,245));b.setForeground(active?Color.WHITE:KfcTheme.INK);b.setBorder(BorderFactory.createEmptyBorder(10,6,10,6));});
  if(load)refresh();
 }
 public NghiepVuPanel(){super(new BorderLayout(10,10));PhienQuanLy.yeuCauDangNhap();setBackground(KfcTheme.BACKGROUND);setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
  JPanel header=new JPanel(new BorderLayout(8,8));header.setBackground(Color.WHITE);header.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
  sectionTitle.setFont(new Font("Segoe UI",Font.BOLD,18));sectionTitle.setForeground(KfcTheme.RED_DARK);header.add(sectionTitle,BorderLayout.NORTH);navigation.setOpaque(false);header.add(navigation,BorderLayout.CENTER);add(header,BorderLayout.NORTH);add(workspace,BorderLayout.CENTER);
  if(role("ADMIN","THU_NGAN","BEP"))orders();if(role("ADMIN","THU_NGAN")){customers();dashboard();}if(role("ADMIN")){staff();categories();}if(role("ADMIN","KHO")){suppliers();receipts();stock();}
  if(!views.isEmpty())select(views.keySet().iterator().next(),false);
 }
 public void refresh(){if(selected!=null)run(selected::load);}
 private String fieldValue(JTextField f,String label){String x=f.getText().trim();if(x.isEmpty())throw new IllegalArgumentException("Không được bỏ trống: "+label);return x;}
 private String phoneValue(JTextField f){String x=fieldValue(f,"Số điện thoại");if(!x.matches("0[0-9]{9}"))throw new IllegalArgumentException("SĐT phải đủ 10 số, bắt đầu bằng 0");return x;}
 private void orders(){String filter=role("ADMIN","THU_NGAN")?"":" WHERE TrangThai IN ('CHO_CHE_BIEN','DANG_CHE_BIEN','SAN_SANG')";View v=new View("SELECT MaHD,NgayLap,TongTien,PhuongThuc,TrangThai,TrangThaiTT,HinhThuc,TienNhan,TienThua,MaGiaoDich FROM hoa_don"+filter+" ORDER BY MaHD DESC LIMIT 500");
  v.addBtn("Chi tiết",()->{JTextArea a=new JTextArea(new kfc.dao.HoaDonDAO().noiDungHoaDon(v.id()),18,45);a.setEditable(false);JOptionPane.showMessageDialog(this,new JScrollPane(a));});
  if(role("ADMIN","THU_NGAN")){v.addBtn("Thu tiền",()->{int id=v.id();String method=v.str(3);BigDecimal total=new BigDecimal(v.str(2));BigDecimal received=new BigDecimal(required("Tiền thực nhận",total.toPlainString()));String code=method.equals("CHUYEN_KHOAN")?required("Mã giao dịch đã kiểm tra tại ngân hàng",""):null;if(!confirm("Xác nhận thực nhận "+received+" đ? Tiền thừa: "+received.subtract(total)+" đ"))return;DonHangService.thanhToan(id,received,code);v.load();});v.addBtn("Giao món",()->{DonHangService.tienTrinh(v.id(),"HOAN_THANH");v.load();});v.addBtn("Hủy đơn",()->{int id=v.id();String reason=required("Lý do hủy","");boolean paid=v.str(5).equals("DA_THANH_TOAN");boolean refund=paid&&role("ADMIN")&&confirm("Đã hoàn tiền thực tế cho khách?");if(paid&&!refund)return;boolean hoan=Arrays.asList("CHO_THANH_TOAN","CHO_CHE_BIEN").contains(v.str(4))&&confirm("Nguyên liệu chưa chế biến — hoàn kho?");if(confirm("Hủy đơn HD"+id+"?")){DonHangService.huy(id,hoan,refund,reason);v.load();}});}
  if(role("ADMIN","BEP")){v.addBtn("Bắt đầu bếp",()->{DonHangService.tienTrinh(v.id(),"DANG_CHE_BIEN");v.load();});v.addBtn("Sẵn sàng",()->{DonHangService.tienTrinh(v.id(),"SAN_SANG");v.load();});}add("Đơn hàng / bếp",v);
 }
 private void customers(){
  View v=new View("SELECT MaKH,TenKH,SDT FROM khach_hang ORDER BY MaKH DESC");
  JTextField name=v.field("Tên khách hàng",0),phone=v.field("Số điện thoại",1);
  v.onSelection(()->{name.setText(v.str(1));phone.setText(v.str(2));});
  v.addBtn("Thêm",()->{PhienQuanLy.yeuCauVaiTro("ADMIN","THU_NGAN");update("INSERT INTO khach_hang(TenKH,SDT) VALUES(?,?)",fieldValue(name,"Tên khách hàng"),phoneValue(phone));v.load();name.setText("");phone.setText("");});
  v.addBtn("Sửa",()->{update("UPDATE khach_hang SET TenKH=?,SDT=? WHERE MaKH=?",fieldValue(name,"Tên khách hàng"),phoneValue(phone),v.id());v.load();});
  v.addBtn("Làm trống",()->{v.table.clearSelection();name.setText("");phone.setText("");});
  v.addBtn("Lịch sử mua",()->{View history=new View("SELECT MaHD,NgayLap,TongTien,TrangThaiTT FROM hoa_don WHERE MaKH=? ORDER BY MaHD DESC",v.id());KfcTheme.apply(history);history.load();history.setPreferredSize(new Dimension(650,300));JOptionPane.showMessageDialog(this,history,"Lịch sử mua hàng",JOptionPane.PLAIN_MESSAGE);});add("Khách hàng",v);
 }
 private String phone(String def){String p=required("SĐT (10 số)",def);if(!p.matches("0[0-9]{9}"))throw new IllegalArgumentException("SĐT không hợp lệ");return p;}
 private String password()throws Exception{JPasswordField p=new JPasswordField(20);if(JOptionPane.showConfirmDialog(this,p,"Mật khẩu mới (ít nhất 10 ký tự)",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)throw new IllegalArgumentException("Đã hủy");char[] chars=p.getPassword();try{return TaiKhoanDAO.bamMatKhau(chars);}finally{Arrays.fill(chars,'\0');}}
 private void staff(){
  View v=new View("SELECT hoten AS TaiKhoan,TenNV,SDT,VaiTro,HoatDong FROM `user` ORDER BY hoten");
  JTextField user=v.field("Tên đăng nhập",0),name=v.field("Họ tên",1),phone=v.field("Số điện thoại",2);
  JComboBox<String> roleBox=new JComboBox<>(new String[]{"THU_NGAN","BEP","KHO","ADMIN"});v.field("Vai trò",3,roleBox);
  v.onSelection(()->{user.setText(v.str(0));name.setText(v.str(1));phone.setText(v.str(2));roleBox.setSelectedItem(v.str(3));});
  v.addBtn("Tạo nhân viên",()->{PhienQuanLy.yeuCauVaiTro("ADMIN");String u=fieldValue(user,"Tên đăng nhập");if(!u.matches("[A-Za-z0-9_.-]{3,50}"))throw new IllegalArgumentException("Tên đăng nhập 3–50 ký tự ASCII");update("INSERT INTO `user`(hoten,mk,TenNV,SDT,VaiTro) VALUES(?,?,?,?,?)",u,password(),fieldValue(name,"Họ tên"),phoneValue(phone),roleBox.getSelectedItem());v.load();});
  v.addBtn("Sửa thông tin",()->{PhienQuanLy.yeuCauVaiTro("ADMIN");update("UPDATE `user` SET TenNV=?,SDT=? WHERE hoten=?",fieldValue(name,"Họ tên"),phoneValue(phone),v.str(0));v.load();});
  v.addBtn("Đổi quyền",()->{String u=v.str(0);if(u.equalsIgnoreCase(PhienQuanLy.taiKhoan()))throw new IllegalArgumentException("Không tự đổi quyền");update("UPDATE `user` SET VaiTro=? WHERE hoten=?",roleBox.getSelectedItem(),u);v.load();});
  v.addBtn("Khóa / mở",()->{String u=v.str(0);if(u.equalsIgnoreCase(PhienQuanLy.taiKhoan()))throw new IllegalArgumentException("Không tự khóa");if(confirm("Đổi trạng thái tài khoản "+u+"?")){update("UPDATE `user` SET HoatDong=NOT HoatDong WHERE hoten=?",u);v.load();}});
  v.addBtn("Đặt lại mật khẩu",()->{String u=v.str(0);update("UPDATE `user` SET mk=? WHERE hoten=?",password(),u);v.load();});
  v.addBtn("Làm trống",()->{v.table.clearSelection();user.setText("");name.setText("");phone.setText("");roleBox.setSelectedIndex(0);});add("Nhân viên",v);
 }
 private void categories(){
  View dm=new View("SELECT TenDM FROM danh_muc ORDER BY TenDM");JTextField name=dm.field("Tên danh mục",0);dm.onSelection(()->name.setText(dm.str(0)));
  dm.addBtn("Thêm danh mục",()->{update("INSERT INTO danh_muc VALUES(?)",fieldValue(name,"Tên danh mục"));dm.load();name.setText("");});
  dm.addBtn("Xóa danh mục trống",()->{if(confirm("Xóa danh mục chưa dùng?")){update("DELETE FROM danh_muc WHERE TenDM=?",dm.str(0));dm.load();name.setText("");}});add("Danh mục",dm);
  View v=new View("SELECT MaMon,TenMon,DanhMuc,HinhAnh FROM monan ORDER BY MaMon");
  JLabel preview=new JLabel("Chọn món để xem ảnh",SwingConstants.CENTER);preview.setPreferredSize(new Dimension(200,156));preview.setBorder(BorderFactory.createLineBorder(KfcTheme.LINE));
  JLabel details=new JLabel("Ảnh được lưu trong thư mục data/images hoặc tài nguyên project");
  v.footer.setVisible(true);v.footer.add(preview,BorderLayout.WEST);v.footer.add(details,BorderLayout.CENTER);
  v.onSelection(()->{preview.setText("");preview.setIcon(KfcImages.menuIcon(v.str(0),v.str(1),v.str(3),192,148));details.setText(v.str(0)+" - "+v.str(1)+" | "+(v.str(3).isEmpty()?"Ảnh minh họa mặc định":v.str(3)));details.setToolTipText(details.getText());});
  v.addBtn("Gán danh mục",()->{String ma=v.str(0);List<String> names=new ArrayList<>();try(ResultSet r=database.query("SELECT TenDM FROM danh_muc ORDER BY TenDM")){while(r.next())names.add(r.getString(1));}if(names.isEmpty())throw new IllegalArgumentException("Tạo danh mục trước");update("UPDATE monan SET DanhMuc=? WHERE MaMon=?",choose("Danh mục",names.toArray(new String[0])),ma);v.load();});
  v.addBtn("Chọn ảnh",()->{String ma=v.str(0);JFileChooser fc=new JFileChooser();fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Ảnh PNG/JPG","png","jpg","jpeg"));if(fc.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)return;File f=fc.getSelectedFile();String path=KfcImages.save(f);try{update("UPDATE monan SET HinhAnh=? WHERE MaMon=?",path,ma);}catch(Exception e){KfcImages.removeNew(path);throw e;}v.load();preview.setIcon(KfcImages.menuIcon(ma,"",path,192,148));preview.setText("");details.setText("Đã lưu: "+path);});
  v.addBtn("Bỏ ảnh",()->{String ma=v.str(0),ten=v.str(1);if(confirm("Dùng ảnh minh họa mặc định cho món này?")){update("UPDATE monan SET HinhAnh=NULL WHERE MaMon=?",ma);v.load();preview.setIcon(KfcImages.menuIcon(ma,ten,"",192,148));preview.setText("");details.setText("Ảnh minh họa mặc định");}});add("Ảnh món",v);
 }
 private void suppliers(){
  View v=new View("SELECT MaNCC,TenNCC,SDT,DiaChi FROM nha_cung_cap ORDER BY MaNCC");
  JTextField code=v.field("Mã NCC",0),name=v.field("Nhà cung cấp",1),phone=v.field("Số điện thoại",2),address=v.field("Địa chỉ",3);
  v.onSelection(()->{code.setText(v.str(0));name.setText(v.str(1));phone.setText(v.str(2));address.setText(v.str(3));});
  v.addBtn("Thêm",()->{update("INSERT INTO nha_cung_cap VALUES(?,?,?,?)",fieldValue(code,"Mã NCC"),fieldValue(name,"Nhà cung cấp"),phone.getText().trim(),address.getText().trim());v.load();});
  v.addBtn("Sửa",()->{update("UPDATE nha_cung_cap SET TenNCC=?,SDT=?,DiaChi=? WHERE MaNCC=?",fieldValue(name,"Nhà cung cấp"),phone.getText().trim(),address.getText().trim(),v.str(0));v.load();});
  v.addBtn("Xóa NCC chưa sử dụng",()->{if(confirm("Xóa nhà cung cấp?")){update("DELETE FROM nha_cung_cap WHERE MaNCC=?",v.str(0));v.load();}});
  v.addBtn("Làm trống",()->{v.table.clearSelection();for(JTextField f:new JTextField[]{code,name,phone,address})f.setText("");});add("Nhà cung cấp",v);
 }
 private void receipts(){
  View v=new View("SELECT p.MaPN,p.NgayNhap,p.MaNCC,p.NguoiNhap, SUM(c.SoLuong*c.DonGia) AS TongChi FROM phieu_nhap p JOIN chi_tiet_phieu_nhap c ON p.MaPN=c.MaPN GROUP BY p.MaPN,p.NgayNhap,p.MaNCC,p.NguoiNhap ORDER BY p.MaPN DESC");
  v.addBtn("Lập phiếu nhập",()->{if(receiptForm())v.load();});
  v.addBtn("Chi tiết phiếu",()->{View d=new View("SELECT MaNL,SoLuong,DonGia,SoLuong*DonGia AS ThanhTien FROM chi_tiet_phieu_nhap WHERE MaPN=?",v.id());KfcTheme.apply(d);d.load();d.setPreferredSize(new Dimension(600,320));JOptionPane.showMessageDialog(this,d,"Chi tiết phiếu nhập",JOptionPane.PLAIN_MESSAGE);});add("Phiếu nhập",v);
 }
 private boolean receiptForm()throws Exception{
  List<String> suppliers=new ArrayList<>(),ingredients=new ArrayList<>();java.util.Map<String,String> supplierCodes=new java.util.LinkedHashMap<>(),ingredientCodes=new java.util.LinkedHashMap<>();
  try(ResultSet r=database.query("SELECT MaNCC,TenNCC FROM nha_cung_cap ORDER BY MaNCC")){while(r.next()){String label=r.getString(1)+" - "+r.getString(2);suppliers.add(label);supplierCodes.put(label,r.getString(1));}}
  try(ResultSet r=database.query("SELECT MaNL,TenNL,DonVi FROM ql_kho ORDER BY MaNL")){while(r.next()){String label=r.getString(1)+" - "+r.getString(2)+" ("+r.getString(3)+")";ingredients.add(label);ingredientCodes.put(label,r.getString(1));}}
  if(suppliers.isEmpty()||ingredients.isEmpty())throw new IllegalArgumentException("Cần có nhà cung cấp và nguyên liệu trước");
  JComboBox<String> ncc=new JComboBox<>(suppliers.toArray(new String[0])),nl=new JComboBox<>(ingredients.toArray(new String[0]));JTextField sl=new JTextField("1"),gia=new JTextField("0");
  JPanel form=new JPanel(new BorderLayout(10,10)),fields=new JPanel(new GridLayout(0,2,8,8)),buttons=new JPanel(new GridLayout(1,2,8,8));
  for(Object[] entry:new Object[][]{{"Nhà cung cấp",ncc},{"Nguyên liệu",nl},{"Số lượng",sl},{"Đơn giá (đ)",gia}}){fields.add(new JLabel(entry[0].toString()));fields.add((JComponent)entry[1]);}
  DefaultTableModel m=new DefaultTableModel(new Object[]{"Mã NL","Tên nguyên liệu","Số lượng","Đơn giá","Thành tiền"},0){public boolean isCellEditable(int r,int c){return false;}};JTable table=new JTable(m);JLabel total=new JLabel("Tổng chi: 0 đ");
  Runnable recalc=()->{BigDecimal sum=BigDecimal.ZERO;for(int i=0;i<m.getRowCount();i++)sum=sum.add((BigDecimal)m.getValueAt(i,4));total.setText("Tổng chi: "+new java.text.DecimalFormat("#,##0.##").format(sum)+" đ");};
  buttons.add(button("Thêm nguyên liệu",()->{String label=String.valueOf(nl.getSelectedItem()),code=ingredientCodes.get(label);BigDecimal quantity=new BigDecimal(sl.getText().trim()),price=new BigDecimal(gia.getText().trim());if(quantity.signum()<=0||quantity.scale()>3||price.signum()<0||price.scale()>2)throw new IllegalArgumentException("Số lượng > 0, đơn giá >= 0; tối đa 3 và 2 chữ số thập phân");for(int i=0;i<m.getRowCount();i++)if(code.equals(m.getValueAt(i,0)))throw new IllegalArgumentException("Nguyên liệu đã có trong phiếu");m.addRow(new Object[]{code,label,quantity,price,quantity.multiply(price)});recalc.run();}));
  buttons.add(button("Xóa dòng",()->{int row=table.getSelectedRow();if(row<0)throw new IllegalArgumentException("Chọn dòng cần xóa");m.removeRow(table.convertRowIndexToModel(row));recalc.run();}));
  JPanel upper=new JPanel(new BorderLayout(8,8));upper.add(fields,BorderLayout.CENTER);upper.add(buttons,BorderLayout.SOUTH);form.add(upper,BorderLayout.NORTH);form.add(new JScrollPane(table),BorderLayout.CENTER);form.add(total,BorderLayout.SOUTH);KfcTheme.apply(form);form.setPreferredSize(new Dimension(760,475));
  if(JOptionPane.showConfirmDialog(this,form,"LẬP PHIẾU NHẬP KHO",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)!=JOptionPane.OK_OPTION)return false;
  List<StockExtra.Line> data=new ArrayList<>();for(int i=0;i<m.getRowCount();i++)data.add(new StockExtra.Line(m.getValueAt(i,0).toString(),(BigDecimal)m.getValueAt(i,2),(BigDecimal)m.getValueAt(i,3)));StockExtra.nhap(supplierCodes.get(String.valueOf(ncc.getSelectedItem())),data);return true;
 }
 private void stock(){View v=new View("SELECT MaNL,TenNL,DonVi,SoLuongTon,TonToiThieu,CASE WHEN SoLuongTon<=TonToiThieu THEN 'SẮP HẾT' ELSE 'OK' END AS CanhBao FROM ql_kho ORDER BY (SoLuongTon<=TonToiThieu) DESC,MaNL");v.addBtn("Đặt ngưỡng",()->{String ma=v.str(0);BigDecimal n=new BigDecimal(required("Ngưỡng tối thiểu",v.str(4)));if(n.signum()<0||n.scale()>3)throw new IllegalArgumentException("Ngưỡng không hợp lệ");update("UPDATE ql_kho SET TonToiThieu=? WHERE MaNL=?",n,ma);v.load();});v.addBtn("Điều chỉnh +/-",()->{String ma=v.str(0);BigDecimal delta=new BigDecimal(required("Chênh lệch (âm để giảm)","0"));if(delta.signum()==0||delta.scale()>3)throw new IllegalArgumentException("Chênh lệch không hợp lệ");String reason=required("Lý do điều chỉnh","");KhoService.giaoDich(c->{try(PreparedStatement p=c.prepareStatement("SET @kfc_reason=?")){p.setString(1,reason);p.execute();}try(PreparedStatement p=c.prepareStatement("UPDATE ql_kho SET SoLuongTon=SoLuongTon+? WHERE MaNL=? AND SoLuongTon+?>=0")){p.setBigDecimal(1,delta);p.setString(2,ma);p.setBigDecimal(3,delta);if(p.executeUpdate()!=1)throw new SQLException("Tồn không đủ");}});v.load();});add("Cảnh báo kho",v);View history=new View("SELECT ID,MaNL,ThoiGian,Truoc,Sau,Sau-Truoc AS ChenhLech,NguoiThucHien,LyDo FROM lich_su_kho ORDER BY ID DESC LIMIT 1000");add("Lịch sử kho",history);}
 private void dashboard(){View v=new View("SELECT DATE(NgayThanhToan) AS Ngay,COUNT(*) AS SoDon,SUM(TongTien) AS DoanhThu FROM hoa_don WHERE TrangThaiTT='DA_THANH_TOAN' AND NgayThanhToan>=CURRENT_DATE-INTERVAL 29 DAY GROUP BY DATE(NgayThanhToan) ORDER BY Ngay");JLabel summary=new JLabel("Tổng doanh thu 30 ngày: —");summary.setFont(new Font("Segoe UI",Font.BOLD,16));v.footer.setVisible(true);v.footer.add(summary,BorderLayout.NORTH);v.onLoaded(()->{BigDecimal total=BigDecimal.ZERO;long n=0;for(int i=0;i<v.table.getModel().getRowCount();i++){total=total.add(new BigDecimal(v.table.getModel().getValueAt(i,2).toString()));n+=((Number)v.table.getModel().getValueAt(i,1)).longValue();}summary.setText("Tổng doanh thu 30 ngày: "+new java.text.DecimalFormat("#,##0.##").format(total)+" đ   |   Số đơn: "+n);});v.addBtn("Biểu đồ 30 ngày",()->{v.load();JPanel chart=new JPanel(){protected void paintComponent(Graphics g){super.paintComponent(g);int n=v.table.getRowCount();if(n==0){g.drawString("Chưa có doanh thu",30,40);return;}double max=1;for(int i=0;i<n;i++)max=Math.max(max,((Number)v.table.getModel().getValueAt(i,2)).doubleValue());int w=Math.max(1,(getWidth()-70)/n);for(int i=0;i<n;i++){double value=((Number)v.table.getModel().getValueAt(i,2)).doubleValue();int h=(int)((getHeight()-70)*value/max);g.setColor(KfcTheme.RED);g.fillRect(35+i*w,getHeight()-35-h,Math.max(1,w-4),h);g.setColor(Color.DARK_GRAY);if(n<=12||i%5==0)g.drawString(v.table.getModel().getValueAt(i,0).toString().substring(5),35+i*w,getHeight()-15);}}};chart.setPreferredSize(new Dimension(800,360));JOptionPane.showMessageDialog(this,chart,"Doanh thu (đ)",JOptionPane.PLAIN_MESSAGE);});v.addBtn("Theo tháng/năm",()->{String unit=choose("Kỳ thống kê","Tháng","Năm");String group=unit.equals("Tháng")?"DATE_FORMAT(NgayThanhToan,'%Y-%m')":"YEAR(NgayThanhToan)";View d=new View("SELECT "+group+" AS Ky,COUNT(*) AS SoDon,SUM(TongTien) AS DoanhThu FROM hoa_don WHERE TrangThaiTT='DA_THANH_TOAN' GROUP BY "+group+" ORDER BY Ky DESC");d.load();d.addBtn("Excel",()->export(d,false));d.addBtn("PDF",()->export(d,true));d.setPreferredSize(new Dimension(720,400));JOptionPane.showMessageDialog(this,d);});v.addBtn("Top 5 món",()->{View d=new View("SELECT c.MaMon,MAX(c.TenMon) AS TenMon,SUM(c.SoLuong) AS SoLuong,SUM(c.ThanhTien) AS DoanhThu FROM chi_tiet_hoa_don c JOIN hoa_don h ON h.MaHD=c.MaHD WHERE h.TrangThaiTT='DA_THANH_TOAN' GROUP BY c.MaMon ORDER BY SoLuong DESC LIMIT 5");d.load();d.setPreferredSize(new Dimension(650,300));JOptionPane.showMessageDialog(this,d);});v.addBtn("Excel",()->export(v,false));v.addBtn("PDF",()->export(v,true));add("Dashboard doanh thu",v);}
 private void export(View v,boolean pdf)throws Exception{JFileChooser fc=new JFileChooser();fc.setSelectedFile(new File(pdf?"bao_cao.pdf":"bao_cao.xlsx"));if(fc.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;File f=fc.getSelectedFile();String ext=pdf?".pdf":".xlsx";if(!f.getName().toLowerCase().endsWith(ext))f=new File(f.getPath()+ext);if(f.exists()&&!confirm("Ghi đè file?"))return;if(pdf)kfc.utils.ReportExport.pdf(v.table.getModel(),f);else kfc.utils.ReportExport.xlsx(v.table.getModel(),f);JOptionPane.showMessageDialog(this,"Đã lưu: "+f.getAbsolutePath());}
}
