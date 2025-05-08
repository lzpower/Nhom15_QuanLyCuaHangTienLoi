package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import connectDB.ConnectDB;
import entity.KhuyenMai;

public class KhuyenMaiDAO {
    private Connection con;

    public KhuyenMaiDAO() {
        con = ConnectDB.getConnection();
    }
    
    public KhuyenMaiDAO(Connection conn) {
        this.con = conn;
    }

    public boolean themKhuyenMai(KhuyenMai khuyenMai) {
        String sql = "INSERT INTO KhuyenMai(maKhuyenMai, tenKhuyenMai, giaTriKhuyenMai, ngayBatDau, ngayKetThuc) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, khuyenMai.getMaKhuyenMai());
            stmt.setString(2, khuyenMai.getTenKhuyenMai());
            stmt.setDouble(3, khuyenMai.getGiaTriKhuyenMai());
            
            if (khuyenMai.getNgayBatDau() != null) {
                stmt.setDate(4, new java.sql.Date(khuyenMai.getNgayBatDau().getTime()));
            } else {
                stmt.setDate(4, new java.sql.Date(new Date().getTime())); // Default to current date
            }
            
            if (khuyenMai.getNgayKetThuc() != null) {
                stmt.setDate(5, new java.sql.Date(khuyenMai.getNgayKetThuc().getTime()));
            } else {
                // Default to 30 days from now
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.DATE, 30);
                stmt.setDate(5, new java.sql.Date(cal.getTimeInMillis()));
            }
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhuyenMai(String maKhuyenMai) {
        String sql = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKhuyenMai);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhuyenMai(KhuyenMai khuyenMai) {
        String sql = "UPDATE KhuyenMai SET tenKhuyenMai = ?, giaTriKhuyenMai = ?, ngayBatDau = ?, ngayKetThuc = ? WHERE maKhuyenMai = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, khuyenMai.getTenKhuyenMai());
            stmt.setDouble(2, khuyenMai.getGiaTriKhuyenMai());
            
            if (khuyenMai.getNgayBatDau() != null) {
                stmt.setDate(3, new java.sql.Date(khuyenMai.getNgayBatDau().getTime()));
            } else {
                stmt.setDate(3, new java.sql.Date(new Date().getTime()));
            }
            
            if (khuyenMai.getNgayKetThuc() != null) {
                stmt.setDate(4, new java.sql.Date(khuyenMai.getNgayKetThuc().getTime()));
            } else {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.add(java.util.Calendar.DATE, 30);
                stmt.setDate(4, new java.sql.Date(cal.getTimeInMillis()));
            }
            
            stmt.setString(5, khuyenMai.getMaKhuyenMai());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhuyenMai getKhuyenMaiTheoMa(String maKhuyenMai) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maKhuyenMai);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                KhuyenMai khuyenMai = new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getDouble("giaTriKhuyenMai"));
                khuyenMai.setNgayBatDau(rs.getDate("ngayBatDau"));
                khuyenMai.setNgayKetThuc(rs.getDate("ngayKetThuc"));
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
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getDouble("giaTriKhuyenMai"));
                khuyenMai.setNgayBatDau(rs.getDate("ngayBatDau"));
                khuyenMai.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                danhSachKhuyenMai.add(khuyenMai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhuyenMai;
    }

    public List<KhuyenMai> timKiemKhuyenMai(String tuKhoa) {
        List<KhuyenMai> danhSachKhuyenMai = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai LIKE ? OR tenKhuyenMai LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                KhuyenMai khuyenMai = new KhuyenMai(
                        rs.getString("maKhuyenMai"),
                        rs.getString("tenKhuyenMai"),
                        rs.getDouble("giaTriKhuyenMai"));
                khuyenMai.setNgayBatDau(rs.getDate("ngayBatDau"));
                khuyenMai.setNgayKetThuc(rs.getDate("ngayKetThuc"));
                danhSachKhuyenMai.add(khuyenMai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhuyenMai;
    }

    public String taoMaKhuyenMaiMoi() {
        String maKhuyenMai = "KM";
        String sql = "SELECT TOP 1 maKhuyenMai FROM KhuyenMai ORDER BY maKhuyenMai DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maKhuyenMai");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maKhuyenMai += String.format("%03d", so);
            } else {
                maKhuyenMai += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maKhuyenMai += "001";
        }
        return maKhuyenMai;
    }
}