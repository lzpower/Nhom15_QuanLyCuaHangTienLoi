package entity;

import java.util.Objects;

public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private LoaiSanPham loaiSanPham;
    private int soLuongHienCo;
    private double giaNhap;
    private double giaBan; 
    private String urlHinhAnh;
    
    public SanPham() {
    }
    
    public SanPham(String maSanPham) {
        setMaSanPham(maSanPham);
    }
    
    public SanPham(String maSanPham, String tenSanPham, LoaiSanPham loaiSanPham, int soLuongHienCo, double giaNhap, double giaBan,
            String urlHinhAnh) {
        setMaSanPham(maSanPham);
        setTenSanPham(tenSanPham);
        setLoaiSanPham(loaiSanPham);
        setSoLuongHienCo(soLuongHienCo);
        setGiaNhap(giaNhap);
        setGiaBan(giaBan);
        setUrlHinhAnh(urlHinhAnh);
    }

    public SanPham(String maSanPham, String tenSanPham, LoaiSanPham loaiSanPham, int soLuongHienCo, double giaNhap, String urlHinhAnh) {
        setMaSanPham(maSanPham);
        setTenSanPham(tenSanPham);
        setLoaiSanPham(loaiSanPham);
        setSoLuongHienCo(soLuongHienCo);
        setGiaNhap(giaNhap);
        setGiaBan(giaNhap * 1.5); // Mặc định giá bán = giá nhập * 1.5
        setUrlHinhAnh(urlHinhAnh);
    }

    public SanPham(String maSanPham, String tenSanPham, double giaNhap) {
        setMaSanPham(maSanPham);
        setTenSanPham(tenSanPham);
        setGiaNhap(giaNhap);
        setGiaBan(giaNhap * 1.5); // Mặc định giá bán = giá nhập * 1.5
    }
    
    public String getMaSanPham() {
        return maSanPham;
    }
    
    public void setMaSanPham(String maSanPham) throws IllegalArgumentException {
        if (maSanPham == null || maSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống");
        }
        this.maSanPham = maSanPham;
    }
    
    public String getTenSanPham() {
        return tenSanPham;
    }
    
    public void setTenSanPham(String tenSanPham) throws IllegalArgumentException {
        if (tenSanPham == null || tenSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        }
        this.tenSanPham = tenSanPham;
    }
    
    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }
    
    public void setLoaiSanPham(LoaiSanPham loaiSanPham) throws IllegalArgumentException {
        if (loaiSanPham == null) {
            throw new IllegalArgumentException("Loại sản phẩm không được để trống");
        }
        this.loaiSanPham = loaiSanPham;
    }
    
    public int getSoLuongHienCo() {
        return soLuongHienCo;
    }
    
    public void setSoLuongHienCo(int soLuongHienCo) throws IllegalArgumentException {
        if (soLuongHienCo < 0) {
            throw new IllegalArgumentException("Số lượng hiện có không được âm");
        }
        this.soLuongHienCo = soLuongHienCo;
    }
    
    public double getGiaNhap() {
        return giaNhap;
    }
    
    public void setGiaNhap(double giaNhap) throws IllegalArgumentException {
        if (giaNhap < 0) {
            throw new IllegalArgumentException("Giá nhập không được âm");
        }
        this.giaNhap = giaNhap;
    }
    
    public double getGiaBan() {
        return giaBan;
    }
    
    public void setGiaBan(double giaBan) throws IllegalArgumentException {
        if (giaBan < 0) {
            throw new IllegalArgumentException("Giá bán không được âm");
        }
        if (giaBan < giaNhap) {
            throw new IllegalArgumentException("Giá bán không được nhỏ hơn giá nhập");
        }
        this.giaBan = giaBan;
    }
    
    public String getUrlHinhAnh() {
        return urlHinhAnh;
    }

    public void setUrlHinhAnh(String urlHinhAnh) {
        this.urlHinhAnh = urlHinhAnh;
    }
    
    @Override
    public String toString() {
        return "SanPham [maSanPham=" + maSanPham + ", tenSanPham=" + tenSanPham 
                + ", loaiSanPham=" + (loaiSanPham != null ? loaiSanPham.getTenLoaiSanPham() : "null") 
                + ", soLuongHienCo=" + soLuongHienCo + ", giaNhap=" + giaNhap 
                + ", giaBan=" + giaBan + ", urlHinhAnh=" + urlHinhAnh + "]";
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maSanPham);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        SanPham other = (SanPham) obj;
        return Objects.equals(maSanPham, other.maSanPham);
    }
}