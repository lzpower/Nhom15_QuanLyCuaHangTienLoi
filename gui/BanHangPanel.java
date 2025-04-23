package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Text;
import com.itextpdf.io.font.constants.StandardFonts;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhuyenMaiDAO;
import dao.SanPhamDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhuyenMai;
import entity.SanPham;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BanHangPanel extends JPanel implements ActionListener {
    private JPanel p;
    private JButton btnTaoHoaDon;
    private JButton btnCapNhat;
    private JButton btnThongKe;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnLamRong;
    private JButton btnInHD;
    private DefaultTableModel model;
    private JTable tb;
    private JLabel jlbMaVach;
    private JLabel jlbMaGiamGia;
    private JLabel jlbTienHang;
    private JLabel jlbTienThue;
    private JLabel jlbTongTien;
    private JLabel jlbSoLuong;
    private JTextField txtMaVach;
    private JTextField txtMaGiamGia;
    private JTextField txtTienHang;
    private JTextField txtTienThue;
    private JTextField txtTongTien;
    private JTextField txtSoLuong;
    
    private SanPhamDAO sanPhamDAO;
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private KhuyenMaiDAO khuyenMaiDAO;
    private String maHoaDonHienTai;

    public BanHangPanel() {
        setLayout(new BorderLayout());

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(10, 10, 15, 10));
        add(p, BorderLayout.CENTER);

        UIManager.put("Label.font", new Font("Time New Roman", Font.BOLD, 17));
        UIManager.put("TextField.font", new Font("Time New Roman", Font.PLAIN, 17));
        UIManager.put("Button.font", new Font("Time New Roman", Font.BOLD, 12));

        // Khởi tạo DAO
        sanPhamDAO = new SanPhamDAO();
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();

        initPNorth();
        initPCenter();
        setFontForLabelsAndTextFields();

        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamRong.addActionListener(this);
        btnTaoHoaDon.addActionListener(this);
        btnInHD.addActionListener(e -> {
            inHoaDon();
        });
    }

    private void setFontForLabelsAndTextFields() {
        Font newFont = new Font("Arial", Font.PLAIN, 18);
        Dimension newSize = new Dimension(250, 25);

        txtMaVach.setFont(newFont);
        txtMaVach.setPreferredSize(newSize);
        txtSoLuong.setFont(newFont);
        txtSoLuong.setPreferredSize(newSize);
        txtMaGiamGia.setFont(newFont);
        txtMaGiamGia.setPreferredSize(newSize);
        txtTienHang.setFont(newFont);
        txtTienHang.setPreferredSize(newSize);
        txtTienThue.setFont(newFont);
        txtTienThue.setPreferredSize(newSize);
        txtTongTien.setFont(newFont);
        txtTongTien.setPreferredSize(newSize);

        for (Component comp : p.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component subComp : panel.getComponents()) {
                    if (subComp instanceof JLabel) {
                        JLabel label = (JLabel) subComp;
                        label.setFont(newFont);
                    }
                }
            }
        }

        btnTaoHoaDon.setFont(newFont);
        btnCapNhat.setFont(newFont);
        btnThongKe.setFont(newFont);
        btnThem.setFont(newFont);
        btnXoa.setFont(newFont);
        btnLamRong.setFont(newFont);
        btnInHD.setFont(newFont);
    }

    public void initPNorth() {
        JPanel pn = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        pn.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        gbc.gridx = 0;
        gbc.gridy = 0;
        pn.add(btnTaoHoaDon = new JButton("Tạo hóa đơn"), gbc);
        gbc.gridx = 1;
        pn.add(btnCapNhat = new JButton("Cập nhật"), gbc);
        gbc.gridx = 2;
        pn.add(btnThongKe = new JButton("Thống kê"), gbc);
        p.add(pn);
    }

    public void initPCenter() {
        JPanel pc = new JPanel(new BorderLayout());
        JPanel pch = new JPanel();
        pch.add(jlbMaVach = new JLabel("Nhập mã vạch:"));
        pch.add(txtMaVach = new JTextField(10));
        pch.add(jlbSoLuong = new JLabel("Nhập số lượng:"));
        pch.add(txtSoLuong = new JTextField(10));
        pch.add(btnThem = new JButton("Thêm"));
        pch.add(btnXoa = new JButton("Xóa"));
        pch.add(btnLamRong = new JButton("Làm Rỗng"));

        pc.add(pch, BorderLayout.NORTH);
        String[] colName = {"STT", "Mã SP", "Tên sản phẩm", "Số lượng", "Giá", "Thành Tiền"};
        Object[][] data = {};
        model = new DefaultTableModel(data, colName);
        tb = new JTable(model);
        tb.setRowHeight(25);
        tb.setPreferredSize(new Dimension(0, 500));

        JPanel pcs = new JPanel(new BorderLayout());
        JPanel pcsw = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        pcsw.setPreferredSize(new Dimension(400, 0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        pcsw.add(new JLabel("Mã giảm giá:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        txtMaGiamGia = new JTextField(10);
        txtMaGiamGia.setPreferredSize(new Dimension(150, 25));
        txtMaGiamGia.setMaximumSize(new Dimension(150, 25));
        txtMaGiamGia.setMinimumSize(new Dimension(150, 25));
        pcsw.add(txtMaGiamGia, gbc);

        JPanel pcsc = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pcsc.add(jlbTienHang = new JLabel("Tiền hàng:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTienHang = new JTextField(10);
        txtTienHang.setEditable(false);
        pcsc.add(txtTienHang, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        pcsc.add(jlbTienThue = new JLabel("Tiền thuế:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTienThue = new JTextField(10);
        txtTienThue.setEditable(false);
        pcsc.add(txtTienThue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        pcsc.add(jlbTongTien = new JLabel("Tổng tiền:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTongTien = new JTextField(10);
        txtTongTien.setEditable(false);
        pcsc.add(txtTongTien, gbc);

        gbc.gridy = 4;
        pcsc.add(btnInHD = new JButton("In hóa đơn"), gbc);

        pcs.add(pcsw, BorderLayout.WEST);
        pcs.add(pcsc, BorderLayout.CENTER);
        pc.add(pcs, BorderLayout.SOUTH);
        pc.add(new JScrollPane(tb), BorderLayout.CENTER);
        p.add(pc);
    }

    private void capNhatTongTien() {
        double tongTien = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            tongTien += Double.parseDouble(model.getValueAt(i, 5).toString());
        }
        txtTienHang.setText(String.format("%.2f", tongTien));
        double tienThue = tongTien * 0.1;
        txtTienThue.setText(String.format("%.2f", tienThue));
        
        // Áp dụng khuyến mãi nếu có
        double tongTienSauThue = tongTien + tienThue;
        String maKM = txtMaGiamGia.getText().trim();
        if (!maKM.isEmpty()) {
            KhuyenMai km = khuyenMaiDAO.getKhuyenMaiTheoMa(maKM);
            if (km != null) {
                double giamGia = tongTienSauThue * (km.getGiaTriKM() / 100.0);
                tongTienSauThue -= giamGia;
            }
        }
        
        txtTongTien.setText(String.format("%.2f", tongTienSauThue));
    }

    private void capNhatSTT() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }
    }

    private void inHoaDon() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm nào để in hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (maHoaDonHienTai == null || maHoaDonHienTai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng tạo hóa đơn trước khi in!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String fileName = "hoadon_" + maHoaDonHienTai + ".pdf";
            PdfWriter writer = new PdfWriter(new FileOutputStream(fileName));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            Text titleText = new Text("HOÁ ĐƠN THANH TOÁN").setFont(boldFont).setFontSize(18);
            Paragraph title = new Paragraph().add(titleText).setMarginBottom(10);
            document.add(title);

            document.add(new Paragraph().add(new Text("Mã Hóa Đơn: ").setFont(boldFont)).add(maHoaDonHienTai));
            document.add(new Paragraph().add(new Text("Khách Hàng: ").setFont(boldFont)).add("Khách lẻ"));

            String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            document.add(new Paragraph().add(new Text("Ngày: ").setFont(boldFont)).add(formattedDate));

            float[] columnWidths = {50F, 150F, 80F, 80F, 100F};
            Table table = new Table(columnWidths);
            table.addHeaderCell("STT");
            table.addHeaderCell("Tên SP");
            table.addHeaderCell("SL");
            table.addHeaderCell("Đơn Giá");
            table.addHeaderCell("Thành Tiền");

            for (int i = 0; i < model.getRowCount(); i++) {
                table.addCell(String.valueOf(i + 1));
                table.addCell(model.getValueAt(i, 2).toString());
                table.addCell(model.getValueAt(i, 3).toString());
                table.addCell(model.getValueAt(i, 4).toString());
                table.addCell(model.getValueAt(i, 5).toString());
            }

            document.add(table);
            document.add(new Paragraph().add(new Text("Tiền hàng: ").setFont(boldFont)).add(txtTienHang.getText()));
            document.add(new Paragraph().add(new Text("Tiền thuế: ").setFont(boldFont)).add(txtTienThue.getText()));
            
            String maKM = txtMaGiamGia.getText().trim();
            if (!maKM.isEmpty()) {
                KhuyenMai km = khuyenMaiDAO.getKhuyenMaiTheoMa(maKM);
                if (km != null) {
                    document.add(new Paragraph().add(new Text("Khuyến mãi: ").setFont(boldFont)).add(km.getTenKM() + " (" + km.getGiaTriKM() + "%)"));
                }
            }
            
            document.add(new Paragraph().add(new Text("Tổng Tiền: ").setFont(boldFont)).add(txtTongTien.getText()));

            document.close();
            JOptionPane.showMessageDialog(this, "Hóa đơn đã được in và lưu thành PDF.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo hóa đơn PDF.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnTaoHoaDon)) {
            // Tạo hóa đơn mới
            maHoaDonHienTai = hoaDonDAO.taoMaHoaDonMoi();
            HoaDon hoaDon = new HoaDon(maHoaDonHienTai, new Date(), "NV001"); // Mã NV cần lấy từ người đăng nhập
            if (hoaDonDAO.themHoaDon(hoaDon)) {
                JOptionPane.showMessageDialog(this, "Đã tạo hóa đơn mới với mã: " + maHoaDonHienTai);
                model.setRowCount(0);
                txtTienHang.setText("0.00");
                txtTienThue.setText("0.00");
                txtTongTien.setText("0.00");
                txtMaGiamGia.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi tạo hóa đơn mới!");
            }
        } else if (o.equals(btnThem)) {
            if (maHoaDonHienTai == null || maHoaDonHienTai.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng tạo hóa đơn trước khi thêm sản phẩm!");
                return;
            }
            
            String maVach = txtMaVach.getText();
            String soLuongStr = txtSoLuong.getText();

            if (maVach.isEmpty() || soLuongStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã vạch và số lượng.");
                return;
            }

            try {
                int soLuong = Integer.parseInt(soLuongStr);
                if (soLuong <= 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0.");
                    return;
                }
                
                // Kiểm tra sản phẩm trong CSDL
                SanPham sp = sanPhamDAO.getSanPhamTheoMa(maVach);
                if (sp == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với mã: " + maVach);
                    return;
                }
                
                if (sp.getSlHienCo() < soLuong) {
                    JOptionPane.showMessageDialog(this, "Số lượng sản phẩm trong kho không đủ! Hiện có: " + sp.getSlHienCo());
                    return;
                }
                
                // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
                boolean daTonTai = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 1).equals(maVach)) {
                        int slCu = Integer.parseInt(model.getValueAt(i, 3).toString());
                        int slMoi = slCu + soLuong;
                        if (sp.getSlHienCo() < slMoi) {
                            JOptionPane.showMessageDialog(this, "Số lượng sản phẩm trong kho không đủ! Hiện có: " + sp.getSlHienCo());
                            return;
                        }
                        double donGia = sp.getGiaBan();
                        double thanhTien = donGia * slMoi;
                        model.setValueAt(slMoi, i, 3);
                        model.setValueAt(thanhTien, i, 5);
                        daTonTai = true;
                        break;
                    }
                }
                
                if (!daTonTai) {
                    double donGia = sp.getGiaBan();
                    double thanhTien = donGia * soLuong;
                    model.addRow(new Object[]{model.getRowCount() + 1, maVach, sp.getTenSP(), soLuong, donGia, thanhTien});
                }
                
                // Cập nhật số lượng sản phẩm trong kho
                sanPhamDAO.capNhatSoLuongSanPham(maVach, sp.getSlHienCo() - soLuong);
                
                // Thêm chi tiết hóa đơn
                ChiTietHoaDon chiTiet = new ChiTietHoaDon(maHoaDonHienTai, maVach, soLuong, sp.getGiaBan());
                chiTietHoaDonDAO.themChiTietHoaDon(chiTiet);
                
                capNhatTongTien();
                txtMaVach.setText("");
                txtSoLuong.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng phải là một số hợp lệ.");
            }
        } else if (o.equals(btnXoa)) {
            int selectedRow = tb.getSelectedRow();
            if (selectedRow >= 0) {
                String maSP = model.getValueAt(selectedRow, 1).toString();
                int soLuong = Integer.parseInt(model.getValueAt(selectedRow, 3).toString());
                
                // Cập nhật lại số lượng sản phẩm trong kho
                SanPham sp = sanPhamDAO.getSanPhamTheoMa(maSP);
                if (sp != null) {
                    sanPhamDAO.capNhatSoLuongSanPham(maSP, sp.getSlHienCo() + soLuong);
                }
                
                // Xóa chi tiết hóa đơn
                if (maHoaDonHienTai != null && !maHoaDonHienTai.isEmpty()) {
                    chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maSP);
                }
                
                model.removeRow(selectedRow);
                capNhatSTT();
                capNhatTongTien();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa.");
            }
        } else if (o.equals(btnLamRong)) {
            txtMaVach.setText("");
            txtSoLuong.setText("");
        }
    }
}