package dao;

import java.sql.Connection;
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
    
//    public LoaiSanPhamDAO(Connection conn) {
//        this.con = conn;
//    }

    public List<LoaiSanPham> getAllLoaiSanPham() {
        List<LoaiSanPham> danhSachLoaiSP = new ArrayList<>();
        String sql = "SELECT * FROM LoaiSanPham";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                LoaiSanPham loaiSP = new LoaiSanPham(
                        rs.getString("maLoaiSanPham"),
                        rs.getString("tenLoaiSanPham"));
                danhSachLoaiSP.add(loaiSP);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLoaiSP;
    }
}