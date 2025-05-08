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

    public SanPhamDAO() {
        con = ConnectDB.getConnection();
    }




	public boolean themSanPham(SanPham sanPham) {
        String sql = "INSERT INTO SanPham(maSanPham, tenSanPham, maLoaiSanPham, soLuongHienCo, giaNhap, giaBan ,urlHinhAnh) VALUES(?, ?, ?, ?, ?,?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, sanPham.getMaSanPham());
            stmt.setString(2, sanPham.getTenSanPham());
            stmt.setString(3, sanPham.getLoaiSanPham().getMaLoaiSanPham());
            stmt.setInt(4, sanPham.getSoLuongHienCo());
            stmt.setDouble(5, sanPham.getGiaNhap());
            stmt.setDouble(6, sanPham.getGiaBan());
            stmt.setString(7, sanPham.getUrlHinhAnh());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaSanPham(String maSanPham) {
        String sql = "DELETE FROM SanPham WHERE maSanPham = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maSanPham);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatSanPham(SanPham sanPham) {
        String sql = "UPDATE SanPham SET tenSanPham = ?, maLoaiSanPham = ?, soLuongHienCo = ?, giaNhap = ?, giaBan=? , urlHinhAnh = ? WHERE maSanPham = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, sanPham.getTenSanPham());
            stmt.setString(2, sanPham.getLoaiSanPham().getMaLoaiSanPham());
            stmt.setInt(3, sanPham.getSoLuongHienCo());
            stmt.setDouble(4, sanPham.getGiaNhap());
            stmt.setDouble(5, sanPham.getGiaBan());
            stmt.setString(6, sanPham.getUrlHinhAnh());
            stmt.setString(7, sanPham.getMaSanPham());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatSoLuongSanPham(String maSanPham, int soLuongMoi) {
        String sql = "UPDATE SanPham SET soLuongHienCo = ? WHERE maSanPham = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, soLuongMoi);
            stmt.setString(2, maSanPham);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public SanPham getSanPhamTheoMa(String maSanPham) {
        String sql = "SELECT * FROM SanPham sp join LoaiSanPham lsp on sp.maLoaiSanPham= lsp.maLoaiSanPham WHERE maSanPham = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maSanPham);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LoaiSanPham loaiSP = new LoaiSanPham(rs.getString("maLoaiSanPham"), rs.getString("tenLoaiSanPham"));
                SanPham sanPham = new SanPham(
                        rs.getString("maSanPham"),
                        rs.getString("tenSanPham"),
                        loaiSP,
                        rs.getInt("soLuongHienCo"),
                        rs.getDouble("giaNhap"),
                        rs.getString("urlHinhAnh"));
                return sanPham;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SanPham> getAllSanPham() {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        String sql = "SELECT * FROM SanPham sp join LoaiSanPham lsp on sp.maLoaiSanPham= lsp.maLoaiSanPham";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
            	LoaiSanPham loaiSP = new LoaiSanPham(rs.getString("maLoaiSanPham"), rs.getString("tenLoaiSanPham"));
                SanPham sanPham = new SanPham(
                        rs.getString("maSanPham"),
                        rs.getString("tenSanPham"),
                        loaiSP,
                        rs.getInt("soLuongHienCo"),
                        rs.getDouble("giaNhap"),
                        rs.getString("urlHinhAnh"));
                danhSachSanPham.add(sanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }

    public List<SanPham> getSanPhamTheoLoai(String maLoaiSanPham) {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        String sql = "SELECT * FROM SanPham sp join LoaiSanPham lsp on sp.maLoaiSanPham = lsp.maLoaiSanPham WHERE sp.maLoaiSanPham = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maLoaiSanPham);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	LoaiSanPham loaiSP = new LoaiSanPham(rs.getString("maLoaiSanPham"), rs.getString("tenLoaiSanPham"));
                SanPham sanPham = new SanPham(
                        rs.getString("maSanPham"),
                        rs.getString("tenSanPham"),
                        loaiSP,
                        rs.getInt("soLuongHienCo"),
                        rs.getDouble("giaNhap"),
                        rs.getString("urlHinhAnh"));
                danhSachSanPham.add(sanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }

    public List<SanPham> timKiemSanPham(String tuKhoa) {
        List<SanPham> danhSachSanPham = new ArrayList<>();
        String sql = "SELECT * FROM SanPham sp join LoaiSanPham lsp on sp.maLoaiSanPham= lsp.maLoaiSanPham WHERE maSanPham LIKE ? OR tenSanPham LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	LoaiSanPham loaiSP = new LoaiSanPham(rs.getString("maLoaiSanPham"), rs.getString("tenLoaiSanPham"));
                SanPham sanPham = new SanPham(
                        rs.getString("maSanPham"),
                        rs.getString("tenSanPham"),
                        loaiSP,
                        rs.getInt("soLuongHienCo"),
                        rs.getDouble("giaNhap"),
                        rs.getString("urlHinhAnh"));
                danhSachSanPham.add(sanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachSanPham;
    }

    public String taoMaSanPhamMoi() {
        String maSanPham = "";
        // Sử dụng mã barcode 12 chữ số
        String sql = "SELECT TOP 1 CAST(maSanPham AS BIGINT) as maSP FROM SanPham ORDER BY maSP DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                long maCuoi = rs.getLong("maSP");
                maSanPham = String.valueOf(maCuoi + 1);
                // Đảm bảo đủ 12 chữ số
                while (maSanPham.length() < 12) {
                    maSanPham = "0" + maSanPham;
                }
            } else {
                maSanPham = "000000000001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maSanPham = "000000000001";
        }
        return maSanPham;
    }
}