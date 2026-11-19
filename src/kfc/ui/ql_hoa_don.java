package kfc.ui;

import kfc.dao.HoaDonDAO;
import kfc.database;
import kfc.service.PhienQuanLy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ql_hoa_don extends javax.swing.JPanel {

    private DefaultTableModel model;
    private final DecimalFormat fmt = new DecimalFormat("#,##0.##");

    public ql_hoa_don() {
        PhienQuanLy.yeuCauDangNhap();
        initComponents();
        init();
    }

    // ===================== LOGIC =====================
    private void init() {
        txtNgay.setText(LocalDate.now().toString());
        model = (DefaultTableModel) bang.getModel();

        bang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienChiTiet();
            }
        });

        tai();
        // Bổ sung giao diện, không sửa chức năng hóa đơn và in ấn cũ.
        KfcTheme.apply(this);
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
                p.setTimestamp(1, from);
                p.setTimestamp(2, to);
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

    void refreshData() {
        tai();
    }

    private void hienChiTiet() {
        int row = bang.getSelectedRow();
        if (row < 0) return;
        int ma = ((Number) model.getValueAt(bang.convertRowIndexToModel(row), 0)).intValue();
        try {
            chiTiet.setText(new HoaDonDAO().noiDungHoaDon(ma));
            chiTiet.setCaretPosition(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc hóa đơn: " + e.getMessage());
        }
    }

    private void inLaiHoaDon() {
        if (bang.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Chọn hóa đơn trước!");
            return;
        }
        try {
            if (!chiTiet.print()) {
                JOptionPane.showMessageDialog(this, "Đã hủy lệnh in; hóa đơn vẫn được lưu.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi in: " + ex.getMessage());
        }
    }

    private void xoaHoaDon() {
        int row = bang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
            return;
        }
        int maHD = ((Number) model.getValueAt(bang.convertRowIndexToModel(row), 0)).intValue();
        String maIn = String.format("HD%06d", maHD);

        Object[] luaChon = {
            "1. Trả lại nguyên liệu về kho",
            "2. Xóa, không trả nguyên liệu",
            "Hủy"
        };

        int chon = JOptionPane.showOptionDialog(
                this,
                "Bạn đang chọn xóa hóa đơn " + maIn + ".\nVui lòng chọn hình thức xử lý kho nguyên liệu:",
                "Xác nhận xóa hóa đơn " + maIn,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                luaChon,
                luaChon[0]
        );

        if (chon != 0 && chon != 1) {
            return;
        }

        boolean hoanKho = (chon == 0);
        try {
            new HoaDonDAO().xoaHoaDon(maHD, hoanKho);
            tai();
            JOptionPane.showMessageDialog(this, hoanKho
                    ? "Đã xóa hóa đơn " + maIn + " và hoàn trả nguyên liệu về kho!"
                    : "Đã xóa hóa đơn " + maIn + " (không hoàn trả nguyên liệu)!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Xóa hóa đơn thất bại: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== GIAO DIỆN (kiểu NetBeans) =====================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtNgay = new javax.swing.JTextField();
        btnXem = new javax.swing.JButton();
        btnHomNay = new javax.swing.JButton();
        btnInLai = new javax.swing.JButton();
        btnXoaHoaDon = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        bang = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        chiTiet = new javax.swing.JTextArea();
        lblTong = new javax.swing.JLabel();

        jLabel1.setText("Ngày (yyyy-MM-dd):");

        btnXem.setText("Xem hóa đơn trong ngày");
        btnXem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemActionPerformed(evt);
            }
        });

        btnHomNay.setText("Hôm nay");
        btnHomNay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomNayActionPerformed(evt);
            }
        });

        btnInLai.setText("In lại hóa đơn đã chọn");
        btnInLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInLaiActionPerformed(evt);
            }
        });

        btnXoaHoaDon.setBackground(new java.awt.Color(220, 53, 69));
        btnXoaHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnXoaHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaHoaDon.setText("Xóa hóa đơn");
        btnXoaHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHoaDonActionPerformed(evt);
            }
        });

        jSplitPane1.setResizeWeight(0.53);

        bang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Mã hóa đơn", "Thời gian", "Tổng tiền (đ)"}
        ) {
            boolean[] canEdit = new boolean [] {false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(bang);

        jSplitPane1.setLeftComponent(jScrollPane1);

        chiTiet.setEditable(false);
        chiTiet.setColumns(20);
        chiTiet.setFont(new java.awt.Font("Monospaced", 0, 13));
        chiTiet.setRows(5);
        jScrollPane2.setViewportView(chiTiet);

        jSplitPane1.setRightComponent(jScrollPane2);

        lblTong.setFont(new java.awt.Font("Segoe UI", 1, 17));
        lblTong.setText("Số hóa đơn: 0  |  Tổng tiền: 0 đ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHomNay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnInLai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoaHoaDon)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblTong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXem)
                    .addComponent(btnHomNay)
                    .addComponent(btnInLai)
                    .addComponent(btnXoaHoaDon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTong)
                .addContainerGap())
        );

    }// </editor-fold>

    // ===================== SỰ KIỆN =====================
    private void btnXemActionPerformed(java.awt.event.ActionEvent evt) {
        tai();
    }

    private void btnHomNayActionPerformed(java.awt.event.ActionEvent evt) {
        txtNgay.setText(LocalDate.now().toString());
        tai();
    }

    private void btnInLaiActionPerformed(java.awt.event.ActionEvent evt) {
        inLaiHoaDon();
    }

    private void btnXoaHoaDonActionPerformed(java.awt.event.ActionEvent evt) {
        xoaHoaDon();
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
            java.util.logging.Logger.getLogger(ql_hoa_don.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dang_nhap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JTable bang;
    private javax.swing.JButton btnHomNay;
    private javax.swing.JButton btnInLai;
    private javax.swing.JButton btnXem;
    private javax.swing.JButton btnXoaHoaDon;
    private javax.swing.JTextArea chiTiet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblTong;
    private javax.swing.JTextField txtNgay;
    // End of variables declaration
}