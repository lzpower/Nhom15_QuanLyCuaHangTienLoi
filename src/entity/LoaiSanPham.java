package entity;

public class LoaiSanPham {
    private String maLoaiSanPham;
    private String tenLoaiSanPham;
    
    public LoaiSanPham(String maLoaiSanPham, String tenLoaiSanPham) {
        setMaLoaiSanPham(maLoaiSanPham);
        setTenLoaiSanPham(tenLoaiSanPham);
    }
    
    public String getMaLoaiSanPham() {
        return maLoaiSanPham;
    }
   
    public void setMaLoaiSanPham(String maLoaiSanPham) {
        if (maLoaiSanPham == null || maLoaiSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã loại sản phẩm không được để trống");
        } else {
            this.maLoaiSanPham = maLoaiSanPham;
        }
    }
    
    public String getTenLoaiSanPham() {
        return tenLoaiSanPham;
    }
    
    public void setTenLoaiSanPham(String tenLoaiSanPham) {
        if (tenLoaiSanPham == null || tenLoaiSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên loại sản phẩm không được để trống");
        } else {
            this.tenLoaiSanPham = tenLoaiSanPham;
        }
    }

	@Override
	public String toString() {
		return "LoaiSanPham [maLoaiSanPham=" + maLoaiSanPham
				+ ", tenLoaiSanPham=" + tenLoaiSanPham + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((maLoaiSanPham == null) ? 0 : maLoaiSanPham.hashCode());
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
		LoaiSanPham other = (LoaiSanPham) obj;
		if (maLoaiSanPham == null) {
			if (other.maLoaiSanPham != null)
				return false;
		} else if (!maLoaiSanPham.equals(other.maLoaiSanPham))
			return false;
		return true;
	}
	
}
