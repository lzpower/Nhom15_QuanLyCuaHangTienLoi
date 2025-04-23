package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.LoaiSanPham;
import entity.SanPham;

public class SanPhamDAO {
    private Connection con;
    private LoaiSanPhamDAO loaiSanPhamDAO;

    public SanPhamDAO() {
        con = ConnectDB.getConnection();
        loaiSanPhamDAO = new LoaiSanPhamDAO();
    }

    public boolean themSanPham(SanPham sanPham) {
        String sql = "INSERT INTO SanPham(maSP, tenSP, maLoaiSP, slHienCo, giaNhap) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, sanPham.getMaSP());
            stmt.setString(2, sanPham.getTenSP());
            stmt.setString(3, sanPham.getLoaiSP().getMaLoaiSP());
            stmt.setInt(4, sanPham.getSlHienCo());
            stmt.setDouble(5, sanPham.getGiaNhap());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaSanPham(String maSP) {
        String sql = "DELETE FROM SanPham WHERE maSP = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maSP);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatSanPham(SanPham sanPham) {
        String sql = "UPDATE SanPham SET tenSP = ?, maLoaiSP = ?, slHienCo = ?, giaNhap = ? WHERE maSP = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, sanPham.getTenSP());
            stmt.setString(2, sanPham.getLoaiSP().getMaLoaiSP());
            stmt.setInt(3, sanPham.getSlHienCo());
            stmt.setDouble(4, sanPham.getGiaNhap());
            stmt.setString(5, sanPham.getMaSP());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatSoLuongSanPham(String maSP, int soLuongMoi) {
        String sql = "UPDATE SanPham SET slHienCo = ? WHERE maSP = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, soLuongMoi);
            stmt.setString(2, maSP);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public SanPham getSanPhamTheoMa(String maSP) {
        String sql = "SELECT * FROM SanPham WHERE maSP = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maSP);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LoaiSanPham loaiSP = loaiSanPhamDAO.getLoaiSanPhamTheoMa(rs.getString("maLoaiSP"));
                SanPham sanPham = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        loaiSP,
                        rs.getInt("slHienCo"),
                        rs.getDouble("giaNhap"));
                return sanPham;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SanPham> getAllSanPham() {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                LoaiSanPham loaiSP = loaiSanPhamDAO.getLoaiSanPhamTheoMa(rs.getString("maLoaiSP"));
                SanPham sanPham = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        loaiSP,
                        rs.getInt("slHienCo"),
                        rs.getDouble("giaNhap"));
                danhSachSanPham.add(sanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }

    public List<SanPham> getSanPhamTheoLoai(String maLoaiSP) {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE maLoaiSP = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maLoaiSP);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LoaiSanPham loaiSP = loaiSanPhamDAO.getLoaiSanPhamTheoMa(rs.getString("maLoaiSP"));
                SanPham sanPham = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        loaiSP,
                        rs.getInt("slHienCo"),
                        rs.getDouble("giaNhap"));
                danhSachSanPham.add(sanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }

    public List<SanPham> timKiemSanPham(String tuKhoa) {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE maSP LIKE ? OR tenSP LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LoaiSanPham loaiSP = loaiSanPhamDAO.getLoaiSanPhamTheoMa(rs.getString("maLoaiSP"));
                SanPham sanPham = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        loaiSP,
                        rs.getInt("slHienCo"),
                        rs.getDouble("giaNhap"));
                danhSachSanPham.add(sanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }

    public String taoMaSanPhamMoi() {
        String maSP = "SP";
        String sql = "SELECT TOP 1 maSP FROM SanPham ORDER BY maSP DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maSP");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maSP += String.format("%03d", so);
            } else {
                maSP += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maSP += "001";
        }
        return maSP;
    }
}