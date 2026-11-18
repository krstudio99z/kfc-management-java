package kfc.ui;

import kfc.dao.TaiKhoanDAO;
import kfc.service.PhienQuanLy;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class dang_nhap extends JFrame {
    private static final Color KFC_RED = new Color(228, 0, 43);
    private static final Color TEXT_DARK = new Color(35, 35, 38);
    private static final Color TEXT_MUTED = new Color(125, 125, 130);

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public dang_nhap() {
        initComponents();
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("KFC - Đăng nhập quản lý");
        setResizable(false);

        JPanel content = new JPanel(new BorderLayout());
        content.setPreferredSize(new Dimension(900, 560));
        content.add(createBrandPanel(), BorderLayout.WEST);
        content.add(createFormPanel(), BorderLayout.CENTER);
        setContentPane(content);
        pack();
    }

    private JPanel createBrandPanel() {
        HeroPanel hero = new HeroPanel();
        hero.setPreferredSize(new Dimension(345, 560));
        hero.setBorder(new EmptyBorder(42, 38, 38, 38));
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));

        JPanel logo = new JPanel(new BorderLayout());
        logo.setOpaque(false);
        logo.setPreferredSize(new Dimension(60, 60));
        logo.setMaximumSize(new Dimension(60, 60));
        JLabel logoText = new JLabel("KFC", SwingConstants.CENTER);
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 19));
        logoText.setForeground(KFC_RED);
        logoText.setOpaque(true);
        logoText.setBackground(Color.WHITE);
        logoText.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 180), 3));
        logo.add(logoText);

        JLabel brand = new JLabel("KENTUCKY FRIED CHICKEN");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 11));
        brand.setForeground(new Color(255, 220, 226));
        brand.setAlignmentX(LEFT_ALIGNMENT);

        JLabel headline = new JLabel("<html>Vận hành<br>thật dễ dàng.</html>");
        headline.setFont(new Font("Segoe UI", Font.BOLD, 31));
        headline.setForeground(Color.WHITE);
        headline.setAlignmentX(LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html>Đăng nhập để quản lý thực đơn,<br>kho nguyên liệu và hóa đơn<br>của cửa hàng.</html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        description.setForeground(new Color(255, 230, 234));
        description.setAlignmentX(LEFT_ALIGNMENT);

        JLabel footer = new JLabel("QUẢN LÝ CỬA HÀNG  ·  KFC");
        footer.setFont(new Font("Segoe UI", Font.BOLD, 10));
        footer.setForeground(new Color(255, 210, 218));
        footer.setAlignmentX(LEFT_ALIGNMENT);

        hero.add(logo);
        hero.add(Box.createVerticalStrut(18));
        hero.add(brand);
        hero.add(Box.createVerticalGlue());
        hero.add(headline);
        hero.add(Box.createVerticalStrut(14));
        hero.add(description);
        hero.add(Box.createVerticalGlue());
        hero.add(footer);
        return hero;
    }

    private JPanel createFormPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(new Color(250, 249, 247));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(38, 42, 38, 42));
        form.setPreferredSize(new Dimension(430, 470));

        JLabel badge = new JLabel("  KHU VỰC QUẢN LÝ  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(KFC_RED);
        badge.setBackground(new Color(255, 239, 242));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(7, 3, 7, 3));
        badge.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("Chào mừng trở lại");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Nhập thông tin để truy cập trang quản lý.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        JLabel userLabel = createFieldLabel("TÀI KHOẢN");
        txtUser = createTextField(false);
        JLabel passwordLabel = createFieldLabel("MẬT KHẨU");
        txtPass = (JPasswordField) createTextField(true);

        btnLogin = new RoundedButton("ĐĂNG NHẬP  →");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setAlignmentX(LEFT_ALIGNMENT);
        btnLogin.addActionListener(evt -> dangNhap());

        form.add(badge);
        form.add(Box.createVerticalStrut(22));
        form.add(title);
        form.add(Box.createVerticalStrut(7));
        form.add(subtitle);
        form.add(Box.createVerticalStrut(30));
        form.add(userLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(txtUser);
        form.add(Box.createVerticalStrut(20));
        form.add(passwordLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(txtPass);
        form.add(Box.createVerticalStrut(26));
        form.add(btnLogin);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(24, 24, 24, 24);
        outer.add(form, gbc);
        return outer;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(new Color(75, 75, 80));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField(boolean password) {
        JTextField field = password ? new JPasswordField() : new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_DARK);
        field.setBackground(new Color(250, 250, 251));
        field.setCaretColor(KFC_RED);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 229, 232)),
                new EmptyBorder(0, 13, 0, 13)));
        field.setPreferredSize(new Dimension(346, 46));
        field.setMinimumSize(new Dimension(346, 46));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        field.setAlignmentX(LEFT_ALIGNMENT);
        return field;
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

    private static class RoundedButton extends JButton {
        private RoundedButton(String text) {
            super(text);
            setForeground(Color.WHITE);
            setBackground(KFC_RED);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(new EmptyBorder(14, 18, 14, 18));
            setPreferredSize(new Dimension(346, 50));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill = new Color(195, 0, 36);
            if (getModel().isPressed()) {
                fill = new Color(165, 0, 31);
            } else if (getModel().isRollover()) {
                fill = new Color(242, 35, 69);
            }
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class HeroPanel extends JPanel {
        private HeroPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(new GradientPaint(0, 0, new Color(235, 12, 52),
                    getWidth(), getHeight(), new Color(150, 0, 31)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(255, 255, 255, 18));
            g2.fill(new Ellipse2D.Float(getWidth() - 112, 82, 200, 200));
            g2.setColor(new Color(255, 255, 255, 13));
            g2.fill(new Ellipse2D.Float(getWidth() - 70, 125, 115, 115));
            g2.setColor(new Color(255, 255, 255, 35));
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(22, 22, getWidth() - 44, getHeight() - 44, 24, 24));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dang_nhap.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new dang_nhap().setVisible(true));
    }
}
