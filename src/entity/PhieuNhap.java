package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PhieuNhap {
    private String maPhieuNhap;
    private Date ngayNhap;
    private NhanVien nhanVien;
    private NhaCungCap nhaCungCap;
    private List<ChiTietPhieuNhap> danhSachChiTiet;
    
    public PhieuNhap() {
        this.danhSachChiTiet = new ArrayList<>();
    }
    
    public PhieuNhap(String maPhieuNhap, Date ngayNhap, NhanVien nhanVien, NhaCungCap nhaCungCap) {
        this.maPhieuNhap = maPhieuNhap;
        this.ngayNhap = ngayNhap;
        this.nhanVien = nhanVien;
        this.nhaCungCap = nhaCungCap;
        this.danhSachChiTiet = new ArrayList<>();
    }
    
    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }
    
    public void setMaPhieuNhap(String maPhieuNhap) {
        if (maPhieuNhap == null || maPhieuNhap.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu nhập không được để trống");
        }
        this.maPhieuNhap = maPhieuNhap;
    }
    
    public Date getNgayNhap() {
        return ngayNhap;
    }
    
    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
    
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    
    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }
    
    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }
    
    public List<ChiTietPhieuNhap> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }
    
    public void setDanhSachChiTiet(List<ChiTietPhieuNhap> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }
    
    public void themChiTiet(ChiTietPhieuNhap chiTiet) {
        this.danhSachChiTiet.add(chiTiet);
    }
    
    public double getTongTien() {
        double tong = 0;
        for (ChiTietPhieuNhap ct : danhSachChiTiet) {
            tong += ct.getThanhTien();
        }
        return tong;
    }
    
    @Override
    public String toString() {
        return "PhieuNhap [maPhieuNhap=" + maPhieuNhap + ", ngayNhap=" + ngayNhap 
                + ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : "null") 
                + ", nhaCungCap=" + (nhaCungCap != null ? nhaCungCap.getMaNhaCungCap() : "null") + "]";
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maPhieuNhap);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PhieuNhap other = (PhieuNhap) obj;
        return Objects.equals(maPhieuNhap, other.maPhieuNhap);
    }
}