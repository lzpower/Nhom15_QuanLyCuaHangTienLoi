package entity;

public class ChiTietHoaDon {
	private String maHoaDon;
	private String maSanPham;
	private int soLuong;
	private double donGia;

	public ChiTietHoaDon() {
	}

	public ChiTietHoaDon(String maHoaDon, String maSanPham, int soLuong,
			double donGia) {
		this.maHoaDon = maHoaDon;
		this.maSanPham = maSanPham;
		this.soLuong = soLuong;
		this.donGia = donGia;
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

	public String getMaSanPham() {
		return maSanPham;
	}

	public void setMaSanPham(String maSanPham) {
		if (!maSanPham.trim().equalsIgnoreCase(""))
			this.maSanPham = maSanPham;
		else
			throw new IllegalArgumentException("Mã sản phẩm không được rỗng");
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		if (soLuong > 0)
			this.soLuong = soLuong;
		else
			throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		if (donGia > 0)
			this.donGia = donGia;
		else
			throw new IllegalArgumentException("Đơn giá phải lớn hơn 0");
	}

	public double getThanhTien() {
		return soLuong * donGia;
	}
}