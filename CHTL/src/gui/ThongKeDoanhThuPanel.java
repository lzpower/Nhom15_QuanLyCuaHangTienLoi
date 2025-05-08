package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import connectDB.ConnectDB;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import dao.HoaDonDAO;

public class ThongKeDoanhThuPanel extends JPanel {

    private static final long serialVersionUID = 838202414562571130L;
	private DefaultTableModel modelBangThongKe;
    private DefaultTableModel modelSanPhamBanChay;
    private DefaultTableModel modelSanPhamKhoBan;
    private JTable bangThongKe;
    private JTable bangSanPhamBanChay;
    private JTable bangSanPhamKhoBan;
    private JLabel lblTongDoanhThu;
    private JTextField txtTuNgay, txtDenNgay;
    private JComboBox<String> cbxLoaiLoc;
    private List<Object[]> duLieuGoc;

    private HoaDonDAO hoaDonDAO;
    private String trangThaiSapXep = "Không sắp xếp";
    private String loaiSanPhamDuocChon = "Tất cả";
    private static final Locale LOCALE_VN = new Locale("vi", "VN");
    private static final DecimalFormat DFORMAT = new DecimalFormat("#,###đ", new java.text.DecimalFormatSymbols(LOCALE_VN));
    private static final Pattern DATE_PATTERN = Pattern.compile("^(200[0-9]|[2-9][0-9]{3})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");

    private String thoiGianLocBanChay = "all";
    private String thoiGianLocKhoBan = "all";
    
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 13);
    private static final Color PRIMARY_BUTTON_COLOR = new Color(0, 120, 215);
    private static final Color SUCCESS_BUTTON_COLOR = new Color(46, 204, 113);

    public ThongKeDoanhThuPanel() {
        setLayout(new BorderLayout());
        duLieuGoc = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            hoaDonDAO = new HoaDonDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        khoiTaoGiaoDien();

        try {
            taiDuLieuTatCa();
            taiSanPhamBanChay("all", "Tất cả");
            taiSanPhamKhoBan("all", "Tất cả");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void khoiTaoGiaoDien() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(0, 102, 204));

        // Tab thống kê doanh thu
        JPanel pDoanhThu = new JPanel(new BorderLayout());
        pDoanhThu.setBackground(Color.WHITE);

        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setBorder(BorderFactory.createTitledBorder("Thống Kê Doanh Thu"));
        pTop.setBackground(Color.WHITE);

        JPanel pFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pFilter.setBackground(Color.WHITE);
        JLabel lblLoaiLoc = new JLabel("Lọc theo tháng:");
        
        String[] cacThang = {"Tất cả", "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", 
                            "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        cbxLoaiLoc = new JComboBox<>(cacThang);
        cbxLoaiLoc.setPreferredSize(new Dimension(100, 30));

        JLabel lblTuNgay = new JLabel("Từ ngày (yyyy-MM-dd):");
        txtTuNgay = new JTextField(10);
        txtTuNgay.setPreferredSize(new Dimension(120, 30));
        
        txtTuNgay.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                
                if (!Character.isDigit(c) && c != '-' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume();
                    return;
                }
                
                String currentText = txtTuNgay.getText();
                if (currentText.length() == 4 || currentText.length() == 7) {
                    if (c != '-') {
                        txtTuNgay.setText(currentText + "-");
                    }
                }
                
                if (currentText.length() >= 10 && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume();
                }
            }
        });

        JLabel lblDenNgay = new JLabel("Đến ngày (yyyy-MM-dd):");
        txtDenNgay = new JTextField(10);
        txtDenNgay.setPreferredSize(new Dimension(120, 30));
        
        txtDenNgay.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                
                if (!Character.isDigit(c) && c != '-' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume();
                    return;
                }
                
                String currentText = txtDenNgay.getText();
                if (currentText.length() == 4 || currentText.length() == 7) {
                    if (c != '-') {
                        txtDenNgay.setText(currentText + "-");
                    }
                }
                
                if (currentText.length() >= 10 && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume();
                }
            }
        });

        JButton btnLoc = new JButton("Lọc");
        btnLoc.setFont(BUTTON_FONT);
        btnLoc.setPreferredSize(new Dimension(80, 30));
        btnLoc.setBackground(SUCCESS_BUTTON_COLOR);
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setFocusPainted(false);
        btnLoc.setBorderPainted(false);

        pFilter.add(lblLoaiLoc);
        pFilter.add(cbxLoaiLoc);
        pFilter.add(lblTuNgay);
        pFilter.add(txtTuNgay);
        pFilter.add(lblDenNgay);
        pFilter.add(txtDenNgay);
        pFilter.add(btnLoc);
        pTop.add(pFilter, BorderLayout.WEST);

        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pButtons.setBackground(Color.WHITE);
        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(BUTTON_FONT);
        btnLamMoi.setPreferredSize(new Dimension(100, 30));
        btnLamMoi.setBackground(SUCCESS_BUTTON_COLOR);
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setBorderPainted(false);
        
        JButton btnHomNay = new JButton("Hôm nay");
        btnHomNay.setFont(BUTTON_FONT);
        btnHomNay.setPreferredSize(new Dimension(100, 30));
        btnHomNay.setBackground(PRIMARY_BUTTON_COLOR);
        btnHomNay.setForeground(Color.WHITE);
        btnHomNay.setFocusPainted(false);
        btnHomNay.setBorderPainted(false);
        
        JButton btnThangNay = new JButton("Tháng này");
        btnThangNay.setFont(BUTTON_FONT);
        btnThangNay.setPreferredSize(new Dimension(120, 30));
        btnThangNay.setBackground(PRIMARY_BUTTON_COLOR);
        btnThangNay.setForeground(Color.WHITE);
        btnThangNay.setFocusPainted(false);
        btnThangNay.setBorderPainted(false);

        pButtons.add(btnHomNay);
        pButtons.add(btnThangNay);
        pButtons.add(btnLamMoi);
        pTop.add(pButtons, BorderLayout.EAST);

        pDoanhThu.add(pTop, BorderLayout.NORTH);

        String[] cot = {"Ngày", "Tên sản phẩm", "Loại sản phẩm", "Số lượng bán", "Doanh thu"};
        modelBangThongKe = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bangThongKe = new JTable(modelBangThongKe);
        bangThongKe.setRowHeight(30);
        bangThongKe.setGridColor(new Color(200, 200, 200));
        bangThongKe.setShowGrid(true);

        bangThongKe.getTableHeader().setDefaultRenderer(new TrinhRenderTieuDe(bangThongKe.getTableHeader()));
        bangThongKe.getTableHeader().setForeground(Color.BLACK);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        bangThongKe.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        bangThongKe.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        TableColumnModel columnModel = bangThongKe.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(bangThongKe);
        pDoanhThu.add(scrollPane, BorderLayout.CENTER);

        JTableHeader header = bangThongKe.getTableHeader();
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = bangThongKe.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = bangThongKe.convertColumnIndexToModel(viewColumn);

                try {
                    if (column == 4) {
                        hienMenuSapXepDoanhThu(e.getPoint());
                    } else if (column == 2) {
                        hienMenuLocLoaiSanPham(e.getPoint());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(ThongKeDoanhThuPanel.this, "Lỗi khi lọc/sắp xếp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        lblTongDoanhThu = new JLabel("Tổng doanh thu: 0 đ");
        lblTongDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTongDoanhThu.setForeground(new Color(204, 0, 0));

        JPanel pSort = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSort.setBackground(Color.WHITE);
        pSort.add(Box.createHorizontalStrut(50));
        pSort.add(lblTongDoanhThu);

        pDoanhThu.add(pSort, BorderLayout.SOUTH);

        // Tab sản phẩm bán chạy
        JPanel pSanPhamBanChay = new JPanel(new BorderLayout());
        pSanPhamBanChay.setBorder(BorderFactory.createTitledBorder("Sản Phẩm Bán Chạy"));
        pSanPhamBanChay.setBackground(Color.WHITE);

        String[] cotBanChay = {"Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Tổng số lượng bán", "Tổng doanh thu"};
        modelSanPhamBanChay = new DefaultTableModel(cotBanChay, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bangSanPhamBanChay = new JTable(modelSanPhamBanChay);
        bangSanPhamBanChay.setRowHeight(30);
        bangSanPhamBanChay.setGridColor(new Color(200, 200, 200));
        bangSanPhamBanChay.setShowGrid(true);
        bangSanPhamBanChay.getTableHeader().setForeground(Color.BLACK);
        
        bangSanPhamBanChay.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        bangSanPhamBanChay.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        JScrollPane scrollPaneBanChay = new JScrollPane(bangSanPhamBanChay);
        pSanPhamBanChay.add(scrollPaneBanChay, BorderLayout.CENTER);

        JPanel pFilterBanChay = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pFilterBanChay.setBackground(Color.WHITE);
        JLabel lblLoaiSanPhamBanChay = new JLabel("Loại sản phẩm:");
        JComboBox<String> cbxLoaiSanPhamBanChay = new JComboBox<>();
        cbxLoaiSanPhamBanChay.addItem("Tất cả");
        try {
            List<String> danhSachLoai = hoaDonDAO.getDanhSachLoaiSanPham();
            for (String loai : danhSachLoai) {
                cbxLoaiSanPhamBanChay.addItem(loai);
            }
            cbxLoaiSanPhamBanChay.setSelectedItem("Tất cả");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải loại sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        cbxLoaiSanPhamBanChay.setPreferredSize(new Dimension(150, 30));

        JButton btnLocBanChayHomNay = new JButton("Hôm nay");
        btnLocBanChayHomNay.setFont(BUTTON_FONT);
        btnLocBanChayHomNay.setPreferredSize(new Dimension(100, 30));
        btnLocBanChayHomNay.setBackground(PRIMARY_BUTTON_COLOR);
        btnLocBanChayHomNay.setForeground(Color.WHITE);
        btnLocBanChayHomNay.setFocusPainted(false);
        btnLocBanChayHomNay.setBorderPainted(false);
        
        JButton btnLocBanChayThang = new JButton("Tháng này");
        btnLocBanChayThang.setFont(BUTTON_FONT);
        btnLocBanChayThang.setPreferredSize(new Dimension(120, 30));
        btnLocBanChayThang.setBackground(PRIMARY_BUTTON_COLOR);
        btnLocBanChayThang.setForeground(Color.WHITE);
        btnLocBanChayThang.setFocusPainted(false);
        btnLocBanChayThang.setBorderPainted(false);
        
        JButton btnLocBanChayTatCa = new JButton("Tất cả");
        btnLocBanChayTatCa.setFont(BUTTON_FONT);
        btnLocBanChayTatCa.setPreferredSize(new Dimension(100, 30));
        btnLocBanChayTatCa.setBackground(PRIMARY_BUTTON_COLOR);
        btnLocBanChayTatCa.setForeground(Color.WHITE);
        btnLocBanChayTatCa.setFocusPainted(false);
        btnLocBanChayTatCa.setBorderPainted(false);

        pFilterBanChay.add(lblLoaiSanPhamBanChay);
        pFilterBanChay.add(cbxLoaiSanPhamBanChay);
        pFilterBanChay.add(btnLocBanChayHomNay);
        pFilterBanChay.add(btnLocBanChayThang);
        pFilterBanChay.add(btnLocBanChayTatCa);
        pSanPhamBanChay.add(pFilterBanChay, BorderLayout.NORTH);

        // Tab sản phẩm khó bán
        JPanel pSanPhamKhoBan = new JPanel(new BorderLayout());
        pSanPhamKhoBan.setBorder(BorderFactory.createTitledBorder("Sản Phẩm Khó Bán"));
        pSanPhamKhoBan.setBackground(Color.WHITE);

        String[] cotKhoBan = {"Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Tổng số lượng bán", "Tổng doanh thu"};
        modelSanPhamKhoBan = new DefaultTableModel(cotKhoBan, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bangSanPhamKhoBan = new JTable(modelSanPhamKhoBan);
        bangSanPhamKhoBan.setRowHeight(30);
        bangSanPhamKhoBan.setGridColor(new Color(200, 200, 200));
        bangSanPhamKhoBan.setShowGrid(true);
        bangSanPhamKhoBan.getTableHeader().setForeground(Color.BLACK);
        
        bangSanPhamKhoBan.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        bangSanPhamKhoBan.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        JScrollPane scrollPaneKhoBan = new JScrollPane(bangSanPhamKhoBan);
        pSanPhamKhoBan.add(scrollPaneKhoBan, BorderLayout.CENTER);

        JPanel pFilterKhoBan = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pFilterKhoBan.setBackground(Color.WHITE);
        JLabel lblLoaiSanPhamKhoBan = new JLabel("Loại sản phẩm:");
        JComboBox<String> cbxLoaiSanPhamKhoBan = new JComboBox<>();
        cbxLoaiSanPhamKhoBan.addItem("Tất cả");
        try {
            List<String> danhSachLoai = hoaDonDAO.getDanhSachLoaiSanPham();
            for (String loai : danhSachLoai) {
                cbxLoaiSanPhamKhoBan.addItem(loai);
            }
            cbxLoaiSanPhamKhoBan.setSelectedItem("Tất cả");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải loại sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        cbxLoaiSanPhamKhoBan.setPreferredSize(new Dimension(150, 30));

        JButton btnLocKhoBanHomNay = new JButton("Hôm nay");
        btnLocKhoBanHomNay.setFont(BUTTON_FONT);
        btnLocKhoBanHomNay.setPreferredSize(new Dimension(100, 30));
        btnLocKhoBanHomNay.setBackground(PRIMARY_BUTTON_COLOR);
        btnLocKhoBanHomNay.setForeground(Color.WHITE);
        btnLocKhoBanHomNay.setFocusPainted(false);
        btnLocKhoBanHomNay.setBorderPainted(false);
        
        JButton btnLocKhoBanThang = new JButton("Tháng này");
        btnLocKhoBanThang.setFont(BUTTON_FONT);
        btnLocKhoBanThang.setPreferredSize(new Dimension(120, 30));
        btnLocKhoBanThang.setBackground(PRIMARY_BUTTON_COLOR);
        btnLocKhoBanThang.setForeground(Color.WHITE);
        btnLocKhoBanThang.setFocusPainted(false);
        btnLocKhoBanThang.setBorderPainted(false);
        
        JButton btnLocKhoBanTatCa = new JButton("Tất cả");
        btnLocKhoBanTatCa.setFont(BUTTON_FONT);
        btnLocKhoBanTatCa.setPreferredSize(new Dimension(100, 30));
        btnLocKhoBanTatCa.setBackground(PRIMARY_BUTTON_COLOR);
        btnLocKhoBanTatCa.setForeground(Color.WHITE);
        btnLocKhoBanTatCa.setFocusPainted(false);
        btnLocKhoBanTatCa.setBorderPainted(false);

        pFilterKhoBan.add(lblLoaiSanPhamKhoBan);
        pFilterKhoBan.add(cbxLoaiSanPhamKhoBan);
        pFilterKhoBan.add(btnLocKhoBanHomNay);
        pFilterKhoBan.add(btnLocKhoBanThang);
        pFilterKhoBan.add(btnLocKhoBanTatCa);
        pSanPhamKhoBan.add(pFilterKhoBan, BorderLayout.NORTH);

        tabbedPane.addTab("Doanh thu", pDoanhThu);
        tabbedPane.addTab("Sản phẩm bán chạy", pSanPhamBanChay);
        tabbedPane.addTab("Sản phẩm khó bán", pSanPhamKhoBan);

        panel.add(tabbedPane, BorderLayout.CENTER);

        cbxLoaiLoc.addActionListener(e -> {
            String selectedMonth = cbxLoaiLoc.getSelectedItem().toString();
            if (!selectedMonth.equals("Tất cả")) {
                int month = Integer.parseInt(selectedMonth.substring(selectedMonth.lastIndexOf(" ") + 1));
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                cal.set(year, month - 1, 1);
                String firstDayOfMonth = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                String lastDayOfMonth = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                
                Calendar currentCal = Calendar.getInstance();
                if (month == currentCal.get(Calendar.MONTH) + 1 && year == currentCal.get(Calendar.YEAR) && 
                    cal.after(currentCal)) {
                    lastDayOfMonth = new SimpleDateFormat("yyyy-MM-dd").format(currentCal.getTime());
                }
                
                txtTuNgay.setText(firstDayOfMonth);
                txtDenNgay.setText(lastDayOfMonth);
                
                try {
                    locTheoNgay();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ThongKeDoanhThuPanel.this, 
                        "Lỗi khi lọc dữ liệu theo tháng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                try {
                    txtTuNgay.setText("");
                    txtDenNgay.setText("");
                    taiDuLieuTatCa();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ThongKeDoanhThuPanel.this, 
                        "Lỗi khi tải tất cả dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnLoc.addActionListener(e -> {
            try {
                locTheoNgay();
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc dữ liệu: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLamMoi.addActionListener(e -> lamMoiBang());

        btnHomNay.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            String todayStr = sdf.format(today);
            txtTuNgay.setText(todayStr);
            txtDenNgay.setText(todayStr);
            try {
                locTheoNgay();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        btnThangNay.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            String firstDayOfMonth = sdf.format(cal.getTime());
            Calendar currentCal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            if (cal.after(currentCal)) {
                cal = currentCal;
            }
            String lastDayOfMonth = sdf.format(cal.getTime());
            txtTuNgay.setText(firstDayOfMonth);
            txtDenNgay.setText(lastDayOfMonth);
            try {
                locTheoNgay();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        cbxLoaiSanPhamBanChay.addActionListener(e -> {
            try {
                String loaiSanPham = cbxLoaiSanPhamBanChay.getSelectedItem() != null ? cbxLoaiSanPhamBanChay.getSelectedItem().toString() : "Tất cả";
                taiSanPhamBanChay(thoiGianLocBanChay, loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm bán chạy: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLocBanChayHomNay.addActionListener(e -> {
            try {
                thoiGianLocBanChay = "day";
                String loaiSanPham = cbxLoaiSanPhamBanChay.getSelectedItem() != null ? cbxLoaiSanPhamBanChay.getSelectedItem().toString() : "Tất cả";
                taiSanPhamBanChay("day", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm bán chạy: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLocBanChayThang.addActionListener(e -> {
            try {
                thoiGianLocBanChay = "month";
                String loaiSanPham = cbxLoaiSanPhamBanChay.getSelectedItem() != null ? cbxLoaiSanPhamBanChay.getSelectedItem().toString() : "Tất cả";
                taiSanPhamBanChay("month", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm bán chạy: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLocBanChayTatCa.addActionListener(e -> {
            try {
                thoiGianLocBanChay = "all";
                String loaiSanPham = cbxLoaiSanPhamBanChay.getSelectedItem() != null ? cbxLoaiSanPhamBanChay.getSelectedItem().toString() : "Tất cả";
                taiSanPhamBanChay("all", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm bán chạy: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        cbxLoaiSanPhamKhoBan.addActionListener(e -> {
            try {
                String loaiSanPham = cbxLoaiSanPhamKhoBan.getSelectedItem() != null ? cbxLoaiSanPhamKhoBan.getSelectedItem().toString() : "Tất cả";
                taiSanPhamKhoBan(thoiGianLocKhoBan, loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm khó bán: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLocKhoBanHomNay.addActionListener(e -> {
            try {
                thoiGianLocKhoBan = "day";
                String loaiSanPham = cbxLoaiSanPhamKhoBan.getSelectedItem() != null ? cbxLoaiSanPhamKhoBan.getSelectedItem().toString() : "Tất cả";
                taiSanPhamKhoBan("day", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm khó bán: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLocKhoBanThang.addActionListener(e -> {
            try {
                thoiGianLocKhoBan = "month";
                String loaiSanPham = cbxLoaiSanPhamKhoBan.getSelectedItem() != null ? cbxLoaiSanPhamKhoBan.getSelectedItem().toString() : "Tất cả";
                taiSanPhamKhoBan("month", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm khó bán: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLocKhoBanTatCa.addActionListener(e -> {
            try {
                thoiGianLocKhoBan = "all";
                String loaiSanPham = cbxLoaiSanPhamKhoBan.getSelectedItem() != null ? cbxLoaiSanPhamKhoBan.getSelectedItem().toString() : "Tất cả";
                taiSanPhamKhoBan("all", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm khó bán: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel, BorderLayout.CENTER);
    }

    private class TrinhRenderTieuDe implements TableCellRenderer {
        private final TableCellRenderer delegate;

        public TrinhRenderTieuDe(JTableHeader header) {
            this.delegate = header.getDefaultRenderer();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setHorizontalAlignment(SwingConstants.LEFT);
                if (column == 4) {
                    String arrow = trangThaiSapXep.equals("Tăng dần") ? " ▲" : trangThaiSapXep.equals("Giảm dần") ? " ▼" : "";
                    label.setText(value + arrow);
                } else if (column == 2) {
                    label.setText(value + (loaiSanPhamDuocChon.equals("Tất cả") ? " ▼" : " (" + loaiSanPhamDuocChon + ")"));
                }
            }
            return c;
        }
    }

    private void hienMenuSapXepDoanhThu(Point point) throws SQLException {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem khongSapXep = new JMenuItem("Không sắp xếp");
        JMenuItem tangDan = new JMenuItem("Tăng dần");
        JMenuItem giamDan = new JMenuItem("Giảm dần");

        popupMenu.add(khongSapXep);
        popupMenu.add(tangDan);
        popupMenu.add(giamDan);

        khongSapXep.addActionListener(e -> {
            trangThaiSapXep = "Không sắp xếp";
            bangThongKe.getTableHeader().repaint();
            try {
                locTheoNgay();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi sắp xếp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        tangDan.addActionListener(e -> {
            trangThaiSapXep = "Tăng dần";
            bangThongKe.getTableHeader().repaint();
            try {
                locTheoNgay();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi sắp xếp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        giamDan.addActionListener(e -> {
            trangThaiSapXep = "Giảm dần";
            bangThongKe.getTableHeader().repaint();
            try {
                locTheoNgay();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi sắp xếp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        popupMenu.show(bangThongKe.getTableHeader(), point.x, point.y);
    }

    private void hienMenuLocLoaiSanPham(Point point) throws SQLException {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem tatCa = new JMenuItem("Tất cả");
        popupMenu.add(tatCa);

        List<String> danhSachLoai = hoaDonDAO.getDanhSachLoaiSanPham();
        for (String loai : danhSachLoai) {
            JMenuItem item = new JMenuItem(loai);
            popupMenu.add(item);
            item.addActionListener(e -> {
                loaiSanPhamDuocChon = loai;
                bangThongKe.getTableHeader().repaint();
                try {
                    locTheoNgay();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lọc loại sản phẩm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        tatCa.addActionListener(e -> {
            loaiSanPhamDuocChon = "Tất cả";
            bangThongKe.getTableHeader().repaint();
            try {
                locTheoNgay();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc loại sản phẩm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        popupMenu.show(bangThongKe.getTableHeader(), point.x, point.y);
    }

    private void taiDuLieuTatCa() throws SQLException {
        modelBangThongKe.setRowCount(0);
        duLieuGoc.clear();

        List<Object[]> ketQua = hoaDonDAO.thongKeDoanhThu(null, null, null, "Không sắp xếp");
        double tongDoanhThu = 0;
        for (Object[] row : ketQua) {
            double doanhThu = (Double) row[4];
            row[4] = DFORMAT.format((long) doanhThu);
            modelBangThongKe.addRow(row);
            duLieuGoc.add(row);
            tongDoanhThu += doanhThu;
        }
        lblTongDoanhThu.setText("Tổng doanh thu: " + DFORMAT.format((long) tongDoanhThu));
    }

    private void taiSanPhamBanChay(String thoiGian, String loaiSanPham) throws SQLException {
        modelSanPhamBanChay.setRowCount(0);
        List<Object[]> ketQua = hoaDonDAO.getSanPhamBanChay(thoiGian, loaiSanPham);
        for (Object[] row : ketQua) {
            double doanhThu = (Double) row[4];
            row[4] = DFORMAT.format((long) doanhThu);
            modelSanPhamBanChay.addRow(row);
        }
    }

    private void taiSanPhamKhoBan(String thoiGian, String loaiSanPham) throws SQLException {
        modelSanPhamKhoBan.setRowCount(0);
        List<Object[]> ketQua = hoaDonDAO.getSanPhamKhoBan(thoiGian, loaiSanPham);
        for (Object[] row : ketQua) {
            double doanhThu = (Double) row[4];
            row[4] = DFORMAT.format((long) doanhThu);
            modelSanPhamKhoBan.addRow(row);
        }
    }

    private void lamMoiBang() {
        try {
            taiDuLieuTatCa();
            taiSanPhamBanChay("all", "Tất cả");
            taiSanPhamKhoBan("all", "Tất cả");
            txtTuNgay.setText("");
            txtDenNgay.setText("");
            trangThaiSapXep = "Không sắp xếp";
            loaiSanPhamDuocChon = "Tất cả";
            cbxLoaiLoc.setSelectedItem("Tất cả");
            bangThongKe.getTableHeader().repaint();
            thoiGianLocBanChay = "all";
            thoiGianLocKhoBan = "all";
            JOptionPane.showMessageDialog(this, "Dữ liệu đã được làm mới!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi làm mới dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void locTheoNgay() throws SQLException {
        String tuNgay = txtTuNgay.getText().trim();
        String denNgay = txtDenNgay.getText().trim();
        String loaiLoc = cbxLoaiLoc.getSelectedItem().toString();
        boolean isFilterByMonth = !loaiLoc.equals("Tất cả");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            if (!tuNgay.isEmpty() && !DATE_PATTERN.matcher(tuNgay).matches()) {
                throw new ParseException("Định dạng ngày không hợp lệ cho 'Từ ngày'", 0);
            }
            if (!denNgay.isEmpty() && !DATE_PATTERN.matcher(denNgay).matches()) {
                throw new ParseException("Định dạng ngày không hợp lệ cho 'Đến ngày'", 0);
            }

            Date tuNgayDate = null;
            Date denNgayDate = null;
            Date currentDate = new Date();

            if (!tuNgay.isEmpty()) {
                tuNgayDate = sdf.parse(tuNgay);
            }
            if (!denNgay.isEmpty()) {
                denNgayDate = sdf.parse(denNgay);
            }

            if (tuNgayDate != null && denNgayDate != null && tuNgayDate.after(denNgayDate)) {
                JOptionPane.showMessageDialog(this, "'Từ ngày' phải nhỏ hơn hoặc bằng 'Đến ngày'.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isFilterByMonth && denNgayDate != null && denNgayDate.after(currentDate)) {
                JOptionPane.showMessageDialog(this, "'Đến ngày' không được sau ngày hiện tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! Vui lòng nhập ngày theo định dạng yyyy-MM-dd và đảm bảo ngày tháng hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modelBangThongKe.setRowCount(0);
        List<Object[]> ketQua = hoaDonDAO.thongKeDoanhThu(tuNgay, denNgay, loaiSanPhamDuocChon, trangThaiSapXep);
        double tongDoanhThu = 0;
        boolean coDuLieu = false;

        for (Object[] row : ketQua) {
            coDuLieu = true;
            double doanhThu = (Double) row[4];
            row[4] = DFORMAT.format((long) doanhThu);
            modelBangThongKe.addRow(row);
            tongDoanhThu += doanhThu;
        }

        if (!coDuLieu) {
            System.out.println("Không tìm thấy dữ liệu với tham số: Từ ngày=" + tuNgay + ", Đến ngày=" + denNgay + 
                              ", Loại lọc=" + loaiLoc + ", Loại sản phẩm=" + loaiSanPhamDuocChon);
            JOptionPane.showMessageDialog(this, "Không có dữ liệu cho khoảng thời gian hoặc loại sản phẩm đã chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            System.out.println("Tìm thấy " + modelBangThongKe.getRowCount() + " kết quả");
        }

        lblTongDoanhThu.setText("Tổng doanh thu: " + DFORMAT.format((long) tongDoanhThu));
    }
}