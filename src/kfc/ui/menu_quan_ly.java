package kfc.ui;

import kfc.service.PhienQuanLy;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class menu_quan_ly extends JFrame {
    public menu_quan_ly() {
        PhienQuanLy.yeuCauDangNhap();
        setTitle("KFC - Menu quản lý");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel title = new JLabel("QUẢN LÝ KFC - " + PhienQuanLy.taiKhoan(), SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 19));
        add(title, BorderLayout.NORTH);
        JPanel panel = new JPanel(new GridLayout(4, 1, 12, 12));
        JButton mon = new JButton("QUẢN LÝ MÓN ĂN");
        JButton kho = new JButton("QUẢN LÝ KHO + CÔNG THỨC");
        JButton hd = new JButton("HÓA ĐƠN / DOANH THU NGÀY");
        JButton exit = new JButton("ĐĂNG XUẤT");
        mon.addActionListener(e -> new ql_mon_an().setVisible(true));
        kho.addActionListener(e -> new ql_kho().setVisible(true));
        hd.addActionListener(e -> new ql_hoa_don().setVisible(true));
        exit.addActionListener(e -> {
            PhienQuanLy.dangXuat();
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof ql_mon_an || w instanceof ql_kho || w instanceof ql_hoa_don) w.dispose();
            }
            dispose();
            new dang_nhap().setVisible(true);
        });
        panel.add(mon); panel.add(kho); panel.add(hd); panel.add(exit);
        add(panel, BorderLayout.CENTER);
        setSize(410, 320);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new dang_nhap().setVisible(true));
    }
}
