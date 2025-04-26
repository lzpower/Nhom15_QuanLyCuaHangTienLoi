package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.HoaDonDAO;
import entity.HoaDon;

public class ThongKeDoanhThuPanel extends JPanel {

    private DefaultTableModel modelThongKe;
    private JTable tableThongKe;
    private JLabel lblTotalRevenueValue;
    private JTextField txtFrom, txtTo;
    private List<Object[]> originalData;

    private HoaDonDAO hoaDonDAO;
	private Container container;
	private Connection conn;
	private String query;

	public ThongKeDoanhThuPanel(Connection conn) {
	    setLayout(new BorderLayout());
	    this.conn = conn; // bạn cần set conn nếu muốn dùng lại trong loadAllData
	    originalData = new ArrayList<>();
	    hoaDonDAO = new HoaDonDAO(conn);
	    createThongKe();
	    
	    try {
	        loadAllData(); // <-- thêm dòng này
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



    private void createThongKe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setBorder(BorderFactory.createTitledBorder("Thống kê doanh thu"));

        JPanel pFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFrom = new JLabel("Từ ngày (yyyy-MM-dd):");
        txtFrom = new JTextField(10);
        JLabel lblTo = new JLabel("Đến ngày (yyyy-MM-dd):");
        txtTo = new JTextField(10);
        JButton btnFilter = new JButton("Lọc");
        pFilter.add(lblFrom);
        pFilter.add(txtFrom);
        pFilter.add(lblTo);
        pFilter.add(txtTo);
        pFilter.add(btnFilter);
        pTop.add(pFilter, BorderLayout.WEST);

        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Làm mới");
        pButtons.add(btnRefresh);
        pTop.add(pButtons, BorderLayout.EAST);

        panel.add(pTop, BorderLayout.NORTH);

        String[] cols = {"Ngày", "Tên sản phẩm", "Loại sản phẩm", "Số lượng bán", "Doanh thu"};
        modelThongKe = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableThongKe = new JTable(modelThongKe);
        JScrollPane scrollPane = new JScrollPane(tableThongKe);
        panel.add(scrollPane, BorderLayout.CENTER);

        lblTotalRevenueValue = new JLabel("Tổng doanh thu: 0 VNĐ");

        JPanel pSort = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRevenueAsc = new JButton("Doanh thu tăng");
        JButton btnRevenueDesc = new JButton("Doanh thu giảm");
        JButton btnHotSelling = new JButton("Bán chạy");
        JButton btnPoorSelling = new JButton("Không bán chạy");

        btnRevenueAsc.addActionListener(e -> sortByColumn("Doanh thu", true));
        btnRevenueDesc.addActionListener(e -> sortByColumn("Doanh thu", false));
        btnHotSelling.addActionListener(e -> sortByColumn("Số lượng bán", false));
        btnPoorSelling.addActionListener(e -> sortByColumn("Số lượng bán", true));

        pSort.add(btnRevenueAsc);
        pSort.add(btnRevenueDesc);
        pSort.add(btnHotSelling);
        pSort.add(btnPoorSelling);

        pSort.add(Box.createHorizontalStrut(50));
        pSort.add(lblTotalRevenueValue);

        panel.add(pSort, BorderLayout.SOUTH);

        btnFilter.addActionListener(e -> {
            try {
                filterByDate();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        btnRefresh.addActionListener(e -> refreshTable());

        add(panel, BorderLayout.CENTER);
    }

    private void loadAllData() throws SQLException {
        modelThongKe.setRowCount(0);
        originalData.clear();

        String sql = "SELECT hd.ngayLap, sp.tenSP, lsp.tenLoaiSP, " +
                "SUM(cthd.soLuong) AS soLuong, " +
                "SUM(cthd.soLuong * cthd.donGia) AS doanhThu " +
                "FROM HoaDon hd " +
                "JOIN ChiTietHoaDon cthd ON hd.maHoaDon = cthd.maHoaDon " +
                "JOIN SanPham sp ON cthd.maSP = sp.maSP " +
                "JOIN LoaiSanPham lsp ON sp.maLoaiSP = lsp.maLoaiSP " +
                "GROUP BY hd.ngayLap, sp.tenSP, lsp.tenLoaiSP " +
                "ORDER BY hd.ngayLap ASC";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        double totalRevenue = 0;
        while (rs.next()) {
            String ngay = rs.getString("ngayLap");
            String tenSP = rs.getString("tenSP");
            String loaiSP = rs.getString("tenloaiSP");
            int soLuong = rs.getInt("soLuong");
            double doanhThu = rs.getDouble("doanhThu");

            modelThongKe.addRow(new Object[]{ngay, tenSP, loaiSP, soLuong, doanhThu});
            totalRevenue += doanhThu;
        }

        lblTotalRevenueValue.setText("Tổng doanh thu: " + String.format("%,.0f VNĐ", totalRevenue));
    }
    private void refreshTable() {
        try {
            loadAllData(); // gọi hàm mới
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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
        sdf.setLenient(false);
        sdf.parse(fromDate);
        sdf.parse(toDate);
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! Vui lòng nhập ngày theo định dạng yyyy-MM-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    modelThongKe.setRowCount(0);

    String sql = "SELECT hd.ngayLap, sp.tenSP, sp.loaiSP, SUM(cthd.soLuong) AS soLuong, SUM(cthd.soLuong * sp.gia) AS doanhThu " +
                 "FROM HoaDon hd " +
                 "JOIN ChiTietHoaDon cthd ON hd.idHoaDon = cthd.idHoaDon " +
                 "JOIN SanPham sp ON cthd.idSanPham = sp.idSanPham " +
                 "WHERE hd.ngayLap BETWEEN ? AND ? " +
                 "GROUP BY hd.ngayLap, sp.tenSP, sp.loaiSP " +
                 "ORDER BY hd.ngayLap ASC";

    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setDate(1, java.sql.Date.valueOf(fromDate));
    ps.setDate(2, java.sql.Date.valueOf(toDate));

    ResultSet rs = ps.executeQuery();

    double totalRevenue = 0;
    boolean hasData = false;

    while (rs.next()) {
        hasData = true;
        String ngay = rs.getString("ngayLap");
        String tenSP = rs.getString("tenSP");
        String loaiSP = rs.getString("loaiSP");
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
            int val1, val2;
            if (colIndex == 4) {
                val1 = parseCurrency(o1[colIndex].toString());
                val2 = parseCurrency(o2[colIndex].toString());
            } else {
                val1 = Integer.parseInt(o1[colIndex].toString());
                val2 = Integer.parseInt(o2[colIndex].toString());
            }
            return ascending ? val1 - val2 : val2 - val1;
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



    private void updateTotalRevenue() {
        long total = 0;
        for (int i = 0; i < modelThongKe.getRowCount(); i++) {
            total += parseCurrency(modelThongKe.getValueAt(i, 4).toString());
        }
        lblTotalRevenueValue.setText("Tổng doanh thu: " + String.format("%,d VNĐ", total));
    }
    

    private void showPanel(JPanel panel) {
        // Assuming you have a container like a JFrame or JPanel to add the panel
        // Example:
        container.removeAll();
        container.add(panel);
        container.revalidate();
        container.repaint();
    }
}
