package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVienDAO {
    private Connection con;

    public NhanVienDAO() {
        con = ConnectDB.getConnection();
    }

    public boolean themNhanVien(NhanVien nhanVien) {
        String sql = "INSERT INTO NhanVien(maNV, tenNV, chucVu, soDT, tenDangNhap, matKhau) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nhanVien.getMaNV());
            stmt.setString(2, nhanVien.getTenNV());
            stmt.setString(3, nhanVien.getChucVu());
            stmt.setString(4, nhanVien.getSoDT());
            stmt.setString(5, nhanVien.getTenDangNhap());
            stmt.setString(6, nhanVien.getMatKhau());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaNhanVien(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE maNV = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatNhanVien(NhanVien nhanVien) {
        String sql = "UPDATE NhanVien SET tenNV = ?, chucVu = ?, soDT = ?, tenDangNhap = ?, matKhau = ? WHERE maNV = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nhanVien.getTenNV());
            stmt.setString(2, nhanVien.getChucVu());
            stmt.setString(3, nhanVien.getSoDT());
            stmt.setString(4, nhanVien.getTenDangNhap());
            stmt.setString(5, nhanVien.getMatKhau());
            stmt.setString(6, nhanVien.getMaNV());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public NhanVien getNhanVienTheoMa(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNV);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NhanVien nhanVien = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("chucVu"),
                        rs.getString("soDT"),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"));
                return nhanVien;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NhanVien getNhanVienTheoTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM NhanVien WHERE tenDangNhap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, tenDangNhap);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NhanVien nhanVien = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("chucVu"),
                        rs.getString("soDT"),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"));
                return nhanVien;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                NhanVien nhanVien = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("chucVu"),
                        rs.getString("soDT"),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"));
                danhSachNhanVien.add(nhanVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhanVien;
    }

    public List<NhanVien> timKiemNhanVien(String tuKhoa) {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien WHERE maNV LIKE ? OR tenNV LIKE ? OR soDT LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            stmt.setString(3, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                NhanVien nhanVien = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("chucVu"),
                        rs.getString("soDT"),
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"));
                danhSachNhanVien.add(nhanVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhanVien;
    }

    public String taoMaNhanVienMoi() {
        String maNV = "NV";
        String sql = "SELECT TOP 1 maNV FROM NhanVien ORDER BY maNV DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maNV");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maNV += String.format("%03d", so);
            } else {
                maNV += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maNV += "001";
        }
        return maNV;
    }

    public boolean kiemTraDangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM NhanVien WHERE tenDangNhap = ? AND matKhau = ?";
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