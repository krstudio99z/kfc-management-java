package kfc.dao;

import kfc.database;
import kfc.entity.mon_an;
import kfc.service.KhoService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class truy_cap {
    public void insert(mon_an m) {
        try {
            KhoService.giaoDich(c -> {
                try (PreparedStatement p = c.prepareStatement(
                        "INSERT INTO monan(MaMon,TenMon,Gia,TrangThai,ChoPhepBan) VALUES(?,?,?,0,?)")) {
                    p.setString(1, m.getMaMon()); p.setString(2, m.getTenMon());
                    p.setDouble(3, m.getGia()); p.setBoolean(4, m.isChoPhepBan());
                    p.executeUpdate();
                }
            });
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void update(mon_an m) {
        try {
            KhoService.giaoDich(c -> {
                try (PreparedStatement p = c.prepareStatement(
                        "UPDATE monan SET TenMon=?,Gia=?,ChoPhepBan=? WHERE MaMon=?")) {
                    p.setString(1, m.getTenMon()); p.setDouble(2, m.getGia());
                    p.setBoolean(3, m.isChoPhepBan()); p.setString(4, m.getMaMon());
                    if (p.executeUpdate() == 0) throw new SQLException("Món ăn không tồn tại");
                }
            });
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(String id) {
        try {
            KhoService.giaoDich(c -> {
                try (PreparedStatement p = c.prepareStatement("DELETE FROM monan WHERE MaMon=?")) {
                    p.setString(1, id);
                    p.executeUpdate();
                }
            });
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private List<mon_an> selectBySql(String sql, Object... args) {
        List<mon_an> list = new ArrayList<>();
        try (Connection c = database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) p.setObject(i + 1, args[i]);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    mon_an m = new mon_an();
                    m.setMaMon(r.getString("MaMon")); m.setTenMon(r.getString("TenMon"));
                    m.setGia(r.getDouble("Gia")); m.setTrangThai(r.getBoolean("TrangThai"));
                    m.setChoPhepBan(r.getBoolean("ChoPhepBan"));
                    list.add(m);
                }
            }
            return list;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<mon_an> selectAll() { return selectBySql("SELECT * FROM monan ORDER BY MaMon"); }
    public List<mon_an> selectByKeyword(String keyword) {
        return selectBySql("SELECT * FROM monan WHERE TenMon LIKE ? ORDER BY MaMon", "%"+keyword+"%");
    }
}
