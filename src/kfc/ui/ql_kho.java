package kfc.ui;

import kfc.dao.KhoDAO;
import kfc.dao.KhoDAO.CongThuc;
import kfc.dao.KhoDAO.NguyenLieu;
import kfc.dao.truy_cap;
import kfc.entity.mon_an;
import kfc.service.PhienQuanLy;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/** Quan ly ton kho va cong thuc tung mon - khong can GUI Builder. */
public class ql_kho extends JFrame {
    private final KhoDAO dao = new KhoDAO();
    private final JTextField txtMa = new JTextField();
    private final JTextField txtTen = new JTextField();
    private final JTextField txtDonVi = new JTextField();
    private final JTextField txtTon = new JTextField();
    private final DefaultTableModel khoModel = new DefaultTableModel(
            new String[]{"Mã NL", "Tên nguyên liệu", "Đơn vị", "Số lượng tồn"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tbKho = new JTable(khoModel);
    private final JComboBox<MonItem> cbMon = new JComboBox<>();
    private final JComboBox<NguyenLieuItem> cbNguyenLieu = new JComboBox<>();
    private final JTextField txtDinhLuong = new JTextField(9);
    private final DefaultTableModel ctModel = new DefaultTableModel(
            new String[]{"Mã NL", "Tên nguyên liệu", "Định lượng / phần", "Đơn vị"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tbCt = new JTable(ctModel);

    private static class MonItem {
        String ma, ten;
        MonItem(String ma, String ten) { this.ma = ma; this.ten = ten; }
        @Override public String toString() { return ma + " - " + ten; }
    }
    private static class NguyenLieuItem {
        String ma, ten, donVi;
        NguyenLieuItem(NguyenLieu n) { ma = n.ma; ten = n.ten; donVi = n.donVi; }
        @Override public String toString() { return ma + " - " + ten + " (" + donVi + ")"; }
    }

    public ql_kho() {
        PhienQuanLy.yeuCauDangNhap();
        setTitle("KFC - Quản lý kho và công thức món ăn");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel left = new JPanel(new BorderLayout(5, 5));
        left.setBorder(BorderFactory.createTitledBorder("QUẢN LÝ TỒN KHO"));
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Mã nguyên liệu:")); form.add(txtMa);
        form.add(new JLabel("Tên nguyên liệu:")); form.add(txtTen);
        form.add(new JLabel("Đơn vị (kg/lít/cái...):")); form.add(txtDonVi);
        form.add(new JLabel("Số lượng tồn:")); form.add(txtTon);
        JPanel controls = new JPanel(new BorderLayout());
        controls.add(form, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Thêm");
        JButton update = new JButton("Sửa");
        JButton delete = new JButton("Xóa");
        JButton topup = new JButton("Nhập thêm");
        JButton reload = new JButton("Làm mới");
        JButton clear = new JButton("Xóa nhập");
        buttons.add(add); buttons.add(update); buttons.add(topup); buttons.add(delete); buttons.add(reload); buttons.add(clear);
        controls.add(buttons, BorderLayout.SOUTH);
        left.add(controls, BorderLayout.NORTH);
        left.add(new JScrollPane(tbKho), BorderLayout.CENTER);
        tbKho.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tbKho.getSelectedRow() >= 0) {
                int row = tbKho.convertRowIndexToModel(tbKho.getSelectedRow());
                txtMa.setText(khoModel.getValueAt(row, 0).toString());
                txtTen.setText(khoModel.getValueAt(row, 1).toString());
                txtDonVi.setText(khoModel.getValueAt(row, 2).toString());
                txtTon.setText(khoModel.getValueAt(row, 3).toString());
            }
        });
        add.addActionListener(e -> xuLy(() -> dao.them(layNguyenLieu())));
        update.addActionListener(e -> xuLy(() -> dao.sua(layNguyenLieu())));
        topup.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Nhập số lượng tăng thêm cho "
                    + txtMa.getText() + " (không phải tổng tồn):", "1");
            if (input != null) xuLy(() -> dao.nhapThem(txtMa.getText().trim(),
                    new BigDecimal(input.trim())));
        });
        delete.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Xóa nguyên liệu " + txtMa.getText() + "?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                xuLy(() -> dao.xoa(txtMa.getText().trim()));
        });
        reload.addActionListener(e -> taiDuLieu());
        clear.addActionListener(e -> {
            txtMa.setText(""); txtTen.setText(""); txtDonVi.setText(""); txtTon.setText("");
            tbKho.clearSelection();
        });

        JPanel right = new JPanel(new BorderLayout(5, 5));
        right.setBorder(BorderFactory.createTitledBorder("CÔNG THỨC NGUYÊN LIỆU CHO MỖI MÓN"));
        JPanel recipeForm = new JPanel(new GridLayout(3, 2, 5, 5));
        recipeForm.add(new JLabel("Chọn món:")); recipeForm.add(cbMon);
        recipeForm.add(new JLabel("Nguyên liệu:")); recipeForm.add(cbNguyenLieu);
        recipeForm.add(new JLabel("Định lượng / 1 phần:")); recipeForm.add(txtDinhLuong);
        JPanel recipeControls = new JPanel(new BorderLayout());
        recipeControls.add(recipeForm, BorderLayout.CENTER);
        JPanel recipeButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton saveRecipe = new JButton("Lưu công thức");
        JButton deleteRecipe = new JButton("Xóa dòng công thức");
        recipeButtons.add(saveRecipe); recipeButtons.add(deleteRecipe);
        recipeControls.add(recipeButtons, BorderLayout.SOUTH);
        right.add(recipeControls, BorderLayout.NORTH);
        right.add(new JScrollPane(tbCt), BorderLayout.CENTER);
        cbMon.addActionListener(e -> taiCongThuc());
        tbCt.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tbCt.getSelectedRow() >= 0) {
                int r = tbCt.convertRowIndexToModel(tbCt.getSelectedRow());
                String ma = ctModel.getValueAt(r, 0).toString();
                for (int i = 0; i < cbNguyenLieu.getItemCount(); i++) {
                    if (cbNguyenLieu.getItemAt(i).ma.equals(ma)) { cbNguyenLieu.setSelectedIndex(i); break; }
                }
                txtDinhLuong.setText(ctModel.getValueAt(r, 2).toString());
            }
        });
        saveRecipe.addActionListener(e -> {
            MonItem m = (MonItem) cbMon.getSelectedItem();
            NguyenLieuItem n = (NguyenLieuItem) cbNguyenLieu.getSelectedItem();
            if (m == null || n == null) { JOptionPane.showMessageDialog(this, "Chọn món và nguyên liệu"); return; }
            xuLy(() -> dao.luuCongThuc(m.ma, n.ma, new BigDecimal(txtDinhLuong.getText().trim())));
        });
        deleteRecipe.addActionListener(e -> {
            MonItem m = (MonItem) cbMon.getSelectedItem();
            int r = tbCt.getSelectedRow();
            if (m == null || r < 0) { JOptionPane.showMessageDialog(this, "Chọn dòng công thức cần xóa"); return; }
            String maNL = ctModel.getValueAt(tbCt.convertRowIndexToModel(r), 0).toString();
            xuLy(() -> dao.xoaCongThuc(m.ma, maNL));
        });
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(.52);
        add(split, BorderLayout.CENTER);
        setSize(1120, 610); setLocationRelativeTo(null);
        taiDuLieu();
    }

    private NguyenLieu layNguyenLieu() {
        return new NguyenLieu(txtMa.getText().trim(), txtTen.getText().trim(),
                txtDonVi.getText().trim(), new BigDecimal(txtTon.getText().trim()));
    }

    @FunctionalInterface private interface ThaoTac { void chay() throws Exception; }
    private void xuLy(ThaoTac t) {
        try {
            t.chay();
            taiDuLieu();
            JOptionPane.showMessageDialog(this, "Đã cập nhật thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật: " + e.getMessage());
        }
    }

    private void taiDuLieu() {
        try {
            List<NguyenLieu> list = dao.danhSachKho();
            String maMonCu = cbMon.getSelectedItem() == null ? "" : ((MonItem)cbMon.getSelectedItem()).ma;
            khoModel.setRowCount(0); cbNguyenLieu.removeAllItems();
            for (NguyenLieu n : list) {
                khoModel.addRow(new Object[]{n.ma, n.ten, n.donVi, n.ton});
                cbNguyenLieu.addItem(new NguyenLieuItem(n));
            }
            cbMon.removeAllItems();
            int idx = 0, restore = 0;
            for (mon_an m : new truy_cap().selectAll()) {
                cbMon.addItem(new MonItem(m.getMaMon(), m.getTenMon()));
                if (m.getMaMon().equals(maMonCu)) restore = idx;
                idx++;
            }
            if (cbMon.getItemCount() > 0) cbMon.setSelectedIndex(restore);
            taiCongThuc();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không tải được kho: " + e.getMessage());
        }
    }
    private void taiCongThuc() {
        ctModel.setRowCount(0);
        MonItem m = (MonItem) cbMon.getSelectedItem();
        if (m == null) return;
        try {
            for (CongThuc c : dao.congThuc(m.ma)) {
                ctModel.addRow(new Object[]{c.maNL, c.tenNL, c.dinhLuong, c.donVi});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc công thức: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new dang_nhap().setVisible(true));
    }
}
