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
import entity.HoaDon;

public class HoaDonDAO {
    private Connection con;

    public HoaDonDAO() {
        con = ConnectDB.getConnection();
    }

    public boolean themHoaDon(HoaDon hoaDon) {
        String sql = "INSERT INTO HoaDon(maHoaDon, ngayLap, maNhanVien) VALUES(?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, hoaDon.getMaHoaDon());
            stmt.setDate(2, new java.sql.Date(hoaDon.getNgayLap().getTime()));
            stmt.setString(3, hoaDon.getMaNhanVien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaHoaDon(String maHoaDon) {
        // Xóa chi tiết hóa đơn trước
        String sqlChiTiet = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
        try {
            PreparedStatement stmtChiTiet = con.prepareStatement(sqlChiTiet);
            stmtChiTiet.setString(1, maHoaDon);
            stmtChiTiet.executeUpdate();
            
            // Sau đó xóa hóa đơn
            String sqlHoaDon = "DELETE FROM HoaDon WHERE maHoaDon = ?";
            PreparedStatement stmtHoaDon = con.prepareStatement(sqlHoaDon);
            stmtHoaDon.setString(1, maHoaDon);
            return stmtHoaDon.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatHoaDon(HoaDon hoaDon) {
        String sql = "UPDATE HoaDon SET ngayLap = ?, maNhanVien = ? WHERE maHoaDon = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(hoaDon.getNgayLap().getTime()));
            stmt.setString(2, hoaDon.getMaNhanVien());
            stmt.setString(3, hoaDon.getMaHoaDon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HoaDon getHoaDonTheoMa(String maHoaDon) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                HoaDon hoaDon = new HoaDon(
                        rs.getString("maHoaDon"),
                        rs.getDate("ngayLap"),
                        rs.getString("maNhanVien"));
                return hoaDon;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                HoaDon hoaDon = new HoaDon(
                        rs.getString("maHoaDon"),
                        rs.getDate("ngayLap"),
                        rs.getString("maNhanVien"));
                danhSachHoaDon.add(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachHoaDon;
    }

    public List<HoaDon> getHoaDonTheoNgay(Date tuNgay, Date denNgay) {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE ngayLap BETWEEN ? AND ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(tuNgay.getTime()));
            stmt.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = new HoaDon(
                        rs.getString("maHoaDon"),
                        rs.getDate("ngayLap"),
                        rs.getString("maNhanVien"));
                danhSachHoaDon.add(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachHoaDon;
    }

    public String taoMaHoaDonMoi() {
        String maHoaDon = "HD";
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon ORDER BY maHoaDon DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maHoaDon");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maHoaDon += String.format("%03d", so);
            } else {
                maHoaDon += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maHoaDon += "001";
        }
        return maHoaDon;
    }
}