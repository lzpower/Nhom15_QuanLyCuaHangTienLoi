package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HoaDon {
	private String maHoaDon;
	private Date ngayLap;
	private String maNhanVien;
	private List<ChiTietHoaDon> danhSachChiTiet;
	private NhanVien nhanVien;

	public HoaDon() {
		this.danhSachChiTiet = new ArrayList<>();
	}

	public HoaDon(String maHoaDon, Date ngayLap, String maNhanVien) {
		this.maHoaDon = maHoaDon;
		this.ngayLap = ngayLap;
		this.maNhanVien = maNhanVien;
		this.danhSachChiTiet = new ArrayList<>();
	}

	public String getMaHoaDon() {
		return maHoaDon;
	}

	public void setMaHoaDon(String maHoaDon) {
		if (!maHoaDon.trim().equalsIgnoreCase(""))
			this.maHoaDon = maHoaDon;
		else
			throw new IllegalArgumentException("Mã hóa đơn không được rỗng");
	}

	public Date getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(Date ngayLap) {
		this.ngayLap = ngayLap;
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public List<ChiTietHoaDon> getDanhSachChiTiet() {
		return danhSachChiTiet;
	}

	public void setDanhSachChiTiet(List<ChiTietHoaDon> danhSachChiTiet) {
		this.danhSachChiTiet = danhSachChiTiet;
	}

	public void themChiTiet(ChiTietHoaDon chiTiet) {
		this.danhSachChiTiet.add(chiTiet);
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
		if (nhanVien != null) {
			this.maNhanVien = nhanVien.getMaNV();
		}
	}

	public Date getNgayBan() {
		return ngayLap;
	}

	public void setNgayBan(Date ngayBan) {
		this.ngayLap = ngayBan;
	}

	public double getTongTien() {
		double tong = 0;
		for (ChiTietHoaDon ct : danhSachChiTiet) {
			tong += ct.getThanhTien();
		}
		return tong;
	}

}
