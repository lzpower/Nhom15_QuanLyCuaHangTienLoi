package gui;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;
import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.SanPhamDAO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class FormHoaDon {
    private final String maHoaDon;
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    public FormHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public boolean xuatHoaDonPDF(String filePath, double giamGia, double tongTien) {
        try {
            HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHoaDon);
            System.out.println("Kiểm tra hoaDon: " + hoaDon);

            if (hoaDon == null) return false;

            List<ChiTietHoaDon> danhSachChiTiet = chiTietHoaDonDAO.getChiTietHoaDonTheoMaHD(maHoaDon);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            DecimalFormat df = new DecimalFormat("#,##0.00");

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Font thường và đậm
            PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            document.setFont(fontNormal);

            // Tiêu đề
            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG")
                    .setFont(fontBold)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Thông tin hóa đơn
            document.add(new Paragraph("Mã hóa đơn: " + maHoaDon));
            document.add(new Paragraph("Ngày lập: " + dateFormat.format(hoaDon.getNgayLap())));

            // Bảng chi tiết hóa đơn
            Table table = new Table(UnitValue.createPercentArray(new float[]{10, 25, 35, 15, 15}))
                    .useAllAvailableWidth();
            String[] headers = {"STT", "Mã SP", "Tên sản phẩm", "Số lượng", "Thành tiền"};
            for (String header : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(header).setFont(fontBold))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            }

            double tongCong = 0;
            for (int i = 0; i < danhSachChiTiet.size(); i++) {
                ChiTietHoaDon chiTiet = danhSachChiTiet.get(i);
                SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(chiTiet.getSanPham().getMaSanPham());
                double thanhTien = chiTiet.getSoLuong() * chiTiet.getDonGia();
                tongCong += thanhTien;

                table.addCell(new Cell().add(new Paragraph(String.valueOf(i + 1))));
                table.addCell(new Cell().add(new Paragraph(chiTiet.getSanPham().getMaSanPham())));
                table.addCell(new Cell().add(new Paragraph(sanPham != null ? sanPham.getTenSanPham() : "Không xác định")));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(chiTiet.getSoLuong()))));
                table.addCell(new Cell().add(new Paragraph(df.format(thanhTien))));
            }

            document.add(table);
            System.out.println("Danh sách chi tiết hóa đơn: " + danhSachChiTiet);

            // Tổng kết
            document.add(new Paragraph("Tổng cộng: " + df.format(tongCong) + " VNĐ"));
            document.add(new Paragraph("Giảm giá: " + df.format(giamGia) + " VNĐ"));
            document.add(new Paragraph("Tổng tiền thanh toán: " + df.format(tongTien) + " VNĐ")
                    .setFont(fontBold));

            // Cảm ơn
            document.add(new Paragraph("Xin cảm ơn quý khách đã mua hàng!")
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}