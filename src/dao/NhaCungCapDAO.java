package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.NhaCungCap;

public class NhaCungCapDAO {
    private Connection con;

    public NhaCungCapDAO() {
        con = ConnectDB.getConnection();
    }
    
    public NhaCungCapDAO(Connection conn) {
        this.con = conn;
    }

    public boolean themNhaCungCap(NhaCungCap nhaCungCap) {
        String sql = "INSERT INTO NhaCungCap(maNhaCungCap, tenNhaCungCap, diaChi, soDienThoai, email) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nhaCungCap.getMaNhaCungCap());
            stmt.setString(2, nhaCungCap.getTenNhaCungCap());
            stmt.setString(3, nhaCungCap.getDiaChi());
            stmt.setString(4, nhaCungCap.getSoDienThoai());
            stmt.setString(5, nhaCungCap.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaNhaCungCap(String maNhaCungCap) {
        String sql = "DELETE FROM NhaCungCap WHERE maNhaCungCap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNhaCungCap);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatNhaCungCap(NhaCungCap nhaCungCap) {
        String sql = "UPDATE NhaCungCap SET tenNhaCungCap = ?, diaChi = ?, soDienThoai = ?, email = ? WHERE maNhaCungCap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nhaCungCap.getTenNhaCungCap());
            stmt.setString(2, nhaCungCap.getDiaChi());
            stmt.setString(3, nhaCungCap.getSoDienThoai());
            stmt.setString(4, nhaCungCap.getEmail());
            stmt.setString(5, nhaCungCap.getMaNhaCungCap());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public NhaCungCap getNhaCungCapTheoMa(String maNhaCungCap) {
        String sql = "SELECT * FROM NhaCungCap WHERE maNhaCungCap = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maNhaCungCap);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NhaCungCap nhaCungCap = new NhaCungCap(
                        rs.getString("maNhaCungCap"),
                        rs.getString("tenNhaCungCap"),
                        rs.getString("diaChi"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"));
                return nhaCungCap;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<NhaCungCap> getAllNhaCungCap() {
        List<NhaCungCap> danhSachNhaCungCap = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                NhaCungCap nhaCungCap = new NhaCungCap(
                        rs.getString("maNhaCungCap"),
                        rs.getString("tenNhaCungCap"),
                        rs.getString("diaChi"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"));
                danhSachNhaCungCap.add(nhaCungCap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhaCungCap;
    }

    public List<NhaCungCap> timKiemNhaCungCap(String tuKhoa) {
        List<NhaCungCap> danhSachNhaCungCap = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE maNhaCungCap LIKE ? OR tenNhaCungCap LIKE ? OR soDienThoai LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            stmt.setString(3, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                NhaCungCap nhaCungCap = new NhaCungCap(
                        rs.getString("maNhaCungCap"),
                        rs.getString("tenNhaCungCap"),
                        rs.getString("diaChi"),
                        rs.getString("soDienThoai"),
                        rs.getString("email"));
                danhSachNhaCungCap.add(nhaCungCap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNhaCungCap;
    }

    public String taoMaNhaCungCapMoi() {
        String maNhaCungCap = "NCC";
        String sql = "SELECT TOP 1 maNhaCungCap FROM NhaCungCap ORDER BY maNhaCungCap DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maNhaCungCap");
                int so = Integer.parseInt(maCuoi.substring(3)) + 1;
                maNhaCungCap += String.format("%03d", so);
            } else {
                maNhaCungCap += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maNhaCungCap += "001";
        }
        return maNhaCungCap;
    }
}