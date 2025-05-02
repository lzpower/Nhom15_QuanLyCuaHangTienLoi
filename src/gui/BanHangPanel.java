package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

import com.kitfox.svg.app.beans.SVGIcon;

import connectDB.ConnectDB;
import dao.*;
import entity.*;

public class BanHangPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel p;
    private JButton btnTaoHoaDon, btnXoa, btnLamRong, btnInHD;
    private JButton btnTimKhachHang, btnDung, btnThanhToan;
    private JComboBox<String> cbbMaVach, cbbMaKhuyenMai;
    private DefaultTableModel model;
    private JTable tb;
    private JScrollPane jsc;
    private JTextField txtTongCong, txtGiamGia, txtSuDungDiem, txtTongTien;
    private JTextField txtSoLuong, txtMaKhachHang, txtTenKhachHang;
    private JTextField txtSoDienThoai, txtMaHoaDon, txtNhanVien, txtNgayTao;
    private JTextField txtDiem, txtTienKhachDua, txtTienThoi;

    private SanPhamDAO sanPhamDAO;
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private KhuyenMaiDAO khuyenMaiDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;

    private String maHoaDonHienTai;
    private String maNhanVienHienTai;
    private String tenNhanVienHienTai;
    private boolean daCoHoaDon = false;
    private double tongCong = 0;
    private KhachHang khachHangHienTai = null;
    private boolean isDungDiem = false;

    // Biểu thức chính quy
    private final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}");
    private final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private final Pattern BARCODE_PATTERN = Pattern.compile("\\d{12}");
    
    // Format tiền tệ
    private final DecimalFormat currencyFormat = new DecimalFormat("#.###");

    public BanHangPanel(String maNhanVien, String tenNhanVien) {
        this.maNhanVienHienTai = maNhanVien;
        this.tenNhanVienHienTai = tenNhanVien;
        
        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Khởi tạo DAO
        sanPhamDAO = new SanPhamDAO();
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
        khachHangDAO = new KhachHangDAO();
        nhanVienDAO = new NhanVienDAO();
        
        // Thiết lập giao diện
        setLayout(new BorderLayout());
        p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 10, 15, 10));
        p.setBackground(Color.WHITE);
        add(p, BorderLayout.CENTER);

        // Thiết lập font chung
        setFont(new Font("Segoe UI", Font.PLAIN, 14), 
                new Font("Segoe UI", Font.BOLD, 14), 
                new Font("Segoe UI", Font.BOLD, 13));

        // Phần trên: Thông tin hóa đơn và nút tạo hóa đơn
        JPanel pnHeader = new JPanel(new BorderLayout());
        pnHeader.setBackground(Color.WHITE);

        // Panel cho nút tạo hóa đơn
        JPanel pnTaoHoaDon = new JPanel(new BorderLayout());
        pnTaoHoaDon.setBackground(new Color(0, 153, 204));
        pnTaoHoaDon.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnTaoHoaDon = new JButton("TẠO HÓA ĐƠN MỚI");
        btnTaoHoaDon.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnTaoHoaDon.setForeground(Color.WHITE);
        btnTaoHoaDon.setBackground(new Color(0, 153, 204));
        btnTaoHoaDon.setFocusPainted(false);
        btnTaoHoaDon.setBorderPainted(false);

        try {
            btnTaoHoaDon.setIcon(new SVGIcon() {{
                setSvgURI(getClass().getResource("/img/taohoadon.svg").toURI());
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }

        pnTaoHoaDon.add(btnTaoHoaDon, BorderLayout.CENTER);

        // Panel thông tin hóa đơn
        JPanel pnThongTinHD = new JPanel(new GridLayout(1, 6, 10, 0));
        pnThongTinHD.setBackground(Color.WHITE);
        pnThongTinHD.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Tạo các label và text field
        JLabel lblMaHD = new JLabel("Mã hóa đơn:");
        lblMaHD.setForeground(new Color(51, 51, 51));

        txtMaHoaDon = new JTextField(10);
        txtMaHoaDon.setEditable(false);
        txtMaHoaDon.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 153, 204)),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JLabel lblNhanVien = new JLabel("Nhân viên:");
        lblNhanVien.setForeground(new Color(51, 51, 51));

        txtNhanVien = new JTextField(10);
        txtNhanVien.setEditable(false);
        txtNhanVien.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 153, 204)),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JLabel lblNgayTao = new JLabel("Ngày tạo:");
        lblNgayTao.setForeground(new Color(51, 51, 51));

        txtNgayTao = new JTextField(10);
        txtNgayTao.setEditable(false);
        txtNgayTao.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 153, 204)),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        pnThongTinHD.add(lblMaHD);
        pnThongTinHD.add(txtMaHoaDon);
        pnThongTinHD.add(lblNhanVien);
        pnThongTinHD.add(txtNhanVien);
        pnThongTinHD.add(lblNgayTao);
        pnThongTinHD.add(txtNgayTao);

        pnHeader.add(pnTaoHoaDon, BorderLayout.WEST);
        pnHeader.add(pnThongTinHD, BorderLayout.CENTER);

        p.add(pnHeader, BorderLayout.NORTH);

        // Tạo panel chính chia làm 2 phần: trái và phải
        JPanel mainPanel = new JPanel(new BorderLayout(15, 0));
        mainPanel.setBackground(Color.WHITE);

        // Phần trái: Thêm sản phẩm và bảng sản phẩm
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(240, 240, 240)),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Panel nhập mã vạch và số lượng
        JPanel pnNhapSP = new JPanel(new BorderLayout(10, 0));
        pnNhapSP.setBackground(Color.WHITE);
        pnNhapSP.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)), "Thêm sản phẩm",
                        TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), new Color(0, 153, 204)));

        JPanel pnMaVach = new JPanel(new BorderLayout(5, 0));
        pnMaVach.setBackground(Color.WHITE);

        JLabel lblMaVach = new JLabel("Mã vạch:");
        lblMaVach.setForeground(new Color(51, 51, 51));

        cbbMaVach = new JComboBox<>();
        cbbMaVach.setEditable(true);

        pnMaVach.add(lblMaVach, BorderLayout.WEST);
        pnMaVach.add(cbbMaVach, BorderLayout.CENTER);

        JPanel pnSoLuong = new JPanel(new BorderLayout(5, 0));
        pnSoLuong.setBackground(Color.WHITE);

        JLabel lblSoLuong = new JLabel("Số lượng:");
        lblSoLuong.setForeground(new Color(51, 51, 51));

        txtSoLuong = new JTextField("1");
        txtSoLuong.setEnabled(false);
        txtSoLuong.setHorizontalAlignment(JTextField.CENTER);

        // Thêm spinner cho số lượng
        JPanel spinnerPanel = new JPanel(new BorderLayout());
        spinnerPanel.setBackground(Color.WHITE);
        
        JButton btnUp = new JButton("▲");
        btnUp.setFont(new Font("Arial", Font.BOLD, 8));
        btnUp.setMargin(new Insets(0, 0, 0, 0));
        btnUp.setPreferredSize(new Dimension(20, 12));
        btnUp.addActionListener(e -> {
            try {
                int soLuong = Integer.parseInt(txtSoLuong.getText());
                txtSoLuong.setText(String.valueOf(soLuong + 1));
            } catch (NumberFormatException ex) {
                txtSoLuong.setText("1");
            }
        });
        
        JButton btnDown = new JButton("▼");
        btnDown.setFont(new Font("Arial", Font.BOLD, 8));
        btnDown.setMargin(new Insets(0, 0, 0, 0));
        btnDown.setPreferredSize(new Dimension(20, 12));
        btnDown.addActionListener(e -> {
            try {
                int soLuong = Integer.parseInt(txtSoLuong.getText());
                if (soLuong > 1) {
                    txtSoLuong.setText(String.valueOf(soLuong - 1));
                }
            } catch (NumberFormatException ex) {
                txtSoLuong.setText("1");
            }
        });
        
        JPanel arrowPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        arrowPanel.add(btnUp);
        arrowPanel.add(btnDown);
        
        spinnerPanel.add(txtSoLuong, BorderLayout.CENTER);
        spinnerPanel.add(arrowPanel, BorderLayout.EAST);
        
        pnSoLuong.add(lblSoLuong, BorderLayout.WEST);
        pnSoLuong.add(spinnerPanel, BorderLayout.CENTER);

        JPanel pnButtonsNhapSP = new JPanel(new GridLayout(1, 2, 5, 0));
        pnButtonsNhapSP.setBackground(Color.WHITE);

        btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(231, 76, 60));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setFocusPainted(false);
        btnXoa.setBorderPainted(false);

        try {
            btnXoa.setIcon(new SVGIcon() {{
                setSvgURI(getClass().getResource("/img/xoa.svg").toURI());
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnLamRong = new JButton("Làm rỗng");
        btnLamRong.setBackground(new Color(52, 152, 219));
        btnLamRong.setForeground(Color.WHITE);
        btnLamRong.setFocusPainted(false);
        btnLamRong.setBorderPainted(false);

        try {
            btnLamRong.setIcon(new SVGIcon() {{
                setSvgURI(getClass().getResource("/img/xoatrang.svg").toURI());
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }

        pnButtonsNhapSP.add(btnXoa);
        pnButtonsNhapSP.add(btnLamRong);

        JPanel pnNhapSPContent = new JPanel(new BorderLayout(10, 10));
        pnNhapSPContent.setBackground(Color.WHITE);
        pnNhapSPContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnNhapSPFields = new JPanel(new GridLayout(1, 2, 10, 0));
        pnNhapSPFields.setBackground(Color.WHITE);
        pnNhapSPFields.add(pnMaVach);
        pnNhapSPFields.add(pnSoLuong);

        pnNhapSPContent.add(pnNhapSPFields, BorderLayout.CENTER);
        pnNhapSPContent.add(pnButtonsNhapSP, BorderLayout.EAST);

        pnNhapSP.add(pnNhapSPContent, BorderLayout.CENTER);

        // Bảng sản phẩm
        JPanel pnTableSP = new JPanel(new BorderLayout());
        pnTableSP.setBackground(Color.WHITE);
        pnTableSP.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)), "Danh sách sản phẩm",
                        TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), new Color(0, 153, 204)));

        String[] colName = { "STT", "Hình ảnh", "Mã vạch", "Tên sản phẩm", "Số lượng", "Giá", "Thành Tiền" };
        model = new DefaultTableModel(colName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Chỉ cho phép sửa cột số lượng
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        tb = new JTable(model);
        tb.setRowHeight(100);
        tb.setShowGrid(true);
        tb.setGridColor(new Color(240, 240, 240));
        tb.setSelectionBackground(new Color(0, 153, 204));
        tb.getTableHeader().setBackground(Color.WHITE);
        tb.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Thiết lập độ rộng cột
        tb.getColumnModel().getColumn(0).setPreferredWidth(40); // STT
        tb.getColumnModel().getColumn(1).setPreferredWidth(100); // Hình ảnh
        tb.getColumnModel().getColumn(2).setPreferredWidth(120); // Mã SP
        tb.getColumnModel().getColumn(3).setPreferredWidth(250); // Tên sản phẩm
        tb.getColumnModel().getColumn(4).setPreferredWidth(80); // Số lượng
        tb.getColumnModel().getColumn(5).setPreferredWidth(100); // Giá
        tb.getColumnModel().getColumn(6).setPreferredWidth(120); // Thành Tiền

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tb.getColumnCount(); i++) {
            if (i != 1 && i != 3) { // Không áp dụng cho cột hình ảnh và tên sản phẩm
                tb.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Tùy chỉnh renderer cho cột số lượng để thêm nút tăng/giảm
        tb.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new BorderLayout(2, 0));
                JLabel label = new JLabel(value.toString(), SwingConstants.CENTER);
                
                if (isSelected) {
                    panel.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    panel.setBackground(table.getBackground());
                    label.setForeground(table.getForeground());
                }
                
                panel.add(label, BorderLayout.CENTER);
                
                return panel;
            }
        });
        
        // Tùy chỉnh editor cho cột số lượng
        tb.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JTextField()) {
            private JPanel panel;
            private JTextField textField;
            private JButton upButton;
            private JButton downButton;
            
            {
                panel = new JPanel(new BorderLayout(2, 0));
                textField = new JTextField();
                textField.setHorizontalAlignment(JTextField.CENTER);
                
                upButton = new JButton("▲");
                upButton.setFont(new Font("Arial", Font.BOLD, 8));
                upButton.setMargin(new Insets(0, 0, 0, 0));
                upButton.setPreferredSize(new Dimension(20, 12));
                
                downButton = new JButton("▼");
                downButton.setFont(new Font("Arial", Font.BOLD, 8));
                downButton.setMargin(new Insets(0, 0, 0, 0));
                downButton.setPreferredSize(new Dimension(20, 12));
                
                JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 0));
                buttonPanel.add(upButton);
                buttonPanel.add(downButton);
                
                panel.add(textField, BorderLayout.CENTER);
                panel.add(buttonPanel, BorderLayout.EAST);
                
                upButton.addActionListener(e -> {
                    try {
                        int value = Integer.parseInt(textField.getText());
                        textField.setText(String.valueOf(value + 1));
                        stopCellEditing();
                    } catch (NumberFormatException ex) {
                        textField.setText("1");
                    }
                });
                
                downButton.addActionListener(e -> {
                    try {
                        int value = Integer.parseInt(textField.getText());
                        if (value > 1) {
                            textField.setText(String.valueOf(value - 1));
                            stopCellEditing();
                        }
                    } catch (NumberFormatException ex) {
                        textField.setText("1");
                    }
                });
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                textField.setText(value.toString());
                return panel;
            }
            
            @Override
            public Object getCellEditorValue() {
                return textField.getText();
            }
        });

        tb.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public void setValue(Object value) {
                if (value instanceof ImageIcon) {
                    setIcon((ImageIcon) value);
                    setText("");
                } else {
                    super.setValue(value);
                }
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        jsc = new JScrollPane(tb);
        jsc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnTableSP.add(jsc, BorderLayout.CENTER);

        leftPanel.add(pnNhapSP, BorderLayout.NORTH);
        leftPanel.add(pnTableSP, BorderLayout.CENTER);

        // Phần phải: Thông tin khách hàng và thanh toán
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(400, 0));

        // Thông tin khách hàng (ở trên)
        JPanel pnKhachHang = new JPanel(new BorderLayout());
        pnKhachHang.setBackground(Color.WHITE);
        pnKhachHang.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)),
                "Thông tin khách hàng", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14),
                new Color(0, 153, 204)));

        JPanel pnKhachHangContent = new JPanel(new GridLayout(5, 1, 0, 10));
        pnKhachHangContent.setBackground(Color.WHITE);
        pnKhachHangContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Khuyến mãi
        JPanel pnKhuyenMai = new JPanel(new BorderLayout(10, 0));
        pnKhuyenMai.setBackground(Color.WHITE);

        JLabel lblKhuyenMai = new JLabel("Mã khuyến mãi:");
        lblKhuyenMai.setForeground(new Color(51, 51, 51));

        cbbMaKhuyenMai = new JComboBox<>();

        pnKhuyenMai.add(lblKhuyenMai, BorderLayout.WEST);
        pnKhuyenMai.add(cbbMaKhuyenMai, BorderLayout.CENTER);

        // Mã khách hàng
        JPanel pnMaKH = new JPanel(new BorderLayout(10, 0));
        pnMaKH.setBackground(Color.WHITE);

        JLabel lblMaKH = new JLabel("Mã khách hàng:");
        lblMaKH.setForeground(new Color(51, 51, 51));

        txtMaKhachHang = new JTextField(10);

        btnTimKhachHang = new JButton();
        btnTimKhachHang.setBackground(new Color(52, 152, 219));
        btnTimKhachHang.setForeground(Color.WHITE);
        btnTimKhachHang.setFocusPainted(false);
        btnTimKhachHang.setBorderPainted(false);

        try {
            btnTimKhachHang.setIcon(new SVGIcon() {{
                setSvgURI(getClass().getResource("/img/tim.svg").toURI());
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel pnMaKHInput = new JPanel(new BorderLayout(5, 0));
        pnMaKHInput.setBackground(Color.WHITE);
        pnMaKHInput.add(txtMaKhachHang, BorderLayout.CENTER);
        pnMaKHInput.add(btnTimKhachHang, BorderLayout.EAST);

        pnMaKH.add(lblMaKH, BorderLayout.WEST);
        pnMaKH.add(pnMaKHInput, BorderLayout.CENTER);

        // Tên khách hàng
        JPanel pnTenKH = new JPanel(new BorderLayout(10, 0));
        pnTenKH.setBackground(Color.WHITE);

        JLabel lblTenKH = new JLabel("Tên khách hàng:");
        lblTenKH.setForeground(new Color(51, 51, 51));

        txtTenKhachHang = new JTextField(10);
        txtTenKhachHang.setEditable(false);
        txtTenKhachHang.setBackground(new Color(245, 245, 245));

        pnTenKH.add(lblTenKH, BorderLayout.WEST);
        pnTenKH.add(txtTenKhachHang, BorderLayout.CENTER);

        // Số điện thoại
        JPanel pnSDT = new JPanel(new BorderLayout(10, 0));
        pnSDT.setBackground(Color.WHITE);

        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setForeground(new Color(51, 51, 51));

        txtSoDienThoai = new JTextField(10);
        txtSoDienThoai.setEditable(false);
        txtSoDienThoai.setBackground(new Color(245, 245, 245));


        pnSDT.add(lblSDT, BorderLayout.WEST);
        pnSDT.add(txtSoDienThoai, BorderLayout.CENTER);

        // Điểm
        JPanel pnDiem = new JPanel(new BorderLayout(10, 0));
        pnDiem.setBackground(Color.WHITE);

        JLabel lblDiem = new JLabel("Điểm tích lũy:");
        lblDiem.setForeground(new Color(51, 51, 51));

        txtDiem = new JTextField(10);
        txtDiem.setEditable(false);
        txtDiem.setBackground(new Color(245, 245, 245));

        btnDung = new JButton("Dùng");
        btnDung.setBackground(new Color(0, 153, 204));
        btnDung.setForeground(Color.WHITE);
        btnDung.setFocusPainted(false);
        btnDung.setBorderPainted(false);
        btnDung.setEnabled(false);

        JPanel pnDiemInput = new JPanel(new BorderLayout(5, 0));
        pnDiemInput.setBackground(Color.WHITE);
        pnDiemInput.add(txtDiem, BorderLayout.CENTER);
        pnDiemInput.add(btnDung, BorderLayout.EAST);

        pnDiem.add(lblDiem, BorderLayout.WEST);
        pnDiem.add(pnDiemInput, BorderLayout.CENTER);

        pnKhachHangContent.add(pnKhuyenMai);
        pnKhachHangContent.add(pnMaKH);
        pnKhachHangContent.add(pnTenKH);
        pnKhachHangContent.add(pnSDT);
        pnKhachHangContent.add(pnDiem);

        pnKhachHang.add(pnKhachHangContent, BorderLayout.CENTER);

        // Thông tin thanh toán (ở dưới)
        JPanel pnThanhToan = new JPanel(new BorderLayout());
        pnThanhToan.setBackground(Color.WHITE);
        pnThanhToan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)),
                "Thanh toán", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), new Color(0, 153, 204)));

        JPanel pnThanhToanContent = new JPanel(new GridLayout(8, 1, 0, 10));
        pnThanhToanContent.setBackground(Color.WHITE);
        pnThanhToanContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tổng cộng
        JPanel pnTongCong = new JPanel(new BorderLayout(10, 0));
        pnTongCong.setBackground(Color.WHITE);

        JLabel lblTongCong = new JLabel("Tổng cộng:");
        lblTongCong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongCong.setForeground(new Color(51, 51, 51));

        txtTongCong = new JTextField(10);
        txtTongCong.setEditable(false);
        txtTongCong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtTongCong.setHorizontalAlignment(JTextField.RIGHT);
        txtTongCong.setBackground(new Color(245, 245, 245));

        pnTongCong.add(lblTongCong, BorderLayout.WEST);
        pnTongCong.add(txtTongCong, BorderLayout.CENTER);

        // Giảm giá
        JPanel pnGiamGia = new JPanel(new BorderLayout(10, 0));
        pnGiamGia.setBackground(Color.WHITE);

        JLabel lblGiamGia = new JLabel("Giảm giá:");
        lblGiamGia.setForeground(new Color(51, 51, 51));

        txtGiamGia = new JTextField(10);
        txtGiamGia.setEditable(false);
        txtGiamGia.setHorizontalAlignment(JTextField.RIGHT);
        txtGiamGia.setBackground(new Color(245, 245, 245));

        pnGiamGia.add(lblGiamGia, BorderLayout.WEST);
        pnGiamGia.add(txtGiamGia, BorderLayout.CENTER);

        // Sử dụng điểm
        JPanel pnSuDungDiem = new JPanel(new BorderLayout(10, 0));
        pnSuDungDiem.setBackground(Color.WHITE);

        JLabel lblSuDungDiem = new JLabel("Sử dụng điểm:");
        lblSuDungDiem.setForeground(new Color(51, 51, 51));

        txtSuDungDiem = new JTextField(10);
        txtSuDungDiem.setEditable(false);
        txtSuDungDiem.setHorizontalAlignment(JTextField.RIGHT);
        txtSuDungDiem.setBackground(new Color(245, 245, 245));

        pnSuDungDiem.add(lblSuDungDiem, BorderLayout.WEST);
        pnSuDungDiem.add(txtSuDungDiem, BorderLayout.CENTER);

        // Tổng tiền
        JPanel pnTongTien = new JPanel(new BorderLayout(10, 0));
        pnTongTien.setBackground(Color.WHITE);

        JLabel lblTongTien = new JLabel("Tổng tiền:");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongTien.setForeground(new Color(51, 51, 51));

        txtTongTien = new JTextField(10);
        txtTongTien.setEditable(false);
        txtTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
        txtTongTien.setForeground(new Color(0, 153, 204));
        txtTongTien.setBackground(new Color(245, 245, 245));

        pnTongTien.add(lblTongTien, BorderLayout.WEST);
        pnTongTien.add(txtTongTien, BorderLayout.CENTER);

        // Tiền khách đưa
        JPanel pnTienKhachDua = new JPanel(new BorderLayout(10, 0));
        pnTienKhachDua.setBackground(Color.WHITE);

        JLabel lblTienKhachDua = new JLabel("Tiền khách đưa:");
        lblTienKhachDua.setForeground(new Color(51, 51, 51));

        txtTienKhachDua = new JTextField(10);
        txtTienKhachDua.setHorizontalAlignment(JTextField.RIGHT);

        pnTienKhachDua.add(lblTienKhachDua, BorderLayout.WEST);
        pnTienKhachDua.add(txtTienKhachDua, BorderLayout.CENTER);

        // Tiền thối lại
        JPanel pnTienThoi = new JPanel(new BorderLayout(10, 0));
        pnTienThoi.setBackground(Color.WHITE);

        JLabel lblTienThoi = new JLabel("Tiền thối lại:");
        lblTienThoi.setForeground(new Color(51, 51, 51));

        txtTienThoi = new JTextField(10);
        txtTienThoi.setEditable(false);
        txtTienThoi.setHorizontalAlignment(JTextField.RIGHT);
        txtTienThoi.setBackground(new Color(245, 245, 245));

        pnTienThoi.add(lblTienThoi, BorderLayout.WEST);
        pnTienThoi.add(txtTienThoi, BorderLayout.CENTER);

        // Nút thanh toán và in hóa đơn
        JPanel pnButtonsThanhToan = new JPanel(new GridLayout(1, 2, 10, 0));
        pnButtonsThanhToan.setBackground(Color.WHITE);

        btnThanhToan = new JButton("THANH TOÁN");
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThanhToan.setBackground(new Color(46, 204, 113));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setFocusPainted(false);
        btnThanhToan.setBorderPainted(false);

        try {
            btnThanhToan.setIcon(new SVGIcon() {{
                setSvgURI(getClass().getResource("/img/thanhtoan.svg").toURI());
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnInHD = new JButton("IN HÓA ĐƠN");
        btnInHD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnInHD.setBackground(new Color(52, 152, 219));
        btnInHD.setForeground(Color.WHITE);
        btnInHD.setFocusPainted(false);
        btnInHD.setBorderPainted(false);

        try {
            btnInHD.setIcon(new SVGIcon() {{
                setSvgURI(getClass().getResource("/img/in.svg").toURI());
            }});
        } catch (Exception e) {
            e.printStackTrace();
        }

        pnButtonsThanhToan.add(btnThanhToan);
        pnButtonsThanhToan.add(btnInHD);

        // Thêm tất cả vào panel thanh toán
        pnThanhToanContent.add(pnTongCong);
        pnThanhToanContent.add(pnGiamGia);
        pnThanhToanContent.add(pnSuDungDiem);
        pnThanhToanContent.add(pnTongTien);
        pnThanhToanContent.add(pnTienKhachDua);
        pnThanhToanContent.add(pnTienThoi);
        pnThanhToanContent.add(new JLabel()); // Khoảng trống
        pnThanhToanContent.add(pnButtonsThanhToan);

        pnThanhToan.add(pnThanhToanContent, BorderLayout.CENTER);

        // Thêm các panel vào panel phải
        rightPanel.add(pnKhachHang, BorderLayout.NORTH);
        rightPanel.add(pnThanhToan, BorderLayout.CENTER);

        // Thêm panel trái và phải vào panel chính
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        p.add(mainPanel, BorderLayout.CENTER);
        
        // Thiết lập các sự kiện
        setupMaVachComboBox();
        loadKhuyenMai();
        setEnabledInputFields(false);
        addEventListeners();
    }
    
    // Phương thức thiết lập font
    private void setFont(Font labelFont, Font textFieldFont, Font buttonFont) {
        UIManager.put("Label.font", labelFont);
        UIManager.put("TextField.font", textFieldFont);
        UIManager.put("Button.font", buttonFont);
        UIManager.put("ComboBox.font", textFieldFont);
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));
    }

    private void addEventListeners() {
        // Thêm sự kiện cho bảng để cập nhật số lượng khi người dùng sửa trực tiếp
        tb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tb.getSelectedRow();
                if (row != -1) {
                    // Cập nhật số lượng hiển thị trong txtSoLuong
                    txtSoLuong.setText(tb.getValueAt(row, 4).toString());
                    txtSoLuong.setEnabled(true);
                }
            }
        });

        // Thêm cell editor để xử lý khi người dùng sửa số lượng trực tiếp trong bảng
        tb.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 4 && e.getFirstRow() == e.getLastRow()) {
                int row = e.getFirstRow();
                try {
                    // Lấy số lượng mới
                    int soLuongMoi = Integer.parseInt(tb.getValueAt(row, 4).toString());
                    if (soLuongMoi <= 0) {
                        JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        // Khôi phục giá trị cũ
                        model.setValueAt(1, row, 4);
                        return;
                    }

                    // Lấy thông tin sản phẩm
                    String maSP = tb.getValueAt(row, 2).toString();
                    SanPham sp = sanPhamDAO.getSanPhamTheoMa(maSP);

                    if (sp != null) {
                        // Kiểm tra số lượng tồn kho
                        if (sp.getSoLuongHienCo() < soLuongMoi) {
                            JOptionPane.showMessageDialog(this,
                                    "Số lượng sản phẩm trong kho không đủ! Hiện có: " + sp.getSoLuongHienCo(), "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                            // Khôi phục giá trị cũ
                            model.setValueAt(1, row, 4);
                            return;
                        }

                        // Tính lại thành tiền
                        double donGia = sp.getGiaBan();
                        double thanhTien = donGia * soLuongMoi;
                        model.setValueAt(formatCurrency(thanhTien), row, 6);

                        // Cập nhật chi tiết hóa đơn trong database
                        if (daCoHoaDon) {
                            ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                            HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHoaDonHienTai);
                            chiTiet.setHoaDon(hoaDon);
                            chiTiet.setSanPham(sp);
                            chiTiet.setSoLuong(soLuongMoi);
                            chiTiet.setDonGia(donGia);

                            // Xóa chi tiết cũ và thêm chi tiết mới
                            chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maSP);
                            chiTietHoaDonDAO.themChiTietHoaDon(chiTiet);

                            // Cập nhật tổng tiền
                            tinhTongCong();
                            updateTongTien();
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    // Khôi phục giá trị cũ
                    model.setValueAt(1, row, 4);
                }
            }
        });

        // Thêm sự kiện cho combobox mã vạch để thêm sản phẩm khi chọn
        cbbMaVach.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged") && cbbMaVach.getSelectedItem() != null) {
                String selectedItem = cbbMaVach.getSelectedItem().toString().trim();
                if (!selectedItem.isEmpty() && !selectedItem.equals(" ")) {
                    // Tự động thêm sản phẩm khi chọn từ combobox
                    themSanPhamVaoHoaDon();
                }
            }
        });

        // Thêm sự kiện cho txtSoLuong để nhấn Enter thêm sản phẩm
        txtSoLuong.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int row = tb.getSelectedRow();
                    if (row != -1) {
                        try {
                            int soLuong = Integer.parseInt(txtSoLuong.getText());
                            tb.setValueAt(soLuong, row, 4);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(BanHangPanel.this, "Số lượng phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Thêm sự kiện cho JTextField của combobox mã vạch để xử lý khi nhấn Enter
        JTextField textField = (JTextField) cbbMaVach.getEditor().getEditorComponent();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String input = textField.getText().trim();
                    if (BARCODE_PATTERN.matcher(input).matches()) {
                        themSanPhamVaoHoaDon();
                    }
                }
            }
        });

        // Thêm sự kiện cho txtTienKhachDua để tính tiền thối
        txtTienKhachDua.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    String tongTienStr = txtTongTien.getText().replace(".", "").replace(" đ", "").trim();
                    String tienKhachStr = txtTienKhachDua.getText().trim();

                    if (!tienKhachStr.isEmpty() && !tongTienStr.isEmpty()) {
                        double tongTien = Double.parseDouble(tongTienStr);
                        double tienKhach = Double.parseDouble(tienKhachStr);

                        if (tienKhach >= tongTien) {
                            double tienThoi = tienKhach - tongTien;
                            txtTienThoi.setText(formatCurrency(tienThoi));
                        } else {
                            txtTienThoi.setText("0 đ");
                        }
                    }
                } catch (NumberFormatException ex) {
                    txtTienThoi.setText("0 đ");
                }
            }
        });

        // Thêm sự kiện cho các nút
        btnXoa.addActionListener(this);
        btnLamRong.addActionListener(this);
        btnTaoHoaDon.addActionListener(this);
        btnInHD.addActionListener(this);
        btnTimKhachHang.addActionListener(this);
        btnDung.addActionListener(this);
        btnThanhToan.addActionListener(this);
        cbbMaKhuyenMai.addActionListener(this);
    }

    private void setupMaVachComboBox() {
        // Lấy danh sách mã sản phẩm từ CSDL
        List<SanPham> danhSachSP = sanPhamDAO.getAllSanPham();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement(" ");
        for (SanPham sp : danhSachSP) {
            model.addElement(sp.getMaSanPham());
        }

        cbbMaVach.setModel(model);

        // Thêm tính năng tìm kiếm tương đối
        JTextField textField = (JTextField) cbbMaVach.getEditor().getEditorComponent();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = textField.getText().toLowerCase();
                if (!input.isEmpty()) {
                    DefaultComboBoxModel<String> filteredModel = new DefaultComboBoxModel<>();
                    for (SanPham sp : danhSachSP) {
                        if (sp.getMaSanPham().toLowerCase().contains(input)
                                || sp.getTenSanPham().toLowerCase().contains(input)) {
                            filteredModel.addElement(sp.getMaSanPham());
                        }
                    }

                    // Lưu lại item đang được chọn
                    String currentText = textField.getText();

                    cbbMaVach.setModel(filteredModel);

                    // Khôi phục text đang nhập và không chọn item nào
                    textField.setText(currentText);
                    textField.setSelectionStart(currentText.length());
                    textField.setSelectionEnd(currentText.length());

                    cbbMaVach.showPopup();
                }
            }
        });
    }

    private void loadKhuyenMai() {
        List<KhuyenMai> danhSachKM = khuyenMaiDAO.getAllKhuyenMai();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
        model.addElement("Không");// mặc định
        for (KhuyenMai km : danhSachKM) {
            model.addElement(km.getMaKhuyenMai() + " - " + km.getTenKhuyenMai());
        }
        cbbMaKhuyenMai.setModel(model);
//        if(txtSuDungDiem!=null)
//        	toggleDungDiem();
    }

    private void setEnabledInputFields(boolean enabled) {
        cbbMaVach.setEnabled(enabled);
        txtSoLuong.setEnabled(enabled);
        btnXoa.setEnabled(enabled);
        btnLamRong.setEnabled(enabled);
        txtMaKhachHang.setEnabled(enabled);
        btnInHD.setEnabled(enabled);
        btnTimKhachHang.setEnabled(enabled);
        btnThanhToan.setEnabled(enabled);
        cbbMaKhuyenMai.setEnabled(enabled);
        txtTienKhachDua.setEnabled(enabled);
    }

    // Phương thức để tạo hóa đơn mới
    private void taoHoaDonMoi() {
        maHoaDonHienTai = hoaDonDAO.taoMaHoaDonMoi();
        NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(maNhanVienHienTai);
        String tenNhanVien = nhanVien != null ? nhanVien.getTenNhanVien() : tenNhanVienHienTai;

        // Lấy ngày giờ hiện tại
        Date ngayHienTai = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String ngayGioHienTai = dateFormat.format(ngayHienTai);

        // Hiển thị thông tin hóa đơn
        txtMaHoaDon.setText(maHoaDonHienTai);
        txtNhanVien.setText(tenNhanVien);
        txtNgayTao.setText(ngayGioHienTai);

        // Lưu hóa đơn vào database
        HoaDon hoaDon = new HoaDon(maHoaDonHienTai, ngayHienTai, nhanVien);
        boolean success = hoaDonDAO.themHoaDon(hoaDon);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo hóa đơn mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setEnabledInputFields(true);
        // Đánh dấu đã có hóa đơn
        daCoHoaDon = true;
        // Làm mới bảng
        model.setRowCount(0);
        tongCong = 0;
        updateTongTien();

        // Reset thông tin khách hàng
        khachHangHienTai = null;
        txtMaKhachHang.setText("");
        txtTenKhachHang.setText("");
        txtSoDienThoai.setText("");
        txtDiem.setText("");
        txtSuDungDiem.setText("");
        isDungDiem = false;
        btnDung.setText("Dùng");
        btnDung.setEnabled(false);

        // Đặt mặc định cho khuyến mãi là "Không"
        cbbMaKhuyenMai.setSelectedIndex(0);

        // Đặt mặc định số lượng là 1
        txtSoLuong.setText("1");
        txtSoLuong.setEnabled(false);
        // Focus vào ô mã vạch
        cbbMaVach.requestFocus();
    }

    private void themSanPhamVaoHoaDon() {
        if (!daCoHoaDon) {
            JOptionPane.showMessageDialog(this, "Vui lòng tạo hóa đơn trước khi thêm sản phẩm!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maVach = cbbMaVach.getSelectedItem() != null ? cbbMaVach.getSelectedItem().toString().trim() : "";
        String soLuongStr = txtSoLuong.getText().trim();

        if (maVach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập mã vạch!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (soLuongStr.isEmpty()) {
            soLuongStr = "1";
            txtSoLuong.setText("1");
        }

        // Kiểm tra số lượng là số nguyên dương
        if (!NUMBER_PATTERN.matcher(soLuongStr).matches()) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int soLuong = Integer.parseInt(soLuongStr);
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy thông tin sản phẩm từ database
            SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(maVach);
            if (sanPham == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với mã vạch: " + maVach, "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra số lượng tồn kho
            if (sanPham.getSoLuongHienCo() < soLuong) {
                JOptionPane.showMessageDialog(this,
                        "Số lượng sản phẩm trong kho không đủ! Hiện có: " + sanPham.getSoLuongHienCo(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra xem sản phẩm đã có trong hóa đơn chưa
            boolean daTonTai = false;
            int rowIndex = -1;
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 2).toString().equals(maVach)) {
                    daTonTai = true;
                    rowIndex = i;
                    break;
                }
            }

            // Tính thành tiền
            double giaBan = sanPham.getGiaBan();
            double thanhTien = giaBan * soLuong;

            if (daTonTai) {
                // Cập nhật số lượng và thành tiền
                int soLuongCu = Integer.parseInt(model.getValueAt(rowIndex, 4).toString());
                int soLuongMoi = soLuongCu + soLuong;
                double thanhTienMoi = giaBan * soLuongMoi;

                // Kiểm tra lại số lượng tồn kho
                if (sanPham.getSoLuongHienCo() < soLuong) {
                    JOptionPane.showMessageDialog(this,
                            "Số lượng sản phẩm trong kho không đủ! Hiện có: " + sanPham.getSoLuongHienCo(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                model.setValueAt(soLuongMoi, rowIndex, 4);
                model.setValueAt(formatCurrency(thanhTienMoi), rowIndex, 6);

                // Cập nhật chi tiết hóa đơn trong database
                ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHoaDonHienTai);
                chiTiet.setHoaDon(hoaDon);
                chiTiet.setSanPham(sanPham);
                chiTiet.setSoLuong(soLuongMoi);
                chiTiet.setDonGia(giaBan);

                // Xóa chi tiết cũ và thêm chi tiết mới
                chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maVach);
                chiTietHoaDonDAO.themChiTietHoaDon(chiTiet);

                // Cập nhật tổng tiền
                tongCong = tongCong - (giaBan * soLuongCu) + thanhTienMoi;
            } else {
                // Thêm vào bảng
                int rowCount = model.getRowCount();
                ImageIcon icon = loadImageIcon(sanPham.getUrlHinhAnh());
                model.addRow(new Object[] { rowCount + 1, icon, sanPham.getMaSanPham(), sanPham.getTenSanPham(),
                        soLuong, formatCurrency(giaBan), formatCurrency(thanhTien) });

                // Thêm chi tiết hóa đơn vào database
                ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHoaDonHienTai);
                chiTiet.setHoaDon(hoaDon);
                chiTiet.setSanPham(sanPham);
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(giaBan);

                chiTietHoaDonDAO.themChiTietHoaDon(chiTiet);

                // Cập nhật tổng tiền
                tongCong += thanhTien;
            }

            // Cập nhật số lượng sản phẩm trong kho
            sanPhamDAO.capNhatSoLuongSanPham(sanPham.getMaSanPham(), sanPham.getSoLuongHienCo() - soLuong);

            // Cập nhật tổng tiền
            updateTongTien();

            // Xóa nội dung các trường nhập liệu
            cbbMaVach.setSelectedIndex(-1);
            txtSoLuong.setText("1"); // Reset về mặc định là 1
            cbbMaVach.requestFocus();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức để xóa sản phẩm khỏi hóa đơn
    private void xoaSanPhamKhoiHoaDon() {
        int selectedRow = tb.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maSP = model.getValueAt(selectedRow, 2).toString();
        int soLuong = Integer.parseInt(model.getValueAt(selectedRow, 4).toString());
        double thanhTien = parseVNDCurrency(model.getValueAt(selectedRow, 6).toString());

        // Xóa chi tiết hóa đơn khỏi database
        boolean success = chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maSP);
        if (!success) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm khỏi hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cập nhật lại số lượng sản phẩm trong kho
        SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(maSP);
        if (sanPham != null) {
            sanPhamDAO.capNhatSoLuongSanPham(maSP, sanPham.getSoLuongHienCo() + soLuong);
        }

        // Xóa dòng khỏi bảng
        model.removeRow(selectedRow);

        // Cập nhật lại STT
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }

        // Cập nhật tổng tiền
        tongCong -= thanhTien;
        updateTongTien();
    }

    // Phương thức để làm rỗng hóa đơn
    private void lamRongHoaDon() {
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn đã trống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tất cả sản phẩm khỏi hóa đơn?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Xóa tất cả chi tiết hóa đơn khỏi database
        for (int i = 0; i < model.getRowCount(); i++) {
            String maSP = model.getValueAt(i, 2).toString();
            int soLuong = Integer.parseInt(model.getValueAt(i, 4).toString());

            // Cập nhật lại số lượng sản phẩm trong kho
            SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(maSP);
            if (sanPham != null) {
                sanPhamDAO.capNhatSoLuongSanPham(maSP, sanPham.getSoLuongHienCo() + soLuong);
            }

            chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maSP);
        }

        // Xóa tất cả dòng khỏi bảng
        model.setRowCount(0);

        // Cập nhật tổng tiền
        tongCong = 0;
        updateTongTien();

        // Reset thông tin khách hàng và điểm
        khachHangHienTai = null;
        txtMaKhachHang.setText("");
        txtTenKhachHang.setText("");
        txtSoDienThoai.setText("");
        txtDiem.setText("");
        txtSuDungDiem.setText("");
        isDungDiem = false;
        btnDung.setText("Dùng");
        btnDung.setEnabled(false);

        // Đặt mặc định cho khuyến mãi là "Không"
        cbbMaKhuyenMai.setSelectedIndex(0);
    }

    private void timKhachHang() {
        String maKhachHang = txtMaKhachHang.getText().trim();
        if (maKhachHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tìm khách hàng trong database
        KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(maKhachHang);
        if (khachHang != null) {
            // Hiển thị thông tin khách hàng
            txtTenKhachHang.setText(khachHang.getTenKhachHang());
            txtSoDienThoai.setText(khachHang.getSoDienThoai());
            txtDiem.setText(String.valueOf(khachHang.getSoDiem()));
            khachHangHienTai = khachHang;

            // Reset trạng thái sử dụng điểm
            isDungDiem = false;
            btnDung.setText("Dùng");
            txtSuDungDiem.setText("0 đ");
            btnDung.setEnabled(khachHang.getSoDiem() > 0);
            updateTongTien();
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng với mã: " + maKhachHang, "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            txtTenKhachHang.setText("");
            txtSoDienThoai.setText("");
            txtDiem.setText("");
            khachHangHienTai = null;
            btnDung.setEnabled(false);
        }
    }

    private void toggleDungDiem() {
        int soDiem = khachHangHienTai.getSoDiem();
        if (soDiem <= 0) {
            JOptionPane.showMessageDialog(this, "Khách hàng không có điểm để sử dụng!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!isDungDiem) {
            isDungDiem = true;
            btnDung.setText("Bỏ Dùng");
            double giaTriDiem = soDiem * 1000;
            double tongCongHienTai = parseVNDCurrency(txtTongCong.getText());
            double giamGia = parseVNDCurrency(txtGiamGia.getText());
            double tongTienPhaiTra = tongCongHienTai - giamGia;
            if (giaTriDiem >= tongTienPhaiTra) {
                giaTriDiem = tongTienPhaiTra;
            }
            txtSuDungDiem.setText(formatCurrency(giaTriDiem));
            giaTriDiem = Math.ceil(giaTriDiem / 1000) * 1000;
            int diemSuDung = (int) (giaTriDiem / 1000);

            txtDiem.setText(String.valueOf(soDiem - diemSuDung));
        } else {
            isDungDiem = false;
            btnDung.setText("Dùng");
            txtSuDungDiem.setText("0 đ");
            txtDiem.setText(String.valueOf(soDiem));
        }
        updateTongTien();
    }

    private void tinhTongCong() {
        tongCong = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String thanhTienStr = model.getValueAt(i, 6).toString();
            double thanhTien = parseVNDCurrency(thanhTienStr);
            tongCong += thanhTien;
        }
    }

    // Phương thức để cập nhật tổng tiền
    private void updateTongTien() {
        txtTongCong.setText(formatCurrency(tongCong));

        // Tính giảm giá nếu có mã khuyến mãi
        double giamGia = 0;
        String maKMSelection = cbbMaKhuyenMai.getSelectedItem() != null ? cbbMaKhuyenMai.getSelectedItem().toString()
                : "";

        if (!maKMSelection.isEmpty() && !maKMSelection.equals("Không")) {
            String maKM = maKMSelection.split(" - ")[0];
            KhuyenMai khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(maKM);
            if (khuyenMai != null) {
                giamGia = tongCong * (khuyenMai.getGiaTriKhuyenMai() / 100.0);
                txtGiamGia.setText("-" + formatCurrency(giamGia));
            }
        } else {
            txtGiamGia.setText("0 đ");
        }

        double giamGiaDiem = 0;
        if (isDungDiem && khachHangHienTai != null) {
            String suDungDiemStr = txtSuDungDiem.getText().replace("-", "").replace(".", "").replace(" đ", "").trim();
            if (!suDungDiemStr.isEmpty() && !suDungDiemStr.equals("0")) {
                giamGiaDiem = Double.parseDouble(suDungDiemStr);
            }
            txtSuDungDiem.setText("-" + formatCurrency(giamGiaDiem));
        } else {
            txtSuDungDiem.setText("0 đ");
        }

        // Tổng tiền sau khi giảm giá và sử dụng điểm
        double tongTien = tongCong - giamGia - giamGiaDiem;
        if (tongTien < 0)
            tongTien = 0; // Đảm bảo tổng tiền không âm
        txtTongTien.setText(formatCurrency(tongTien));
    }

    // Phương thức để đặt lại trạng thái sau khi in hóa đơn
    private void resetHoaDon() {
        // Xóa thông tin hóa đơn
        txtMaHoaDon.setText("");
        txtNhanVien.setText("");
        txtNgayTao.setText("");

        // Xóa bảng
        model.setRowCount(0);

        // Xóa các trường nhập liệu
        cbbMaVach.setSelectedIndex(-1);
        txtSoLuong.setText("1");
        txtMaKhachHang.setText("");
        txtTenKhachHang.setText("");
        txtSoDienThoai.setText("");
        txtDiem.setText("");
        cbbMaKhuyenMai.setSelectedIndex(0);
        txtTongCong.setText("");
        txtGiamGia.setText("");
        txtSuDungDiem.setText("");
        txtTongTien.setText("");
        txtTienKhachDua.setText("");
        txtTienThoi.setText("");

        // Vô hiệu hóa các trường nhập liệu
        setEnabledInputFields(false);

        // Đặt lại trạng thái
        daCoHoaDon = false;
        tongCong = 0;
        khachHangHienTai = null;
        isDungDiem = false;
        btnDung.setText("Dùng");
        btnDung.setEnabled(false);
    }

    // Phương thức thanh toán
    public void thanhToan() {
        if (!daCoHoaDon || model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm nào trong hóa đơn!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String tongTienStr = txtTongTien.getText().replace(".", "").replace(" đ", "").trim();
            String tienKhachStr = txtTienKhachDua.getText().trim();

            if (tienKhachStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách đưa!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                txtTienKhachDua.requestFocus();
                return;
            }

            // Kiểm tra tiền khách đưa là số
            if (!NUMBER_PATTERN.matcher(tienKhachStr).matches()) {
                JOptionPane.showMessageDialog(this, "Tiền khách đưa phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtTienKhachDua.requestFocus();
                return;
            }

            double tongTien = Double.parseDouble(tongTienStr);
            double tienKhach = Double.parseDouble(tienKhachStr);

            if (tienKhach < tongTien) {
                JOptionPane.showMessageDialog(this, "Số tiền khách đưa không đủ!", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                txtTienKhachDua.requestFocus();
                return;
            }

            double tienThoi = tienKhach - tongTien;
            txtTienThoi.setText(formatCurrency(tienThoi));

            // Cập nhật thông tin hóa đơn
            HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHoaDonHienTai);
            if (hoaDon != null) {
                // Cập nhật khách hàng nếu có
                if (khachHangHienTai != null) {
                    hoaDon.setKhachHang(khachHangHienTai);

                    // Cập nhật điểm khách hàng nếu sử dụng điểm
                    int diemHienTai = khachHangHienTai.getSoDiem();
                    int diemSuDung = 0;
                    
                    if (isDungDiem) {
                        String suDungDiemStr = txtSuDungDiem.getText().replace("-", "").replace(".", "").replace(" đ", "").trim();
                        if (!suDungDiemStr.isEmpty() && !suDungDiemStr.equals("0")) {
                            double giaTriDiem = Double.parseDouble(suDungDiemStr);
                            diemSuDung = (int) Math.ceil(giaTriDiem / 1000);
                        }
                    }
                    
                    // Tính điểm tích lũy mới: điểm ban đầu - điểm sử dụng + điểm mới (1% tổng tiền, làm tròn xuống)
                    int diemMoi = (int) Math.floor(tongTien / 100000);
                    int diemTichLuyMoi = diemHienTai - diemSuDung + diemMoi;
                    
                    khachHangHienTai.setSoDiem(diemTichLuyMoi);
                    khachHangDAO.capNhatKhachHang(khachHangHienTai);
                }

                // Cập nhật khuyến mãi nếu có
                String maKMSelection = cbbMaKhuyenMai.getSelectedItem() != null
                        ? cbbMaKhuyenMai.getSelectedItem().toString()
                        : "";
                if (!maKMSelection.isEmpty() && !maKMSelection.equals("Không")) {
                    String maKM = maKMSelection.split(" - ")[0];
                    KhuyenMai khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(maKM);
                    if (khuyenMai != null) {
                        hoaDon.setKhuyenMai(khuyenMai);
                    }
                }

                // Cập nhật tổng tiền
                hoaDon.setDanhSachChiTiet(chiTietHoaDonDAO.getChiTietHoaDonTheoMaHD(maHoaDonHienTai));
                hoaDonDAO.capNhatHoaDon(hoaDon);
            }

            JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            // Hỏi người dùng có muốn in hóa đơn không
            int option = JOptionPane.showConfirmDialog(this, "Bạn có muốn in hóa đơn không?", "In hóa đơn",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                inHoaDon();
            } else {
                resetHoaDon();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTienKhachDua.requestFocus();
        }
    }

    // Phương thức in hóa đơn
    private void inHoaDon() {
        if (!daCoHoaDon || maHoaDonHienTai == null || maHoaDonHienTai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có hóa đơn để in!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn không có sản phẩm nào!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Đường dẫn thư mục hiện tại
            String thuMucLuu = System.getProperty("user.dir") + "/hoadon/";
            File folder = new File(thuMucLuu);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File fileToSave = new File(thuMucLuu + maHoaDonHienTai + ".pdf");

            String giamGiaStr = txtGiamGia.getText().replace("-", "").replace(".", "").replace(" đ", "").trim();
            String tongTienStr = txtTongTien.getText().replace(".", "").replace(" đ", "").trim();
            String tienKhachDuaStr = txtTienKhachDua.getText().trim();
            String tienThoiStr = txtTienThoi.getText().replace(".", "").replace(" đ", "").trim();

            double giamGia = giamGiaStr.isEmpty() ? 0 : Double.parseDouble(giamGiaStr);
            double tongTien = tongTienStr.isEmpty() ? 0 : Double.parseDouble(tongTienStr);
            double tienKhachDua = tienKhachDuaStr.isEmpty() ? 0 : Double.parseDouble(tienKhachDuaStr);
            double tienThoi = tienThoiStr.isEmpty() ? 0 : Double.parseDouble(tienThoiStr);

            FormHoaDon formHoaDon = new FormHoaDon(maHoaDonHienTai);
            boolean thanhCong = formHoaDon.xuatHoaDonPDF(fileToSave.getAbsolutePath(), giamGia, tongTien, tienKhachDua, tienThoi);

            if (thanhCong) {
                JOptionPane.showMessageDialog(this,
                        "In hóa đơn thành công!\nĐã lưu tại: " + fileToSave.getAbsolutePath(), "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                resetHoaDon();
            } else {
                JOptionPane.showMessageDialog(this, "In hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btnTaoHoaDon) {
            taoHoaDonMoi();
        } else if (o == btnXoa) {
            xoaSanPhamKhoiHoaDon();
        } else if (o == btnLamRong) {
            lamRongHoaDon();
        } else if (o == btnInHD) {
            inHoaDon();
        } else if (o == btnTimKhachHang) {
            timKhachHang();
        } else if (o == btnDung) {
            toggleDungDiem();
        } else if (o == cbbMaKhuyenMai) {
            updateTongTien();
        } else if (o == btnThanhToan) {
            thanhToan();
        }
    }

    private ImageIcon loadImageIcon(String urlHinhAnh) {
        ImageIcon icon = null;
        if (urlHinhAnh != null && !urlHinhAnh.isEmpty()) {
            String mappedUrl = "/img/" + urlHinhAnh;
            try {
                URL imageUrl = getClass().getResource(mappedUrl);
                if (imageUrl != null) {
                    icon = new ImageIcon(imageUrl);
                    Image image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(image);
                } else {
                    System.out.println("Đường dẫn hình ảnh: " + mappedUrl + ", URL: " + imageUrl);
                }
            } catch (Exception ex) {
                System.err.println("Lỗi tải hình ảnh " + mappedUrl + ": " + ex.getMessage());
            }
        }

        return icon;
    }
    
    // Phương thức định dạng tiền tệ
    private String formatCurrency(double amount) {
        return currencyFormat.format(amount) + " đ";
    }
    
    // Phương thức chuyển đổi chuỗi tiền tệ thành số
    private double parseVNDCurrency(String currencyStr) {
        return Double.parseDouble(currencyStr.replace(".", "").replace(" đ", "").trim());
    }
}
