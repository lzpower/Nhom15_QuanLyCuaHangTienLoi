package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

    public ThongKeDoanhThuPanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        originalData = new ArrayList<>();
        hoaDonDAO = new HoaDonDAO(conn);
        createThongKe();
        
        try {
            loadAllData();
            loadBestSellingProducts();
            loadPoorSellingProducts();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createThongKe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Tạo tab panel để chứa các bảng thống kê khác nhau
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
        JScrollPane scrollPane = new JScrollPane(tableThongKe);
        pDoanhThu.add(scrollPane, BorderLayout.CENTER);

        lblTotalRevenueValue = new JLabel("Tổng doanh thu: 0 VNĐ");

        JPanel pSort = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRevenueAsc = new JButton("Doanh thu tăng");
        JButton btnRevenueDesc = new JButton("Doanh thu giảm");
        JButton btnQuantityAsc = new JButton("Số lượng tăng");
        JButton btnQuantityDesc = new JButton("Số lượng giảm");

        btnRevenueAsc.addActionListener(e -> sortByColumn("Doanh thu", true));
        btnRevenueDesc.addActionListener(e -> sortByColumn("Doanh thu", false));
        btnQuantityAsc.addActionListener(e -> sortByColumn("Số lượng bán", true));
        btnQuantityDesc.addActionListener(e -> sortByColumn("Số lượng bán", false));

        pSort.add(btnRevenueAsc);
        pSort.add(btnRevenueDesc);
        pSort.add(btnQuantityAsc);
        pSort.add(btnQuantityDesc);

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
        JButton btnFilterBanChayToday = new JButton("Hôm nay");
        JButton btnFilterBanChayMonth = new JButton("Tháng này");
        JButton btnFilterBanChayAll = new JButton("Tất cả");
        
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
        JButton btnFilterKhoBanToday = new JButton("Hôm nay");
        JButton btnFilterKhoBanMonth = new JButton("Tháng này");
        JButton btnFilterKhoBanAll = new JButton("Tất cả");
        
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
                loadBestSellingProducts("day");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        btnFilterBanChayMonth.addActionListener(e -> {
            try {
                loadBestSellingProducts("month");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        btnFilterBanChayAll.addActionListener(e -> {
            try {
                loadBestSellingProducts();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        // Sự kiện cho các nút lọc sản phẩm khó bán
        btnFilterKhoBanToday.addActionListener(e -> {
            try {
                loadPoorSellingProducts("day");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        btnFilterKhoBanMonth.addActionListener(e -> {
            try {
                loadPoorSellingProducts("month");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        btnFilterKhoBanAll.addActionListener(e -> {
            try {
                loadPoorSellingProducts();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        add(panel, BorderLayout.CENTER);
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

            Object[] row = new Object[]{ngay, tenSP, loaiSP, soLuong, doanhThu};
            modelThongKe.addRow(row);
            originalData.add(row);
            totalRevenue += doanhThu;
        }

        lblTotalRevenueValue.setText("Tổng doanh thu: " + String.format("%,.0f VNĐ", totalRevenue));
    }
    
    private void loadBestSellingProducts() throws SQLException {
        loadBestSellingProducts("all");
    }
    
    private void loadBestSellingProducts(String timeFilter) throws SQLException {
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
        
        // Thêm điều kiện lọc theo thời gian
        if (timeFilter.equals("day")) {
            sql.append("WHERE CONVERT(date, hd.ngayLap) = CONVERT(date, GETDATE()) ");
        } else if (timeFilter.equals("month")) {
            sql.append("WHERE MONTH(hd.ngayLap) = MONTH(GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE()) ");
        }
        
        sql.append("GROUP BY sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham " +
                  "ORDER BY tongSoLuong DESC");
        
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            String maSP = rs.getString("maSanPham");
            String tenSP = rs.getString("tenSanPham");
            String loaiSP = rs.getString("tenLoaiSanPham");
            int tongSoLuong = rs.getInt("tongSoLuong");
            double tongDoanhThu = rs.getDouble("tongDoanhThu");
            
            modelSanPhamBanChay.addRow(new Object[]{
                maSP, tenSP, loaiSP, tongSoLuong, String.format("%,.0f", tongDoanhThu)
            });
        }
    }
    
    private void loadPoorSellingProducts() throws SQLException {
        loadPoorSellingProducts("all");
    }
    
    private void loadPoorSellingProducts(String timeFilter) throws SQLException {
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
        
        // Thêm điều kiện lọc theo thời gian
        if (timeFilter.equals("day")) {
            sql.append("AND CONVERT(date, hd.ngayLap) = CONVERT(date, GETDATE()) ");
        } else if (timeFilter.equals("month")) {
            sql.append("AND MONTH(hd.ngayLap) = MONTH(GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE()) ");
        }
        
        sql.append("GROUP BY sp.maSanPham, sp.tenSanPham, lsp.tenLoaiSanPham " +
                  "ORDER BY tongSoLuong ASC");
        
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            String maSP = rs.getString("maSanPham");
            String tenSP = rs.getString("tenSanPham");
            String loaiSP = rs.getString("tenLoaiSanPham");
            int tongSoLuong = rs.getInt("tongSoLuong");
            double tongDoanhThu = rs.getDouble("tongDoanhThu");
            
            modelSanPhamKhoBan.addRow(new Object[]{
                maSP, tenSP, loaiSP, tongSoLuong, String.format("%,.0f", tongDoanhThu)
            });
        }
    }
    
    private void refreshTable() {
        try {
            loadAllData();
            loadBestSellingProducts();
            loadPoorSellingProducts();
            txtFrom.setText("");
            txtTo.setText("");
            JOptionPane.showMessageDialog(this, "Dữ liệu đã được làm mới!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi làm mới dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void filterByDate() throws SQLException {
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
        
        // Thêm điều kiện lọc
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            if (filterType.equals("Ngày")) {
                sqlBuilder.append("WHERE hd.ngayLap BETWEEN ? AND ? ");
            } else { // Tháng
                sqlBuilder.append("WHERE YEAR(hd.ngayLap) * 100 + MONTH(hd.ngayLap) BETWEEN YEAR(?) * 100 + MONTH(?) AND YEAR(?) * 100 + MONTH(?) ");
            }
        }
        
        sqlBuilder.append("GROUP BY hd.ngayLap, sp.tenSanPham, lsp.tenLoaiSanPham " +
                         "ORDER BY hd.ngayLap DESC");

        PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString());
        
        if (!fromDate.isEmpty() && !toDate.isEmpty()) {
            if (filterType.equals("Ngày")) {
                ps.setDate(1, java.sql.Date.valueOf(fromDate));
                ps.setDate(2, java.sql.Date.valueOf(toDate));
            } else { // Tháng
                ps.setDate(1, java.sql.Date.valueOf(fromDate));
                ps.setDate(2, java.sql.Date.valueOf(fromDate));
                ps.setDate(3, java.sql.Date.valueOf(toDate));
                ps.setDate(4, java.sql.Date.valueOf(toDate));
            }
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

            modelThongKe.addRow(new Object[] {ngay, tenSP, loaiSP, soLuong, doanhThu});
            totalRevenue += doanhThu;
        }

        if (!hasData) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu cho khoảng thời gian đã chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        lblTotalRevenueValue.setText("Tổng doanh thu: " + String.format("%,.0f VNĐ", totalRevenue));
    }

    private void sortByColumn(String columnName, boolean ascending) {
        int colIndex = columnName.equals("Doanh thu") ? 4 : 3;
        List<Object[]> dataList = new ArrayList<>();
        for (int i = 0; i < modelThongKe.getRowCount(); i++) {
            Object[] row = new Object[modelThongKe.getColumnCount()];
            for (int j = 0; j < modelThongKe.getColumnCount(); j++) {
                row[j] = modelThongKe.getValueAt(i, j);
            }
            dataList.add(row);
        }

        dataList.sort((o1, o2) -> {
            double val1, val2;
            if (colIndex == 4) {
                val1 = o1[colIndex] instanceof String ? 
                       Double.parseDouble(((String)o1[colIndex]).replaceAll("[^\\d.]", "")) : 
                       (double)o1[colIndex];
                val2 = o2[colIndex] instanceof String ? 
                       Double.parseDouble(((String)o2[colIndex]).replaceAll("[^\\d.]", "")) : 
                       (double)o2[colIndex];
            } else {
                val1 = o1[colIndex] instanceof String ? 
                       Integer.parseInt(((String)o1[colIndex]).replaceAll("[^\\d]", "")) : 
                       (int)o1[colIndex];
                val2 = o2[colIndex] instanceof String ? 
                       Integer.parseInt(((String)o2[colIndex]).replaceAll("[^\\d]", "")) : 
                       (int)o2[colIndex];
            }
            return ascending ? Double.compare(val1, val2) : Double.compare(val2, val1);
        });

        modelThongKe.setRowCount(0);
        for (Object[] row : dataList) {
            modelThongKe.addRow(row);
        }
    }

    private int parseCurrency(String string) {
        try {
            return Integer.parseInt(string.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}