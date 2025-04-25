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

public class LoaiSanPhamDAO {
    private Connection con;

    public LoaiSanPhamDAO() {
        con = ConnectDB.getConnection();
    }
    public LoaiSanPhamDAO(Connection conn) {
        this.con = conn;
    }
    public boolean themLoaiSanPham(LoaiSanPham loaiSP) {
        String sql = "INSERT INTO LoaiSanPham(maLoaiSP, tenLoaiSP) VALUES(?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, loaiSP.getMaLoaiSP());
            stmt.setString(2, loaiSP.getTenLoaiSP());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaLoaiSanPham(String maLoaiSP) {
        String sql = "DELETE FROM LoaiSanPham WHERE maLoaiSP = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maLoaiSP);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatLoaiSanPham(LoaiSanPham loaiSP) {
        String sql = "UPDATE LoaiSanPham SET tenLoaiSP = ? WHERE maLoaiSP = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, loaiSP.getTenLoaiSP());
            stmt.setString(2, loaiSP.getMaLoaiSP());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public LoaiSanPham getLoaiSanPhamTheoMa(String maLoaiSP) {
        String sql = "SELECT * FROM LoaiSanPham WHERE maLoaiSP = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maLoaiSP);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LoaiSanPham loaiSP = new LoaiSanPham(
                        rs.getString("maLoaiSP"),
                        rs.getString("tenLoaiSP"));
                return loaiSP;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LoaiSanPham> getAllLoaiSanPham() {
        List<LoaiSanPham> danhSachLoaiSP = new ArrayList<>();
        String sql = "SELECT * FROM LoaiSanPham";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                LoaiSanPham loaiSP = new LoaiSanPham(
                        rs.getString("maLoaiSP"),
                        rs.getString("tenLoaiSP"));
                danhSachLoaiSP.add(loaiSP);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLoaiSP;
    }

    public String taoMaLoaiSanPhamMoi() {
        String maLoaiSP = "LSP";
        String sql = "SELECT TOP 1 maLoaiSP FROM LoaiSanPham ORDER BY maLoaiSP DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maLoaiSP");
                int so = Integer.parseInt(maCuoi.substring(3)) + 1;
                maLoaiSP += String.format("%03d", so);
            } else {
                maLoaiSP += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maLoaiSP += "001";
        }
        return maLoaiSP;
    }
}