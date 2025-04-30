package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dao.HoaDonDAO;
import entity.HoaDon;

public class ThongKeDoanhThuPanel extends JPanel {

    private DefaultTableModel modelThongKe;
    private DefaultTableModel modelSanPhamBanChay;
    private DefaultTableModel modelSanPhamKhoBan;
    private JTable tableThongKe;
    private JTable tableSanPhamBanChay;
    private JTable tableSanPhamKhoBan;
    private JLabel lblTotalRevenueValue;
    private JTextField txtFrom, txtTo;
    private JComboBox<String> cbxFilterType;
    private List<Object[]> originalData;

    private HoaDonDAO hoaDonDAO;
    private Connection conn;
    private String sortState = "Không sắp xếp"; // Trạng thái sắp xếp cột Doanh thu
    private String selectedLoaiSanPham = "Tất cả"; // Loại sản phẩm được chọn

    public ThongKeDoanhThuPanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        originalData = new ArrayList<>();
        hoaDonDAO = new HoaDonDAO(conn);
        createThongKe();
        
        try {
            loadAllData();
            loadBestSellingProducts("all", "Tất cả");
            loadPoorSellingProducts("all", "Tất cả");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createThongKe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tạo tab panel
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab thống kê doanh thu
        JPanel pDoanhThu = new JPanel(new BorderLayout());
        
        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setBorder(BorderFactory.createTitledBorder("Thống kê doanh thu"));

        JPanel pFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFilterType = new JLabel("Lọc theo:");
        String[] filterTypes = {"Ngày", "Tháng"};
        cbxFilterType = new JComboBox<>(filterTypes);
        
        JLabel lblFrom = new JLabel("Từ ngày (yyyy-MM-dd):");
        txtFrom = new JTextField(10);
        JLabel lblTo = new JLabel("Đến ngày (yyyy-MM-dd):");
        txtTo = new JTextField(10);
        
        JButton btnFilter = new JButton("Lọc");
        
        pFilter.add(lblFilterType);
        pFilter.add(cbxFilterType);
        pFilter.add(lblFrom);
        pFilter.add(txtFrom);
        pFilter.add(lblTo);
        pFilter.add(txtTo);
        pFilter.add(btnFilter);
        pTop.add(pFilter, BorderLayout.WEST);

        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnToday = new JButton("Hôm nay");
        JButton btnThisMonth = new JButton("Tháng này");
        
        pButtons.add(btnToday);
        pButtons.add(btnThisMonth);
        pButtons.add(btnRefresh);
        pTop.add(pButtons, BorderLayout.EAST);

        pDoanhThu.add(pTop, BorderLayout.NORTH);

        String[] cols = {"Ngày", "Tên sản phẩm", "Loại sản phẩm", "Số lượng bán", "Doanh thu"};
        modelThongKe = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableThongKe = new JTable(modelThongKe);
        
        // Tùy chỉnh renderer cho tiêu đề cột để thêm mũi tên
        tableThongKe.getTableHeader().setDefaultRenderer(new HeaderRenderer(tableThongKe.getTableHeader()));
        
        JScrollPane scrollPane = new JScrollPane(tableThongKe);
        pDoanhThu.add(scrollPane, BorderLayout.CENTER);

        // Thêm sự kiện nhấp vào tiêu đề cột
        JTableHeader header = tableThongKe.getTableHeader();
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableThongKe.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableThongKe.convertColumnIndexToModel(viewColumn);
                
                try {
                    if (column == 4) { // Cột "Doanh thu" (index 4)
                        showDoanhThuSortMenu(e.getPoint());
                    } else if (column == 2) { // Cột "Loại sản phẩm" (index 2)
                        showLoaiSanPhamFilter(e.getPoint());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(ThongKeDoanhThuPanel.this, "Lỗi khi lọc/sắp xếp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        lblTotalRevenueValue = new JLabel("Tổng doanh thu: 0 VNĐ");

        JPanel pSort = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSort.add(Box.createHorizontalStrut(50));
        pSort.add(lblTotalRevenueValue);

        pDoanhThu.add(pSort, BorderLayout.SOUTH);
        
        // Tab sản phẩm bán chạy
        JPanel pSanPhamBanChay = new JPanel(new BorderLayout());
        pSanPhamBanChay.setBorder(BorderFactory.createTitledBorder("Sản phẩm bán chạy"));
        
        String[] colsBanChay = {"Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Tổng số lượng bán", "Tổng doanh thu"};
        modelSanPhamBanChay = new DefaultTableModel(colsBanChay, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSanPhamBanChay = new JTable(modelSanPhamBanChay);
        JScrollPane scrollPaneBanChay = new JScrollPane(tableSanPhamBanChay);
        pSanPhamBanChay.add(scrollPaneBanChay, BorderLayout.CENTER);
        
        JPanel pFilterBanChay = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblLoaiSanPhamBanChay = new JLabel("Loại sản phẩm:");
        JComboBox<String> cbxLoaiSanPhamBanChay = new JComboBox<>();
        cbxLoaiSanPhamBanChay.addItem("Tất cả");
        try {
            String sql = "SELECT tenLoaiSanPham FROM LoaiSanPham";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cbxLoaiSanPhamBanChay.addItem(rs.getString("tenLoaiSanPham"));
            }
            cbxLoaiSanPhamBanChay.setSelectedItem("Tất cả");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải loại sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        JButton btnFilterBanChayToday = new JButton("Hôm nay");
        JButton btnFilterBanChayMonth = new JButton("Tháng này");
        JButton btnFilterBanChayAll = new JButton("Tất cả");
        
        pFilterBanChay.add(lblLoaiSanPhamBanChay);
        pFilterBanChay.add(cbxLoaiSanPhamBanChay);
        pFilterBanChay.add(btnFilterBanChayToday);
        pFilterBanChay.add(btnFilterBanChayMonth);
        pFilterBanChay.add(btnFilterBanChayAll);
        pSanPhamBanChay.add(pFilterBanChay, BorderLayout.NORTH);
        
        // Tab sản phẩm khó bán
        JPanel pSanPhamKhoBan = new JPanel(new BorderLayout());
        pSanPhamKhoBan.setBorder(BorderFactory.createTitledBorder("Sản phẩm khó bán"));
        
        String[] colsKhoBan = {"Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Tổng số lượng bán", "Tổng doanh thu"};
        modelSanPhamKhoBan = new DefaultTableModel(colsKhoBan, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSanPhamKhoBan = new JTable(modelSanPhamKhoBan);
        JScrollPane scrollPaneKhoBan = new JScrollPane(tableSanPhamKhoBan);
        pSanPhamKhoBan.add(scrollPaneKhoBan, BorderLayout.CENTER);
        
        JPanel pFilterKhoBan = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblLoaiSanPhamKhoBan = new JLabel("Loại sản phẩm:");
        JComboBox<String> cbxLoaiSanPhamKhoBan = new JComboBox<>();
        cbxLoaiSanPhamKhoBan.addItem("Tất cả");
        try {
            String sql = "SELECT tenLoaiSanPham FROM LoaiSanPham";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cbxLoaiSanPhamKhoBan.addItem(rs.getString("tenLoaiSanPham"));
            }
            cbxLoaiSanPhamKhoBan.setSelectedItem("Tất cả");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải loại sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        JButton btnFilterKhoBanToday = new JButton("Hôm nay");
        JButton btnFilterKhoBanMonth = new JButton("Tháng này");
        JButton btnFilterKhoBanAll = new JButton("Tất cả");
        
        pFilterKhoBan.add(lblLoaiSanPhamKhoBan);
        pFilterKhoBan.add(cbxLoaiSanPhamKhoBan);
        pFilterKhoBan.add(btnFilterKhoBanToday);
        pFilterKhoBan.add(btnFilterKhoBanMonth);
        pFilterKhoBan.add(btnFilterKhoBanAll);
        pSanPhamKhoBan.add(pFilterKhoBan, BorderLayout.NORTH);
        
        // Thêm các tab vào tabbed pane
        tabbedPane.addTab("Doanh thu", pDoanhThu);
        tabbedPane.addTab("Sản phẩm bán chạy", pSanPhamBanChay);
        tabbedPane.addTab("Sản phẩm khó bán", pSanPhamKhoBan);
        
        panel.add(tabbedPane, BorderLayout.CENTER);

        // Thêm sự kiện cho các nút
        btnFilter.addActionListener(e -> {
            try {
                filterByDate();
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc dữ liệu: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRefresh.addActionListener(e -> refreshTable());
        
        btnToday.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            String todayStr = sdf.format(today);
            txtFrom.setText(todayStr);
            txtTo.setText(todayStr);
            try {
                filterByDate();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        btnThisMonth.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            String firstDayOfMonth = sdf.format(cal.getTime());
            
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String lastDayOfMonth = sdf.format(cal.getTime());
            
            txtFrom.setText(firstDayOfMonth);
            txtTo.setText(lastDayOfMonth);
            try {
                filterByDate();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        // Sự kiện cho các nút lọc sản phẩm bán chạy
        btnFilterBanChayToday.addActionListener(e -> {
            try {
                String loaiSanPham = cbxLoaiSanPhamBanChay.getSelectedItem() != null ? cbxLoaiSanPhamBanChay.getSelectedItem().toString() : "Tất cả";
                loadBestSellingProducts("day", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm bán chạy: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFilterBanChayMonth.addActionListener(e -> {
            try {
                String loaiSanPham = cbxLoaiSanPhamBanChay.getSelectedItem() != null ? cbxLoaiSanPhamBanChay.getSelectedItem().toString() : "Tất cả";
                loadBestSellingProducts("month", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm bán chạy: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFilterBanChayAll.addActionListener(e -> {
            try {
                String loaiSanPham = cbxLoaiSanPhamBanChay.getSelectedItem() != null ? cbxLoaiSanPhamBanChay.getSelectedItem().toString() : "Tất cả";
                loadBestSellingProducts("all", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm bán chạy: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Sự kiện cho các nút lọc sản phẩm khó bán
        btnFilterKhoBanToday.addActionListener(e -> {
            try {
                String loaiSanPham = cbxLoaiSanPhamKhoBan.getSelectedItem() != null ? cbxLoaiSanPhamKhoBan.getSelectedItem().toString() : "Tất cả";
                loadPoorSellingProducts("day", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm khó bán: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFilterKhoBanMonth.addActionListener(e -> {
            try {
                String loaiSanPham = cbxLoaiSanPhamKhoBan.getSelectedItem() != null ? cbxLoaiSanPhamKhoBan.getSelectedItem().toString() : "Tất cả";
                loadPoorSellingProducts("month", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm khó bán: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFilterKhoBanAll.addActionListener(e -> {
            try {
                String loaiSanPham = cbxLoaiSanPhamKhoBan.getSelectedItem() != null ? cbxLoaiSanPhamKhoBan.getSelectedItem().toString() : "Tất cả";
                loadPoorSellingProducts("all", loaiSanPham);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc sản phẩm khó bán: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel, BorderLayout.CENTER);
    }

    // Renderer tùy chỉnh cho tiêu đề cột để thêm mũi tên
    private class HeaderRenderer implements TableCellRenderer {
        private final TableCellRenderer delegate;

        public HeaderRenderer(JTableHeader header) {
            this.delegate = header.getDefaultRenderer();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setHorizontalAlignment(SwingConstants.LEFT);
                if (column == 4 || column == 2) { // Cột "Doanh thu" hoặc "Loại sản phẩm"
                    label.setText(value + " ▼"); // Thêm mũi tên xuống
                }
            }
            return c;
        }
    }

    // Hiển thị menu sắp xếp cho cột Doanh thu
    private void showDoanhThuSortMenu(Point point) throws SQLException {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem noSortItem = new JMenuItem("Không sắp xếp");
        JMenuItem ascItem = new JMenuItem("Tăng dần");
        JMenuItem descItem = new JMenuItem("Giảm dần");

        popupMenu.add(noSortItem);
        popupMenu.add(ascItem);
        popupMenu.add(descItem);

        noSortItem.addActionListener(e -> {
            sortState = "Không sắp xếp";
            try {
                filterByDate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi sắp xếp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        ascItem.addActionListener(e -> {
            sortState = "Tăng dần";
            try {
                filterByDate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi sắp xếp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        descItem.addActionListener(e -> {
            sortState = "Giảm dần";
            try {
                filterByDate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi sắp xếp: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        popupMenu.show(tableThongKe.getTableHeader(), point.x, point.y);
    }

    // Hiển thị menu lọc loại sản phẩm
    private void showLoaiSanPhamFilter(Point point) throws SQLException {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem allItem = new JMenuItem("Tất cả");
        popupMenu.add(allItem);
        
        String sql = "SELECT tenLoaiSanPham FROM LoaiSanPham";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String loai = rs.getString("tenLoaiSanPham");
            JMenuItem item = new JMenuItem(loai);
            popupMenu.add(item);
            item.addActionListener(e -> {
                selectedLoaiSanPham = loai;
                try {
                    filterByDate();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lọc loại sản phẩm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        allItem.addActionListener(e -> {
            selectedLoaiSanPham = "Tất cả";
            try {
                filterByDate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lọc loại sản phẩm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        popupMenu.show(tableThongKe.getTableHeader(), point.x, point.y);
    }

    private void loadAllData() throws SQLException {
        modelThongKe.setRowCount(0);
        originalData.clear();

        String sql = "SELECT hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham, " +
                "SUM(cthd.soLuong) AS soLuong, " +
                "SUM(cthd.soLuong * cthd.donGia) AS doanhThu " +
                "FROM HoaDon hd " +
                "JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon " +
                "JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
                "JOIN LoaiSanPham lsp ON sp.maLoaiSanPham = lsp.maLoaiSanPham " +
                "GROUP BY hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham " +
                "ORDER BY hd.ngayLap DESC";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        double totalRevenue = 0;
        while (rs.next()) {
            String ngay = rs.getString("ngayLap");
            String tenSP = rs.getString("tenSanPham");
            String loaiSP = rs.getString("tenLoaiSanPham");
            int soLuong = rs.getInt("soLuong");
            double doanhThu = rs.getDouble("doanhThu");

            Object[] row = new Object[]{ngay, tenSP, loaiSP, soLuong, String.format("%,.0f VNĐ", doanhThu)};
            modelThongKe.addRow(row);
            originalData.add(row);
            totalRevenue += doanhThu;
        }

        lblTotalRevenueValue.setText("Tổng doanh thu: " + String.format("%,.0f VNĐ", totalRevenue));
    }

    private void loadBestSellingProducts(String timeFilter, String loaiSanPham) throws SQLException {
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
        if (timeFilter.equals("day")) {
            conditions.add("CONVERT(date, hd.ngayLap) = CONVERT(date, GETDATE())");
        } else if (timeFilter.equals("month")) {
            conditions.add("MONTH(hd.ngayLap) = MONTH(GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE())");
        }
        
        if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
            conditions.add("lsp.tenLoaiSanPham = ?");
        }

        if (!conditions.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", conditions));
        }
        
        sql.append(" GROUP BY sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham " +
                  " ORDER BY tongSoLuong DESC");
        
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
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
                maSP, tenSP, loaiSP, tongSoLuong, String.format("%,.0f VNĐ", tongDoanhThu)
            });
        }
    }

    private void loadPoorSellingProducts(String timeFilter, String loaiSanPham) throws SQLException {
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
        if (timeFilter.equals("day")) {
            conditions.add("CONVERT(date, hd.ngayLap) = CONVERT(date, GETDATE())");
        } else if (timeFilter.equals("month")) {
            conditions.add("MONTH(hd.ngayLap) = MONTH(GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE())");
        }
        
        if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
            conditions.add("lsp.tenLoaiSanPham = ?");
        }

        if (!conditions.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", conditions));
        }
        
        sql.append(" GROUP BY sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham " +
                  " ORDER BY tongSoLuong ASC");
        
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        if (loaiSanPham != null && !loaiSanPham.equals("Tất cả")) {
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
                maSP, tenSP, loaiSP, tongSoLuong, String.format("%,.0f VNĐ", tongDoanhThu)
            });
        }
    }

    private void refreshTable() {
        try {
            loadAllData();
            loadBestSellingProducts("all", "Tất cả");
            loadPoorSellingProducts("all", "Tất cả");
            txtFrom.setText("");
            txtTo.setText("");
            sortState = "Không sắp xếp";
            selectedLoaiSanPham = "Tất cả";
            JOptionPane.showMessageDialog(this, "Dữ liệu đã được làm mới!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi làm mới dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterByDate() throws SQLException {
        String fromDate = txtFrom.getText().trim();
        String toDate = txtTo.getText().trim();
        String filterType = cbxFilterType.getSelectedItem().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.setLenient(false);
            if (!fromDate.isEmpty()) sdf.parse(fromDate);
            if (!toDate.isEmpty()) sdf.parse(toDate);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! Vui lòng nhập ngày theo định dạng yyyy-MM-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modelThongKe.setRowCount(0);

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
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            if (filterType.equals("Ngày")) {
                conditions.add("hd.ngayLap BETWEEN ? AND ?");
            } else {
                conditions.add("YEAR(hd.ngayLap) * 100 + MONTH(hd.ngayLap) BETWEEN YEAR(?) * 100 + MONTH(?) AND YEAR(?) * 100 + MONTH(?)");
            }
        }

        if (selectedLoaiSanPham != null && !selectedLoaiSanPham.equals("Tất cả")) {
            conditions.add("lsp.tenLoaiSanPham = ?");
        }

        if (!conditions.isEmpty()) {
            sqlBuilder.append("WHERE ").append(String.join(" AND ", conditions));
        }

        sqlBuilder.append(" GROUP BY hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham");

        if (sortState.equals("Không sắp xếp")) {
            sqlBuilder.append(" ORDER BY hd.ngayLap DESC");
        } else {
            sqlBuilder.append(" ORDER BY doanhThu ").append(sortState.equals("Tăng dần") ? "ASC" : "DESC");
        }

        PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString());

        int paramIndex = 1;
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            if (filterType.equals("Ngày")) {
                ps.setDate(paramIndex++, java.sql.Date.valueOf(fromDate));
                ps.setDate(paramIndex++, java.sql.Date.valueOf(toDate));
            } else {
                ps.setDate(paramIndex++, java.sql.Date.valueOf(fromDate));
                ps.setDate(paramIndex++, java.sql.Date.valueOf(fromDate));
                ps.setDate(paramIndex++, java.sql.Date.valueOf(toDate));
                ps.setDate(paramIndex++, java.sql.Date.valueOf(toDate));
            }
        }

        if (selectedLoaiSanPham != null && !selectedLoaiSanPham.equals("Tất cả")) {
            ps.setString(paramIndex++, selectedLoaiSanPham);
        }

        ResultSet rs = ps.executeQuery();

        double totalRevenue = 0;
        boolean hasData = false;

        while (rs.next()) {
            hasData = true;
            String ngay = rs.getString("ngayLap");
            String tenSP = rs.getString("tenSanPham");
            String loaiSP = rs.getString("tenLoaiSanPham");
            int soLuong = rs.getInt("soLuong");
            double doanhThu = rs.getDouble("doanhThu");

            modelThongKe.addRow(new Object[] {ngay, tenSP, loaiSP, soLuong, String.format("%,.0f VNĐ", doanhThu)});
            totalRevenue += doanhThu;
        }

        if (!hasData) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu cho khoảng thời gian hoặc loại sản phẩm đã chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        lblTotalRevenueValue.setText("Tổng doanh thu: " + String.format("%,.0f VNĐ", totalRevenue));
    }
}