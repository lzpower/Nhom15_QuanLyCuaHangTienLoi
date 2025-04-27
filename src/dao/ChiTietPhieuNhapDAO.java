package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietPhieuNhap;
import entity.PhieuNhap;
import entity.SanPham;

public class ChiTietPhieuNhapDAO {
    private Connection con;
    private PhieuNhapDAO phieuNhapDAO;
    private SanPhamDAO sanPhamDAO;

    public ChiTietPhieuNhapDAO() {
        con = ConnectDB.getConnection();
        phieuNhapDAO = new PhieuNhapDAO();
        sanPhamDAO = new SanPhamDAO();
    }

    public ChiTietPhieuNhapDAO(Connection conn) {
        this.con = conn;
        phieuNhapDAO = new PhieuNhapDAO(conn);
        sanPhamDAO = new SanPhamDAO(conn);
    }

    public boolean themChiTietPhieuNhap(ChiTietPhieuNhap chiTiet) {
        String sql = "INSERT INTO ChiTietPhieuNhap(maPhieuNhap, maSanPham, soLuong, donGia) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, chiTiet.getPhieuNhap().getMaPhieuNhap());
            stmt.setString(2, chiTiet.getSanPham().getMaSanPham());
            stmt.setInt(3, chiTiet.getSoLuong());
            stmt.setDouble(4, chiTiet.getDonGia());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaChiTietPhieuNhap(String maPhieuNhap, String maSanPham) {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap = ? AND maSanPham = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhieuNhap);
            stmt.setString(2, maSanPham);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatChiTietPhieuNhap(ChiTietPhieuNhap chiTiet) {
        String sql = "UPDATE ChiTietPhieuNhap SET soLuong = ?, donGia = ? WHERE maPhieuNhap = ? AND maSanPham = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, chiTiet.getSoLuong());
            stmt.setDouble(2, chiTiet.getDonGia());
            stmt.setString(3, chiTiet.getPhieuNhap().getMaPhieuNhap());
            stmt.setString(4, chiTiet.getSanPham().getMaSanPham());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietPhieuNhap> getChiTietPhieuNhapTheoMaPN(String maPhieuNhap) {
        List<ChiTietPhieuNhap> danhSachChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE maPhieuNhap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhieuNhap);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PhieuNhap phieuNhap = phieuNhapDAO.getPhieuNhapTheoMa(rs.getString("maPhieuNhap"));
                SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(rs.getString("maSanPham"));
                
                ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap(
                        phieuNhap,
                        sanPham,
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"));
                danhSachChiTiet.add(chiTiet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachChiTiet;
    }
    
    public double getTongTienPhieuNhap(String maPhieuNhap) {
        double tongTien = 0;
        String sql = "SELECT SUM(soLuong * donGia) AS tongTien FROM ChiTietPhieuNhap WHERE maPhieuNhap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhieuNhap);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tongTien = rs.getDouble("tongTien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongTien;
    }
}