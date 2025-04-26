package entity;

import java.util.Objects;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String soDT;
    private int soDiem;

    public KhachHang(String maKH, String tenKH, String soDT, int soDiem) {
        super();
        setMaKH(maKH);
        setTenKH(tenKH);
        setSoDT(soDT);
        setSoDiem(soDiem);
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khách hàng không được để trống");
        }
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        if (tenKH == null || tenKH.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }
        this.tenKH = tenKH;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        if (soDT == null || soDT.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        if (!soDT.matches("\\d{10}")) {
            throw new IllegalArgumentException("Số điện thoại phải là 10 chữ số");
        }
        this.soDT = soDT;
    }

    public int getSoDiem() {
        return soDiem;
    }

    public void setSoDiem(int soDiem) {
        if (soDiem < 0) {
            throw new IllegalArgumentException("Số điểm không được âm");
        }
        this.soDiem = soDiem;
    }

    @Override
    public String toString() {
        return "KhachHang [maKH=" + maKH + ", tenKH=" + tenKH + ", soDT=" + soDT + ", soDiem=" + soDiem + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKH);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        KhachHang other = (KhachHang) obj;
        return Objects.equals(maKH, other.maKH);
    }
}