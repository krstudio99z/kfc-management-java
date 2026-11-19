package kfc.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Chỉ trang trí Swing: KHÔNG chạm tới DAO, dữ liệu hay ActionListener cũ.
 * Gọi một lần sau initComponents() để giữ nguyên vùng code sinh bởi NetBeans.
 */
public final class KfcTheme {
    public static final Color RED = new Color(220, 16, 52);
    public static final Color RED_DARK = new Color(181, 12, 39);
    public static final Color INK = new Color(30, 35, 43);
    public static final Color MUTED = new Color(104, 111, 124);
    public static final Color SURFACE = Color.WHITE;
    public static final Color BACKGROUND = new Color(247, 248, 250);
    public static final Color LINE = new Color(227, 231, 236);

    private KfcTheme() { }

    public static void apply(JFrame frame, String screenName) {
        Container old = frame.getContentPane();
        if (!(old instanceof JComponent) || Boolean.TRUE.equals(((JComponent) old).getClientProperty("kfc.theming"))) {
            return;
        }
        JComponent body = (JComponent) old;
        body.putClientProperty("kfc.theming", Boolean.TRUE);
        styleTree(body);
        body.setBackground(BACKGROUND);
        body.setBorder(new EmptyBorder(20, 22, 20, 22));

        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(BACKGROUND);
        shell.add(createHeader(screenName), BorderLayout.NORTH);
        shell.add(body, BorderLayout.CENTER);
        frame.setContentPane(shell);

        int width = screenName.contains("KHO") ? 1160 : screenName.contains("HÓA ĐƠN") ? 1150 : 1050;
        int height = screenName.contains("KHO") ? 810 : 740;
        frame.pack();
        frame.setSize(Math.max(frame.getWidth(), width), Math.max(frame.getHeight(), height));
        frame.setMinimumSize(new Dimension(800, 560));
        frame.setLocationRelativeTo(null);
    }

    private static JComponent createHeader(String title) {
        JPanel panel = new JPanel(new BorderLayout(16, 0));
        panel.setBackground(SURFACE);
        panel.setBorder(new EmptyBorder(17, 25, 17, 26));

        JLabel logo = new JLabel("KFC");
        logo.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        logo.setForeground(RED);
        logo.setBorder(new EmptyBorder(0, 2, 0, 24));

        JPanel labels = new JPanel(new BorderLayout(0, 5));
        labels.setOpaque(false);
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 21));
        heading.setForeground(INK);
        JLabel caption = new JLabel(title.startsWith("THỰC ĐƠN")
                ? "Chọn món yêu thích của bạn  •  Gà giòn mỗi ngày"
                : "Hệ thống quản lý cửa hàng  •  KFC Store");
        caption.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        caption.setForeground(MUTED);
        labels.add(heading, BorderLayout.NORTH);
        labels.add(caption, BorderLayout.SOUTH);

        JLabel tag = new JLabel(title.startsWith("THỰC ĐƠN") ? "●  ĐẶT MÓN" : "●  KFC STORE", SwingConstants.CENTER);
        tag.setOpaque(true);
        tag.setBackground(new Color(255, 241, 243));
        tag.setForeground(RED_DARK);
        tag.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tag.setBorder(new EmptyBorder(9, 13, 9, 13));

        panel.add(logo, BorderLayout.WEST);
        panel.add(labels, BorderLayout.CENTER);
        panel.add(tag, BorderLayout.EAST);
        JPanel container = new JPanel(new BorderLayout());
        container.add(panel, BorderLayout.CENTER);
        JPanel redLine = new JPanel();
        redLine.setPreferredSize(new Dimension(1, 4));
        redLine.setBackground(RED);
        container.add(redLine, BorderLayout.SOUTH);
        return container;
    }

    private static void styleTree(Component component) {
        if (component instanceof JButton) {
            styleButton((JButton) component);
        } else if (component instanceof JTable) {
            styleTable((JTable) component);
        } else if (component instanceof JTextField) {
            styleInput((JTextField) component);
        } else if (component instanceof JTextArea) {
            JTextArea field = (JTextArea) component;
            field.setBackground(SURFACE);
            field.setForeground(INK);
            field.setBorder(new EmptyBorder(12, 14, 12, 14));
        } else if (component instanceof JRadioButton) {
            JRadioButton radio = (JRadioButton) component;
            radio.setOpaque(false);
            radio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            radio.setForeground(INK);
        } else if (component instanceof JLabel) {
            styleLabel((JLabel) component);
        } else if (component instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) component;
            scroll.setBorder(BorderFactory.createLineBorder(LINE));
            scroll.getViewport().setBackground(SURFACE);
        } else if (component instanceof JSplitPane) {
            JSplitPane split = (JSplitPane) component;
            split.setBorder(null);
            split.setDividerSize(12);
            split.setBackground(BACKGROUND);
        } else if (component instanceof JComboBox) {
            component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            component.setBackground(SURFACE);
            component.setForeground(INK);
            component.setPreferredSize(new Dimension(Math.max(180, component.getPreferredSize().width), 34));
        } else if (component instanceof JSpinner) {
            component.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        } else if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            panel.setBackground(SURFACE);
            if (panel.getBorder() instanceof TitledBorder) {
                TitledBorder border = (TitledBorder) panel.getBorder();
                border.setTitleColor(RED_DARK);
                border.setTitleFont(new Font("Segoe UI", Font.BOLD, 15));
                border.setBorder(BorderFactory.createLineBorder(LINE, 1, true));
            }
        }
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) styleTree(child);
        }
    }

    private static void styleLabel(JLabel label) {
        String text = label.getText() == null ? "" : label.getText();
        if (text.contains("THỰC ĐƠN") || text.contains("GIỎ HÀNG") || text.contains("Tổng tiền") || text.contains("Số hóa đơn")) {
            label.setForeground(INK);
            label.setFont(new Font("Segoe UI", Font.BOLD, 17));
        } else {
            label.setForeground(INK);
            if (label.getFont().getSize() < 15) label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
    }

    private static void styleInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(INK);
        field.setBackground(SURFACE);
        field.setCaretColor(RED);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE, 1, true),
                new EmptyBorder(6, 11, 6, 11)));
        field.setPreferredSize(new Dimension(Math.max(field.getPreferredSize().width, 120), 36));
    }

    private static void styleButton(JButton button) {
        String title = button.getText() == null ? "" : button.getText().toLowerCase(java.util.Locale.ROOT);
        final boolean danger = title.contains("xóa") || title.contains("bỏ món");
        final boolean primary = !danger && (title.contains("thêm") || title.contains("lưu")
                || title.contains("đặt") || title.contains("xem") || title.contains("đăng")
                || title.contains("nhập thêm"));
        final Color base = primary ? RED : danger ? new Color(188, 45, 55) : new Color(238, 241, 245);
        final Color hover = primary ? RED_DARK : danger ? new Color(151, 27, 39) : new Color(224, 229, 234);
        button.setUI(new BasicButtonUI() {
            @Override public void update(Graphics g, JComponent c) { paint(g, c); }
            @Override public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = !b.isEnabled() ? new Color(205, 210, 216)
                        : b.getModel().isPressed() ? hover.darker()
                        : b.getModel().isRollover() ? hover : base;
                g2.setColor(color);
                g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 13, 13);
                g2.dispose();
                super.paint(g, c);
            }
        });
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setRolloverEnabled(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(primary || danger ? Color.WHITE : INK);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(255, 231, 235));
        table.setSelectionForeground(INK);
        table.setBackground(SURFACE);
        table.setFillsViewportHeight(true);
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.setReorderingAllowed(false);
            header.setPreferredSize(new Dimension(header.getPreferredSize().width, 43));
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override public Component getTableCellRendererComponent(
                        JTable t, Object value, boolean selected, boolean focus, int row, int column) {
                    super.getTableCellRendererComponent(t, value, selected, focus, row, column);
                    setBackground(new Color(37, 39, 43));
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    setBorder(new EmptyBorder(0, 13, 0, 6));
                    return this;
                }
            });
        }
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean selected, boolean focus, int row, int column) {
                super.getTableCellRendererComponent(t, value, selected, focus, row, column);
                setBorder(new EmptyBorder(0, 12, 0, 8));
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setForeground(INK);
                setBackground(selected ? new Color(255, 231, 235)
                        : row % 2 == 0 ? SURFACE : new Color(250, 250, 252));
                String head = t.getColumnName(column).toLowerCase(java.util.Locale.ROOT);
                if (head.contains("trạng thái")) {
                    String val = value == null ? "" : value.toString();
                    setForeground(val.contains("Còn") ? new Color(14, 129, 76) : RED_DARK);
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                if (head.contains("giá") || head.contains("tiền") || head.contains("tồn")) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });
    }
}
