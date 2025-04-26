package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.TaiKhoan;

public class TaiKhoanDAO {
    private Connection con;

    public TaiKhoanDAO() {
        con = ConnectDB.getConnection();
    }

    public boolean themTaiKhoan(TaiKhoan taiKhoan) {
        String sql = "INSERT INTO TaiKhoan(tenDangNhap, matKhau, vaiTro, maNhanVien) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, taiKhoan.getTenDangNhap());
            stmt.setString(2, taiKhoan.getMatKhau());
            stmt.setString(3, taiKhoan.getVaiTro());
            stmt.setString(4, taiKhoan.getMaNhanVien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaTaiKhoan(String tenDangNhap) {
        String sql = "DELETE FROM TaiKhoan WHERE tenDangNhap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, tenDangNhap);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatTaiKhoan(TaiKhoan taiKhoan) {
        String sql = "UPDATE TaiKhoan SET matKhau = ?, vaiTro = ?, maNhanVien = ? WHERE tenDangNhap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, taiKhoan.getMatKhau());
            stmt.setString(2, taiKhoan.getVaiTro());
            stmt.setString(3, taiKhoan.getMaNhanVien());
            stmt.setString(4, taiKhoan.getTenDangNhap());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public TaiKhoan getTaiKhoanTheoTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, tenDangNhap);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TaiKhoan taiKhoan = new TaiKhoan(
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"),
                        rs.getString("vaiTro"),
                        rs.getString("maNhanVien"));
                return taiKhoan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> danhSachTaiKhoan = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                TaiKhoan taiKhoan = new TaiKhoan(
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"),
                        rs.getString("vaiTro"),
                        rs.getString("maNhanVien"));
                danhSachTaiKhoan.add(taiKhoan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachTaiKhoan;
    }

    public boolean kiemTraDangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matKhau);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}