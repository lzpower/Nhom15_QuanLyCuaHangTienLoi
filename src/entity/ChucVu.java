package entity;

public class ChucVu {
	private String maChucVu;
	private String tenChucVu;
	
	
	public ChucVu() {
	}
	public ChucVu(String maChucVu) {
		this.maChucVu = maChucVu;
	}
	public ChucVu(String maChucVu, String tenChucVu) {
		this.maChucVu = maChucVu;
		this.tenChucVu = tenChucVu;
	}
	
	public String getMaChucVu() {
		return maChucVu;
	}
	public void setMaChucVu(String maChucVu) {
		if (maChucVu == null || maChucVu.trim().isEmpty())
			throw new IllegalArgumentException("Mã chức vụ không được để trống");
		this.maChucVu = maChucVu;
	}
	public String getTenChucVu() {
		return tenChucVu;
	}
	public void setTenChucVu(String tenChucVu) {
		if (tenChucVu == null || tenChucVu.trim().isEmpty())
			throw new IllegalArgumentException("Tên chức vụ không được để trống");
		this.tenChucVu = tenChucVu;
	}
	@Override
	public String toString() {
		return "ChucVu [maChucVu=" + maChucVu + ", tenChucVu=" + tenChucVu
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((maChucVu == null) ? 0 : maChucVu.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChucVu other = (ChucVu) obj;
		if (maChucVu == null) {
			if (other.maChucVu != null)
				return false;
		} else if (!maChucVu.equals(other.maChucVu))
			return false;
		return true;
	}
	
	
	
}
