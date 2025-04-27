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
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, khachHang.getMaKhachHang());
            stmt.setString(2, khachHang.getTenKhachHang());
            stmt.setString(3, khachHang.getSoDienThoai());
            stmt.setInt(4, khachHang.getSoDiem());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhachHang(String maKhachHang) {
        String sql = "DELETE FROM KhachHang WHERE maKhachHang = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKhachHang);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhachHang(KhachHang khachHang) {
        String sql = "UPDATE KhachHang SET tenKhachHang = ?, soDienThoai = ?, soDiem = ? WHERE maKhachHang = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, khachHang.getTenKhachHang());
            stmt.setString(2, khachHang.getSoDienThoai());
            stmt.setInt(3, khachHang.getSoDiem());
            stmt.setString(4, khachHang.getMaKhachHang());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHang getKhachHangTheoMa(String maKhachHang) {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKhachHang);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getInt("soDiem"));
                return khachHang;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KhachHang getKhachHangTheoSDT(String soDienThoai) {
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, soDienThoai);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getInt("soDiem"));
                return khachHang;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getInt("soDiem"));
                danhSachKhachHang.add(khachHang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhachHang;
    }

    public List<KhachHang> timKiemKhachHang(String tuKhoa) {
        List<KhachHang> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang LIKE ? OR tenKhachHang LIKE ? OR soDienThoai LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            stmt.setString(3, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getInt("soDiem"));
                danhSachKhachHang.add(khachHang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhachHang;
    }

    public String taoMaKhachHangMoi() {
        String maKhachHang = "KH";
        String sql = "SELECT TOP 1 maKhachHang FROM KhachHang ORDER BY maKhachHang DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maKhachHang");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maKhachHang += String.format("%03d", so);
            } else {
                maKhachHang += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maKhachHang += "001";
        }
        return maKhachHang;
    }
}