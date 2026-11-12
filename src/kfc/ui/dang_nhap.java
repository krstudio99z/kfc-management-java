package kfc.ui;

import kfc.dao.TaiKhoanDAO;
import kfc.service.PhienQuanLy;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class dang_nhap extends JFrame {
    private final JTextField txtUser = new JTextField(19);
    private final JPasswordField txtPass = new JPasswordField(19);

    public dang_nhap() {
        setTitle("KFC - Đăng nhập quản lý");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel body = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(9, 10, 9, 10);
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        JLabel heading = new JLabel("ĐĂNG NHẬP QUẢN LÝ KFC");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        body.add(heading, gc);
        gc.gridwidth = 1; gc.gridy++;
        body.add(new JLabel("Tài khoản:"), gc);
        gc.gridx = 1; body.add(txtUser, gc);
        gc.gridx = 0; gc.gridy++;
        body.add(new JLabel("Mật khẩu:"), gc);
        gc.gridx = 1; body.add(txtPass, gc);
        gc.gridx = 0; gc.gridy++; gc.gridwidth = 2;
        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.addActionListener(e -> dangNhap());
        body.add(btnLogin, gc);
        getRootPane().setDefaultButton(btnLogin);
        add(body, BorderLayout.CENTER);
        pack(); setResizable(false); setLocationRelativeTo(null);
    }

    private void dangNhap() {
        char[] password = txtPass.getPassword();
        try {
            boolean ok = new TaiKhoanDAO().dangNhap(txtUser.getText(), password);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
                txtPass.setText("");
                return;
            }
            PhienQuanLy.dangNhap(txtUser.getText().trim());
            new menu_quan_ly().setVisible(true);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đăng nhập: " + e.getMessage());
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new dang_nhap().setVisible(true));
    }
}
