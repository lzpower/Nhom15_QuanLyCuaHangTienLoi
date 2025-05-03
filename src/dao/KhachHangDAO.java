package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {
    private Connection con;

    public KhachHangDAO() {
        con = ConnectDB.getConnection();
    }
    
    public KhachHangDAO(Connection conn) {
        this.con = conn;
    }
    public boolean themKhachHang(KhachHang khachHang) {
        String sql = "INSERT INTO KhachHang(maKhachHang, tenKhachHang, soDienThoai, soDiem) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, khachHang.getMaKhachHang());
            stmt.setString(2, khachHang.getTenKhachHang());
            stmt.setString(3, khachHang.getSoDienThoai());
            stmt.setInt(4, khachHang.getSoDiem());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm khách hàng: " + e.getMessage());
            return false;
        }
    }
    public boolean xoaKhachHang(String maKhachHang) {
        String sql = "DELETE FROM KhachHang WHERE maKhachHang = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKhachHang);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa khách hàng: " + e.getMessage());
            return false;
        }
    }
    public boolean capNhatKhachHang(KhachHang khachHang) {
        String sql = "UPDATE KhachHang SET tenKhachHang = ?, soDienThoai = ?, soDiem = ? WHERE maKhachHang = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, khachHang.getTenKhachHang());
            stmt.setString(2, khachHang.getSoDienThoai());
            stmt.setInt(3, khachHang.getSoDiem());
            stmt.setString(4, khachHang.getMaKhachHang());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật khách hàng: " + e.getMessage());
            return false;
        }
    }
    public KhachHang getKhachHangTheoMa(String maKhachHang) {
        String sql = "SELECT maKhachHang, tenKhachHang, soDienThoai, soDiem FROM KhachHang WHERE maKhachHang = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKhachHang);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("maKhachHang"),
                            rs.getString("tenKhachHang"),
                            rs.getString("soDienThoai"),
                            rs.getInt("soDiem"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khách hàng theo mã: " + e.getMessage());
        }
        return null;
    }
    public KhachHang getKhachHangTheoSDT(String soDienThoai) {
        String sql = "SELECT maKhachHang, tenKhachHang, soDienThoai, soDiem FROM KhachHang WHERE soDienThoai = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, soDienThoai);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("maKhachHang"),
                            rs.getString("tenKhachHang"),
                            rs.getString("soDienThoai"),
                            rs.getInt("soDiem"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm khách hàng theo số điện thoại: " + e.getMessage());
        }
        return null;
    }
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT maKhachHang, tenKhachHang, soDienThoai, soDiem FROM KhachHang";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getInt("soDiem"));
                danhSachKhachHang.add(khachHang);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách khách hàng: " + e.getMessage());
        }
        return danhSachKhachHang;
    }
    public List<KhachHang> timKiemKhachHang(String tuKhoa) {
        List<KhachHang> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT maKhachHang, tenKhachHang, soDienThoai, soDiem FROM KhachHang " +
                     "WHERE maKhachHang LIKE ? OR tenKhachHang LIKE ? OR soDienThoai LIKE ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            String searchPattern = "%" + tuKhoa + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KhachHang khachHang = new KhachHang(
                            rs.getString("maKhachHang"),
                            rs.getString("tenKhachHang"),
                            rs.getString("soDienThoai"),
                            rs.getInt("soDiem"));
                    danhSachKhachHang.add(khachHang);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm khách hàng: " + e.getMessage());
        }
        return danhSachKhachHang;
    }
    public String taoMaKhachHangMoi() {
        String maKhachHang = "KH";
        String sql = "SELECT TOP 1 maKhachHang FROM KhachHang ORDER BY maKhachHang DESC";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String maCuoi = rs.getString("maKhachHang");
                if (maCuoi != null && maCuoi.length() > 2) {
                    try {
                        int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                        maKhachHang += String.format("%03d", so);
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi khi chuyển đổi mã khách hàng: " + e.getMessage());
                        maKhachHang += "001";
                    }
                } else {
                    maKhachHang += "001";
                }
            } else {
                maKhachHang += "001";
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo mã khách hàng mới: " + e.getMessage());
            maKhachHang += "001";
        }
        return maKhachHang;
    }

    public boolean capNhatDiem(String maKhachHang, int diemMoi) {
        String sql = "UPDATE KhachHang SET soDiem = ? WHERE maKhachHang = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, diemMoi);
            stmt.setString(2, maKhachHang);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật điểm khách hàng: " + e.getMessage());
            return false;
        }
    }

    public boolean kiemTraSoDienThoaiTonTai(String soDienThoai, String maKhachHangHienTai) {
        String sql = "SELECT COUNT(*) FROM KhachHang WHERE soDienThoai = ? AND maKhachHang <> ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, soDienThoai);
            stmt.setString(2, maKhachHangHienTai != null ? maKhachHangHienTai : "");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra số điện thoại: " + e.getMessage());
        }
        return false;
    }
    
    public String taoKhachHangMoi() {
        String maKhachHang = "KH";
        String sql = "SELECT TOP 1 maKhachHang FROM KhachHang ORDER BY maKhachHang DESC";
   
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maKhachHang");
                // Extract the numeric part and increment
                if (maCuoi != null && maCuoi.length() > 2) {
                    int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                    maKhachHang += String.format("%03d", so);
                } else {
                	maKhachHang += "001";
                }
            } else {
            	maKhachHang += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maKhachHang += "001";
        }
        return maKhachHang;
    }
    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
        }
    }
}