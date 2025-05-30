package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.NhanVien;
import entity.TaiKhoan;
import connectDB.ConnectDB;

public class TaiKhoanDAO {
    private Connection con;
    
    public TaiKhoanDAO() {
        try {
            ConnectDB.getInstance();
			con = ConnectDB.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean themTaiKhoan(TaiKhoan taiKhoan) {
        String sql = "INSERT INTO TaiKhoan (tenDangNhap, matKhau, vaiTro, maNhanVien) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, taiKhoan.getTenDangNhap());
            stmt.setString(2, taiKhoan.getMatKhau());
            stmt.setString(3, taiKhoan.getVaiTro());
            stmt.setString(4, taiKhoan.getNhanVien().getMaNhanVien());
            
            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean capNhatTaiKhoan(TaiKhoan taiKhoan) {
        String sql = "UPDATE TaiKhoan SET matKhau = ?, vaiTro = ? WHERE tenDangNhap = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, taiKhoan.getMatKhau());
            stmt.setString(2, taiKhoan.getVaiTro());
            stmt.setString(3, taiKhoan.getTenDangNhap());
            
            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaTaiKhoan(String tenDangNhap) {
        String sql = "DELETE FROM TaiKhoan WHERE tenDangNhap = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tenDangNhap);
            
            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public TaiKhoan getTaiKhoanTheoTenDangNhap(String tenDangNhap) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tenDangNhap);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String matKhau = rs.getString("matKhau");
                    String vaiTro = rs.getString("vaiTro");
                    String maNhanVien = rs.getString("maNhanVien");
                    
                    // Lấy thông tin nhân viên từ mã nhân viên
                    NhanVienDAO nhanVienDAO = new NhanVienDAO();
                    NhanVien nhanVien = nhanVienDAO.getNhanVienById(maNhanVien);
                    
                    return new TaiKhoan(tenDangNhap, matKhau, vaiTro, nhanVien);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public TaiKhoan getTaiKhoanByMaNhanVien(String maNhanVien) {
        String sql = "SELECT * FROM TaiKhoan WHERE maNhanVien = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maNhanVien);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tenDangNhap = rs.getString("tenDangNhap");
                    String matKhau = rs.getString("matKhau");
                    String vaiTro = rs.getString("vaiTro");
                    
                    // Lấy thông tin nhân viên từ mã nhân viên
                    NhanVienDAO nhanVienDAO = new NhanVienDAO();
                    NhanVien nhanVien = nhanVienDAO.getNhanVienById(maNhanVien);
                    
                    return new TaiKhoan(tenDangNhap, matKhau, vaiTro, nhanVien);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<TaiKhoan> getAllTaiKhoan() {
        List<TaiKhoan> danhSachTaiKhoan = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            NhanVienDAO nhanVienDAO = new NhanVienDAO();
            
            while (rs.next()) {
                String tenDangNhap = rs.getString("tenDangNhap");
                String matKhau = rs.getString("matKhau");
                String vaiTro = rs.getString("vaiTro");
                String maNhanVien = rs.getString("maNhanVien");
                
                // Lấy thông tin nhân viên từ mã nhân viên
                NhanVien nhanVien = nhanVienDAO.getNhanVienById(maNhanVien);
                
                TaiKhoan taiKhoan = new TaiKhoan(tenDangNhap, matKhau, vaiTro, nhanVien);
                danhSachTaiKhoan.add(taiKhoan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachTaiKhoan;
    }
    
    public TaiKhoan kiemTraDangNhap(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matKhau);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String vaiTro = rs.getString("vaiTro");
                    String maNhanVien = rs.getString("maNhanVien");
                    
                    // Lấy thông tin nhân viên từ mã nhân viên
                    NhanVienDAO nhanVienDAO = new NhanVienDAO();
                    NhanVien nhanVien = nhanVienDAO.getNhanVienById(maNhanVien);
                    
                    return new TaiKhoan(tenDangNhap, matKhau, vaiTro, nhanVien);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean doiMatKhau(String tenDangNhap, String matKhauCu, String matKhauMoi) {
        // Kiểm tra mật khẩu cũ có đúng không
        TaiKhoan taiKhoan = kiemTraDangNhap(tenDangNhap, matKhauCu);
        if (taiKhoan == null) {
            return false; // Mật khẩu cũ không đúng
        }
        
        // Cập nhật mật khẩu mới
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenDangNhap = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, matKhauMoi);
            stmt.setString(2, tenDangNhap);
            
            int n = stmt.executeUpdate();
            return n > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}