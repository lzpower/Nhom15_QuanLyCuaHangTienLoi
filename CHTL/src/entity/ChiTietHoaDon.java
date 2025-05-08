package entity;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private SanPham sanPham;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, SanPham sanPham, int soLuong, double donGia) {
        this.hoaDon = hoaDon;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        if (hoaDon != null)
            this.hoaDon = hoaDon;
        else
            throw new IllegalArgumentException("Hóa đơn không được null");
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        if (sanPham != null)
            this.sanPham = sanPham;
        else
            throw new IllegalArgumentException("Sản phẩm không được null");
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong > 0)
            this.soLuong = soLuong;
        else
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        if (donGia > 0)
            this.donGia = donGia;
        else
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0");
    }

    public double getThanhTien() {
        return soLuong * donGia;
    }
    
    @Override
    public String toString() {
        return "ChiTietHoaDon [hoaDon=" + (hoaDon != null ? hoaDon.getMaHoaDon() : "null") 
                + ", sanPham=" + (sanPham != null ? sanPham.getMaSanPham() : "null") 
                + ", soLuong=" + soLuong + ", donGia=" + donGia + "]";
    }
}