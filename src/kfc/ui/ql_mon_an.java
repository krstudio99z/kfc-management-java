package kfc.ui;


import kfc.dao.truy_cap;
import kfc.entity.mon_an;
import kfc.kt_loi;
import kfc.service.PhienQuanLy;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.regex.Pattern;

public class ql_mon_an extends javax.swing.JFrame {

    private final truy_cap dao = new truy_cap();
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    public ql_mon_an() {
        PhienQuanLy.yeuCauDangNhap();
        initComponents();
        init();
    }

    // ===================== LOGIC =====================
    private void init() {
        setLocationRelativeTo(null);
        tableModel = (DefaultTableModel) tblMonAn.getModel();
        sorter = new TableRowSorter<>(tableModel);
        tblMonAn.setRowSorter(sorter);
        fillTable();
    }

    private void fillTable() {
        tableModel.setRowCount(0);
        try {
            List<mon_an> list = dao.selectAll();
            for (mon_an ma : list) {
                tableModel.addRow(new Object[]{
                    ma.getMaMon(),
                    ma.getTenMon(),
                    ma.getGia(),
                    !ma.isChoPhepBan() ? "Ngừng bán" : (ma.isTrangThai() ? "Còn bán" : "Hết nguyên liệu")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu từ CSDL!");
        }
    }

    private mon_an getForm() {
        mon_an ma = new mon_an();
        ma.setMaMon(txtMaMon.getText().trim());
        ma.setTenMon(txtTenMon.getText().trim());
        ma.setGia(Double.parseDouble(txtGia.getText().trim()));
        ma.setChoPhepBan(rdoConBan.isSelected());
        return ma;
    }

    private void insert() {
        if (kt_loi.checkEmpty(txtMaMon, "Không để trống Mã món!")
                && kt_loi.checkEmpty(txtTenMon, "Không để trống Tên món!")
                && kt_loi.checkPrice(txtGia)) {
            try {
                dao.insert(getForm());
                fillTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Thêm mới thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Thêm mới thất bại! Mã món có thể đã tồn tại.");
            }
        }
    }

    private void update() {
        if (kt_loi.checkEmpty(txtTenMon, "Không để trống Tên món!")
                && kt_loi.checkPrice(txtGia)) {
            try {
                dao.update(getForm());
                fillTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    private void delete() {
        String maMon = txtMaMon.getText().trim();
        if (maMon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có thực sự muốn xóa món này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dao.delete(maMon);
                fillTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Xóa thất bại! Cần xóa công thức trước; không xóa được món đã có hóa đơn.");
            }
        }
    }

    private void clearForm() {
        txtMaMon.setText("");
        txtTenMon.setText("");
        txtGia.setText("");
        rdoConBan.setSelected(true);
        tblMonAn.clearSelection();
        txtMaMon.requestFocus();
    }

    private void edit() {
        int viewRow = tblMonAn.getSelectedRow();
        if (viewRow < 0) return;
        int row = tblMonAn.convertRowIndexToModel(viewRow); // đúng cả khi đang lọc

        txtMaMon.setText(tableModel.getValueAt(row, 0).toString());
        txtTenMon.setText(tableModel.getValueAt(row, 1).toString());
        txtGia.setText(tableModel.getValueAt(row, 2).toString());
        if (!"Ngừng bán".equals(tableModel.getValueAt(row, 3).toString())) {
            rdoConBan.setSelected(true);
        } else {
            rdoNgungBan.setSelected(true);
        }
    }

    private void search() {
        String text = txtTimKiem.getText().trim();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text), 1));
        }
    }

    // ===================== GIAO DIỆN (kiểu NetBeans) =====================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMaMon = new javax.swing.JTextField();
        txtTenMon = new javax.swing.JTextField();
        txtGia = new javax.swing.JTextField();
        txtTimKiem = new javax.swing.JTextField();
        rdoConBan = new javax.swing.JRadioButton();
        rdoNgungBan = new javax.swing.JRadioButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMonAn = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản Lý Món Ăn - KFC");

        jLabel1.setText("Mã món:");
        jLabel2.setText("Tên món:");
        jLabel3.setText("Giá:");
        jLabel4.setText("Trạng thái:");
        jLabel5.setText("Tìm theo tên:");

        buttonGroup1.add(rdoConBan);
        rdoConBan.setSelected(true);
        rdoConBan.setText("Còn bán");

        buttonGroup1.add(rdoNgungBan);
        rdoNgungBan.setText("Ngừng bán");

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

        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        tblMonAn.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Mã món", "Tên món", "Giá", "Trạng thái"}
        ) {
            boolean[] canEdit = new boolean[]{false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblMonAn.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblMonAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMonAnMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMonAn);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaMon, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenMon, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rdoConBan)
                                .addGap(18, 18, 18)
                                .addComponent(rdoNgungBan))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnThem)
                        .addGap(10, 10, 10)
                        .addComponent(btnSua)
                        .addGap(10, 10, 10)
                        .addComponent(btnXoa)
                        .addGap(10, 10, 10)
                        .addComponent(btnMoi))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMaMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTenMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rdoConBan)
                    .addComponent(rdoNgungBan))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnMoi))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    // ===================== SỰ KIỆN =====================
    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {
        insert();
    }

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {
        update();
    }

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {
        delete();
    }

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {
        clearForm();
    }

    private void tblMonAnMouseClicked(java.awt.event.MouseEvent evt) {
        edit();
    }

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {
        search();
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
            java.util.logging.Logger.getLogger(ql_mon_an.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dang_nhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdoConBan;
    private javax.swing.JRadioButton rdoNgungBan;
    private javax.swing.JTable tblMonAn;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMaMon;
    private javax.swing.JTextField txtTenMon;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration
}