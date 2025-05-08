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
		ConnectDB.getInstance();
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
			stmt.setTimestamp(2, new java.sql.Timestamp(hoaDon.getNgayLap().getTime()));
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
		String sqlChiTiet = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
		try {
			PreparedStatement stmtChiTiet = con.prepareStatement(sqlChiTiet);
			stmtChiTiet.setString(1, maHoaDon);
			stmtChiTiet.executeUpdate();

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
		String sql = "UPDATE HoaDon SET maNhanVien = ?, maKhachHang = ?, maKhuyenMai = ?, tongTien = ? WHERE maHoaDon = ?";
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, hoaDon.getNhanVien().getMaNhanVien());

			if (hoaDon.getKhachHang() != null) {
				stmt.setString(2, hoaDon.getKhachHang().getMaKhachHang());
			} else {
				stmt.setNull(2, java.sql.Types.VARCHAR);
			}

			if (hoaDon.getKhuyenMai() != null) {
				stmt.setString(3, hoaDon.getKhuyenMai().getMaKhuyenMai());
			} else {
				stmt.setNull(3, java.sql.Types.VARCHAR);
			}

			stmt.setDouble(4, hoaDon.getTongTien());
			stmt.setString(5, hoaDon.getMaHoaDon());
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

				HoaDon hoaDon = new HoaDon(rs.getString("maHoaDon"), rs.getTimestamp("ngayLap"), nhanVien, khachHang,
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

				HoaDon hoaDon = new HoaDon(rs.getString("maHoaDon"), rs.getTimestamp("ngayLap"), nhanVien, khachHang,
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

				HoaDon hoaDon = new HoaDon(rs.getString("maHoaDon"), rs.getTimestamp("ngayLap"), nhanVien, khachHang,
						khuyenMai);
				danhSachHoaDon.add(hoaDon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return danhSachHoaDon;
	}

	public String taoMaHoaDonMoi() {
		String maHoaDon = "HD";
		String sql = "select top 1 maHoaDon from HoaDon order by maHoaDon desc";
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

	// Lấy danh sách loại sản phẩm
	public List<String> getDanhSachLoaiSanPham() throws SQLException {
		List<String> danhSachLoai = new ArrayList<>();
		String sql = "SELECT tenLoaiSanPham FROM LoaiSanPham";
		try (PreparedStatement stmt = con.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				danhSachLoai.add(rs.getString("tenLoaiSanPham"));
			}
		}
		return danhSachLoai;
	}

	// Thống kê doanh thu
	public List<Object[]> thongKeDoanhThu(String tuNgay, String denNgay, String loaiSanPham, String sapXepDoanhThu) throws SQLException {
		List<Object[]> ketQua = new ArrayList<>();
		StringBuilder sqlBuilder = new StringBuilder(
			"SELECT hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham, " +
			"SUM(cthd.soLuong) AS soLuong, " +
			"SUM(cthd.soLuong * cthd.donGia) AS doanhThu " +
			"FROM HoaDon hd " +
			"JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon " +
			"JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
			"JOIN LoaiSanPham lsp ON sp.maLoaiSanPham = lsp.maLoaiSanPham "
		);

		List<String> conditions = new ArrayList<>();
		if (tuNgay != null && !tuNgay.isEmpty() && denNgay != null && !denNgay.isEmpty()) {
			conditions.add("CONVERT(date, hd.ngayLap) BETWEEN CONVERT(date, ?) AND CONVERT(date, ?)");
		}

		if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
			conditions.add("lsp.tenLoaiSanPham = ?");
		}

		if (!conditions.isEmpty()) {
			sqlBuilder.append("WHERE ").append(String.join(" AND ", conditions));
		}

		sqlBuilder.append(" GROUP BY hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham");

		if (sapXepDoanhThu.equals("Không sắp xếp")) {
			sqlBuilder.append(" ORDER BY hd.ngayLap DESC");
		} else {
			sqlBuilder.append(" ORDER BY doanhThu ").append(sapXepDoanhThu.equals("Tăng dần") ? "ASC" : "DESC");
		}

		try (PreparedStatement stmt = con.prepareStatement(sqlBuilder.toString())) {
			int paramIndex = 1;
			if (tuNgay != null && !tuNgay.isEmpty() && denNgay != null && !denNgay.isEmpty()) {
				stmt.setString(paramIndex++, tuNgay);
				stmt.setString(paramIndex++, denNgay);
			}

			if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
				stmt.setString(paramIndex++, loaiSanPham);
			}

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Object[] row = new Object[5];
					row[0] = rs.getString("ngayLap");
					row[1] = rs.getString("tenSanPham");
					row[2] = rs.getString("tenLoaiSanPham");
					row[3] = rs.getInt("soLuong");
					row[4] = rs.getDouble("doanhThu");
					ketQua.add(row);
				}
			}
		}
		return ketQua;
	}

	// Lấy danh sách sản phẩm bán chạy
	public List<Object[]> getSanPhamBanChay(String thoiGian, String loaiSanPham) throws SQLException {
		List<Object[]> ketQua = new ArrayList<>();
		StringBuilder sql = new StringBuilder(
			"SELECT sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham, " +
			"SUM(cthd.soLuong) AS tongSoLuong, " +
			"SUM(cthd.soLuong * cthd.donGia) AS tongDoanhThu " +
			"FROM HoaDon hd " +
			"JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon " +
			"JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
			"JOIN LoaiSanPham lsp ON sp.maLoaiSanPham = lsp.maLoaiSanPham "
		);

		List<String> conditions = new ArrayList<>();
		if (thoiGian.equals("day")) {
			conditions.add("CONVERT(date, hd.ngayLap) = CONVERT(date, GETDATE())");
		} else if (thoiGian.equals("month")) {
			conditions.add("MONTH(hd.ngayLap) = MONTH(GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE())");
		}

		if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
			conditions.add("lsp.tenLoaiSanPham = ?");
		}

		if (!conditions.isEmpty()) {
			sql.append("WHERE ").append(String.join(" AND ", conditions));
		}

		sql.append(" GROUP BY sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham " +
				  " ORDER BY tongSoLuong DESC");

		try (PreparedStatement stmt = con.prepareStatement(sql.toString())) {
			if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
				stmt.setString(1, loaiSanPham);
			}

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Object[] row = new Object[5];
					row[0] = rs.getString("maSanPham");
					row[1] = rs.getString("tenSanPham");
					row[2] = rs.getString("tenLoaiSanPham");
					row[3] = rs.getInt("tongSoLuong");
					row[4] = rs.getDouble("tongDoanhThu");
					ketQua.add(row);
				}
			}
		}
		return ketQua;
	}

	// Lấy danh sách sản phẩm khó bán
	public List<Object[]> getSanPhamKhoBan(String thoiGian, String loaiSanPham) throws SQLException {
		List<Object[]> ketQua = new ArrayList<>();
		StringBuilder sql = new StringBuilder(
			"SELECT sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham, " +
			"ISNULL(SUM(cthd.soLuong), 0) AS tongSoLuong, " +
			"ISNULL(SUM(cthd.soLuong * cthd.donGia), 0) AS tongDoanhThu " +
			"FROM SanPham sp " +
			"JOIN LoaiSanPham lsp ON sp.maLoaiSanPham = lsp.maLoaiSanPham " +
			"LEFT JOIN ChiTietHoaDon cthd ON sp.maSanPham = cthd.maSanPham " +
			"LEFT JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon "
		);

		List<String> conditions = new ArrayList<>();
		if (thoiGian.equals("day")) {
			conditions.add("(CONVERT(date, hd.ngayLap) = CONVERT(date, GETDATE()) OR hd.ngayLap IS NULL)");
		} else if (thoiGian.equals("month")) {
			conditions.add("(MONTH(hd.ngayLap) = MONTH(GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE()) OR hd.ngayLap IS NULL)");
		}

		if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
			conditions.add("lsp.tenLoaiSanPham = ?");
		}

		if (!conditions.isEmpty()) {
			sql.append("WHERE ").append(String.join(" AND ", conditions));
		}

		sql.append(" GROUP BY sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham " +
				  " ORDER BY tongSoLuong ASC");

		try (PreparedStatement stmt = con.prepareStatement(sql.toString())) {
			if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
				stmt.setString(1, loaiSanPham);
			}

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Object[] row = new Object[5];
					row[0] = rs.getString("maSanPham");
					row[1] = rs.getString("tenSanPham");
					row[2] = rs.getString("tenLoaiSanPham");
					row[3] = rs.getInt("tongSoLuong");
					row[4] = rs.getDouble("tongDoanhThu");
					ketQua.add(row);
				}
			}
		}
		return ketQua;
	}
}