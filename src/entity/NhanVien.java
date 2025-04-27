package entity;

import java.util.Objects;

public class NhanVien {
    private String maNhanVien;
    private String tenNhanVien;
    private ChucVu chucVu;
    private String soDienThoai;

    public NhanVien() {
    }

    public NhanVien(String maNhanVien, String tenNhanVien, ChucVu chucVu, String soDienThoai) {
        setMaNhanVien(maNhanVien);
        setTenNhanVien(tenNhanVien);
        setChucVu(chucVu);
        setSoDienThoai(soDienThoai);
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhân viên không được để trống");
        }
        this.maNhanVien = maNhanVien;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }
        this.tenNhanVien = tenNhanVien;
    }

    public ChucVu getChucVu() {
        return chucVu;
    }

    public void setChucVu(ChucVu chucVu) {
        if (chucVu == null) {
            throw new IllegalArgumentException("Chức vụ không được null");
        }
        this.chucVu = chucVu;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        if (!soDienThoai.matches("\\d{10}")) {
            throw new IllegalArgumentException("Số điện thoại phải là 10 chữ số");
        }
        this.soDienThoai = soDienThoai;
    }

    @Override
    public String toString() {
        return "NhanVien [maNhanVien=" + maNhanVien + ", tenNhanVien=" + tenNhanVien 
                + ", chucVu=" + (chucVu != null ? chucVu.getTenChucVu() : "null") 
                + ", soDienThoai=" + soDienThoai + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNhanVien);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        NhanVien other = (NhanVien) obj;
        return Objects.equals(maNhanVien, other.maNhanVien);
    }
    
}