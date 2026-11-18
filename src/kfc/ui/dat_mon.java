package kfc.ui;


import kfc.dao.truy_cap;
import kfc.dao.HoaDonDAO;
import kfc.service.KhoService;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import kfc.entity.mon_an;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Màn hình dành cho khách: xem thực đơn, chọn món, gọi món.
 */
public class dat_mon extends javax.swing.JFrame {

    private final truy_cap dao = new truy_cap();
    private final DecimalFormat money = new DecimalFormat("#,###");

    private DefaultTableModel menuModel;
    private DefaultTableModel cartModel;
    private TableRowSorter<DefaultTableModel> sorter;

    public dat_mon() {
        initComponents();
        init();
    }

    // ===================== LOGIC =====================
    private void init() {
        setLocationRelativeTo(null);
        menuModel = (DefaultTableModel) tblThucDon.getModel();
        cartModel = (DefaultTableModel) tblGioHang.getModel();

        sorter = new TableRowSorter<>(menuModel);
        tblThucDon.setRowSorter(sorter);

        spnSoLuong.setModel(new javax.swing.SpinnerNumberModel(1, 1, 99, 1));

        // Hiển thị tiền có dấu phân cách hàng nghìn
        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                setHorizontalAlignment(RIGHT);
                setText(value instanceof Number ? money.format(((Number) value).doubleValue()) : "");
            }
        };
        tblThucDon.getColumnModel().getColumn(2).setCellRenderer(moneyRenderer);
        tblGioHang.getColumnModel().getColumn(3).setCellRenderer(moneyRenderer);
        tblGioHang.getColumnModel().getColumn(4).setCellRenderer(moneyRenderer);

        loadMenu();
        updateTotal();
        // Tu cap nhat menu moi 15 giay neu quan ly nhap kho tren cua so khac.
        new javax.swing.Timer(15000, e -> loadMenu()).start();
    }

    // Hien ca mon het hang va mon quan ly ngung ban
    private void loadMenu() {
        try {
            KhoService.dongBo();
            List<mon_an> list = dao.selectAll();
            menuModel.setRowCount(0);
            for (mon_an ma : list) {
                String tt = !ma.isChoPhepBan() ? "Ngừng bán" :
                        (ma.isTrangThai() ? "Còn món" : "Hết món");
                menuModel.addRow(new Object[]{ma.getMaMon(), ma.getTenMon(), ma.getGia(), tt});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không tải được thực đơn từ CSDL!");
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

    // Thêm món đang chọn ở thực đơn vào giỏ
    private void addToCart() {
        int viewRow = tblThucDon.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một món trong thực đơn!");
            return;
        }
        int row = tblThucDon.convertRowIndexToModel(viewRow);
        if (!"Còn món".equals(menuModel.getValueAt(row, 3))) {
            JOptionPane.showMessageDialog(this, "Món này hiện đã hết hoặc ngừng bán!");
            return;
        }
        String ma = menuModel.getValueAt(row, 0).toString();
        String ten = menuModel.getValueAt(row, 1).toString();
        double gia = ((Number) menuModel.getValueAt(row, 2)).doubleValue();
        int soLuong = (Integer) spnSoLuong.getValue();

        // Nếu món đã có trong giỏ thì cộng dồn số lượng
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if (cartModel.getValueAt(i, 0).equals(ma)) {
                int sl = (Integer) cartModel.getValueAt(i, 2) + soLuong;
                cartModel.setValueAt(sl, i, 2);
                cartModel.setValueAt(sl * gia, i, 4);
                updateTotal();
                spnSoLuong.setValue(1);
                return;
            }
        }
        cartModel.addRow(new Object[]{ma, ten, soLuong, gia, soLuong * gia});
        updateTotal();
        spnSoLuong.setValue(1);
    }

    private void removeSelected() {
        int row = tblGioHang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món trong giỏ cần bỏ!");
            return;
        }
        cartModel.removeRow(row);
        updateTotal();
    }

    private void clearCart() {
        if (cartModel.getRowCount() == 0) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa toàn bộ giỏ hàng?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            cartModel.setRowCount(0);
            updateTotal();
        }
    }

    private double getTotal() {
        double total = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            total += ((Number) cartModel.getValueAt(i, 4)).doubleValue();
        }
        return total;
    }

    private void updateTotal() {
        lblTongTien.setText(money.format(getTotal()) + " đ");
    }

    private void placeOrder() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống, hãy chọn món trước!");
            return;
        }
        StringBuilder sb = new StringBuilder("XÁC NHẬN ĐẶT MÓN\n\n");
        List<HoaDonDAO.MatHang> gio = new ArrayList<>();
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String ma = cartModel.getValueAt(i, 0).toString();
            int sl = ((Number)cartModel.getValueAt(i, 2)).intValue();
            BigDecimal gia = BigDecimal.valueOf(((Number)cartModel.getValueAt(i, 3)).doubleValue());
            gio.add(new HoaDonDAO.MatHang(ma, sl, gia));
            sb.append(cartModel.getValueAt(i, 1)).append(" x").append(sl)
              .append(" = ").append(money.format(((Number)cartModel.getValueAt(i, 4)).doubleValue()))
              .append(" đ\n");
        }
        sb.append("\nTổng tiền: ").append(money.format(getTotal())).append(" đ");
        int confirm = JOptionPane.showConfirmDialog(this, sb.toString(),
                "Xác nhận đặt món", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;

        // Chi ghi nhan thanh cong SAU khi luu hoa don va tru kho cung giao dich.
        HoaDonDAO hddao = new HoaDonDAO();
        int maHD;
        try {
            maHD = hddao.datHang(gio);
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Đặt món thất bại: " + e.getMessage(),
                    "Không thể đặt món", JOptionPane.ERROR_MESSAGE);
            loadMenu();
            return;
        }

        // Tu day hoa don DA LUOC LUU thanh cong; loi in an khong huy hoa don.
        cartModel.setRowCount(0);
        updateTotal();
        loadMenu();
        String maIn = String.format("HD%06d", maHD);
        jLabel4.setText("GIỎ HÀNG CỦA BẠN   |   Mã hóa đơn: " + maIn);
        setTitle("KFC - Đặt món - Hóa đơn " + maIn);
        try {
            JTextArea bill = new JTextArea(hddao.noiDungHoaDon(maHD), 19, 49);
            bill.setFont(new Font("Monospaced", Font.PLAIN, 12));
            bill.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(bill),
                    "Đã lưu đơn " + maIn + " - Chuẩn bị in", JOptionPane.INFORMATION_MESSAGE);
            if (!bill.print()) {
                JOptionPane.showMessageDialog(this, "Bạn đã hủy in. Hóa đơn " + maIn
                        + " đã được lưu và có thể in lại ở Quản lý hóa đơn.");
            }
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Đã lưu đơn " + maIn
                    + " nhưng chưa lấy được nội dung để in: " + e.getMessage());
        } catch (java.awt.print.PrinterException e) {
            JOptionPane.showMessageDialog(this, "Đã lưu đơn " + maIn
                    + " nhưng in thất bại: " + e.getMessage());
        }
    }

    // ===================== GIAO DIỆN (kiểu NetBeans) =====================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        spnSoLuong = new javax.swing.JSpinner();
        btnThemGio = new javax.swing.JButton();
        btnXoaMon = new javax.swing.JButton();
        btnXoaHet = new javax.swing.JButton();
        btnDatMon = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblThucDon = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("KFC - Chọn Món & Gọi Món");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jLabel1.setText("THỰC ĐƠN");

        jLabel2.setText("Tìm món:");
        jLabel3.setText("Số lượng:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jLabel4.setText("GIỎ HÀNG CỦA BẠN");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel5.setText("Tổng tiền:");

        lblTongTien.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblTongTien.setForeground(new java.awt.Color(204, 0, 0));
        lblTongTien.setText("0 đ");

        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });

        btnThemGio.setText("Thêm vào giỏ");
        btnThemGio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemGioActionPerformed(evt);
            }
        });

        btnXoaMon.setText("Bỏ món");
        btnXoaMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaMonActionPerformed(evt);
            }
        });

        btnXoaHet.setText("Xóa hết");
        btnXoaHet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHetActionPerformed(evt);
            }
        });

        btnDatMon.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnDatMon.setText("ĐẶT MÓN");
        btnDatMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatMonActionPerformed(evt);
            }
        });

        tblThucDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Mã món", "Tên món", "Giá", "Trạng thái"}
        ) {
            boolean[] canEdit = new boolean[]{false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblThucDon.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblThucDon.setRowHeight(24);
        tblThucDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThucDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblThucDon);

        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{},
            new String[]{"Mã món", "Tên món", "SL", "Đơn giá", "Thành tiền"}
        ) {
            boolean[] canEdit = new boolean[]{false, false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tblGioHang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblGioHang.setRowHeight(24);
        jScrollPane2.setViewportView(tblGioHang);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(10, 10, 10)
                        .addComponent(spnSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThemGio)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(lblTongTien))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnXoaMon)
                        .addGap(10, 10, 10)
                        .addComponent(btnXoaHet)
                        .addGap(10, 10, 10)
                        .addComponent(btnDatMon, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(spnSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnThemGio)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(lblTongTien))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXoaMon)
                            .addComponent(btnXoaHet)
                            .addComponent(btnDatMon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    // ===================== SỰ KIỆN =====================
    private void btnThemGioActionPerformed(java.awt.event.ActionEvent evt) {
        addToCart();
    }

    private void btnXoaMonActionPerformed(java.awt.event.ActionEvent evt) {
        removeSelected();
    }

    private void btnXoaHetActionPerformed(java.awt.event.ActionEvent evt) {
        clearCart();
    }

    private void btnDatMonActionPerformed(java.awt.event.ActionEvent evt) {
        placeOrder();
    }

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {
        search();
    }

    // Nhấp đúp vào món trong thực đơn = thêm nhanh vào giỏ
    private void tblThucDonMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            addToCart();
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
            java.util.logging.Logger.getLogger(dat_mon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dat_mon().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnDatMon;
    private javax.swing.JButton btnThemGio;
    private javax.swing.JButton btnXoaHet;
    private javax.swing.JButton btnXoaMon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JSpinner spnSoLuong;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTable tblThucDon;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration
}