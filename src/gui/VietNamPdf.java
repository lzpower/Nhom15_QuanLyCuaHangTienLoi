package gui;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class VietNamPdf {
    public static void main(String[] args) throws Exception {
        String dest = "utput.pdf";
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Đường dẫn đến font hỗ trợ tiếng Việt (VD: Arial Unicode MS hoặc DejaVuSans)
        String fontPath = "font/NotoSans-Regular.ttf"; // Đảm bảo bạn có file font này

        PdfFont font = PdfFontFactory.createFont(
                fontPath,
                PdfEncodings.IDENTITY_H,
                EmbeddingStrategy.PREFER_EMBEDDED,
                pdf
            );


        String vietnameseText = "Xin chào, đây là văn bản tiếng Việt!";
        Paragraph para = new Paragraph(vietnameseText).setFont(font);

        document.add(para);
        document.close();

        System.out.println("PDF đã được tạo thành công.");
    }
}

