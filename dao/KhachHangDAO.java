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

    public boolean themKhachHang(KhachHang khachHang) {
        String sql = "INSERT INTO KhachHang(maKH, tenKH, soDT, soDiem) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, khachHang.getMaKH());
            stmt.setString(2, khachHang.getTenKH());
            stmt.setString(3, khachHang.getSoDT());
            stmt.setInt(4, khachHang.getSoDiem());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhachHang(KhachHang khachHang) {
        String sql = "UPDATE KhachHang SET tenKH = ?, soDT = ?, soDiem = ? WHERE maKH = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, khachHang.getTenKH());
            stmt.setString(2, khachHang.getSoDT());
            stmt.setInt(3, khachHang.getSoDiem());
            stmt.setString(4, khachHang.getMaKH());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHang getKhachHangTheoMa(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKH);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("soDT"),
                        rs.getInt("soDiem"));
                return khachHang;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KhachHang getKhachHangTheoSDT(String soDT) {
        String sql = "SELECT * FROM KhachHang WHERE soDT = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, soDT);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("soDT"),
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
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("soDT"),
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
        String sql = "SELECT * FROM KhachHang WHERE maKH LIKE ? OR tenKH LIKE ? OR soDT LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            stmt.setString(3, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                KhachHang khachHang = new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("soDT"),
                        rs.getInt("soDiem"));
                danhSachKhachHang.add(khachHang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhachHang;
    }

    public String taoMaKhachHangMoi() {
        String maKH = "KH";
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY maKH DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maKH");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maKH += String.format("%03d", so);
            } else {
                maKH += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maKH += "001";
        }
        return maKH;
    }
}