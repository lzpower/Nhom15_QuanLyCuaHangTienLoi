package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhuyenMai;

public class KhuyenMaiDAO {
    private Connection con;

    public KhuyenMaiDAO() {
        con = ConnectDB.getConnection();
    }

    public boolean themKhuyenMai(KhuyenMai khuyenMai) {
        String sql = "INSERT INTO KhuyenMai(maKM, tenKM, giaTriKM) VALUES(?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, khuyenMai.getMaKM());
            stmt.setString(2, khuyenMai.getTenKM());
            stmt.setDouble(3, khuyenMai.getGiaTriKM());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhuyenMai(String maKM) {
        String sql = "DELETE FROM KhuyenMai WHERE maKM = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKM);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhuyenMai(KhuyenMai khuyenMai) {
        String sql = "UPDATE KhuyenMai SET tenKM = ?, giaTriKM = ? WHERE maKM = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, khuyenMai.getTenKM());
            stmt.setDouble(2, khuyenMai.getGiaTriKM());
            stmt.setString(3, khuyenMai.getMaKM());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhuyenMai getKhuyenMaiTheoMa(String maKM) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKM = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKM);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                KhuyenMai khuyenMai = new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getDouble("giaTriKM"));
                return khuyenMai;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<KhuyenMai> getAllKhuyenMai() {
        List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                KhuyenMai khuyenMai = new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getDouble("giaTriKM"));
                danhSachKhuyenMai.add(khuyenMai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhuyenMai;
    }

    public List<KhuyenMai> timKiemKhuyenMai(String tuKhoa) {
        List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE maKM LIKE ? OR tenKM LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                KhuyenMai khuyenMai = new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getDouble("giaTriKM"));
                danhSachKhuyenMai.add(khuyenMai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhuyenMai;
    }

    public String taoMaKhuyenMaiMoi() {
        String maKM = "KM";
        String sql = "SELECT TOP 1 maKM FROM KhuyenMai ORDER BY maKM DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maKM");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maKM += String.format("%03d", so);
            } else {
                maKM += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maKM += "001";
        }
        return maKM;
    }
}