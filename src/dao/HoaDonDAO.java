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
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;

public class HoaDonDAO {
    private Connection con;
    private NhanVienDAO nhanVienDAO;
    private KhachHangDAO khachHangDAO;
    private KhuyenMaiDAO khuyenMaiDAO;

    public HoaDonDAO() {
        con = ConnectDB.getConnection();
        nhanVienDAO = new NhanVienDAO();
        khachHangDAO = new KhachHangDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
    }
    public boolean themHoaDon(HoaDon hoaDon) {
        String sql = "INSERT INTO HoaDon(maHoaDon, ngayLap, maNhanVien, maKhachHang, maKhuyenMai, tongTien) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, hoaDon.getMaHoaDon());
            stmt.setDate(2, new java.sql.Date(hoaDon.getNgayLap().getTime()));
            stmt.setString(3, hoaDon.getNhanVien().getMaNhanVien());
            
            if (hoaDon.getKhachHang() != null) {
                stmt.setString(4, hoaDon.getKhachHang().getMaKhachHang());
            } else {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            }
            
            if (hoaDon.getKhuyenMai() != null) {
                stmt.setString(5, hoaDon.getKhuyenMai().getMaKhuyenMai());
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }
            
            stmt.setDouble(6, hoaDon.getTongTien());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaHoaDon(String maHoaDon) {
        // Xóa chi tiết hóa đơn trước
        String sqlChiTiet = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
        try {
            PreparedStatement stmtChiTiet = con.prepareStatement(sqlChiTiet);
            stmtChiTiet.setString(1, maHoaDon);
            stmtChiTiet.executeUpdate();
            
            // Sau đó xóa hóa đơn
            String sqlHoaDon = "DELETE FROM HoaDon WHERE maHoaDon = ?";
            PreparedStatement stmtHoaDon = con.prepareStatement(sqlHoaDon);
            stmtHoaDon.setString(1, maHoaDon);
            return stmtHoaDon.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatHoaDon(HoaDon hoaDon) {
        String sql = "UPDATE HoaDon SET ngayLap = ?, maNhanVien = ?, maKhachHang = ?, maKhuyenMai = ?, tongTien = ? WHERE maHoaDon = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(hoaDon.getNgayLap().getTime()));
            stmt.setString(2, hoaDon.getNhanVien().getMaNhanVien());
            
            if (hoaDon.getKhachHang() != null) {
                stmt.setString(3, hoaDon.getKhachHang().getMaKhachHang());
            } else {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            }
            
            if (hoaDon.getKhuyenMai() != null) {
                stmt.setString(4, hoaDon.getKhuyenMai().getMaKhuyenMai());
            } else {
                stmt.setNull(4, java.sql.Types.VARCHAR);
            }
            
            stmt.setDouble(5, hoaDon.getTongTien());
            stmt.setString(6, hoaDon.getMaHoaDon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HoaDon getHoaDonTheoMa(String maHoaDon) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNhanVien"));
                KhachHang khachHang = null;
                if (rs.getString("maKhachHang") != null) {
                    khachHang = khachHangDAO.getKhachHangTheoMa(rs.getString("maKhachHang"));
                }
                
                KhuyenMai khuyenMai = null;
                if (rs.getString("maKhuyenMai") != null) {
                    khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(rs.getString("maKhuyenMai"));
                }
                
                HoaDon hoaDon = new HoaDon(
                        rs.getString("maHoaDon"),
                        rs.getDate("ngayLap"),
                        nhanVien,
                        khachHang,
                        khuyenMai);
                return hoaDon;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNhanVien"));
                KhachHang khachHang = null;
                if (rs.getString("maKhachHang") != null) {
                    khachHang = khachHangDAO.getKhachHangTheoMa(rs.getString("maKhachHang"));
                }
                
                KhuyenMai khuyenMai = null;
                if (rs.getString("maKhuyenMai") != null) {
                    khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(rs.getString("maKhuyenMai"));
                }
                
                HoaDon hoaDon = new HoaDon(
                        rs.getString("maHoaDon"),
                        rs.getDate("ngayLap"),
                        nhanVien,
                        khachHang,
                        khuyenMai);
                danhSachHoaDon.add(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachHoaDon;
    }

    public List<HoaDon> getHoaDonTheoNgay(Date tuNgay, Date denNgay) {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE ngayLap BETWEEN ? AND ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDate(1, new java.sql.Date(tuNgay.getTime()));
            stmt.setDate(2, new java.sql.Date(denNgay.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(rs.getString("maNhanVien"));
                KhachHang khachHang = null;
                if (rs.getString("maKhachHang") != null) {
                    khachHang = khachHangDAO.getKhachHangTheoMa(rs.getString("maKhachHang"));
                }
                
                KhuyenMai khuyenMai = null;
                if (rs.getString("maKhuyenMai") != null) {
                    khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(rs.getString("maKhuyenMai"));
                }
                
                HoaDon hoaDon = new HoaDon(
                        rs.getString("maHoaDon"),
                        rs.getDate("ngayLap"),
                        nhanVien,
                        khachHang,
                        khuyenMai);
                danhSachHoaDon.add(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachHoaDon;
    }

    public ResultSet getHoaDonByDateRange(String fromDate, String toDate) throws SQLException {
        String query = "SELECT h.maHoaDon, h.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham, cthd.soLuong, (cthd.soLuong * cthd.donGia) AS doanhThu " +
                "FROM HoaDon h " +
                "JOIN ChiTietHoaDon cthd ON h.maHoaDon = cthd.maHoaDon " +
                "JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
                "JOIN LoaiSanPham lsp ON sp.maLoaiSanPham = lsp.maLoaiSanPham " +
                "WHERE h.ngayLap BETWEEN ? AND ?";
        
        PreparedStatement ps = con.prepareStatement(query);
        ps.setDate(1, java.sql.Date.valueOf(fromDate));
        ps.setDate(2, java.sql.Date.valueOf(toDate));
        return ps.executeQuery();
    }

    public String taoMaHoaDonMoi() {
        String maHoaDon = "HD";
        String sql = "SELECT TOP 1 maHoaDon FROM HoaDon ORDER BY maHoaDon DESC";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maCuoi = rs.getString("maHoaDon");
                int so = Integer.parseInt(maCuoi.substring(2)) + 1;
                maHoaDon += String.format("%03d", so);
            } else {
                maHoaDon += "001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            maHoaDon += "001";
        }
        return maHoaDon;
    }
}