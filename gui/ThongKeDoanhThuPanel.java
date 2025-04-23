package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.SanPhamDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;

public class ThongKeDoanhThuPanel extends JPanel {

    private DefaultTableModel modelThongKe;
    private JTable tableThongKe;
    private JLabel lblTotalRevenueValue;
    private JTextField txtFrom, txtTo;
    private List<Object[]> originalData;
    
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private SanPhamDAO sanPhamDAO;

    public ThongKeDoanhThuPanel() {
        setLayout(new BorderLayout());
        originalData = new ArrayList<>();
        
        // Khởi tạo DAO
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        sanPhamDAO = new SanPhamDAO();
        
        createThongKe();
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

        // Lấy dữ liệu thống kê từ CSDL
        loadThongKeData();

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

        btnFilter.addActionListener(e -> filterByDate());
        btnRefresh.addActionListener(e -> refreshTable());

        add(panel, BorderLayout.CENTER);
    }
    
    private void loadThongKeData() {
        // Xóa dữ liệu cũ
        modelThongKe.setRowCount(0);
        originalData.clear();
        
        // Lấy danh sách hóa đơn
        List<HoaDon> danhSachHoaDon = hoaDonDAO.getAllHoaDon();
        
        // Nếu không có dữ liệu, thêm dữ liệu mẫu
        if (danhSachHoaDon.isEmpty()) {
            addSampleData();
            return;
        }
        
        // Duyệt qua từng hóa đơn
        for (HoaDon hoaDon : danhSachHoaDon) {
            // Lấy chi tiết hóa đơn
            List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getChiTietHoaDonTheoMaHD(hoaDon.getMaHoaDon());
            
            // Duyệt qua từng chi tiết
            for (ChiTietHoaDon chiTiet : chiTietList) {
                // Lấy thông tin sản phẩm
                SanPham sp = sanPhamDAO.getSanPhamTheoMa(chiTiet.getMaSanPham());
                if (sp != null) {
                    // Định dạng ngày
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String ngay = sdf.format(hoaDon.getNgayLap());
                    
                    // Tính doanh thu
                    double doanhThu = chiTiet.getSoLuong() * chiTiet.getDonGia();
                    
                    // Thêm vào bảng
                    Object[] row = {
                            ngay,
                            sp.getTenSP(),
                            sp.getLoaiSP().getTenLoaiSP(),
                            chiTiet.getSoLuong(),
                            String.format("%,.0f VNĐ", doanhThu)
                    };
                    
                    modelThongKe.addRow(row);
                    originalData.add(row.clone());
                }
            }
        }
        
        updateTotalRevenue();
    }

    private void addSampleData() {
        Object[][] sampleData = {
            {"2025-04-22", "Đồ hộp cá ngừ", "Đồ hộp", 150, "15,000 VNĐ"},
            {"2025-04-22", "Đồ hộp thịt heo", "Đồ hộp", 100, "12,000 VNĐ"},
            {"2025-04-21", "Đồ hộp đậu xanh", "Đồ hộp", 200, "10,000 VNĐ"},
            {"2025-04-20", "Đồ hộp ngô", "Đồ hộp", 250, "12,500 VNĐ"},
            {"2025-04-19", "Bánh sữa", "Bánh", 80, "8,000 VNĐ"}
        };

        for (Object[] row : sampleData) {
            modelThongKe.addRow(row);
            originalData.add(row.clone());
        }
        updateTotalRevenue();
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

    private void filterByDate() {
        String fromDate = txtFrom.getText().trim();
        String toDate = txtTo.getText().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            Date from = fromDate.isEmpty() ? null : sdf.parse(fromDate);
            Date to = toDate.isEmpty() ? null : sdf.parse(toDate);

            if (from != null && to != null && from.after(to)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lọc dữ liệu theo ngày
            List<HoaDon> danhSachHoaDon;
            if (from != null && to != null) {
                danhSachHoaDon = hoaDonDAO.getHoaDonTheoNgay(from, to);
            } else {
                danhSachHoaDon = hoaDonDAO.getAllHoaDon();
            }
            
            // Xóa dữ liệu cũ
            modelThongKe.setRowCount(0);
            originalData.clear();
            
            // Duyệt qua từng hóa đơn
            for (HoaDon hoaDon : danhSachHoaDon) {
                Date ngayLap = hoaDon.getNgayLap();
                if ((from == null || !ngayLap.before(from)) && (to == null || !ngayLap.after(to))) {
                    // Lấy chi tiết hóa đơn
                    List<ChiTietHoaDon> chiTietList = chiTietHoaDonDAO.getChiTietHoaDonTheoMaHD(hoaDon.getMaHoaDon());
                    
                    // Duyệt qua từng chi tiết
                    for (ChiTietHoaDon chiTiet : chiTietList) {
                        // Lấy thông tin sản phẩm
                        SanPham sp = sanPhamDAO.getSanPhamTheoMa(chiTiet.getMaSanPham());
                        if (sp != null) {
                            // Định dạng ngày
                            String ngay = sdf.format(hoaDon.getNgayLap());
                            
                            // Tính doanh thu
                            double doanhThu = chiTiet.getSoLuong() * chiTiet.getDonGia();
                            
                            // Thêm vào bảng
                            Object[] row = {
                                    ngay,
                                    sp.getTenSP(),
                                    sp.getLoaiSP().getTenLoaiSP(),
                                    chiTiet.getSoLuong(),
                                    String.format("%,.0f VNĐ", doanhThu)
                            };
                            
                            modelThongKe.addRow(row);
                            originalData.add(row.clone());
                        }
                    }
                }
            }
            
            updateTotalRevenue();
            JOptionPane.showMessageDialog(this, "Lọc dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày theo định dạng yyyy-MM-dd!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        loadThongKeData();
        txtFrom.setText("");
        txtTo.setText("");
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được làm mới!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTotalRevenue() {
        long total = 0;
        for (int i = 0; i < modelThongKe.getRowCount(); i++) {
            total += parseCurrency(modelThongKe.getValueAt(i, 4).toString());
        }
        lblTotalRevenueValue.setText("Tổng doanh thu: " + String.format("%,d VNĐ", total));
    }
}