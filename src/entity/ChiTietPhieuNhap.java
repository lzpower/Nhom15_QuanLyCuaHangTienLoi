package entity;

import java.util.Objects;

public class ChiTietPhieuNhap {
    private PhieuNhap phieuNhap;
    private SanPham sanPham;
    private int soLuong;
    private double donGia;
    
    public ChiTietPhieuNhap() {
    }
    
    public ChiTietPhieuNhap(PhieuNhap phieuNhap, SanPham sanPham, int soLuong, double donGia) {
        this.phieuNhap = phieuNhap;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }
    
    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }
    
    public void setPhieuNhap(PhieuNhap phieuNhap) {
        if (phieuNhap == null) {
            throw new IllegalArgumentException("Phiếu nhập không được null");
        }
        this.phieuNhap = phieuNhap;
    }
    
    public SanPham getSanPham() {
        return sanPham;
    }
    
    public void setSanPham(SanPham sanPham) {
        if (sanPham == null) {
            throw new IllegalArgumentException("Sản phẩm không được null");
        }
        this.sanPham = sanPham;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public void setSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        this.soLuong = soLuong;
    }
    
    public double getDonGia() {
        return donGia;
    }
    
    public void setDonGia(double donGia) {
        if (donGia <= 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0");
        }
        this.donGia = donGia;
    }
    
    public double getThanhTien() {
        return soLuong * donGia;
    }
    
    @Override
    public String toString() {
        return "ChiTietPhieuNhap [phieuNhap=" + (phieuNhap != null ? phieuNhap.getMaPhieuNhap() : "null") 
                + ", sanPham=" + (sanPham != null ? sanPham.getMaSanPham() : "null") 
                + ", soLuong=" + soLuong + ", donGia=" + donGia + "]";
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(phieuNhap, sanPham);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ChiTietPhieuNhap other = (ChiTietPhieuNhap) obj;
        return Objects.equals(phieuNhap, other.phieuNhap) && Objects.equals(sanPham, other.sanPham);
    }
}