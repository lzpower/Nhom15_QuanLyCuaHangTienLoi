package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import connectDB.ConnectDB;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
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
import entity.HoaDon;

public class ThongKeDoanhThuPanel extends JPanel {

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
    
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 13); // Font cho JButton

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
        JLabel lblLoaiLoc = new JLabel("Lọc theo:");
        String[] loaiLoc = {"Ngày", "Tháng"};
        cbxLoaiLoc = new JComboBox<>(loaiLoc);
        cbxLoaiLoc.setPreferredSize(new Dimension(100, 30));

        JLabel lblTuNgay = new JLabel("Từ ngày (yyyy-MM-dd):");
        txtTuNgay = new JTextField(10);
        txtTuNgay.setPreferredSize(new Dimension(120, 30));
        txtTuNgay.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                String text = txtTuNgay.getText() + evt.getKeyChar();
                if (!DATE_PATTERN.matcher(text).matches() && text.length() <= 10) {
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
                String text = txtDenNgay.getText() + evt.getKeyChar();
                if (!DATE_PATTERN.matcher(text).matches() && text.length() <= 10) {
                    evt.consume();
                }
            }
        });

        JButton btnLoc = new JButton("Lọc");
        btnLoc.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLoc.setPreferredSize(new Dimension(80, 30));
        btnLoc.setBackground(new Color(46, 204, 113));
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
        btnLamMoi.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLamMoi.setPreferredSize(new Dimension(100, 30));
        btnLamMoi.setBackground(new Color(46, 204, 113));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setBorderPainted(false);
        JButton btnHomNay = new JButton("Hôm nay");
        btnHomNay.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnHomNay.setPreferredSize(new Dimension(100, 30));
        btnHomNay.setBackground(new Color(0, 120, 215));
        btnHomNay.setForeground(Color.WHITE);
        btnHomNay.setFocusPainted(false);
        btnHomNay.setBorderPainted(false);
        JButton btnThangNay = new JButton("Tháng này");
        btnThangNay.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnThangNay.setPreferredSize(new Dimension(120, 30));
        btnThangNay.setBackground(new Color(0, 120, 215));
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
        lblTongDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 12));
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

        JScrollPane scrollPaneBanChay = new JScrollPane(bangSanPhamBanChay);
        pSanPhamBanChay.add(scrollPaneBanChay, BorderLayout.CENTER);

        JPanel pFilterBanChay = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pFilterBanChay.setBackground(Color.WHITE);
        JLabel lblLoaiSanPhamBanChay = new JLabel("Loại sản phẩm:");
        JComboBox<String> cbxLoaiSanPhamBanChay = new JComboBox<>();
        cbxLoaiSanPhamBanChay.addItem("Tất cả");
        try {
            String sql = "SELECT tenLoaiSanPham FROM LoaiSanPham";
            PreparedStatement ps = ConnectDB.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cbxLoaiSanPhamBanChay.addItem(rs.getString("tenLoaiSanPham"));
            }
            cbxLoaiSanPhamBanChay.setSelectedItem("Tất cả");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải loại sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        cbxLoaiSanPhamBanChay.setPreferredSize(new Dimension(150, 30));

        JButton btnLocBanChayHomNay = new JButton("Hôm nay");
        btnLocBanChayHomNay.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLocBanChayHomNay.setPreferredSize(new Dimension(100, 30));
        btnLocBanChayHomNay.setBackground(new Color(0, 120, 215));
        btnLocBanChayHomNay.setForeground(Color.WHITE);
        btnLocBanChayHomNay.setFocusPainted(false);
        btnLocBanChayHomNay.setBorderPainted(false);
        JButton btnLocBanChayThang = new JButton("Tháng này");
        btnLocBanChayThang.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLocBanChayThang.setPreferredSize(new Dimension(120, 30));
        btnLocBanChayThang.setBackground(new Color(0, 120, 215));
        btnLocBanChayThang.setForeground(Color.WHITE);
        btnLocBanChayThang.setFocusPainted(false);
        btnLocBanChayThang.setBorderPainted(false);
        JButton btnLocBanChayTatCa = new JButton("Tất cả");
        btnLocBanChayTatCa.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLocBanChayTatCa.setPreferredSize(new Dimension(100, 30));
        btnLocBanChayTatCa.setBackground(new Color(0, 120, 215));
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

        JScrollPane scrollPaneKhoBan = new JScrollPane(bangSanPhamKhoBan);
        pSanPhamKhoBan.add(scrollPaneKhoBan, BorderLayout.CENTER);

        JPanel pFilterKhoBan = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pFilterKhoBan.setBackground(Color.WHITE);
        JLabel lblLoaiSanPhamKhoBan = new JLabel("Loại sản phẩm:");
        JComboBox<String> cbxLoaiSanPhamKhoBan = new JComboBox<>();
        cbxLoaiSanPhamKhoBan.addItem("Tất cả");
        try {
            String sql = "SELECT tenLoaiSanPham FROM LoaiSanPham";
            PreparedStatement ps = ConnectDB.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cbxLoaiSanPhamKhoBan.addItem(rs.getString("tenLoaiSanPham"));
            }
            cbxLoaiSanPhamKhoBan.setSelectedItem("Tất cả");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải loại sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        cbxLoaiSanPhamKhoBan.setPreferredSize(new Dimension(150, 30));

        JButton btnLocKhoBanHomNay = new JButton("Hôm nay");
        btnLocKhoBanHomNay.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLocKhoBanHomNay.setPreferredSize(new Dimension(100, 30));
        btnLocKhoBanHomNay.setBackground(new Color(0, 120, 215));
        btnLocKhoBanHomNay.setForeground(Color.WHITE);
        btnLocKhoBanHomNay.setFocusPainted(false);
        btnLocKhoBanHomNay.setBorderPainted(false);
        JButton btnLocKhoBanThang = new JButton("Tháng này");
        btnLocKhoBanThang.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLocKhoBanThang.setPreferredSize(new Dimension(120, 30));
        btnLocKhoBanThang.setBackground(new Color(0, 120, 215));
        btnLocKhoBanThang.setForeground(Color.WHITE);
        btnLocKhoBanThang.setFocusPainted(false);
        btnLocKhoBanThang.setBorderPainted(false);
        JButton btnLocKhoBanTatCa = new JButton("Tất cả");
        btnLocKhoBanTatCa.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLocKhoBanTatCa.setPreferredSize(new Dimension(100, 30));
        btnLocKhoBanTatCa.setBackground(new Color(0, 120, 215));
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

            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
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
                    label.setText(value + (loaiSanPhamDuocChon.equals("Tất cả") ? " ▼" : ""));
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

        String sql = "SELECT tenLoaiSanPham FROM LoaiSanPham";
        PreparedStatement ps = ConnectDB.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String loai = rs.getString("tenLoaiSanPham");
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

        String sql = "SELECT hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham, " +
                "SUM(cthd.soLuong) AS soLuong, " +
                "SUM(cthd.soLuong * cthd.donGia) AS doanhThu " +
                "FROM HoaDon hd " +
                "JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon " +
                "JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
                "JOIN LoaiSanPham lsp ON sp.maLoaiSanPham = lsp.maLoaiSanPham " +
                "GROUP BY hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham " +
                "ORDER BY hd.ngayLap DESC";

        PreparedStatement ps = ConnectDB.getInstance().getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        double tongDoanhThu = 0;
        while (rs.next()) {
            String ngay = rs.getString("ngayLap");
            String tenSP = rs.getString("tenSanPham");
            String loaiSP = rs.getString("tenLoaiSanPham");
            int soLuong = rs.getInt("soLuong");
            double doanhThu = rs.getDouble("doanhThu");

            Object[] row = new Object[]{ngay, tenSP, loaiSP, soLuong, DFORMAT.format((long)doanhThu)};
            modelBangThongKe.addRow(row);
            duLieuGoc.add(row);
            tongDoanhThu += doanhThu;
        }

        lblTongDoanhThu.setText("Tổng doanh thu: " + DFORMAT.format((long)tongDoanhThu));
    }

    private void taiSanPhamBanChay(String thoiGian, String loaiSanPham) throws SQLException {
        modelSanPhamBanChay.setRowCount(0);

        StringBuilder sql = new StringBuilder(
            "SELECT sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham, " +
            "SUM(cthd.soLuong) AS tongSoLuong, " +
            "SUM(cthd.soLuong * cthd.donGia) AS tongDoanhThu " +
            "FROM HoaDon hd " +
            "JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon " +
            "JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
            "JOIN LoaiSanPham lsp ON sp.maLoaiSanPham = lsp.maLoaiSanPham "
        );

        List<String> conditions = new ArrayList<>();
        if (thoiGian.equals("day")) {
            conditions.add("CONVERT(date, hd.ngayLap) = CONVERT(date, GETDATE())");
        } else if (thoiGian.equals("month")) {
            conditions.add("MONTH(hd.ngayLap) = MONTH(GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE())");
        }

        if (!loaiSanPham.equals("Tất cả")) {
            conditions.add("lsp.tenLoaiSanPham = ?");
        }

        if (!conditions.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", conditions));
        }

        sql.append(" GROUP BY sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham " +
                  " ORDER BY tongSoLuong DESC");

        PreparedStatement ps = ConnectDB.getInstance().getConnection().prepareStatement(sql.toString());
        if (!loaiSanPham.equals("Tất cả")) {
            ps.setString(1, loaiSanPham);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String maSP = rs.getString("maSanPham");
            String tenSP = rs.getString("tenSanPham");
            String loaiSP = rs.getString("tenLoaiSanPham");
            int tongSoLuong = rs.getInt("tongSoLuong");
            double tongDoanhThu = rs.getDouble("tongDoanhThu");

            modelSanPhamBanChay.addRow(new Object[]{
                maSP, tenSP, loaiSP, tongSoLuong, DFORMAT.format((long)tongDoanhThu)
            });
        }
    }

    private void taiSanPhamKhoBan(String thoiGian, String loaiSanPham) throws SQLException {
        modelSanPhamKhoBan.setRowCount(0);

        StringBuilder sql = new StringBuilder(
            "SELECT sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham, " +
            "ISNULL(SUM(cthd.soLuong), 0) AS tongSoLuong, " +
            "ISNULL(SUM(cthd.soLuong * cthd.donGia), 0) AS tongDoanhThu " +
            "FROM SanPham sp " +
            "JOIN LoaiSanPham lsp ON sp.maLoaiSanPham = lsp.maLoaiSanPham " +
            "LEFT JOIN ChiTietHoaDon cthd ON sp.maSanPham = cthd.maSanPham " +
            "LEFT JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon "
        );

        List<String> conditions = new ArrayList<>();
        if (thoiGian.equals("day")) {
            conditions.add("CONVERT(date, hd.ngayLap) = CONVERT(date, GETDATE())");
        } else if (thoiGian.equals("month")) {
            conditions.add("MONTH(hd.ngayLap) = MONTH(GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE())");
        }

        if (!loaiSanPham.equals("Tất cả")) {
            conditions.add("lsp.tenLoaiSanPham = ?");
        }

        if (!conditions.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", conditions));
        }

        sql.append(" GROUP BY sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham " +
                  " ORDER BY tongSoLuong ASC");

        PreparedStatement ps = ConnectDB.getInstance().getConnection().prepareStatement(sql.toString());
        if (!loaiSanPham.equals("Tất cả")) {
            ps.setString(1, loaiSanPham);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String maSP = rs.getString("maSanPham");
            String tenSP = rs.getString("tenSanPham");
            String loaiSP = rs.getString("tenLoaiSanPham");
            int tongSoLuong = rs.getInt("tongSoLuong");
            double tongDoanhThu = rs.getDouble("tongDoanhThu");

            modelSanPhamKhoBan.addRow(new Object[]{
                maSP, tenSP, loaiSP, tongSoLuong, DFORMAT.format((long)tongDoanhThu)
            });
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            if (!tuNgay.isEmpty() && !DATE_PATTERN.matcher(tuNgay).matches()) {
                throw new ParseException("Invalid date format for 'Từ ngày'", 0);
            }
            if (!denNgay.isEmpty() && !DATE_PATTERN.matcher(denNgay).matches()) {
                throw new ParseException("Invalid date format for 'Đến ngày'", 0);
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

            if (denNgayDate != null && denNgayDate.after(currentDate)) {
                JOptionPane.showMessageDialog(this, "'Đến ngày' không được sau ngày hiện tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! Vui lòng nhập ngày theo định dạng yyyy-MM-dd và đảm bảo ngày tháng hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modelBangThongKe.setRowCount(0);

        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham, " +
            "SUM(cthd.soLuong) AS soLuong, " +
            "SUM(cthd.soLuong * cthd.donGia) AS doanhThu " +
            "FROM HoaDon hd " +
            "JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon " +
            "JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
            "JOIN LoaiSanPham lsp ON sp.maLoaiSanPham = lsp.maLoaiSanPham "
        );

        List<String> conditions = new ArrayList<>();
        if (!tuNgay.isEmpty() && !denNgay.isEmpty()) {
            if (loaiLoc.equals("Ngày")) {
                conditions.add("hd.ngayLap BETWEEN ? AND ?");
            } else {
                conditions.add("YEAR(hd.ngayLap) * 100 + MONTH(hd.ngayLap) BETWEEN YEAR(?) * 100 + MONTH(?) AND YEAR(?) * 100 + MONTH(?)");
            }
        }

        if (!loaiSanPhamDuocChon.equals("Tất cả")) {
            conditions.add("lsp.tenLoaiSanPham = ?");
        }

        if (!conditions.isEmpty()) {
            sqlBuilder.append("WHERE ").append(String.join(" AND ", conditions));
        }

        sqlBuilder.append(" GROUP BY hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham");

        if (trangThaiSapXep.equals("Không sắp xếp")) {
            sqlBuilder.append(" ORDER BY hd.ngayLap DESC");
        } else {
            sqlBuilder.append(" ORDER BY doanhThu ").append(trangThaiSapXep.equals("Tăng dần") ? "ASC" : "DESC");
        }

        PreparedStatement ps = ConnectDB.getInstance().getConnection().prepareStatement(sqlBuilder.toString());

        int paramIndex = 1;
        if (!tuNgay.isEmpty() && !denNgay.isEmpty()) {
            if (loaiLoc.equals("Ngày")) {
                ps.setDate(paramIndex++, java.sql.Date.valueOf(tuNgay));
                ps.setDate(paramIndex++, java.sql.Date.valueOf(denNgay));
            } else {
                ps.setDate(paramIndex++, java.sql.Date.valueOf(tuNgay));
                ps.setDate(paramIndex++, java.sql.Date.valueOf(tuNgay));
                ps.setDate(paramIndex++, java.sql.Date.valueOf(denNgay));
                ps.setDate(paramIndex++, java.sql.Date.valueOf(denNgay));
            }
        }

        if (!loaiSanPhamDuocChon.equals("Tất cả")) {
            ps.setString(paramIndex++, loaiSanPhamDuocChon);
        }

        ResultSet rs = ps.executeQuery();

        double tongDoanhThu = 0;
        boolean coDuLieu = false;

        while (rs.next()) {
            coDuLieu = true;
            String ngay = rs.getString("ngayLap");
            String tenSP = rs.getString("tenSanPham");
            String loaiSP = rs.getString("tenLoaiSanPham");
            int soLuong = rs.getInt("soLuong");
            double doanhThu = rs.getDouble("doanhThu");

            modelBangThongKe.addRow(new Object[] {ngay, tenSP, loaiSP, soLuong, DFORMAT.format((long)doanhThu)});
            tongDoanhThu += doanhThu;
        }

        if (!coDuLieu) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu cho khoảng thời gian hoặc loại sản phẩm đã chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        lblTongDoanhThu.setText("Tổng doanh thu: " + DFORMAT.format((long)tongDoanhThu));
    }
}