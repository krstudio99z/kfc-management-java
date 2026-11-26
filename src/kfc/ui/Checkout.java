package kfc.ui;
import javax.swing.*;import java.awt.*;import java.math.BigDecimal;
public final class Checkout {
 public static final class Request {final String method,phone;final boolean takeaway;Request(String m,String p,boolean t){method=m;phone=p;takeaway=t;}}
 public static Request ask(Component parent,BigDecimal total){JComboBox<String> pay=new JComboBox<>(new String[]{"Tiền mặt","Chuyển khoản"});JComboBox<String> mode=new JComboBox<>(new String[]{"Tại chỗ","Mang đi"});JTextField phone=new JTextField(12);JPanel p=new JPanel(new GridLayout(0,1,4,4));p.add(new JLabel("Tổng: "+total.toPlainString()+" đ — thu ngân xác nhận sau"));p.add(pay);p.add(mode);p.add(new JLabel("SĐT thành viên (nếu có)"));p.add(phone);while(JOptionPane.showConfirmDialog(parent,p,"Thanh toán / nhận món",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){String s=phone.getText().trim();if(s.isEmpty()||s.matches("0[0-9]{9}"))return new Request(pay.getSelectedIndex()==0?"TIEN_MAT":"CHUYEN_KHOAN",s,mode.getSelectedIndex()==1);JOptionPane.showMessageDialog(parent,"SĐT không hợp lệ");}return null;}
}
