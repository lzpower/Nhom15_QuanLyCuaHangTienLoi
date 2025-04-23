package entity;

public class LoaiSanPham {
	private String maLoaiSP;
	private String tenLoaiSP;
	
	public String getMaLoaiSP() {
		return maLoaiSP;
	}
	
	public void setMaLoaiSP(String maLoaiSP) {
		if (maLoaiSP == null || maLoaiSP.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã loại sản phẩm không được để trống");
		} else {
			this.maLoaiSP = maLoaiSP;
		}
	}
	
	public String getTenLoaiSP() {
		return tenLoaiSP;
	}
	
	public void setTenLoaiSP(String tenLoaiSP) {
		if (tenLoaiSP == null || tenLoaiSP.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên loại sản phẩm không được để trống");
		} else {
			this.tenLoaiSP = tenLoaiSP;
		}
	}

	public LoaiSanPham(String maLoaiSP, String tenLoaiSP) {
		setMaLoaiSP(maLoaiSP);
		setTenLoaiSP(tenLoaiSP);
	}
	
	public LoaiSanPham(String maLoaiSP) {
		setMaLoaiSP(maLoaiSP);
	}
}