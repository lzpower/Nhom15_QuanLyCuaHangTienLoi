package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;

public class ChiTietHoaDonDAO {
    private Connection con;
    private HoaDonDAO hoaDonDAO;
    private SanPhamDAO sanPhamDAO;

    public ChiTietHoaDonDAO() {
    	ConnectDB.getInstance();
        con = ConnectDB.getConnection();
        hoaDonDAO = new HoaDonDAO();
        sanPhamDAO = new SanPhamDAO();
    }

    public boolean themChiTietHoaDon(ChiTietHoaDon chiTiet) {
        String sql = "INSERT INTO ChiTietHoaDon(maHoaDon, maSanPham, soLuong, donGia) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, chiTiet.getHoaDon().getMaHoaDon());
            stmt.setString(2, chiTiet.getSanPham().getMaSanPham());
            stmt.setInt(3, chiTiet.getSoLuong());
            stmt.setDouble(4, chiTiet.getDonGia());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaChiTietHoaDon(String maHoaDon, String maSanPham) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ? AND maSanPham = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHoaDon);
            stmt.setString(2, maSanPham);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatChiTietHoaDon(ChiTietHoaDon chiTiet) {
        String sql = "UPDATE ChiTietHoaDon SET soLuong = ?, donGia = ? WHERE maHoaDon = ? AND maSanPham = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, chiTiet.getSoLuong());
            stmt.setDouble(2, chiTiet.getDonGia());
            stmt.setString(3, chiTiet.getHoaDon().getMaHoaDon());
            stmt.setString(4, chiTiet.getSanPham().getMaSanPham());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChiTietHoaDon> getChiTietHoaDonTheoMaHD(String maHoaDon) {
        List<ChiTietHoaDon> danhSachChiTiet = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(rs.getString("maHoaDon"));
                SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(rs.getString("maSanPham"));
                
                ChiTietHoaDon chiTiet = new ChiTietHoaDon(
                        hoaDon,
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
}