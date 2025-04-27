package entity;

import java.util.Objects;

public class NhaCungCap {
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String diaChi;
    private String soDienThoai;
    private String email;
    
    public NhaCungCap() {
    }
    
    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String diaChi, String soDienThoai, String email) {
        setMaNhaCungCap(maNhaCungCap);
        setTenNhaCungCap(tenNhaCungCap);
        setDiaChi(diaChi);
        setSoDienThoai(soDienThoai);
        setEmail(email);
    }
    
    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }
    
    public void setMaNhaCungCap(String maNhaCungCap) {
        if (maNhaCungCap == null || maNhaCungCap.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nhà cung cấp không được để trống");
        }
        this.maNhaCungCap = maNhaCungCap;
    }
    
    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }
    
    public void setTenNhaCungCap(String tenNhaCungCap) {
        if (tenNhaCungCap == null || tenNhaCungCap.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được để trống");
        }
        this.tenNhaCungCap = tenNhaCungCap;
    }
    
    public String getDiaChi() {
        return diaChi;
    }
    
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "NhaCungCap [maNhaCungCap=" + maNhaCungCap + ", tenNhaCungCap=" + tenNhaCungCap 
                + ", diaChi=" + diaChi + ", soDienThoai=" + soDienThoai + ", email=" + email + "]";
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maNhaCungCap);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        NhaCungCap other = (NhaCungCap) obj;
        return Objects.equals(maNhaCungCap, other.maNhaCungCap);
    }
}