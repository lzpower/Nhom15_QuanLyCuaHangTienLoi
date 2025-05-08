package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChucVu;
import entity.NhanVien;

public class NhanVienDAO {
    private Connection con;

    public NhanVienDAO() {
        con = ConnectDB.getConnection();
    }
    
    public NhanVienDAO(Connection conn) {
        this.con = conn;
    }

    public boolean themNhanVien(NhanVien nhanVien) {
        String sql = "INSERT INTO NhanVien(maNhanVien, tenNhanVien, maChucVu, soDienThoai) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nhanVien.getMaNhanVien());
            stmt.setString(2, nhanVien.getTenNhanVien());
            stmt.setString(3, nhanVien.getChucVu().getMaChucVu());
            stmt.setString(4, nhanVien.getSoDienThoai());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean xoaNhanVien(String maNhanVien) { 
//        String sqlDeleteTaiKhoan = "DELETE FROM TaiKhoan WHERE maNhanVien = ?";
//        String sqlDeleteNhanVien = "DELETE FROM NhanVien WHERE maNhanVien = ?";
//
//        try {
//            // Xóa dữ liệu trong bảng TaiKhoan
//            PreparedStatement stmtDeleteTaiKhoan = con.prepareStatement(sqlDeleteTaiKhoan);
//            stmtDeleteTaiKhoan.setString(1, maNhanVien);
//            int n = stmtDeleteTaiKhoan.executeUpdate();
//
//            // Xóa dữ liệu trong bảng NhanVien
//            PreparedStatement stmtDeleteNhanVien = con.prepareStatement(sqlDeleteNhanVien);
//            stmtDeleteNhanVien.setString(1, maNhanVien);
//            int m = stmtDeleteNhanVien.executeUpdate();
//
//            // Kiểm tra xem việc xóa có thành công không
//            return n > 0 && m > 0 ;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean xoaNhanVien(String maNhanVien) {
        String sql = "DELETE FROM NhanVien WHERE maNhanVien = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNhanVien);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatNhanVien(NhanVien nhanVien) {
        String sql = "UPDATE NhanVien SET tenNhanVien = ?, maChucVu = ?, soDienThoai = ? WHERE maNhanVien = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nhanVien.getTenNhanVien());
            stmt.setString(2, nhanVien.getChucVu().getMaChucVu());
            stmt.setString(3, nhanVien.getSoDienThoai());
            stmt.setString(4, nhanVien.getMaNhanVien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public NhanVien getNhanVienTheoMa(String maNhanVien) {
        String sql = "SELECT nv.maNhanVien, nv.tenNhanVien, nv.soDienThoai, cv.maChucVu, cv.tenChucVu " +
                     "FROM NhanVien nv JOIN ChucVu cv ON nv.maChucVu = cv.maChucVu " +
                     "WHERE nv.maNhanVien = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNhanVien);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ChucVu chucVu = new ChucVu(rs.getString("maChucVu"), rs.getString("tenChucVu"));
                NhanVien nhanVien = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        chucVu,
                        rs.getString("soDienThoai"));
                return nhanVien;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        String sql = "SELECT nv.maNhanVien, nv.tenNhanVien, nv.soDienThoai, cv.maChucVu, cv.tenChucVu " +
                     "FROM NhanVien nv JOIN ChucVu cv ON nv.maChucVu = cv.maChucVu";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ChucVu chucVu = new ChucVu(rs.getString("maChucVu"), rs.getString("tenChucVu"));
                NhanVien nhanVien = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        chucVu,
                        rs.getString("soDienThoai"));
                danhSachNhanVien.add(nhanVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhanVien;
    }

    public List<NhanVien> timKiemNhanVien(String tuKhoa) {
        List<NhanVien> danhSachNhanVien = new ArrayList<>();
        String sql = "SELECT nv.maNhanVien, nv.tenNhanVien, nv.soDienThoai, cv.maChucVu, cv.tenChucVu " +
                     "FROM NhanVien nv JOIN ChucVu cv ON nv.maChucVu = cv.maChucVu " +
                     "WHERE nv.maNhanVien LIKE ? OR nv.tenNhanVien LIKE ? OR nv.soDienThoai LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            stmt.setString(3, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChucVu chucVu = new ChucVu(rs.getString("maChucVu"), rs.getString("tenChucVu"));
                NhanVien nhanVien = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        chucVu,
                        rs.getString("soDienThoai"));
                danhSachNhanVien.add(nhanVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhanVien;
    }

    public String taoMaNhanVienMoi() {
        String maNhanVien = "NV";
        String sql = "SELECT TOP 1 maNhanVien FROM NhanVien ORDER BY maNhanVien DESC";
   
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maNhanVien");
                // Extract the numeric part and increment
                if (maCuoi != null && maCuoi.length() > 2) {
                    int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                    maNhanVien += String.format("%03d", so);
                } else {
                    maNhanVien += "001";
                }
            } else {
                maNhanVien += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maNhanVien += "001";
        }
        return maNhanVien;
    }
    public NhanVien getNhanVienById(String maNhanVien) {
        // Phương thức này là một alias cho getNhanVienTheoMa để duy trì tính nhất quán với TaiKhoanDAO
        return getNhanVienTheoMa(maNhanVien);
    }
    // Close resources to prevent memory leaks
    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}