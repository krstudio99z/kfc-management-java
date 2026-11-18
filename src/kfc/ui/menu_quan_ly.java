package kfc.ui;

import kfc.service.PhienQuanLy;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class menu_quan_ly extends javax.swing.JFrame {

    public menu_quan_ly() {
        PhienQuanLy.yeuCauDangNhap();
        initComponents();
        init();
    }

    // ===================== LOGIC =====================
    private void init() {
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(248, 247, 245));
        lblWelcome.setText("Chào mừng quản lý " + PhienQuanLy.taiKhoan() + " đến với hệ thống KFC!");
    }

    private void dangXuat() {
        PhienQuanLy.dangXuat();
        for (java.awt.Window w : java.awt.Window.getWindows()) {
            if (w instanceof ql_mon_an || w instanceof ql_kho || w instanceof ql_hoa_don) {
                w.dispose();
            }
        }
        dispose();
        new dang_nhap().setVisible(true);
    }

    // ===================== COMPONENT TÙY CHỈNH (STYLE GIỐNG ẢNH MẪU) =====================
    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bgColor;
        private final Color borderColor;

        public RoundedPanel(int radius, Color bgColor, Color borderColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedButton extends JButton {
        private final int radius;
        private final Color baseColor;
        private final Color hoverColor;

        public RoundedButton(String text, Color baseColor, Color hoverColor, int radius) {
            super(text);
            this.baseColor = baseColor;
            this.hoverColor = hoverColor;
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
            g2.setColor(getModel().isRollover() ? hoverColor : baseColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class KfcLogoPanel extends JPanel {
        private Image customLogo;

        public KfcLogoPanel() {
            setOpaque(false);
            customLogo = loadOptionalImage("kfc_logo.png");
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            if (customLogo != null) {
                g2.drawImage(customLogo, 0, 0, w, h, null);
            } else {
                g2.setColor(new Color(228, 0, 43));
                g2.fillOval(2, 2, w - 4, h - 4);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(4, 4, w - 8, h - 8);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                g2.drawString("KFC", w / 2 - 15, h / 2 + 6);
            }
            g2.dispose();
        }
    }

    private static class ManagerShowcasePanel extends JPanel {
        private Image customBanner;

        public ManagerShowcasePanel() {
            setOpaque(false);
            customBanner = loadOptionalImage("kfc_banner.png");
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

            if (customBanner != null) {
                g2.drawImage(customBanner, 12, 12, w - 24, h - 24, null);
                g2.dispose();
                return;
            }

            // Nhan trang thai
            g2.setColor(new Color(40, 167, 69));
            g2.fill(new RoundRectangle2D.Float(16, 14, 112, 24, 12, 12));
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString("KFC Store Admin", 26, 30);

            g2.setColor(new Color(30, 30, 30));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 19));
            g2.drawString("Điều Hành Cửa Hàng", 16, 64);

            g2.setColor(new Color(95, 70, 65));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("• Cập nhật thực đơn & giá bán", 16, 90);
            g2.drawString("• Kiểm soát tồn kho nguyên liệu", 16, 110);
            g2.drawString("• Theo dõi hóa đơn & doanh thu", 16, 130);

            // Minh hoa xo ga KFC nho o goc duoi phai
            int bx = w - 108;
            int by = h - 105;

            g2.setColor(new Color(214, 125, 35));
            g2.fillOval(bx + 10, by - 10, 34, 24);
            g2.fillOval(bx + 36, by - 14, 38, 26);

            int[] xPts = {bx + 8, bx + 84, bx + 74, bx + 18};
            int[] yPts = {by + 6, by + 6, by + 84, by + 84};
            g2.setColor(Color.WHITE);
            g2.fillPolygon(xPts, yPts, 4);

            g2.setColor(new Color(228, 0, 43));
            g2.fillPolygon(new int[]{bx + 10, bx + 24, bx + 29, bx + 19},
                    new int[]{by + 6, by + 6, by + 84, by + 84}, 4);
            g2.fillPolygon(new int[]{bx + 38, bx + 53, bx + 51, bx + 40},
                    new int[]{by + 6, by + 6, by + 84, by + 84}, 4);
            g2.fillPolygon(new int[]{bx + 67, bx + 82, bx + 73, bx + 62},
                    new int[]{by + 6, by + 6, by + 84, by + 84}, 4);

            g2.fill(new RoundRectangle2D.Float(bx + 4, by + 2, 84, 8, 5, 5));

            g2.dispose();
        }
    }

    private static Image loadOptionalImage(String fileName) {
        try {
            URL url = menu_quan_ly.class.getResource("/kfc/images/" + fileName);
            if (url != null) {
                return ImageIO.read(url);
            }
            File f1 = new File("src/kfc/images/" + fileName);
            if (f1.exists()) {
                return ImageIO.read(f1);
            }
            File f2 = new File("images/" + fileName);
            if (f2.exists()) {
                return ImageIO.read(f2);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    // ===================== GIAO DIỆN (kiểu NetBeans) =====================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        pnlHeader = new RoundedPanel(20, Color.WHITE, new Color(235, 232, 228));
        pnlLogo = new KfcLogoPanel();
        lblTitle = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        pnlShowcase = new ManagerShowcasePanel();
        pnlActions = new RoundedPanel(20, Color.WHITE, new Color(235, 232, 228));
        lblMenuTitle = new javax.swing.JLabel();
        mon = new RoundedButton("QUẢN LÝ MÓN ĂN", new Color(228, 0, 43), new Color(195, 0, 36), 14);
        kho = new RoundedButton("QUẢN LÝ KHO + CÔNG THỨC", new Color(228, 0, 43), new Color(195, 0, 36), 14);
        hd = new RoundedButton("HÓA ĐƠN / DOANH THU NGÀY", new Color(228, 0, 43), new Color(195, 0, 36), 14);
        exit = new RoundedButton("ĐĂNG XUẤT", new Color(30, 30, 30), new Color(65, 65, 70), 14);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("KFC - Menu quản lý");
        setResizable(false);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTitle.setForeground(new java.awt.Color(30, 30, 30));
        lblTitle.setText("QUẢN LÝ HỆ THỐNG KFC");

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 0, 13));
        lblWelcome.setForeground(new java.awt.Color(228, 0, 43));
        lblWelcome.setText("Chào mừng quản lý đến với hệ thống KFC!");

        javax.swing.GroupLayout pnlLogoLayout = new javax.swing.GroupLayout(pnlLogo);
        pnlLogo.setLayout(pnlLogoLayout);
        pnlLogoLayout.setHorizontalGroup(
            pnlLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );
        pnlLogoLayout.setVerticalGroup(
            pnlLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(pnlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addComponent(lblWelcome))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pnlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlHeaderLayout.createSequentialGroup()
                        .addComponent(lblTitle)
                        .addGap(2, 2, 2)
                        .addComponent(lblWelcome)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlShowcaseLayout = new javax.swing.GroupLayout(pnlShowcase);
        pnlShowcase.setLayout(pnlShowcaseLayout);
        pnlShowcaseLayout.setHorizontalGroup(
            pnlShowcaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
        );
        pnlShowcaseLayout.setVerticalGroup(
            pnlShowcaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lblMenuTitle.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblMenuTitle.setForeground(new java.awt.Color(55, 55, 55));
        lblMenuTitle.setText("Chức năng quản lý");

        mon.setFont(new java.awt.Font("Segoe UI", 1, 13));
        mon.setText("QUẢN LÝ MÓN ĂN");
        mon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monActionPerformed(evt);
            }
        });

        kho.setFont(new java.awt.Font("Segoe UI", 1, 13));
        kho.setText("QUẢN LÝ KHO + CÔNG THỨC");
        kho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                khoActionPerformed(evt);
            }
        });

        hd.setFont(new java.awt.Font("Segoe UI", 1, 13));
        hd.setText("HÓA ĐƠN / DOANH THU NGÀY");
        hd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hdActionPerformed(evt);
            }
        });

        exit.setFont(new java.awt.Font("Segoe UI", 1, 12));
        exit.setText("ĐĂNG XUẤT");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlActionsLayout = new javax.swing.GroupLayout(pnlActions);
        pnlActions.setLayout(pnlActionsLayout);
        pnlActionsLayout.setHorizontalGroup(
            pnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlActionsLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMenuTitle)
                    .addComponent(mon, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kho, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hd, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlActionsLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        pnlActionsLayout.setVerticalGroup(
            pnlActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlActionsLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblMenuTitle)
                .addGap(12, 12, 12)
                .addComponent(mon, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(kho, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(hd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlShowcase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(pnlActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlShowcase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlActions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    // ===================== SỰ KIỆN =====================
    private void monActionPerformed(java.awt.event.ActionEvent evt) {
        new ql_mon_an().setVisible(true);
    }

    private void khoActionPerformed(java.awt.event.ActionEvent evt) {
        new ql_kho().setVisible(true);
    }

    private void hdActionPerformed(java.awt.event.ActionEvent evt) {
        new ql_hoa_don().setVisible(true);
    }

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {
        dangXuat();
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
            java.util.logging.Logger.getLogger(menu_quan_ly.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dang_nhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton exit;
    private javax.swing.JButton hd;
    private javax.swing.JButton kho;
    private javax.swing.JLabel lblMenuTitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JButton mon;
    private javax.swing.JPanel pnlActions;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlLogo;
    private javax.swing.JPanel pnlShowcase;
    // End of variables declaration
}