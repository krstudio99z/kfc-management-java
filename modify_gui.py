from pathlib import Path
base=Path('/mnt/data/KFC_NangCap/src/kfc/ui')
p=base/'dat_mon.java'
s=p.read_text()
s=s.replace('import kfc.dao.truy_cap;', 'import kfc.dao.truy_cap;\nimport kfc.dao.HoaDonDAO;\nimport kfc.service.KhoService;\nimport java.math.BigDecimal;\nimport java.util.ArrayList;\nimport javax.swing.JTextArea;\nimport javax.swing.JScrollPane;\nimport java.awt.Font;')
s=s.replace('    // Chỉ hiển thị các món đang "Còn bán"', '    // Hien ca mon het hang va mon quan ly ngung ban')
s=s.replace('''        menuModel.setRowCount(0);
        try {
            List<mon_an> list = dao.selectAll();
            for (mon_an ma : list) {
                if (ma.isTrangThai()) {
                    menuModel.addRow(new Object[]{ma.getMaMon(), ma.getTenMon(), ma.getGia()});
                }
            }
''','''        try {
            KhoService.dongBo();
            List<mon_an> list = dao.selectAll();
            menuModel.setRowCount(0);
            for (mon_an ma : list) {
                String tt = !ma.isChoPhepBan() ? "Ngừng bán" :
                        (ma.isTrangThai() ? "Còn món" : "Hết món");
                menuModel.addRow(new Object[]{ma.getMaMon(), ma.getTenMon(), ma.getGia(), tt});
            }
''')
s=s.replace('''        String ma = menuModel.getValueAt(row, 0).toString();
        String ten = menuModel.getValueAt(row, 1).toString();''','''        if (!"Còn món".equals(menuModel.getValueAt(row, 3))) {
            JOptionPane.showMessageDialog(this, "Món này hiện đã hết hoặc ngừng bán!");
            return;
        }
        String ma = menuModel.getValueAt(row, 0).toString();
        String ten = menuModel.getValueAt(row, 1).toString();''')
a=s.index('    private void placeOrder() {')
b=s.index('    // ===================== GIAO DIỆN',a)
s=s[:a]+'''    private void placeOrder() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống, hãy chọn món trước!");
            return;
        }
        StringBuilder sb = new StringBuilder("XÁC NHẬN ĐẶT MÓN\\n\\n");
        List<HoaDonDAO.MatHang> gio = new ArrayList<>();
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String ma = cartModel.getValueAt(i, 0).toString();
            int sl = ((Number)cartModel.getValueAt(i, 2)).intValue();
            BigDecimal gia = BigDecimal.valueOf(((Number)cartModel.getValueAt(i, 3)).doubleValue());
            gio.add(new HoaDonDAO.MatHang(ma, sl, gia));
            sb.append(cartModel.getValueAt(i, 1)).append(" x").append(sl)
              .append(" = ").append(money.format(((Number)cartModel.getValueAt(i, 4)).doubleValue()))
              .append(" đ\\n");
        }
        sb.append("\\nTổng tiền: ").append(money.format(getTotal())).append(" đ");
        int confirm = JOptionPane.showConfirmDialog(this, sb.toString(),
                "Xác nhận đặt món", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;

        // Chi ghi nhan thanh cong SAU khi luu hoa don va tru kho cung giao dich.
        try {
            HoaDonDAO hddao = new HoaDonDAO();
            int maHD = hddao.datHang(gio);
            cartModel.setRowCount(0);
            updateTotal();
            loadMenu();
            String maIn = String.format("HD%06d", maHD);
            jLabel4.setText("GIỎ HÀNG CỦA BẠN   |   Mã hóa đơn: " + maIn);
            setTitle("KFC - Đặt món - Hóa đơn " + maIn);
            JTextArea bill = new JTextArea(hddao.noiDungHoaDon(maHD), 19, 49);
            bill.setFont(new Font("Monospaced", Font.PLAIN, 12));
            bill.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(bill),
                    "Đã lưu đơn " + maIn + " - Chuẩn bị in", JOptionPane.INFORMATION_MESSAGE);
            // Hien hop thoai chon may in; huy in KHONG huy hoa don da luu.
            if (!bill.print()) {
                JOptionPane.showMessageDialog(this, "Bạn đã hủy in. Hóa đơn " + maIn
                        + " đã được lưu và có thể in lại ở Quản lý hóa đơn.");
            }
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Đặt món thất bại: " + e.getMessage(),
                    "Không thể đặt món", JOptionPane.ERROR_MESSAGE);
            loadMenu();
        } catch (java.awt.print.PrinterException e) {
            JOptionPane.showMessageDialog(this, "Đơn đã được lưu, nhưng in thất bại: " + e.getMessage());
        }
    }

'''+s[b:]
s=s.replace('new String[]{"Mã món", "Tên món", "Giá"}', 'new String[]{"Mã món", "Tên món", "Giá", "Trạng thái"}')
s=s.replace('boolean[] canEdit = new boolean[]{false, false, false};', 'boolean[] canEdit = new boolean[]{false, false, false, false};')
# Add button simple: no layout modification? use double click header refresh (not ideal). Add timer for auto fresh
s=s.replace('''        loadMenu();
        updateTotal();
    }''','''        loadMenu();
        updateTotal();
        // Tu cap nhat menu moi 15 giay neu quan ly nhap kho tren cua so khac.
        new javax.swing.Timer(15000, e -> loadMenu()).start();
    }''',1)
p.write_text(s)

p=base/'ql_mon_an.java'
s=p.read_text()
s=s.replace('import kfc.kt_loi;', 'import kfc.kt_loi;\nimport kfc.service.PhienQuanLy;')
s=s.replace('''    public ql_mon_an() {
        initComponents();''','''    public ql_mon_an() {
        PhienQuanLy.yeuCauDangNhap();
        initComponents();''')
s=s.replace('''ma.isTrangThai() ? "Còn bán" : "Ngừng bán"''','''!ma.isChoPhepBan() ? "Ngừng bán" : (ma.isTrangThai() ? "Còn bán" : "Hết nguyên liệu")''')
s=s.replace('''        ma.setTrangThai(rdoConBan.isSelected());''', '''        ma.setChoPhepBan(rdoConBan.isSelected());''')
s=s.replace('''if ("Còn bán".equals(tableModel.getValueAt(row, 3).toString()))''','''if (!"Ngừng bán".equals(tableModel.getValueAt(row, 3).toString()))''')
s=s.replace('''setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);''','''setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);''')
s=s.replace('''        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ql_mon_an().setVisible(true);
            }
        });''','''        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dang_nhap().setVisible(true);
            }
        });''')
s=s.replace('''"Xóa thất bại! Món này có thể đang nằm trong Hóa đơn."''','''"Xóa thất bại! Cần xóa công thức trước; không xóa được món đã có hóa đơn."''')
p.write_text(s)
