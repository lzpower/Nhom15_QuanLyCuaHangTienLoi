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

public class ChucVuDAO {
    private Connection con;
    public ChucVuDAO() {
        con = ConnectDB.getConnection();
    }

    public ChucVuDAO(Connection conn) {
        this.con = conn;
    }

    public boolean themChucVu(ChucVu chucVu) {
        String sql = "INSERT INTO ChucVu(maChucVu, tenChucVu) VALUES(?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, chucVu.getMaChucVu());
            stmt.setString(2, chucVu.getTenChucVu());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaChucVu(String maChucVu) {
        String sql = "DELETE FROM ChucVu WHERE maChucVu = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maChucVu);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatChucVu(ChucVu chucVu) {
        String sql = "UPDATE ChucVu SET tenChucVu = ? WHERE maChucVu = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, chucVu.getTenChucVu());
            stmt.setString(2, chucVu.getMaChucVu());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ChucVu getChucVuTheoMa(String maChucVu) {
        String sql = "SELECT * FROM ChucVu WHERE maChucVu = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maChucVu);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ChucVu chucVu = new ChucVu(
                        rs.getString("maChucVu"),
                        rs.getString("tenChucVu"));
                return chucVu;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ChucVu> getAllChucVu() {
        List<ChucVu> danhSachChucVu = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ChucVu chucVu = new ChucVu(
                        rs.getString("maChucVu"),
                        rs.getString("tenChucVu"));
                danhSachChucVu.add(chucVu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachChucVu;
    }

    public List<ChucVu> timKiemChucVu(String tuKhoa) {
        List<ChucVu> danhSachChucVu = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu WHERE maChucVu LIKE ? OR tenChucVu LIKE ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tuKhoa + "%");
            stmt.setString(2, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChucVu chucVu = new ChucVu(
                        rs.getString("maChucVu"),
                        rs.getString("tenChucVu"));
                danhSachChucVu.add(chucVu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachChucVu;
    }

    public String taoMaChucVuMoi() {
        String maChucVu = "CV";
        String sql = "SELECT TOP 1 maChucVu FROM ChucVu ORDER BY maChucVu DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maChucVu");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maChucVu += String.format("%03d", so);
            } else {
                maChucVu += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maChucVu += "001";
        }
        return maChucVu;
    }
    
    public boolean kiemTraTonTai(String maChucVu) {
        String sql = "SELECT COUNT(*) FROM ChucVu WHERE maChucVu = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maChucVu);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}