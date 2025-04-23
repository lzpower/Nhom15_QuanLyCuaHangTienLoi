package entity;

import java.util.Objects;

public class NhanVien {
	private String maNV;
	private String tenNV;
	private String chucVu;
	private String soDT;
	private String tenDangNhap;
	private String matKhau;

	public NhanVien(String maNV, String tenNV, String chucVu, String soDT, String tenDangNhap, String matKhau) {
		super();
		setMaNV(maNV);
		setTenNV(tenNV);
		setChucVu(chucVu);
		setSoDT(soDT);
		setTenDangNhap(tenDangNhap);
		setMatKhau(matKhau);
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		if (maNV == null || maNV.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã nhân viên không được để trống");
		}
		this.maNV = maNV;
	}

	public String getTenNV() {
		return tenNV;
	}

	public void setTenNV(String tenNV) {
		if (tenNV == null || tenNV.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên nhân viên không được để trống");
		}
		this.tenNV = tenNV;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		if (chucVu == null || chucVu.trim().isEmpty()) {
			throw new IllegalArgumentException("Chức vụ không được để trống");
		}
		this.chucVu = chucVu;
	}

	public String getSoDT() {
		return soDT;
	}

	public void setSoDT(String soDT) {
		if (soDT == null || soDT.trim().isEmpty()) {
			throw new IllegalArgumentException("Số điện thoại không được để trống");
		}
		this.soDT = soDT;
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}

	public void setTenDangNhap(String tenDangNhap) {
		if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên đăng nhập không được để trống");
		}
		this.tenDangNhap = tenDangNhap;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		if (matKhau == null || matKhau.trim().isEmpty()) {
			throw new IllegalArgumentException("Mật khẩu không được để trống");
		}
		this.matKhau = matKhau;
	}

	@Override
	public String toString() {
		return "NhanVien [maNV=" + maNV + ", tenNV=" + tenNV + ", chucVu=" + chucVu + ", soDT=" + soDT
				+ ", tenDangNhap=" + tenDangNhap + ", matKhau=" + matKhau + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNV);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNV, other.maNV);
	}
}
