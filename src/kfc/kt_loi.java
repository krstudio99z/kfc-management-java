package kfc;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class kt_loi {

    // Bắt lỗi bỏ trống dữ liệu
    public static boolean checkEmpty(JTextField txt, String msg) {
        if (txt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, msg, "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txt.requestFocus();
            return false;
        }
        return true;
    }

    // Bẫy lỗi sai định dạng SĐT (phải đủ 10 số)
    public static boolean checkPhone(JTextField txt) {
        String phoneRegex = "^0\\d{9}$";
        if (!txt.getText().trim().matches(phoneRegex)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (Phải bắt đầu bằng 0 và đủ 10 số)", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
            txt.requestFocus();
            return false;
        }
        return true;
    }

    // Bẫy lỗi giá tiền (phải là số dương)
    public static boolean checkPrice(JTextField txt) {
        try {
            double price = Double.parseDouble(txt.getText().trim());
            if (price <= 0) {
                 JOptionPane.showMessageDialog(null, "Giá tiền phải là số dương lớn hơn 0", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                 txt.requestFocus();
                 return false;
            }
        } catch (NumberFormatException e) {
             JOptionPane.showMessageDialog(null, "Giá tiền phải là một con số", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
             txt.requestFocus();
             return false;
        }
        return true;
    }
}

