package entity;

import java.util.Date;

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private double giaTriKhuyenMai;
    private Date ngayBatDau;
    private Date ngayKetThuc;

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, double giaTriKhuyenMai) {
        setMaKhuyenMai(maKhuyenMai);
        setTenKhuyenMai(tenKhuyenMai);
        setGiaTriKhuyenMai(giaTriKhuyenMai);
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khuyến mãi không được để trống.");
        }
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        if (tenKhuyenMai == null || tenKhuyenMai.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống.");
        }
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public double getGiaTriKhuyenMai() {
        return giaTriKhuyenMai;
    }

    public void setGiaTriKhuyenMai(double giaTriKhuyenMai) {
        if (giaTriKhuyenMai <= 0 || giaTriKhuyenMai > 100) {
            throw new IllegalArgumentException("Giá trị khuyến mãi phải lớn hơn 0 và nhỏ hơn hoặc bằng 100.");
        }
        this.giaTriKhuyenMai = giaTriKhuyenMai;
    }

    
    public Date getNgayBatDau() {
		return ngayBatDau;
	}

	public void setNgayBatDau(Date ngayBatDau) {
		this.ngayBatDau = ngayBatDau;
	}

	public Date getNgayKetThuc() {
		return ngayKetThuc;
	}

	public void setNgayKetThuc(Date ngayKetThuc) {
		this.ngayKetThuc = ngayKetThuc;
	}

	@Override
    public String toString() {
        return tenKhuyenMai + " (" + maKhuyenMai + ")";
    }
}
