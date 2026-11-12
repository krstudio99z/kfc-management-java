package kfc.ui;

import kfc.dao.HoaDonDAO;
import kfc.database;
import kfc.service.PhienQuanLy;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ql_hoa_don extends JFrame {
    private final JTextField txtNgay = new JTextField(LocalDate.now().toString(), 12);
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã hóa đơn", "Thời gian", "Tổng tiền (đ)"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable bang = new JTable(model);
    private final JTextArea chiTiet = new JTextArea();
    private final JLabel lblTong = new JLabel("Số hóa đơn: 0  |  Tổng tiền: 0 đ");
    private final DecimalFormat fmt = new DecimalFormat("#,##0.##");

    public ql_hoa_don() {
        PhienQuanLy.yeuCauDangNhap();
        setTitle("KFC - Hóa đơn và doanh thu theo ngày");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton xem = new JButton("Xem hóa đơn trong ngày");
        JButton homNay = new JButton("Hôm nay");
        JButton in = new JButton("In lại hóa đơn đã chọn");
        top.add(new JLabel("Ngày (yyyy-MM-dd):")); top.add(txtNgay);
        top.add(xem); top.add(homNay); top.add(in);
        add(top, BorderLayout.NORTH);
        chiTiet.setEditable(false);
        chiTiet.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(bang), new JScrollPane(chiTiet));
        split.setResizeWeight(.53);
        add(split, BorderLayout.CENTER);
        lblTong.setFont(new Font("Segoe UI", Font.BOLD, 17));
        add(lblTong, BorderLayout.SOUTH);
        xem.addActionListener(e -> tai());
        homNay.addActionListener(e -> { txtNgay.setText(LocalDate.now().toString()); tai(); });
        bang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) hienChiTiet();
        });
        in.addActionListener(e -> {
            if (bang.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Chọn hóa đơn trước!"); return; }
            try {
                if (!chiTiet.print()) JOptionPane.showMessageDialog(this, "Đã hủy lệnh in; hóa đơn vẫn được lưu.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi in: " + ex.getMessage());
            }
        });
        setSize(1020, 610); setLocationRelativeTo(null);
        tai();
    }

    private void tai() {
        try {
            LocalDate d = LocalDate.parse(txtNgay.getText().trim());
            Timestamp from = Timestamp.valueOf(d.atStartOfDay());
            Timestamp to = Timestamp.valueOf(d.plusDays(1).atStartOfDay());
            String sql = "SELECT MaHD,NgayLap,TongTien FROM hoa_don "
                    + "WHERE NgayLap >= ? AND NgayLap < ? ORDER BY NgayLap DESC,MaHD DESC";
            model.setRowCount(0);
            BigDecimal total = BigDecimal.ZERO;
            try (Connection c = database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
                p.setTimestamp(1, from); p.setTimestamp(2, to);
                try (ResultSet r = p.executeQuery()) {
                    while (r.next()) {
                        BigDecimal soTien = r.getBigDecimal("TongTien");
                        total = total.add(soTien);
                        model.addRow(new Object[]{r.getInt("MaHD"), r.getTimestamp("NgayLap"), soTien});
                    }
                }
            }
            lblTong.setText("Ngày " + d + "   |   Số hóa đơn: " + model.getRowCount()
                    + "   |   Tổng tiền: " + fmt.format(total) + " đ");
            chiTiet.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ hoặc lỗi CSDL: " + e.getMessage());
        }
    }

    private void hienChiTiet() {
        int row = bang.getSelectedRow();
        if (row < 0) return;
        int ma = ((Number) model.getValueAt(bang.convertRowIndexToModel(row), 0)).intValue();
        try { chiTiet.setText(new HoaDonDAO().noiDungHoaDon(ma)); chiTiet.setCaretPosition(0); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, "Lỗi đọc hóa đơn: " + e.getMessage()); }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new dang_nhap().setVisible(true));
    }
}
