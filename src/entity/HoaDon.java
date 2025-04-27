package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HoaDon {
    private String maHoaDon;
    private Date ngayLap;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private KhuyenMai khuyenMai;
    private List<ChiTietHoaDon> danhSachChiTiet;

    public HoaDon() {
        this.danhSachChiTiet = new ArrayList<>();
    }

    public HoaDon(String maHoaDon, Date ngayLap, NhanVien nhanVien) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.nhanVien = nhanVien;
        this.danhSachChiTiet = new ArrayList<>();
    }
    
    public HoaDon(String maHoaDon, Date ngayLap, NhanVien nhanVien, KhachHang khachHang, KhuyenMai khuyenMai) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.khuyenMai = khuyenMai;
        this.danhSachChiTiet = new ArrayList<>();
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        if (maHoaDon != null && !maHoaDon.trim().isEmpty())
            this.maHoaDon = maHoaDon;
        else
            throw new IllegalArgumentException("Mã hóa đơn không được rỗng");
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public List<ChiTietHoaDon> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(List<ChiTietHoaDon> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }

    public void themChiTiet(ChiTietHoaDon chiTiet) {
        this.danhSachChiTiet.add(chiTiet);
    }

    public double getTongTien() {
        double tong = 0;
        for (ChiTietHoaDon ct : danhSachChiTiet) {
            tong += ct.getThanhTien();
        }
        return tong;
    }
    
    public double getTongTienSauKhuyenMai() {
        double tongTien = getTongTien();
        if (khuyenMai != null) {
            return tongTien * (1 - khuyenMai.getGiaTriKhuyenMai() / 100);
        }
        return tongTien;
    }
    
    @Override
    public String toString() {
        return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLap=" + ngayLap 
                + ", nhanVien=" + (nhanVien != null ? nhanVien.getMaNhanVien() : "null") 
                + ", khachHang=" + (khachHang != null ? khachHang.getMaKhachHang() : "null") 
                + ", khuyenMai=" + (khuyenMai != null ? khuyenMai.getMaKhuyenMai() : "null") + "]";
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maHoaDon);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        HoaDon other = (HoaDon) obj;
        return Objects.equals(maHoaDon, other.maHoaDon);
    }
}