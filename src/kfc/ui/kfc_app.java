package kfc.ui;

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

/**
 * Chay class nay de chon vao giao dien khach hay quan ly.
 */
public class kfc_app extends javax.swing.JFrame {

    public kfc_app() {
        initComponents();
        init();
    }

    // ===================== LOGIC =====================
    private void init() {
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(248, 247, 245));
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

    private static class KfcHeroArtworkPanel extends JPanel {

        private Image customBanner;

        public KfcHeroArtworkPanel() {
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

            // Card nen hong dao giong the "5 leg Piece" trong anh mau
            g2.setPaint(new GradientPaint(0, 0, new Color(253, 226, 220), w, h, new Color(249, 203, 194)));
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 24, 24));

            if (customBanner != null) {
                g2.drawImage(customBanner, 12, 12, w - 24, h - 24, null);
                g2.dispose();
                return;
            }

            // Nhan "Hot Deal"
            g2.setColor(new Color(255, 193, 7));
            g2.fill(new RoundRectangle2D.Float(18, 16, 78, 24, 12, 12));
            g2.setColor(new Color(45, 30, 10));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString("Hot Deal", 33, 32);

            // Tieu de quang ba ben trai
            g2.setColor(new Color(30, 30, 30));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 21));
            g2.drawString("5 Leg Piece", 18, 68);
            g2.drawString("Bucket", 18, 93);

            g2.setColor(new Color(100, 75, 70));
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("Gà rán giòn rụm", 18, 116);
            g2.drawString("Nóng hổi mỗi ngày", 18, 134);

            // Ve minh hoa xo ga KFC do-trang + mieng ga vang gion ben phai
            int bx = w - 135;
            int by = 48;

            // Mieng ga ran vang ruom phia tren xo
            g2.setColor(new Color(214, 125, 35));
            g2.fillOval(bx + 8, by - 16, 42, 30);
            g2.fillOval(bx + 36, by - 20, 46, 32);
            g2.fillOval(bx + 62, by - 12, 38, 28);
            g2.setColor(new Color(242, 166, 65));
            g2.fillOval(bx + 15, by - 12, 28, 18);
            g2.fillOval(bx + 44, by - 14, 30, 18);

            // Than xo KFC soc do trang
            int[] xPts = {bx + 8, bx + 102, bx + 90, bx + 20};
            int[] yPts = {by + 6, by + 6, by + 104, by + 104};
            g2.setColor(Color.WHITE);
            g2.fillPolygon(xPts, yPts, 4);

            // 3 soc do dac trung cua KFC
            g2.setColor(new Color(228, 0, 43));
            g2.fillPolygon(new int[]{bx + 10, bx + 28, bx + 34, bx + 21},
                    new int[]{by + 6, by + 6, by + 104, by + 104}, 4);
            g2.fillPolygon(new int[]{bx + 46, bx + 64, bx + 62, bx + 48},
                    new int[]{by + 6, by + 6, by + 104, by + 104}, 4);
            g2.fillPolygon(new int[]{bx + 82, bx + 100, bx + 89, bx + 76},
                    new int[]{by + 6, by + 6, by + 104, by + 104}, 4);

            // Vien mieng xo
            g2.setColor(new Color(228, 0, 43));
            g2.fill(new RoundRectangle2D.Float(bx + 4, by + 2, 102, 10, 6, 6));

            // Nhan KFC giua xo
            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(bx + 31, by + 38, 48, 24, 8, 8));
            g2.setColor(new Color(228, 0, 43));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString("KFC", bx + 41, by + 55);

            g2.dispose();
        }
    }

    private static Image loadOptionalImage(String fileName) {
        try {
            URL url = kfc_app.class.getResource("/kfc/images/" + fileName);
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
        lblBrand = new javax.swing.JLabel();
        lblHours = new javax.swing.JLabel();
        lblBadge = new javax.swing.JLabel();
        pnlHeroCard = new KfcHeroArtworkPanel();
        pnlWelcomeCard = new RoundedPanel(20, Color.WHITE, new Color(235, 232, 228));
        lblGreeting = new javax.swing.JLabel();
        lblSubGreeting = new javax.swing.JLabel();
        lblHighlight1 = new javax.swing.JLabel();
        lblHighlight2 = new javax.swing.JLabel();
        pnlBottomBar = new RoundedPanel(22, new Color(30, 30, 30), null);
        lblChooseMode = new javax.swing.JLabel();
        guest = new RoundedButton("KHÁCH HÀNG - ĐẶT MÓN", new Color(228, 0, 43), new Color(195, 0, 36), 16);
        manager = new RoundedButton("QUẢN LÝ - ĐĂNG NHẬP", new Color(62, 62, 66), new Color(85, 85, 90), 16);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("KFC - Hệ thống đặt món");
        setResizable(false);

        lblBrand.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblBrand.setForeground(new java.awt.Color(30, 30, 30));
        lblBrand.setText("Kentucky Fried Chicken");

        lblHours.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblHours.setForeground(new java.awt.Color(200, 120, 40));
        lblHours.setText("Open 7:30 AM  •  Close 11:30 PM");

        lblBadge.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblBadge.setForeground(new java.awt.Color(228, 0, 43));
        lblBadge.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBadge.setText("KFC VIETNAM");

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
                                        .addComponent(lblBrand)
                                        .addComponent(lblHours))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblBadge, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16))
        );
        pnlHeaderLayout.setVerticalGroup(
                pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlHeaderLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                        .addComponent(pnlLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlHeaderLayout.createSequentialGroup()
                                                .addComponent(lblBrand)
                                                .addGap(2, 2, 2)
                                                .addComponent(lblHours))
                                        .addComponent(lblBadge))
                                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlHeroCardLayout = new javax.swing.GroupLayout(pnlHeroCard);
        pnlHeroCard.setLayout(pnlHeroCardLayout);
        pnlHeroCardLayout.setHorizontalGroup(
                pnlHeroCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 310, Short.MAX_VALUE)
        );
        pnlHeroCardLayout.setVerticalGroup(
                pnlHeroCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 185, Short.MAX_VALUE)
        );

        lblGreeting.setFont(new java.awt.Font("Segoe UI", 1, 17));
        lblGreeting.setForeground(new java.awt.Color(228, 0, 43));
        lblGreeting.setText("Chào mừng bạn đã đến với KFC!");

        lblSubGreeting.setFont(new java.awt.Font("Segoe UI", 0, 12));
        lblSubGreeting.setForeground(new java.awt.Color(95, 95, 95));
        lblSubGreeting.setText("<html>Hệ thống gọi món & quản lý cửa hàng nhanh chóng, tiện lợi.</html>");

        lblHighlight1.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblHighlight1.setForeground(new java.awt.Color(40, 40, 40));
        lblHighlight1.setText("• Thực đơn gà rán, burger & combo hấp dẫn");

        lblHighlight2.setFont(new java.awt.Font("Segoe UI", 1, 12));
        lblHighlight2.setForeground(new java.awt.Color(40, 40, 40));
        lblHighlight2.setText("• Phục vụ tại quầy & in hóa đơn tức thì");

        javax.swing.GroupLayout pnlWelcomeCardLayout = new javax.swing.GroupLayout(pnlWelcomeCard);
        pnlWelcomeCard.setLayout(pnlWelcomeCardLayout);
        pnlWelcomeCardLayout.setHorizontalGroup(
                pnlWelcomeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlWelcomeCardLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(pnlWelcomeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblGreeting, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                        .addComponent(lblSubGreeting, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(lblHighlight1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblHighlight2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(16, 16, 16))
        );
        pnlWelcomeCardLayout.setVerticalGroup(
                pnlWelcomeCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlWelcomeCardLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblGreeting)
                                .addGap(8, 8, 8)
                                .addComponent(lblSubGreeting, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(lblHighlight1)
                                .addGap(8, 8, 8)
                                .addComponent(lblHighlight2)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblChooseMode.setFont(new java.awt.Font("Segoe UI", 1, 13));
        lblChooseMode.setForeground(new java.awt.Color(235, 235, 235));
        lblChooseMode.setText("Chọn chế độ truy cập:");

        guest.setFont(new java.awt.Font("Segoe UI", 1, 13));
        guest.setText("KHÁCH HÀNG - ĐẶT MÓN");
        guest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guestActionPerformed(evt);
            }
        });

        manager.setFont(new java.awt.Font("Segoe UI", 1, 13));
        manager.setText("QUẢN LÝ - ĐĂNG NHẬP");
        manager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                managerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBottomBarLayout = new javax.swing.GroupLayout(pnlBottomBar);
        pnlBottomBar.setLayout(pnlBottomBarLayout);
        pnlBottomBarLayout.setHorizontalGroup(
                pnlBottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlBottomBarLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblChooseMode)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(guest, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(manager, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(16, 16, 16))
        );
        pnlBottomBarLayout.setVerticalGroup(
                pnlBottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlBottomBarLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(pnlBottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblChooseMode)
                                        .addComponent(guest, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(manager, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(12, Short.MAX_VALUE))
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
                                                .addComponent(pnlHeroCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(14, 14, 14)
                                                .addComponent(pnlWelcomeCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(pnlBottomBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlHeroCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pnlWelcomeCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(14, 14, 14)
                                .addComponent(pnlBottomBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    // ===================== SỰ KIỆN =====================
    private void guestActionPerformed(java.awt.event.ActionEvent evt) {
        new dat_mon().setVisible(true);
    }

    private void managerActionPerformed(java.awt.event.ActionEvent evt) {
        new dang_nhap().setVisible(true);
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
            java.util.logging.Logger.getLogger(kfc_app.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new kfc_app().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton guest;
    private javax.swing.JLabel lblBadge;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblChooseMode;
    private javax.swing.JLabel lblGreeting;
    private javax.swing.JLabel lblHighlight1;
    private javax.swing.JLabel lblHighlight2;
    private javax.swing.JLabel lblHours;
    private javax.swing.JLabel lblSubGreeting;
    private javax.swing.JButton manager;
    private javax.swing.JPanel pnlBottomBar;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHeroCard;
    private javax.swing.JPanel pnlLogo;
    private javax.swing.JPanel pnlWelcomeCard;
    // End of variables declaration
}
