package entity;

public class KhuyenMai {
    private String maKM;
    private String tenKM;
    private double giaTriKM;

    public KhuyenMai(String maKM, String tenKM, double giaTriKM) {
        setMaKM(maKM);
        setTenKM(tenKM);
        setGiaTriKM(giaTriKM);
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        if (maKM == null || maKM.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khuyến mãi không được để trống.");
        }
        this.maKM = maKM;
    }

    public String getTenKM() {
        return tenKM;
    }

    public void setTenKM(String tenKM) {
        if (tenKM == null || tenKM.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống.");
        }
        this.tenKM = tenKM;
    }

    public double getGiaTriKM() {
        return giaTriKM;
    }

    public void setGiaTriKM(double giaTriKM) {
        if (giaTriKM <= 0 ) {
            throw new IllegalArgumentException("Giá trị khuyến mãi phải lớn hơn 0 và nhỏ hơn hoặc bằng 100.");
        }
        this.giaTriKM = giaTriKM;
    }

    @Override
    public String toString() {
        return tenKM + " (" + maKM + ")";
    }
}
