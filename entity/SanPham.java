package entity;

public class SanPham {
	private String maSP;
	private String tenSP;
	private LoaiSanPham loaiSP;
	private int slHienCo;
	private double giaNhap;
	private double giaBan; 
<<<<<<< HEAD
	private String urlHinhAnh;
=======
>>>>>>> 8d30f36358accfa122b287ec8e3a21af0446811d
	
	public String getMaSP() {
		return maSP;
	}
	
	public void setMaSP(String maSP) throws IllegalArgumentException {
		if (maSP == null || maSP.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã sản phẩm không được để trống");
		} else {
			this.maSP = maSP;
		}
	}
	
	public String getTenSP() {
		return tenSP;
	}
	
	public void setTenSP(String tenSP) throws IllegalArgumentException {
		if (tenSP == null || tenSP.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên sản phẩm không được để trống");
		} else {
			this.tenSP = tenSP;
		}
	}
	
	public LoaiSanPham getLoaiSP() {
		return loaiSP;
	}
	
	public void setLoaiSP(LoaiSanPham loaiSP) throws IllegalArgumentException{
		if (loaiSP == null) {
			throw new IllegalArgumentException("Loại sản phẩm không được để trống");
		} else {
			this.loaiSP = loaiSP;
		}
	}
	
	public int getSlHienCo() {
		return slHienCo;
	}
	
	public void setSlHienCo(int slHienCo) throws IllegalArgumentException {
		if (slHienCo < 0) {
			throw new IllegalArgumentException("Số lượng hiện có không được âm");
		} else {
			this.slHienCo = slHienCo;
		}
	}
	
	
	public double getGiaNhap() {
		return giaNhap;
	}
	
	public void setGiaNhap(double giaNhap) throws IllegalArgumentException {
		if (giaNhap < 0) {
			throw new IllegalArgumentException("Giá nhập không được âm");
		} else {
			this.giaNhap = giaNhap;
			this.giaBan = giaNhap * 1.5; // cập nhật luôn giá bán mỗi khi giá nhập thay đổi
		}
	}
	
	public double getGiaBan() {
		return giaBan;
	}
	
<<<<<<< HEAD
	public String getUrlHinhAnh() {
	    return urlHinhAnh;
	}

	public void setUrlHinhAnh(String urlHinhAnh) {
	    this.urlHinhAnh = urlHinhAnh;
	}

	public SanPham(String maSP, String tenSP, LoaiSanPham loaiSP, int slHienCo, double giaNhap, double giaBan,
			String urlHinhAnh) {
=======

	public SanPham(String maSP, String tenSP, LoaiSanPham loaiSP, int slHienCo, double giaNhap) {
>>>>>>> 8d30f36358accfa122b287ec8e3a21af0446811d
		setMaSP(maSP);
		setTenSP(tenSP);
		setLoaiSP(loaiSP);
		setSlHienCo(slHienCo);
<<<<<<< HEAD
		setGiaNhap(giaNhap);
		this.giaBan = giaBan;
		setUrlHinhAnh(urlHinhAnh);
	}

	public SanPham(String maSP, String tenSP, LoaiSanPham loaiSP, int slHienCo, double giaNhap, String urlHinhAnh) {
		setMaSP(maSP);
		setTenSP(tenSP);
		setLoaiSP(loaiSP);
		setSlHienCo(slHienCo);
		setGiaNhap(giaNhap);
		setUrlHinhAnh(urlHinhAnh);
	}
	

	
=======
		setGiaNhap(giaNhap); 
	}
	public SanPham(String maSP, String tenSP,double giaNhap) {
		setMaSP(maSP);
		setTenSP(tenSP);
		setGiaNhap(giaNhap); 
	}
>>>>>>> 8d30f36358accfa122b287ec8e3a21af0446811d


	
}
