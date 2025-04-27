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
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiPanel extends JPanel {

    private DefaultTableModel kmTableModel;
    private JTable kmTable;
    private JTextField txtTimKM, txtMaKM, txtTenKM, txtGiaTriKM;
    private List<KhuyenMai> danhSachKhuyenMai;
    private KhuyenMaiDAO khuyenMaiDAO;

    public KhuyenMaiPanel() {
        setLayout(new BorderLayout());

        // Khởi tạo DAO
        khuyenMaiDAO = new KhuyenMaiDAO();
        
        // Lấy danh sách khuyến mãi từ CSDL
        danhSachKhuyenMai = khuyenMaiDAO.getAllKhuyenMai();
        if (danhSachKhuyenMai.isEmpty()) {
            // Nếu không có dữ liệu, thêm dữ liệu mẫu
            danhSachKhuyenMai.add(new KhuyenMai("KM001", "Giảm giá mùa thi lại", 10));
            danhSachKhuyenMai.add(new KhuyenMai("KM002", "Khuyến mãi Tết cho sinh viên rớt môn", 20));
            danhSachKhuyenMai.add(new KhuyenMai("KM003", "Black Friday cho sinh viên FA", 15));
            danhSachKhuyenMai.add(new KhuyenMai("KM004", "FAN JACK", 25));
            
            // Lưu dữ liệu mẫu vào CSDL
            for (KhuyenMai km : danhSachKhuyenMai) {
                khuyenMaiDAO.themKhuyenMai(km);
            }
        }

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
        JButton btnTimKM = new JButton("Tìm kiếm");
        pTop.add(txtTimKM);
        pTop.add(btnTimKM);
        pBorder.add(pTop, BorderLayout.NORTH);

        // Center panel: Promotion table
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã KM", "Tên KM", "Giá trị KM (%)"};
        kmTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        kmTable = new JTable(kmTableModel);

        // Renderer to highlight matched rows
        kmTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(kmTable);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Bottom panel: Input fields and buttons
        JPanel pBottom = new JPanel(new BorderLayout());
        JPanel pNhap = new JPanel();
        pNhap.setLayout(new BoxLayout(pNhap, BoxLayout.Y_AXIS));
        pNhap.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pMaKM = new JPanel(new BorderLayout());
        JLabel lblMaKM = new JLabel("Mã khuyến mãi:");
        lblMaKM.setPreferredSize(new Dimension(100, 20));
        txtMaKM = new JTextField();
        pMaKM.add(lblMaKM, BorderLayout.WEST);
        pMaKM.add(txtMaKM, BorderLayout.CENTER);
        pNhap.add(pMaKM);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pTenKM = new JPanel(new BorderLayout());
        JLabel lblTenKM = new JLabel("Tên khuyến mãi:");
        lblTenKM.setPreferredSize(new Dimension(100, 20));
        txtTenKM = new JTextField();
        pTenKM.add(lblTenKM, BorderLayout.WEST);
        pTenKM.add(txtTenKM, BorderLayout.CENTER);
        pNhap.add(pTenKM);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pGiaTriKM = new JPanel(new BorderLayout());
        JLabel lblGiaTriKM = new JLabel("Giá trị (%):");
        lblGiaTriKM.setPreferredSize(new Dimension(100, 20));
        txtGiaTriKM = new JTextField();
        pGiaTriKM.add(lblGiaTriKM, BorderLayout.WEST);
        pGiaTriKM.add(txtGiaTriKM, BorderLayout.CENTER);
        pNhap.add(pGiaTriKM);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel pBtn = new JPanel();
        JButton btnThemKM = new JButton("Thêm");
        JButton btnXoaKM = new JButton("Xóa");
        JButton btnSuaKM = new JButton("Sửa");
        JButton btnLuuKM = new JButton("Lưu");
        pBtn.add(btnThemKM);
        pBtn.add(btnXoaKM);
        pBtn.add(btnSuaKM);
        pBtn.add(btnLuuKM);

        pBottom.add(pNhap, BorderLayout.CENTER);
        pBottom.add(pBtn, BorderLayout.SOUTH);
        pBorder.add(pBottom, BorderLayout.SOUTH);

        // Display initial promotion list
        for (KhuyenMai km : danhSachKhuyenMai) {
            kmTableModel.addRow(new Object[]{
                    km.getMaKhuyenMai(),
                    km.getTenKhuyenMai(),
                    km.getGiaTriKhuyenMai()
            });
        }

        // ActionListener for Search button
        btnTimKM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = txtTimKM.getText().trim().toLowerCase();
                if (keyword.isEmpty()) {
                    // If search field is empty, display all promotions
                    kmTableModel.setRowCount(0);
                    for (KhuyenMai km : danhSachKhuyenMai) {
                        kmTableModel.addRow(new Object[]{
                        		km.getMaKhuyenMai(),
                                km.getTenKhuyenMai(),
                                km.getGiaTriKhuyenMai()
                        });
                    }
                    return;
                }

                // Search using DAO
                List<KhuyenMai> ketQuaTimKiem = khuyenMaiDAO.timKiemKhuyenMai(keyword);
                
                // Display search results
                kmTableModel.setRowCount(0);
                if (ketQuaTimKiem.isEmpty()) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Không tìm thấy khuyến mãi với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (KhuyenMai km : ketQuaTimKiem) {
                        kmTableModel.addRow(new Object[]{
                        		km.getMaKhuyenMai(),
                                km.getTenKhuyenMai(),
                                km.getGiaTriKhuyenMai()
                        });
                    }
                }
            }
        });

        // ActionListener for Add button
        btnThemKM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maKM = txtMaKM.getText().trim();
                String tenKM = txtTenKM.getText().trim();
                String giaTriKMStr = txtGiaTriKM.getText().trim();

                // Validate input
                if (maKM.isEmpty() || tenKM.isEmpty() || giaTriKMStr.isEmpty()) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double giaTriKM = Double.parseDouble(giaTriKMStr);
                    if (giaTriKM <= 0 || giaTriKM > 100) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải lớn hơn 0 và nhỏ hơn hoặc bằng 100.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Check for duplicate promotion ID
                    if (khuyenMaiDAO.getKhuyenMaiTheoMa(maKM) != null) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Mã khuyến mãi đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Add promotion
                    KhuyenMai km = new KhuyenMai(maKM, tenKM, giaTriKM);
                    if (khuyenMaiDAO.themKhuyenMai(km)) {
                        danhSachKhuyenMai.add(km);
                        kmTableModel.addRow(new Object[]{
                        		km.getMaKhuyenMai(),
                                km.getTenKhuyenMai(),
                                km.getGiaTriKhuyenMai()
                        });

                        // Clear form
                        txtMaKM.setText("");
                        txtTenKM.setText("");
                        txtGiaTriKM.setText("");
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
        btnXoaKM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = kmTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKM = (String) kmTableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(KhuyenMaiPanel.this, "Bạn có chắc muốn xóa khuyến mãi " + maKM + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (khuyenMaiDAO.xoaKhuyenMai(maKM)) {
                            danhSachKhuyenMai.removeIf(km -> km.getMaKhuyenMai().equals(maKM));
                            kmTableModel.removeRow(selectedRow);
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
        btnSuaKM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = kmTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKM = (String) kmTableModel.getValueAt(selectedRow, 0);
                    txtMaKM.setText(maKM);
                    txtTenKM.setText((String) kmTableModel.getValueAt(selectedRow, 1));
                    txtGiaTriKM.setText(String.valueOf(kmTableModel.getValueAt(selectedRow, 2)));
                    txtMaKM.setEditable(false);
                } else {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng chọn một khuyến mãi để sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // ActionListener for Save button
        btnLuuKM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = kmTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKM = txtMaKM.getText().trim();
                    String tenKM = txtTenKM.getText().trim();
                    String giaTriKMStr = txtGiaTriKM.getText().trim();

                    if (tenKM.isEmpty() || giaTriKMStr.isEmpty()) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        double giaTriKM = Double.parseDouble(giaTriKMStr);
                        if (giaTriKM <= 0 || giaTriKM > 100) {
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải lớn hơn 0 và nhỏ hơn hoặc bằng 100.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Update promotion
                        KhuyenMai km = new KhuyenMai(maKM, tenKM, giaTriKM);
                        if (khuyenMaiDAO.capNhatKhuyenMai(km)) {
                            // Update in-memory list
                            for (int i = 0; i < danhSachKhuyenMai.size(); i++) {
                                if (danhSachKhuyenMai.get(i).getMaKhuyenMai().equals(maKM)) {
                                    danhSachKhuyenMai.set(i, km);
                                    break;
                                }
                            }
                            
                            // Update table
                            kmTableModel.setValueAt(tenKM, selectedRow, 1);
                            kmTableModel.setValueAt(giaTriKM, selectedRow, 2);

                            // Clear form
                            txtMaKM.setText("");
                            txtTenKM.setText("");
                            txtGiaTriKM.setText("");
                            txtMaKM.setEditable(true);
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Cập nhật khuyến mãi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Lỗi khi cập nhật khuyến mãi trong CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng chọn một khuyến mãi để lưu.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        return pBorder;
    }
}