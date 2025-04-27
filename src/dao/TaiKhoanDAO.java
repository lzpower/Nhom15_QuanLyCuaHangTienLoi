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
import entity.TaiKhoan;

public class TaiKhoanDAO {
    private Connection con;
    private NhanVienDAO nhanVienDAO;

    public TaiKhoanDAO() {
        con = ConnectDB.getConnection();
        nhanVienDAO = new NhanVienDAO();
    }
    
    public TaiKhoanDAO(Connection conn) {
        this.con = conn;
        nhanVienDAO = new NhanVienDAO(conn);
    }

    public boolean themTaiKhoan(TaiKhoan taiKhoan) {
        String sql = "INSERT INTO TaiKhoan(tenDangNhap, matKhau, vaiTro, maNhanVien) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, taiKhoan.getTenDangNhap());
            stmt.setString(2, taiKhoan.getMatKhau());
            stmt.setString(3, taiKhoan.getVaiTro());
            
            if (taiKhoan.getNhanVien() != null) {
                stmt.setString(4, taiKhoan.getNhanVien().getMaNhanVien());
            } else {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            }
            
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
            
            if (taiKhoan.getNhanVien() != null) {
                stmt.setString(3, taiKhoan.getNhanVien().getMaNhanVien());
            } else {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            }
            
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
                NhanVien nhanVien = null;
                if (rs.getString("maNhanVien") != null) {
                    nhanVien = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNhanVien"));
                }
                
                TaiKhoan taiKhoan = new TaiKhoan(
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"),
                        rs.getString("vaiTro"),
                        nhanVien);
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
                NhanVien nhanVien = null;
                if (rs.getString("maNhanVien") != null) {
                    nhanVien = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNhanVien"));
                }
                
                TaiKhoan taiKhoan = new TaiKhoan(
                        rs.getString("tenDangNhap"),
                        rs.getString("matKhau"),
                        rs.getString("vaiTro"),
                        nhanVien);
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