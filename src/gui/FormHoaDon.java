package gui;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.image.ImageDataFactory;

import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;
import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.SanPhamDAO;

import java.io.IOException;
import java.net.URL;
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

    public boolean xuatHoaDonPDF(String filePath, double giamGia, double tongTien, double tienKhachDua, double tienThoi) {
        try {
            HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHoaDon);
            if (hoaDon == null)
                return false;

            List<ChiTietHoaDon> danhSachChiTiet = chiTietHoaDonDAO.getChiTietHoaDonTheoMaHD(maHoaDon);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            DecimalFormat df = new DecimalFormat("#,##0");

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            
            // Use a narrower page width to ensure it fits on one page
            PageSize receiptPageSize = new PageSize(226.77f, 800f);
            Document document = new Document(pdfDoc, receiptPageSize);
            document.setMargins(10, 10, 10, 10); // Reduce margins

            PdfFont fontNormal = null;
            PdfFont fontBold = null;

            try {
                // Create font from file in fonts directory
                fontNormal = PdfFontFactory.createFont("font/NotoSans-Regular.ttf", PdfEncodings.IDENTITY_H,
                        EmbeddingStrategy.PREFER_EMBEDDED, pdfDoc);
                fontBold = PdfFontFactory.createFont("font/NotoSans-Bold.ttf", PdfEncodings.IDENTITY_H,
                        EmbeddingStrategy.PREFER_EMBEDDED, pdfDoc);
            } catch (IOException e) {
                // If font not found, use default font
                fontNormal = PdfFontFactory.createRegisteredFont("Helvetica", PdfEncodings.IDENTITY_H);
                fontBold = PdfFontFactory.createRegisteredFont("Helvetica-Bold", PdfEncodings.IDENTITY_H);
            }

            document.setFont(fontNormal);

            // Add CircleK logo
            try {
                URL logoUrl = getClass().getResource("/img/circlek.png");
                if (logoUrl != null) {
                    Image logo = new Image(ImageDataFactory.create(logoUrl));
                    logo.setWidth(150);
                    logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    document.add(logo);
                }
            } catch (Exception e) {
                System.err.println("Không thể tải logo: " + e.getMessage());
            }

            // Add store info
            Paragraph storeName = new Paragraph("CỬA HÀNG TIỆN LỢI CIRCLE K")
                .setFont(fontBold)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(storeName);

            Paragraph storeAddress = new Paragraph("12 Nguyễn Văn Bảo, P4, Gò Vấp, TP Hồ Chí Minh")
                .setFont(fontNormal)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER);
            document.add(storeAddress);

            // Add separator
            document.add(new Paragraph("----------------------------------------")
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(5)
                .setMarginBottom(5));

            // Invoice info
            document.add(new Paragraph("Mã hóa đơn: " + maHoaDon)
                .setFont(fontBold)
                .setFontSize(9)
                .setMarginBottom(2));
            document.add(new Paragraph("Ngày lập: " + dateFormat.format(hoaDon.getNgayLap()))
                .setFontSize(9)
                .setMarginBottom(2));
            document.add(new Paragraph("Nhân viên: " + 
                (hoaDon.getNhanVien() != null ? hoaDon.getNhanVien().getTenNhanVien() : ""))
                .setFontSize(9)
                .setMarginBottom(2));

            // Add customer info if available
            if (hoaDon.getKhachHang() != null) {
                document.add(new Paragraph("Khách hàng: " + hoaDon.getKhachHang().getTenKhachHang())
                    .setFontSize(9)
                    .setMarginBottom(2));
                document.add(new Paragraph("SĐT: " + hoaDon.getKhachHang().getSoDienThoai())
                    .setFontSize(9)
                    .setMarginBottom(2));
                document.add(new Paragraph("Điểm tích lũy: " + hoaDon.getKhachHang().getSoDiem())
                    .setFontSize(9)
                    .setMarginBottom(2));
            }

            // Add separator
            document.add(new Paragraph("----------------------------------------")
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(5)
                .setMarginBottom(5));

            // Invoice details title
            document.add(new Paragraph("CHI TIẾT HÓA ĐƠN")
                .setFont(fontBold)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5));

            // Invoice details table
            Table table = new Table(UnitValue.createPercentArray(new float[] { 5, 40, 15, 20, 20 }))
                .useAllAvailableWidth()
                .setMarginBottom(5);

            // Add table header
            table.addCell(new Cell().add(new Paragraph("STT").setFont(fontBold).setFontSize(8)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph("Tên sản phẩm").setFont(fontBold).setFontSize(7)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph("SL").setFont(fontBold).setFontSize(7)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph("Đơn giá").setFont(fontBold).setFontSize(7)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph("Thành tiền").setFont(fontBold).setFontSize(7)).setBorder(null));

            // Add table data
            double tongCong = 0;
            for (int i = 0; i < danhSachChiTiet.size(); i++) {
                ChiTietHoaDon chiTiet = danhSachChiTiet.get(i);
                SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(chiTiet.getSanPham().getMaSanPham());
                double thanhTien = chiTiet.getSoLuong() * chiTiet.getDonGia();
                tongCong += thanhTien;

                table.addCell(new Cell().add(new Paragraph(String.valueOf(i + 1)).setFontSize(6)).setBorder(null));
                table.addCell(new Cell().add(new Paragraph(sanPham != null ? sanPham.getTenSanPham() : "").setFontSize(6))
                        .setBorder(null));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(chiTiet.getSoLuong())).setFontSize(6))
                        .setBorder(null));
                table.addCell(new Cell().add(new Paragraph(df.format(chiTiet.getDonGia()) + " đ").setFontSize(6))
                        .setBorder(null));
                table.addCell(new Cell().add(new Paragraph(df.format(thanhTien) + " đ").setFontSize(6))
                        .setBorder(null));
            }

            document.add(table);

            // Add separator
            document.add(new Paragraph("----------------------------------------")
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(5)
                .setMarginBottom(5));

            // Payment info
            Table paymentTable = new Table(UnitValue.createPercentArray(new float[] { 70, 30 }))
                .useAllAvailableWidth()
                .setMarginBottom(5);

            paymentTable.addCell(new Cell().add(new Paragraph("Tổng cộng:").setFont(fontBold).setFontSize(8)).setBorder(null));
            paymentTable.addCell(new Cell().add(new Paragraph(df.format(tongCong) + " đ").setFont(fontBold).setFontSize(8)).setBorder(null));

            // Add discount info if available
            if (giamGia > 0) {
                paymentTable.addCell(new Cell().add(new Paragraph("Giảm giá:").setFontSize(8)).setBorder(null));
                paymentTable.addCell(new Cell().add(new Paragraph("-" + df.format(giamGia) + " đ").setFontSize(8)).setBorder(null));
            }

            // Add points discount if used
            double diemSuDung = tongCong - giamGia - tongTien;
            if (diemSuDung > 0) {
                paymentTable.addCell(new Cell().add(new Paragraph("Điểm sử dụng:").setFontSize(8)).setBorder(null));
                paymentTable.addCell(new Cell().add(new Paragraph("-" + df.format(diemSuDung) + " đ").setFontSize(8)).setBorder(null));
            }

            // Final total
            paymentTable.addCell(new Cell().add(new Paragraph("Tổng tiền thanh toán:").setFont(fontBold).setFontSize(9)).setBorder(null));
            paymentTable.addCell(new Cell().add(new Paragraph(df.format(tongTien) + " đ").setFont(fontBold).setFontSize(9)).setBorder(null));

            // Add customer payment and change
            paymentTable.addCell(new Cell().add(new Paragraph("Tiền khách đưa:").setFontSize(8)).setBorder(null));
            paymentTable.addCell(new Cell().add(new Paragraph(df.format(tienKhachDua) + " đ").setFontSize(8)).setBorder(null));

            paymentTable.addCell(new Cell().add(new Paragraph("Tiền thối lại:").setFontSize(8)).setBorder(null));
            paymentTable.addCell(new Cell().add(new Paragraph(df.format(tienThoi) + " đ").setFontSize(8)).setBorder(null));

            document.add(paymentTable);

            // Add separator
            document.add(new Paragraph("----------------------------------------")
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(5)
                .setMarginBottom(5));

            // Add thank you message
            document.add(new Paragraph("Xin cảm ơn quý khách đã mua hàng!")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(8)
                .setMarginBottom(2));

            document.add(new Paragraph("Hẹn gặp lại quý khách lần sau!")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(8));

            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}