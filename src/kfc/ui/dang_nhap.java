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
import java.awt.Image;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

public class dang_nhap extends javax.swing.JFrame {

    private static final Color KFC_RED = new Color(228, 0, 43);
    private static final Color PAGE_BACKGROUND = new Color(248, 247, 245);
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public dang_nhap() {
        initComponents();
        init();
    }

    private void init() {
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void dangNhap() {
        char[] password = txtPass.getPassword();
        try {
            if(PhienQuanLy.daDangNhap()){JOptionPane.showMessageDialog(this,"Đã có phiên quản lý. Hãy đăng xuất trước.");return;}
            String ten = txtUser.getText().trim();
            String key = ten.toLowerCase(java.util.Locale.ROOT);
            TaiKhoanDAO.kiemTraKhoa(key);
            TaiKhoanDAO dao = new TaiKhoanDAO();
            boolean ok = dao.dangNhap(ten, password);
            TaiKhoanDAO.ghiKetQua(key, ok);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
                txtPass.setText("");
                return;
            }
            PhienQuanLy.dangNhap(ten, dao.getVaiTro());
            try {new menu_quan_ly().setVisible(true);} catch(RuntimeException ex){PhienQuanLy.dangXuat();throw ex;}
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đăng nhập: " + e.getMessage());
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("KFC - Đăng nhập quản lý");
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBackground(PAGE_BACKGROUND);
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        RoundedPanel header = new RoundedPanel(20, Color.WHITE, new Color(235, 232, 228));
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 10));
        KfcLogoPanel logo = new KfcLogoPanel();
        logo.setPreferredSize(new Dimension(46, 46));
        header.add(logo);

        JPanel brand = new JPanel(new GridLayout(2, 1, 0, 2));
        brand.setOpaque(false);
        JLabel brandName = new JLabel("Kentucky Fried Chicken");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandName.setForeground(new Color(30, 30, 30));
        JLabel brandCaption = new JLabel("CỔNG QUẢN LÝ CỬA HÀNG");
        brandCaption.setFont(new Font("Segoe UI", Font.BOLD, 11));
        brandCaption.setForeground(KFC_RED);
        brand.add(brandName);
        brand.add(brandCaption);
        header.add(brand);
        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(14, 0));
        content.setOpaque(false);
        content.add(new ManagerShowcasePanel(), BorderLayout.CENTER);
        content.add(createLoginCard(), BorderLayout.EAST);
        root.add(content, BorderLayout.CENTER);

        JLabel footer = new JLabel("KFC VIETNAM  •  HỆ THỐNG QUẢN LÝ", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.BOLD, 10));
        footer.setForeground(new Color(130, 125, 122));
        root.add(footer, BorderLayout.SOUTH);

        setSize(700, 470);
        setMinimumSize(new Dimension(700, 470));
    }

    private JPanel createLoginCard() {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE, new Color(235, 232, 228));
        card.setPreferredSize(new Dimension(310, 0));
        card.setLayout(new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 24, 20, 24));

        JLabel title = new JLabel("Đăng nhập quản lý");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(new Color(32, 32, 32));
        JLabel description = new JLabel("Vui lòng nhập thông tin tài khoản");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        description.setForeground(new Color(110, 110, 110));

        JLabel userLabel = createFieldLabel("TÀI KHOẢN");
        txtUser = createTextField();
        JLabel passLabel = createFieldLabel("MẬT KHẨU");
        txtPass = new JPasswordField();
        styleTextField(txtPass);

        btnLogin = new RoundedButton("ĐĂNG NHẬP", KFC_RED, new Color(195, 0, 36), 14);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setPreferredSize(new Dimension(250, 42));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnLogin.addActionListener(evt -> dangNhap());

        JLabel securityNote = new JLabel("BẢO MẬT  •  CHỈ DÀNH CHO QUẢN LÝ");
        securityNote.setFont(new Font("Segoe UI", Font.BOLD, 9));
        securityNote.setForeground(new Color(145, 140, 137));
        securityNote.setAlignmentX(LEFT_ALIGNMENT);

        card.add(title);
        card.add(javax.swing.Box.createVerticalStrut(5));
        card.add(description);
        card.add(javax.swing.Box.createVerticalStrut(25));
        card.add(userLabel);
        card.add(javax.swing.Box.createVerticalStrut(7));
        card.add(txtUser);
        card.add(javax.swing.Box.createVerticalStrut(16));
        card.add(passLabel);
        card.add(javax.swing.Box.createVerticalStrut(7));
        card.add(txtPass);
        card.add(javax.swing.Box.createVerticalStrut(22));
        card.add(btnLogin);
        card.add(javax.swing.Box.createVerticalGlue());
        card.add(securityNote);
        return card;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(new Color(65, 65, 65));
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(new Color(45, 45, 45));
        field.setBackground(new Color(250, 250, 250));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 221, 218)),
                new EmptyBorder(9, 10, 9, 10)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(250, 40));
        field.setAlignmentX(LEFT_ALIGNMENT);
    }

    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color background;
        private final Color border;

        RoundedPanel(int radius, Color background, Color border) {
            this.radius = radius;
            this.background = background;
            this.border = border;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            if (border != null) {
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            }
            g2.dispose();
        }
    }

    private static class RoundedButton extends JButton {
        private final int radius;
        private final Color normal;
        private final Color hover;

        RoundedButton(String text, Color normal, Color hover, int radius) {
            super(text);
            this.normal = normal;
            this.hover = hover;
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hover : normal);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class KfcLogoPanel extends JPanel {
        private final Image customLogo=KfcImages.asset("kfc/images/kfc_logo.png");
        KfcLogoPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if(customLogo!=null){KfcImages.drawContain(g2,customLogo,0,0,getWidth(),getHeight());g2.dispose();return;}
            int size = Math.min(getWidth(), getHeight());
            g2.setColor(KFC_RED);
            g2.fillOval(1, 1, size - 2, size - 2);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(4, 4, size - 8, size - 8);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
            g2.drawString("KFC", (size - g2.getFontMetrics().stringWidth("KFC")) / 2, size / 2 + 5);
            g2.dispose();
        }
    }

    private static class ManagerShowcasePanel extends JPanel {
        ManagerShowcasePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(330, 0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setPaint(new GradientPaint(0, 0, new Color(253, 226, 220), w, h, new Color(249, 203, 194)));
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 22, 22));

            g2.setColor(new Color(40, 167, 69));
            g2.fill(new RoundRectangle2D.Float(22, 24, 125, 25, 13, 13));
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString("KFC STORE ADMIN", 34, 41);

            g2.setColor(new Color(32, 32, 32));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 23));
            g2.drawString("Điều hành", 22, 91);
            g2.drawString("cửa hàng", 22, 120);
            g2.setColor(new Color(100, 75, 70));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            g2.drawString("Quản lý cửa hàng KFC", 22, 148);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("•  Thực đơn và giá bán", 22, 190);
            g2.drawString("•  Tồn kho và công thức", 22, 214);
            g2.drawString("•  Hóa đơn và doanh thu", 22, 238);

            int bx = w - 112;
            int by = h - 112;
            g2.setColor(new Color(214, 125, 35));
            g2.fillOval(bx + 12, by - 12, 36, 27);
            g2.fillOval(bx + 40, by - 16, 40, 29);
            g2.setColor(Color.WHITE);
            g2.fillPolygon(new int[]{bx + 8, bx + 86, bx + 75, bx + 19},
                    new int[]{by + 5, by + 5, by + 86, by + 86}, 4);
            g2.setColor(KFC_RED);
            g2.fillPolygon(new int[]{bx + 11, bx + 25, bx + 30, bx + 20},
                    new int[]{by + 6, by + 6, by + 86, by + 86}, 4);
            g2.fillPolygon(new int[]{bx + 39, bx + 54, bx + 52, bx + 41},
                    new int[]{by + 6, by + 6, by + 86, by + 86}, 4);
            g2.fillPolygon(new int[]{bx + 68, bx + 82, bx + 74, bx + 63},
                    new int[]{by + 6, by + 6, by + 86, by + 86}, 4);
            g2.fill(new RoundRectangle2D.Float(bx + 4, by + 2, 86, 9, 5, 5));
            g2.dispose();
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dang_nhap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dang_nhap().setVisible(true);
            }
        });
    }
}
