package kfc.ui;

import kfc.dao.KhoDAO;
import kfc.dao.KhoDAO.CongThuc;
import kfc.dao.KhoDAO.NguyenLieu;
import kfc.dao.truy_cap;
import kfc.entity.mon_an;
import kfc.service.PhienQuanLy;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ql_kho extends javax.swing.JFrame {

    private final KhoDAO dao = new KhoDAO();
    private DefaultTableModel khoModel;
    private DefaultTableModel ctModel;

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
        initComponents();
        init();
    }

    // ===================== LOGIC =====================
    private void init() {
        setLocationRelativeTo(null);
        khoModel = (DefaultTableModel) tbKho.getModel();
        ctModel = (DefaultTableModel) tbCt.getModel();

        tbKho.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tbKho.getSelectedRow() >= 0) {
                int row = tbKho.convertRowIndexToModel(tbKho.getSelectedRow());
                txtMa.setText(khoModel.getValueAt(row, 0).toString());
                txtTen.setText(khoModel.getValueAt(row, 1).toString());
                txtDonVi.setText(khoModel.getValueAt(row, 2).toString());
                txtTon.setText(khoModel.getValueAt(row, 3).toString());
            }
        });

        tbCt.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tbCt.getSelectedRow() >= 0) {
                int r = tbCt.convertRowIndexToModel(tbCt.getSelectedRow());
                String ma = ctModel.getValueAt(r, 0).toString();
                for (int i = 0; i < cbNguyenLieu.getItemCount(); i++) {
                    if (cbNguyenLieu.getItemAt(i).ma.equals(ma)) {
                        cbNguyenLieu.setSelectedIndex(i);
                        break;
                    }
                }
                txtDinhLuong.setText(ctModel.getValueAt(r, 2).toString());
            }
        });

        taiDuLieu();
    }

    private NguyenLieu layNguyenLieu() {
        return new NguyenLieu(txtMa.getText().trim(), txtTen.getText().trim(),
                txtDonVi.getText().trim(), new BigDecimal(txtTon.getText().trim()));
    }

    @FunctionalInterface
    private interface ThaoTac {
        void chay() throws Exception;
    }

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
            String maMonCu = cbMon.getSelectedItem() == null ? "" : ((MonItem) cbMon.getSelectedItem()).ma;
            khoModel.setRowCount(0);
            cbNguyenLieu.removeAllItems();
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

    // ===================== GIAO DIỆN (kiểu NetBeans) =====================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        pnlKho = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        txtTen = new javax.swing.JTextField();
        txtDonVi = new javax.swing.JTextField();
        txtTon = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnNhapThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnXoaNhap = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbKho = new javax.swing.JTable();
        pnlCongThuc = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbMon = new javax.swing.JComboBox<>();
        cbNguyenLieu = new javax.swing.JComboBox<>();
        txtDinhLuong = new javax.swing.JTextField();
        btnLuuCongThuc = new javax.swing.JButton();
        btnXoaCongThuc = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCt = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("KFC - Quản lý kho và công thức món ăn");

        jSplitPane1.setResizeWeight(0.52);

        pnlKho.setBorder(javax.swing.BorderFactory.createTitledBorder("QUẢN LÝ TỒN KHO"));

        jLabel1.setText("Mã nguyên liệu:");
        jLabel2.setText("Tên nguyên liệu:");
        jLabel3.setText("Đơn vị (kg/lít/cái...):");
        jLabel4.setText("Số lượng tồn:");

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnNhapThem.setText("Nhập thêm");
        btnNhapThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhapThemActionPerformed(evt);
            }
        });

        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnXoaNhap.setText("Xóa nhập");
        btnXoaNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaNhapActionPerformed(evt);
            }
        });

        tbKho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Mã NL", "Tên nguyên liệu", "Đơn vị", "Số lượng tồn"}
        ) {
            boolean[] canEdit = new boolean [] {false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbKho.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tbKho);

        javax.swing.GroupLayout pnlKhoLayout = new javax.swing.GroupLayout(pnlKho);
        pnlKho.setLayout(pnlKhoLayout);
        pnlKhoLayout.setHorizontalGroup(
            pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKhoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(pnlKhoLayout.createSequentialGroup()
                        .addGroup(pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(12, 12, 12)
                        .addGroup(pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMa)
                            .addComponent(txtTen)
                            .addComponent(txtDonVi)
                            .addComponent(txtTon)))
                    .addGroup(pnlKhoLayout.createSequentialGroup()
                        .addComponent(btnThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNhapThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLamMoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaNhap)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlKhoLayout.setVerticalGroup(
            pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKhoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtTon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlKhoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnNhapThem)
                    .addComponent(btnXoa)
                    .addComponent(btnLamMoi)
                    .addComponent(btnXoaNhap))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(pnlKho);

        pnlCongThuc.setBorder(javax.swing.BorderFactory.createTitledBorder("CÔNG THỨC NGUYÊN LIỆU CHO MỖI MÓN"));

        jLabel5.setText("Chọn món:");
        jLabel6.setText("Nguyên liệu:");
        jLabel7.setText("Định lượng / 1 phần:");

        cbMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMonActionPerformed(evt);
            }
        });

        btnLuuCongThuc.setText("Lưu công thức");
        btnLuuCongThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuCongThucActionPerformed(evt);
            }
        });

        btnXoaCongThuc.setText("Xóa dòng công thức");
        btnXoaCongThuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaCongThucActionPerformed(evt);
            }
        });

        tbCt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Mã NL", "Tên nguyên liệu", "Định lượng / phần", "Đơn vị"}
        ) {
            boolean[] canEdit = new boolean [] {false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCt.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tbCt);

        javax.swing.GroupLayout pnlCongThucLayout = new javax.swing.GroupLayout(pnlCongThuc);
        pnlCongThuc.setLayout(pnlCongThucLayout);
        pnlCongThucLayout.setHorizontalGroup(
            pnlCongThucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCongThucLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCongThucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(pnlCongThucLayout.createSequentialGroup()
                        .addGroup(pnlCongThucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(12, 12, 12)
                        .addGroup(pnlCongThucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbMon, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbNguyenLieu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDinhLuong)))
                    .addGroup(pnlCongThucLayout.createSequentialGroup()
                        .addComponent(btnLuuCongThuc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaCongThuc)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlCongThucLayout.setVerticalGroup(
            pnlCongThucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCongThucLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCongThucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cbMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCongThucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbNguyenLieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlCongThucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtDinhLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlCongThucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuuCongThuc)
                    .addComponent(btnXoaCongThuc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(pnlCongThuc);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1108, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    // ===================== SỰ KIỆN =====================
    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {
        xuLy(() -> dao.them(layNguyenLieu()));
    }

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {
        xuLy(() -> dao.sua(layNguyenLieu()));
    }

    private void btnNhapThemActionPerformed(java.awt.event.ActionEvent evt) {
        String input = JOptionPane.showInputDialog(this, "Nhập số lượng tăng thêm cho "
                + txtMa.getText() + " (không phải tổng tồn):", "1");
        if (input != null) {
            xuLy(() -> dao.nhapThem(txtMa.getText().trim(), new BigDecimal(input.trim())));
        }
    }

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {
        if (JOptionPane.showConfirmDialog(this, "Xóa nguyên liệu " + txtMa.getText() + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            xuLy(() -> dao.xoa(txtMa.getText().trim()));
        }
    }

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {
        taiDuLieu();
    }

    private void btnXoaNhapActionPerformed(java.awt.event.ActionEvent evt) {
        txtMa.setText("");
        txtTen.setText("");
        txtDonVi.setText("");
        txtTon.setText("");
        tbKho.clearSelection();
    }

    private void cbMonActionPerformed(java.awt.event.ActionEvent evt) {
        taiCongThuc();
    }

    private void btnLuuCongThucActionPerformed(java.awt.event.ActionEvent evt) {
        MonItem m = (MonItem) cbMon.getSelectedItem();
        NguyenLieuItem n = (NguyenLieuItem) cbNguyenLieu.getSelectedItem();
        if (m == null || n == null) {
            JOptionPane.showMessageDialog(this, "Chọn món và nguyên liệu");
            return;
        }
        xuLy(() -> dao.luuCongThuc(m.ma, n.ma, new BigDecimal(txtDinhLuong.getText().trim())));
    }

    private void btnXoaCongThucActionPerformed(java.awt.event.ActionEvent evt) {
        MonItem m = (MonItem) cbMon.getSelectedItem();
        int r = tbCt.getSelectedRow();
        if (m == null || r < 0) {
            JOptionPane.showMessageDialog(this, "Chọn dòng công thức cần xóa");
            return;
        }
        String maNL = ctModel.getValueAt(tbCt.convertRowIndexToModel(r), 0).toString();
        xuLy(() -> dao.xoaCongThuc(m.ma, maNL));
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
            java.util.logging.Logger.getLogger(ql_kho.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dang_nhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLuuCongThuc;
    private javax.swing.JButton btnNhapThem;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaCongThuc;
    private javax.swing.JButton btnXoaNhap;
    private javax.swing.JComboBox<MonItem> cbMon;
    private javax.swing.JComboBox<NguyenLieuItem> cbNguyenLieu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pnlCongThuc;
    private javax.swing.JPanel pnlKho;
    private javax.swing.JTable tbCt;
    private javax.swing.JTable tbKho;
    private javax.swing.JTextField txtDinhLuong;
    private javax.swing.JTextField txtDonVi;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTon;
    // End of variables declaration
}