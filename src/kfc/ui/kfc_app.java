package kfc.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/** Chay class nay de chon vao giao dien khach hay quan ly. */
public class kfc_app extends JFrame {
    public kfc_app() {
        setTitle("KFC - Hệ thống đặt món");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel title = new JLabel("HỆ THỐNG KFC", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);
        JPanel p = new JPanel(new GridLayout(2, 1, 12, 12));
        JButton guest = new JButton("KHÁCH HÀNG - ĐẶT MÓN");
        JButton manager = new JButton("QUẢN LÝ - ĐĂNG NHẬP");
        guest.addActionListener(e -> new dat_mon().setVisible(true));
        manager.addActionListener(e -> new dang_nhap().setVisible(true));
        p.add(guest); p.add(manager);
        add(p, BorderLayout.CENTER);
        setSize(400, 230);
        setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new kfc_app().setVisible(true));
    }
}
