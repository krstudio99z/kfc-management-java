package kfc.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.RowSorterEvent;
import javax.swing.table.DefaultTableModel;

/**
 * Xem thực đơn dưới dạng card có ảnh. Bảng gốc vẫn được giữ ở tab "Xem bảng".
 * Nút Thêm chọn đúng dòng của JTable rồi gọi lại addToCart() cũ trong dat_mon.
 * Không thay đổi bất kỳ thao tác đặt hàng, kho, hóa đơn nào.
 */
public final class KfcMenuCards {
    private final JTable table;
    private final Runnable onAdd;
    private final DecimalFormat money = new DecimalFormat("#,##0.##");
    private final ScrollPanel cards = new ScrollPanel();
    
    private final JComboBox<String> categories = new JComboBox<>(new String[]{
        "Tất cả món", "Combo", "Gà rán", "Burger", "Món kèm", "Nước uống", "Tráng miệng"
    });
    private final JPanel views = new JPanel(new CardLayout());
    private final JButton switchMode = new JButton("☷  Xem bảng");
    private boolean pending;
    private boolean showCards = true;

    private KfcMenuCards(JTable table, Runnable onAdd) {
        this.table = table;
        this.onAdd = onAdd;
    }

    public static void install(JTable originalTable, JScrollPane originalScroll, Runnable onAdd) {
        KfcMenuCards gallery = new KfcMenuCards(originalTable, onAdd);
        gallery.mount(originalScroll);
    }

    private void mount(JScrollPane outer) {
        cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));
        cards.setBackground(KfcTheme.BACKGROUND);
        cards.setBorder(new EmptyBorder(12, 6, 12, 6));

        JScrollPane cardScroll = new JScrollPane(cards);
        cardScroll.setBorder(null);
        cardScroll.getViewport().setBackground(KfcTheme.BACKGROUND);
        cardScroll.getVerticalScrollBar().setUnitIncrement(18);
        cardScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(KfcTheme.LINE));
        tableScroll.getViewport().setBackground(Color.WHITE);
        tableScroll.getVerticalScrollBar().setUnitIncrement(16);

        views.add(cardScroll, "cards");
        views.add(tableScroll, "table");
        views.setBackground(KfcTheme.BACKGROUND);

        JPanel toolbar = new JPanel(new BorderLayout(8, 0));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(10, 9, 10, 9));
        categories.setFont(new Font("Segoe UI", Font.BOLD, 12));
        categories.setBackground(Color.WHITE);
        categories.setPreferredSize(new Dimension(145, 32));
        categories.addActionListener(e -> scheduleRebuild());
        toolbar.add(categories, BorderLayout.CENTER);

        switchMode.setBackground(KfcTheme.RED);
        switchMode.setForeground(Color.WHITE);
        switchMode.setFont(new Font("Segoe UI", Font.BOLD, 12));
        switchMode.setFocusPainted(false);
        switchMode.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        switchMode.setBorder(new EmptyBorder(7, 11, 7, 11));
        switchMode.addActionListener(e -> {
            showCards = !showCards;
            ((CardLayout) views.getLayout()).show(views, showCards ? "cards" : "table");
            switchMode.setText(showCards ? "☷  Xem bảng" : "▦  Xem ảnh");
            categories.setEnabled(showCards);
        });
        toolbar.add(switchMode, BorderLayout.EAST);

        FillViewportPanel holder = new FillViewportPanel(new BorderLayout());
        holder.setBackground(KfcTheme.BACKGROUND);
        holder.add(toolbar, BorderLayout.NORTH);
        holder.add(views, BorderLayout.CENTER);
        outer.setViewportView(holder);
        outer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        outer.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        outer.setBorder(null);

        table.getModel().addTableModelListener(e -> scheduleRebuild());
        table.getRowSorter().addRowSorterListener((RowSorterEvent e) -> scheduleRebuild());
        scheduleRebuild();
    }

    private void scheduleRebuild() {
        if (pending) return;
        pending = true;
        SwingUtilities.invokeLater(() -> {
            pending = false;
            rebuild();
        });
    }

    private void rebuild() {
        int oldScroll = 0;
        java.awt.Container parent = cards.getParent();
        if (parent instanceof javax.swing.JViewport) {
            oldScroll = ((javax.swing.JViewport) parent).getViewPosition().y;
        }
        cards.removeAll();
        String category = (String) categories.getSelectedItem();
        for(int i=0;i<table.getModel().getRowCount();i++){String dm=str(table.getModel().getValueAt(i,4)); if(!dm.isEmpty()){boolean exists=false;for(int j=0;j<categories.getItemCount();j++)if(dm.equals(categories.getItemAt(j)))exists=true;if(!exists)categories.addItem(dm);}}
        int displayed = 0;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int view = 0; view < table.getRowCount(); view++) {
            int modelRow = table.convertRowIndexToModel(view);
            String code = str(model.getValueAt(modelRow, 0));
            String name = str(model.getValueAt(modelRow, 1));
            java.math.BigDecimal price = (java.math.BigDecimal) model.getValueAt(modelRow, 2);
            String status = str(model.getValueAt(modelRow, 3));
            String dbCategory = str(model.getValueAt(modelRow, 4));
            if (!"Tất cả món".equals(category) && !(dbCategory.isEmpty() ? matchesCategory(category,name) : category.equals(dbCategory))) continue;
            JPanel item = makeCard(code, name, price, status, modelRow);
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
            cards.add(item);
            cards.add(Box.createVerticalStrut(11));
            displayed++;
        }
        if (displayed == 0) {
            JLabel empty = new JLabel("Chưa có món phù hợp với tìm kiếm / danh mục.", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            empty.setForeground(KfcTheme.MUTED);
            empty.setBorder(new EmptyBorder(55, 4, 55, 4));
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            cards.add(empty);
        }
        cards.revalidate();
        cards.repaint();
        if (parent instanceof javax.swing.JViewport) {
            final int y = oldScroll;
            SwingUtilities.invokeLater(() -> ((javax.swing.JViewport) parent).setViewPosition(
                    new java.awt.Point(0, Math.min(y, Math.max(0, cards.getHeight() - parent.getHeight())))));
        }
    }

    private JPanel makeCard(String code, String name, java.math.BigDecimal price, String status, int modelRow) {
        RoundedCard card = new RoundedCard();
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(12, 12, 12, 12));
        card.setPreferredSize(new Dimension(350, 158));

        String path = str(table.getModel().getValueAt(modelRow,5));
        JLabel photo = new JLabel(imageFor(code, name, path));
        photo.setPreferredSize(new Dimension(152, 132));
        photo.setMinimumSize(new Dimension(152, 132));
        photo.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(photo, BorderLayout.WEST);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("<html><body style='width: 155px'>" + name.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") + "</body></html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(KfcTheme.INK);
        title.setToolTipText(name);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel moneyLabel = new JLabel(money.format(price) + " đ");
        moneyLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        moneyLabel.setForeground(KfcTheme.RED);
        moneyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel availability = new JLabel(status + "  •  " + code);
        availability.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        availability.setForeground("Còn món".equals(status) ? new Color(24, 125, 71) : KfcTheme.RED_DARK);
        availability.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton add = new JButton("+  Thêm");
        add.setFont(new Font("Segoe UI", Font.BOLD, 12));
        add.setForeground(Color.WHITE);
        add.setBackground(KfcTheme.RED);
        add.setFocusPainted(false);
        add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add.setEnabled("Còn món".equals(status));
        add.setAlignmentX(Component.LEFT_ALIGNMENT);
        add.addActionListener(e -> {
            int found=-1; for(int i=0;i<table.getModel().getRowCount();i++) if(code.equals(str(table.getModel().getValueAt(i,0)))) {found=i;break;}
            int row = found<0?-1:table.convertRowIndexToView(found);
            if (row >= 0) {
                table.getSelectionModel().setSelectionInterval(row, row);
                onAdd.run();
            }
        });

        info.add(title);
        info.add(Box.createVerticalStrut(9));
        info.add(moneyLabel);
        info.add(Box.createVerticalStrut(6));
        info.add(availability);
        info.add(Box.createVerticalGlue());
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.add(add);
        info.add(actions);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private ImageIcon imageFor(String code, String name, String path) {
        return KfcImages.menuIcon(code, name, path, 152, 132);
    }

    private static boolean matchesCategory(String category, String name) {
        if (category == null || "Tất cả món".equals(category)) return true;
        String n = slug(name);
        switch (category) {
            case "Combo": return n.contains("combo") || n.contains("phan_an") || n.contains("set_");
            case "Gà rán": return !n.contains("combo") && !n.contains("burger") && (n.contains("ga") || n.contains("chicken") || n.contains("wing"));
            case "Burger": return n.contains("burger") || n.contains("hamburger") || n.contains("banh_mi");
            case "Món kèm": return n.contains("khoai") || n.contains("popcorn") || n.contains("vien") || n.contains("nugget") || n.contains("salad");
            case "Tráng miệng": return n.contains("kem") || n.contains("sundae");
            case "Nước uống": return n.contains("pepsi") || n.contains("coca") || n.contains("nuoc") || n.contains("tra") || n.contains("7up");
            default: return true;
        }
    }

    private static String slug(String input) {
        String s = Normalizer.normalize(input == null ? "" : input.toLowerCase(Locale.ROOT)
                .replace('đ', 'd'), Normalizer.Form.NFD);
        return s.replaceAll("\\p{M}+", "").replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
    }

    private static String str(Object value) { return value == null ? "" : value.toString(); }

    private static final class RoundedCard extends JPanel {
        RoundedCard() { setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE); g2.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 17, 17);
            g2.setColor(KfcTheme.LINE); g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 17, 17);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static final class ScrollPanel extends JPanel implements Scrollable {
        @Override public Dimension getPreferredScrollableViewportSize() { return new Dimension(410, 420); }
        @Override public int getScrollableUnitIncrement(Rectangle visible, int direction, int orientation) { return 24; }
        @Override public int getScrollableBlockIncrement(Rectangle visible, int direction, int orientation) { return 140; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }

    private static final class FillViewportPanel extends JPanel implements Scrollable {
        FillViewportPanel(java.awt.LayoutManager layout) { super(layout); }
        @Override public Dimension getPreferredScrollableViewportSize() { return new Dimension(410, 420); }
        @Override public int getScrollableUnitIncrement(Rectangle visible, int direction, int orientation) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle visible, int direction, int orientation) { return 140; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return true; }
    }
}
