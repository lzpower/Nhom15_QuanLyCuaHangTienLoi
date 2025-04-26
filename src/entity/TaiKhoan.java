package entity;

import java.util.Objects;

public class TaiKhoan {
	private String tenDangNhap;
	private String matKhau;
	private String vaiTro;
	private String maNhanVien;

	public TaiKhoan(String tenDangNhap, String matKhau, String vaiTro, String maNhanVien) {
		super();
		setTenDangNhap(tenDangNhap);
		setMatKhau(matKhau);
		setVaiTro(vaiTro);
		setMaNhanVien(maNhanVien);
	}

	public TaiKhoan(String tenDangNhap, String matKhau) {
		super();
		setTenDangNhap(tenDangNhap);
		setMatKhau(matKhau);
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}

	public void setTenDangNhap(String tenDangNhap) throws IllegalArgumentException {
		if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên đăng nhập không được để trống");
		}
		this.tenDangNhap = tenDangNhap;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) throws IllegalArgumentException {
		if (matKhau == null || matKhau.trim().isEmpty()) {
			throw new IllegalArgumentException("Mật khẩu không được để trống");
		}
		this.matKhau = matKhau;
	}

	public String getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(String vaiTro) throws IllegalArgumentException {
		if (vaiTro == null || vaiTro.trim().isEmpty()) {
			throw new IllegalArgumentException("Vai trò không được để trống");
		}
		this.vaiTro = vaiTro;
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) throws IllegalArgumentException {
		if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã nhân viên không được để trống");
		}
		this.maNhanVien = maNhanVien;
	}

	@Override
	public String toString() {
		return "TaiKhoan [tenDangNhap=" + tenDangNhap + ", matKhau=" + matKhau + ", vaiTro=" + vaiTro + ", maNhanVien="
				+ maNhanVien + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNhanVien);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaiKhoan other = (TaiKhoan) obj;
		return Objects.equals(maNhanVien, other.maNhanVien);
	}
}
