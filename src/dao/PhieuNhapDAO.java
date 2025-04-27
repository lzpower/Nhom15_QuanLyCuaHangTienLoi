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
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhap;

public class PhieuNhapDAO {
    private Connection con;
    private NhanVienDAO nhanVienDAO;
    private NhaCungCapDAO nhaCungCapDAO;

    public PhieuNhapDAO() {
        con = ConnectDB.getConnection();
        nhanVienDAO = new NhanVienDAO();
        nhaCungCapDAO = new NhaCungCapDAO();
    }
    
    public PhieuNhapDAO(Connection conn) {
        this.con = conn;
        nhanVienDAO = new NhanVienDAO(conn);
        nhaCungCapDAO = new NhaCungCapDAO(conn);
    }

    public boolean themPhieuNhap(PhieuNhap phieuNhap) {
        String sql = "INSERT INTO PhieuNhap(maPhieuNhap, ngayNhap, maNhanVien, maNhaCungCap) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, phieuNhap.getMaPhieuNhap());
            stmt.setDate(2, new java.sql.Date(phieuNhap.getNgayNhap().getTime()));
            stmt.setString(3, phieuNhap.getNhanVien().getMaNhanVien());
            stmt.setString(4, phieuNhap.getNhaCungCap().getMaNhaCungCap());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaPhieuNhap(String maPhieuNhap) {
        // Xóa chi tiết phiếu nhập trước
        String sqlChiTiet = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap = ?";
        try {
            PreparedStatement stmtChiTiet = con.prepareStatement(sqlChiTiet);
            stmtChiTiet.setString(1, maPhieuNhap);
            stmtChiTiet.executeUpdate();
            
            // Sau đó xóa phiếu nhập
            String sqlPhieuNhap = "DELETE FROM PhieuNhap WHERE maPhieuNhap = ?";
            PreparedStatement stmtPhieuNhap = con.prepareStatement(sqlPhieuNhap);
            stmtPhieuNhap.setString(1, maPhieuNhap);
            return stmtPhieuNhap.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatPhieuNhap(PhieuNhap phieuNhap) {
        String sql = "UPDATE PhieuNhap SET ngayNhap = ?, maNhanVien = ?, maNhaCungCap = ? WHERE maPhieuNhap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(phieuNhap.getNgayNhap().getTime()));
            stmt.setString(2, phieuNhap.getNhanVien().getMaNhanVien());
            stmt.setString(3, phieuNhap.getNhaCungCap().getMaNhaCungCap());
            stmt.setString(4, phieuNhap.getMaPhieuNhap());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PhieuNhap getPhieuNhapTheoMa(String maPhieuNhap) {
        String sql = "SELECT * FROM PhieuNhap WHERE maPhieuNhap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhieuNhap);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNhanVien"));
                NhaCungCap nhaCungCap = nhaCungCapDAO.getNhaCungCapTheoMa(rs.getString("maNhaCungCap"));
                
                PhieuNhap phieuNhap = new PhieuNhap(
                        rs.getString("maPhieuNhap"),
                        rs.getDate("ngayNhap"),
                        nhanVien,
                        nhaCungCap);
                return phieuNhap;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PhieuNhap> getAllPhieuNhap() {
        List<PhieuNhap> danhSachPhieuNhap = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNhanVien"));
                NhaCungCap nhaCungCap = nhaCungCapDAO.getNhaCungCapTheoMa(rs.getString("maNhaCungCap"));
                
                PhieuNhap phieuNhap = new PhieuNhap(
                        rs.getString("maPhieuNhap"),
                        rs.getDate("ngayNhap"),
                        nhanVien,
                        nhaCungCap);
                danhSachPhieuNhap.add(phieuNhap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachPhieuNhap;
    }

    public List<PhieuNhap> getPhieuNhapTheoNgay(Date tuNgay, Date denNgay) {
        List<PhieuNhap> danhSachPhieuNhap = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap WHERE ngayNhap BETWEEN ? AND ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(tuNgay.getTime()));
            stmt.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNhanVien"));
                NhaCungCap nhaCungCap = nhaCungCapDAO.getNhaCungCapTheoMa(rs.getString("maNhaCungCap"));
                
                PhieuNhap phieuNhap = new PhieuNhap(
                        rs.getString("maPhieuNhap"),
                        rs.getDate("ngayNhap"),
                        nhanVien,
                        nhaCungCap);
                danhSachPhieuNhap.add(phieuNhap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachPhieuNhap;
    }

    public String taoMaPhieuNhapMoi() {
        String maPhieuNhap = "PN";
        String sql = "SELECT TOP 1 maPhieuNhap FROM PhieuNhap ORDER BY maPhieuNhap DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maPhieuNhap");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maPhieuNhap += String.format("%03d", so);
            } else {
                maPhieuNhap += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maPhieuNhap += "001";
        }
        return maPhieuNhap;
    }
}