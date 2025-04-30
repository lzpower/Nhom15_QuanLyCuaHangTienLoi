package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KhuyenMaiPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtTimKM, txtMaKM, txtTenKM, txtGiaTriKM;
    private JTextField txtNgayBatDau, txtNgayKetThuc; // Added date fields
    private List<KhuyenMai> danhSachKhuyenMai;
    private KhuyenMaiDAO khuyenMaiDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Date formatter

    public KhuyenMaiPanel() {
        setLayout(new BorderLayout());

        // Khởi tạo DAO
        khuyenMaiDAO = new KhuyenMaiDAO();
        
        // Lấy danh sách khuyến mãi từ CSDL
        danhSachKhuyenMai = khuyenMaiDAO.getAllKhuyenMai();
        UIManager.put("Label.font", new Font("Tahoma", Font.PLAIN, 17));
        UIManager.put("TextField.font", new Font("Tahoma", Font.BOLD, 17));
        UIManager.put("Button.font", new Font("Tahoma", Font.BOLD, 13));
        
        // Add the promotion management panel
        JPanel formKhuyenMai = createFormKhuyenMai();
        add(formKhuyenMai, BorderLayout.CENTER);
    }

    private JPanel createFormKhuyenMai() {
        JPanel pBorder = new JPanel(new BorderLayout());
        pBorder.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel: Search promotions
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBorder(BorderFactory.createTitledBorder("Tìm khuyến mãi"));
        txtTimKM = new JTextField(20);
        JButton btnTim = new JButton("Tìm kiếm");
        pTop.add(txtTimKM);
        pTop.add(btnTim);
        pBorder.add(pTop, BorderLayout.NORTH);

        // Center panel: Promotion table
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã KM", "Tên KM", "Giá trị KM (%)", "Ngày bắt đầu", "Ngày kết thúc"}; // Added date columns
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 17));
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.setRowHeight(30);
        
        // Renderer to highlight matched rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String maKM = (String) table.getModel().getValueAt(row, 0);
                String keyword = txtTimKM.getText().trim().toLowerCase();
                if (!keyword.isEmpty() && maKM.toLowerCase().contains(keyword)) {
                    c.setBackground(Color.LIGHT_GRAY);
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Bottom panel: Input fields and buttons
        JPanel pBottom = new JPanel(new BorderLayout());
        JPanel pNhap = new JPanel();
        pNhap.setLayout(new BoxLayout(pNhap, BoxLayout.Y_AXIS));
        pNhap.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pMaKM = new JPanel(new BorderLayout());
        JLabel lblMaKM = new JLabel("Mã khuyến mãi:");
        lblMaKM.setPreferredSize(new Dimension(140, 20));
        txtMaKM = new JTextField();
        pMaKM.add(lblMaKM, BorderLayout.WEST);
        pMaKM.add(txtMaKM, BorderLayout.CENTER);
        pNhap.add(pMaKM);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pTenKM = new JPanel(new BorderLayout());
        JLabel lblTenKM = new JLabel("Tên khuyến mãi:");
        lblTenKM.setPreferredSize(new Dimension(140, 20));
        txtTenKM = new JTextField();
        pTenKM.add(lblTenKM, BorderLayout.WEST);
        pTenKM.add(txtTenKM, BorderLayout.CENTER);
        pNhap.add(pTenKM);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pGiaTriKM = new JPanel(new BorderLayout());
        JLabel lblGiaTriKM = new JLabel("Giá trị (%):");
        lblGiaTriKM.setPreferredSize(new Dimension(140, 20));
        txtGiaTriKM = new JTextField();
        pGiaTriKM.add(lblGiaTriKM, BorderLayout.WEST);
        pGiaTriKM.add(txtGiaTriKM, BorderLayout.CENTER);
        pNhap.add(pGiaTriKM);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add date fields
        JPanel pNgayBatDau = new JPanel(new BorderLayout());
        JLabel lblNgayBatDau = new JLabel("Ngày bắt đầu:");
        lblNgayBatDau.setPreferredSize(new Dimension(140, 20));
        txtNgayBatDau = new JTextField();
        pNgayBatDau.add(lblNgayBatDau, BorderLayout.WEST);
        pNgayBatDau.add(txtNgayBatDau, BorderLayout.CENTER);
        pNhap.add(pNgayBatDau);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pNgayKetThuc = new JPanel(new BorderLayout());
        JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc:");
        lblNgayKetThuc.setPreferredSize(new Dimension(140, 20));
        txtNgayKetThuc = new JTextField();
        pNgayKetThuc.add(lblNgayKetThuc, BorderLayout.WEST);
        pNgayKetThuc.add(txtNgayKetThuc, BorderLayout.CENTER);
        pNhap.add(pNgayKetThuc);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pBtn = new JPanel();
        JButton btnThem = new JButton("Thêm");
        JButton btnXoa = new JButton("Xóa");
        JButton btnXoaTrang = new JButton("Xóa Trắng");
        JButton btnSua = new JButton("Sửa");
        
        styleButton(btnThem, new Color(46, 204, 113));
        styleButton(btnXoa, new Color(231, 76, 60));
        styleButton(btnXoaTrang, new Color(52, 152, 219));
        styleButton(btnSua, new Color(41, 128, 185));
        styleButton(btnTim, new Color(41, 128, 185));
        
        pBtn.add(btnThem);
        pBtn.add(btnXoa);
        pBtn.add(btnXoaTrang);
        pBtn.add(btnSua);

        pBottom.add(pNhap, BorderLayout.CENTER);
        pBottom.add(pBtn, BorderLayout.SOUTH);
        pBorder.add(pBottom, BorderLayout.SOUTH);

        // Display initial promotion list
        for (KhuyenMai km : danhSachKhuyenMai) {
            tableModel.addRow(new Object[]{
                    km.getMaKhuyenMai(),
                    km.getTenKhuyenMai(),
                    km.getGiaTriKhuyenMai(),
                    formatDate(km.getNgayBatDau()),
                    formatDate(km.getNgayKetThuc())
            });
        }

        // ActionListener for Search button
        btnTim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = txtTimKM.getText().trim().toLowerCase();
                if (keyword.isEmpty()) {
                    // If search field is empty, display all promotions
                    tableModel.setRowCount(0);
                    for (KhuyenMai km : danhSachKhuyenMai) {
                        tableModel.addRow(new Object[]{
                                km.getMaKhuyenMai(),
                                km.getTenKhuyenMai(),
                                km.getGiaTriKhuyenMai(),
                                formatDate(km.getNgayBatDau()),
                                formatDate(km.getNgayKetThuc())
                        });
                    }
                    return;
                }

                // Search using DAO
                List<KhuyenMai> ketQuaTimKiem = khuyenMaiDAO.timKiemKhuyenMai(keyword);
                
                // Display search results
                tableModel.setRowCount(0);
                if (ketQuaTimKiem.isEmpty()) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Không tìm thấy khuyến mãi với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (KhuyenMai km : ketQuaTimKiem) {
                        tableModel.addRow(new Object[]{
                                km.getMaKhuyenMai(),
                                km.getTenKhuyenMai(),
                                km.getGiaTriKhuyenMai(),
                                formatDate(km.getNgayBatDau()),
                                formatDate(km.getNgayKetThuc())
                        });
                    }
                }
            }
        });

        // ActionListener for Add button
        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maKM = txtMaKM.getText().trim();
                String tenKM = txtTenKM.getText().trim();
                String giaTriKMStr = txtGiaTriKM.getText().trim();
                String ngayBatDauStr = txtNgayBatDau.getText().trim();
                String ngayKetThucStr = txtNgayKetThuc.getText().trim();

                // Validate input
                if (maKM.isEmpty() || tenKM.isEmpty() || giaTriKMStr.isEmpty() || 
                    ngayBatDauStr.isEmpty() || ngayKetThucStr.isEmpty()) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double giaTriKM = Double.parseDouble(giaTriKMStr);
                    if (giaTriKM <= 0 || giaTriKM > 100) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải lớn hơn 0 và nhỏ hơn hoặc bằng 100.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Parse dates
                    Date ngayBatDau = parseDate(ngayBatDauStr);
                    Date ngayKetThuc = parseDate(ngayKetThucStr);
                    
                    if (ngayBatDau == null || ngayKetThuc == null) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (ngayBatDau.after(ngayKetThuc)) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Ngày bắt đầu phải trước ngày kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Check for duplicate promotion ID
                    if (khuyenMaiDAO.getKhuyenMaiTheoMa(maKM) != null) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Mã khuyến mãi đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Add promotion
                    KhuyenMai km = new KhuyenMai(maKM, tenKM, giaTriKM);
                    km.setNgayBatDau(ngayBatDau);
                    km.setNgayKetThuc(ngayKetThuc);
                    
                    if (khuyenMaiDAO.themKhuyenMai(km)) {
                        danhSachKhuyenMai.add(km);
                        tableModel.addRow(new Object[]{
                                km.getMaKhuyenMai(),
                                km.getTenKhuyenMai(),
                                km.getGiaTriKhuyenMai(),
                                formatDate(km.getNgayBatDau()),
                                formatDate(km.getNgayKetThuc())
                        });

                        // Clear form
                        txtMaKM.setText("");
                        txtTenKM.setText("");
                        txtGiaTriKM.setText("");
                        txtNgayBatDau.setText("");
                        txtNgayKetThuc.setText("");
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Thêm khuyến mãi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Lỗi khi thêm khuyến mãi vào CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener for Delete button
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKM = (String) tableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(KhuyenMaiPanel.this, "Bạn có chắc muốn xóa khuyến mãi " + maKM + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (khuyenMaiDAO.xoaKhuyenMai(maKM)) {
                            danhSachKhuyenMai.removeIf(km -> km.getMaKhuyenMai().equals(maKM));
                            tableModel.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Xóa khuyến mãi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Lỗi khi xóa khuyến mãi từ CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng chọn một khuyến mãi để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // ActionListener for Edit button
        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKM = (String) tableModel.getValueAt(selectedRow, 0);
                    txtMaKM.setText(maKM);
                    txtTenKM.setText((String) tableModel.getValueAt(selectedRow, 1));
                    txtGiaTriKM.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
                    txtNgayBatDau.setText((String) tableModel.getValueAt(selectedRow, 3));
                    txtNgayKetThuc.setText((String) tableModel.getValueAt(selectedRow, 4));
                    txtMaKM.setEditable(false);
                } else {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng chọn một khuyến mãi để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // ActionListener for Clear button
        btnXoaTrang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKM = txtMaKM.getText().trim();
                    String tenKM = txtTenKM.getText().trim();
                    String giaTriKMStr = txtGiaTriKM.getText().trim();
                    String ngayBatDauStr = txtNgayBatDau.getText().trim();
                    String ngayKetThucStr = txtNgayKetThuc.getText().trim();

                    if (tenKM.isEmpty() || giaTriKMStr.isEmpty() || ngayBatDauStr.isEmpty() || ngayKetThucStr.isEmpty()) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        double giaTriKM = Double.parseDouble(giaTriKMStr);
                        if (giaTriKM <= 0 || giaTriKM > 100) {
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải lớn hơn 0 và nhỏ hơn hoặc bằng 100.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        // Parse dates
                        Date ngayBatDau = parseDate(ngayBatDauStr);
                        Date ngayKetThuc = parseDate(ngayKetThucStr);
                        
                        if (ngayBatDau == null || ngayKetThuc == null) {
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        if (ngayBatDau.after(ngayKetThuc)) {
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Ngày bắt đầu phải trước ngày kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Update promotion
                        KhuyenMai km = new KhuyenMai(maKM, tenKM, giaTriKM);
                        km.setNgayBatDau(ngayBatDau);
                        km.setNgayKetThuc(ngayKetThuc);
                        
                        if (khuyenMaiDAO.capNhatKhuyenMai(km)) {
                            // Update in-memory list
                            for (int i = 0; i < danhSachKhuyenMai.size(); i++) {
                                if (danhSachKhuyenMai.get(i).getMaKhuyenMai().equals(maKM)) {
                                    danhSachKhuyenMai.set(i, km);
                                    break;
                                }
                            }
                            
                            // Update table
                            tableModel.setValueAt(tenKM, selectedRow, 1);
                            tableModel.setValueAt(giaTriKM, selectedRow, 2);
                            tableModel.setValueAt(formatDate(ngayBatDau), selectedRow, 3);
                            tableModel.setValueAt(formatDate(ngayKetThuc), selectedRow, 4);

                            // Clear form
                            txtMaKM.setText("");
                            txtTenKM.setText("");
                            txtGiaTriKM.setText("");
                            txtNgayBatDau.setText("");
                            txtNgayKetThuc.setText("");
                            txtMaKM.setEditable(true);
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Cập nhật khuyến mãi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Lỗi khi cập nhật khuyến mãi trong CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Clear all fields if no row is selected
                    txtMaKM.setText("");
                    txtTenKM.setText("");
                    txtGiaTriKM.setText("");
                    txtNgayBatDau.setText("");
                    txtNgayKetThuc.setText("");
                    txtMaKM.setEditable(true);
                }
            }
        });

        return pBorder;
    }
    
    // Helper method to format Date to String
    private String formatDate(Date date) {
        if (date == null) return "";
        return dateFormat.format(date);
    }
    
    // Helper method to parse String to Date
    private Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    public void styleButton(JButton button, Color bgColor) {
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setForeground(Color.white);
    }
}