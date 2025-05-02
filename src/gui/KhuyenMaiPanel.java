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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class KhuyenMaiPanel extends JPanel {

    private DefaultTableModel kmTableModel; // Mô hình bảng khuyến mãi
    private JTable kmTable; // Bảng hiển thị danh sách khuyến mãi
    private JTextField txtTimKM, txtMaKM, txtTenKM, txtGiaTriKM, txtNgayBatDau, txtNgayKetThuc; // Các ô nhập liệu
    private List<KhuyenMai> danhSachKhuyenMai; // Danh sách khuyến mãi
    private KhuyenMaiDAO khuyenMaiDAO; // Đối tượng DAO để làm việc với CSDL
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày

    // Biểu thức chính quy cho mã và tên khuyến mãi
    private static final Pattern MA_KM_PATTERN = Pattern.compile("^KM[0-9]{3}$");
    private static final Pattern TEN_KM_PATTERN = Pattern.compile("^Khuyến mãi[\\p{L}0-9\\s-]*$");

    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 13); // Font cho JButton
    private static final Dimension BUTTON_SIZE = new Dimension(100, 30); // Kích thước đồng bộ cho các nút

    public KhuyenMaiPanel() {
        setLayout(new BorderLayout());

        // Khởi tạo DAO
        khuyenMaiDAO = new KhuyenMaiDAO();
        
        // Lấy danh sách khuyến mãi từ CSDL
        danhSachKhuyenMai = khuyenMaiDAO.getAllKhuyenMai();

        // Thêm panel quản lý khuyến mãi
        JPanel formKhuyenMai = createFormKhuyenMai();
        add(formKhuyenMai, BorderLayout.CENTER);
    }

    private JPanel createFormKhuyenMai() {
        // Tạo panel chính với viền và nền trắng
        JPanel pBorder = new JPanel(new BorderLayout());
        pBorder.setBorder(new EmptyBorder(10, 10, 10, 10));
        pBorder.setBackground(Color.WHITE);

        // Panel phía trên: Tìm kiếm và nhập liệu
        JPanel pNorth = new JPanel();
        pNorth.setLayout(new BoxLayout(pNorth, BoxLayout.Y_AXIS));
        pNorth.setBackground(Color.WHITE);

        // Panel tìm kiếm
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBorder(BorderFactory.createTitledBorder("Tìm khuyến mãi"));
        pTop.setBackground(Color.WHITE);
        txtTimKM = new JTextField(20);
        txtTimKM.setPreferredSize(new Dimension(200, 30));
        JButton btnTimKM = new JButton("Tìm kiếm");
        btnTimKM.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnTimKM.setPreferredSize(BUTTON_SIZE); // Kích thước đồng bộ
        btnTimKM.setBackground(new Color(41, 128, 185));
        btnTimKM.setForeground(Color.WHITE);
        btnTimKM.setFocusPainted(false);
        btnTimKM.setBorderPainted(false);
        pTop.add(txtTimKM);
        pTop.add(btnTimKM);
        pNorth.add(pTop);
        pNorth.add(Box.createRigidArea(new Dimension(0, 10)));

        // Panel nhập liệu và nút
        JPanel pInputAndButtons = new JPanel();
        pInputAndButtons.setBorder(BorderFactory.createTitledBorder("Quản lý khuyến mãi"));
        pInputAndButtons.setBackground(Color.WHITE);
        pInputAndButtons.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Các ô nhập liệu
        JLabel lblMaKM = new JLabel("Mã KM:");
        lblMaKM.setPreferredSize(new Dimension(80, 30));
        txtMaKM = new JTextField(10);
        JLabel lblTenKM = new JLabel("Tên KM:");
        lblTenKM.setPreferredSize(new Dimension(80, 30));
        txtTenKM = new JTextField(15);
        JLabel lblGiaTriKM = new JLabel("Giá trị (%):");
        lblGiaTriKM.setPreferredSize(new Dimension(110, 30));
        txtGiaTriKM = new JTextField(5);
        JLabel lblNgayBatDau = new JLabel("Ngày bắt đầu:");
        lblNgayBatDau.setPreferredSize(new Dimension(110, 30));
        txtNgayBatDau = new JTextField(10);
        JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc:");
        lblNgayKetThuc.setPreferredSize(new Dimension(110, 30));
        txtNgayKetThuc = new JTextField(10);

        // Các nút
        JButton btnThemKM = new JButton("Thêm");
        btnThemKM.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnThemKM.setPreferredSize(BUTTON_SIZE); // Kích thước đồng bộ
        btnThemKM.setBackground(new Color(46, 204, 113));
        btnThemKM.setForeground(Color.WHITE);
        btnThemKM.setFocusPainted(false);
        btnThemKM.setBorderPainted(false);
        JButton btnXoaKM = new JButton("Xóa");
        btnXoaKM.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnXoaKM.setPreferredSize(BUTTON_SIZE); // Kích thước đồng bộ
        btnXoaKM.setBackground(new Color(231, 76, 60));
        btnXoaKM.setForeground(Color.WHITE);
        btnXoaKM.setFocusPainted(false);
        btnXoaKM.setBorderPainted(false);
        JButton btnLuuKM = new JButton("Cập nhật");
        btnLuuKM.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLuuKM.setPreferredSize(BUTTON_SIZE); // Kích thước đồng bộ
        btnLuuKM.setBackground(new Color(41, 128, 185));
        btnLuuKM.setForeground(Color.WHITE);
        btnLuuKM.setFocusPainted(false);
        btnLuuKM.setBorderPainted(false);
        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.setFont(BUTTON_FONT); // Đặt font trực tiếp
        btnLamMoi.setPreferredSize(BUTTON_SIZE); // Kích thước đồng bộ
        btnLamMoi.setBackground(new Color(52, 152, 219));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setBorderPainted(false);

        // Thêm các thành phần vào cùng một hàng với trọng số
        gbc.gridy = 0;

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        pInputAndButtons.add(lblMaKM, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.1;
        pInputAndButtons.add(txtMaKM, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        pInputAndButtons.add(lblTenKM, gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        pInputAndButtons.add(txtTenKM, gbc);

        gbc.gridx = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        pInputAndButtons.add(lblGiaTriKM, gbc);

        gbc.gridx = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.1;
        pInputAndButtons.add(txtGiaTriKM, gbc);

        gbc.gridx = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        pInputAndButtons.add(lblNgayBatDau, gbc);

        gbc.gridx = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.1;
        pInputAndButtons.add(txtNgayBatDau, gbc);

        gbc.gridx = 8;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        pInputAndButtons.add(lblNgayKetThuc, gbc);

        gbc.gridx = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.1;
        pInputAndButtons.add(txtNgayKetThuc, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.gridx = 10;
        pInputAndButtons.add(btnThemKM, gbc);
        gbc.gridx = 11;
        pInputAndButtons.add(btnXoaKM, gbc);
        gbc.gridx = 12;
        pInputAndButtons.add(btnLuuKM, gbc);
        gbc.gridx = 13;
        pInputAndButtons.add(btnLamMoi, gbc);

        pNorth.add(pInputAndButtons);

        pBorder.add(pNorth, BorderLayout.NORTH);
        
        // Panel chính giữa: Bảng khuyến mãi
        JPanel pCenter = new JPanel(new BorderLayout());
        pCenter.setBackground(Color.WHITE);
        String[] columnNames = {"Mã KM", "Tên KM", "Giá trị KM (%)", "Ngày bắt đầu", "Ngày kết thúc"};
        kmTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa ô trong bảng
            }
        };
        kmTable = new JTable(kmTableModel);
        kmTable.setRowHeight(30);
        kmTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        kmTable.setGridColor(new Color(200, 200, 200));
        kmTable.setShowGrid(true);

        // Renderer để tô màu dòng được chọn và dòng khớp tìm kiếm
        kmTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String maKM = (String) table.getModel().getValueAt(row, 0);
                String keyword = txtTimKM.getText().trim().toLowerCase();
                
                // Nếu dòng được chọn, tô đen nền và trắng chữ
                if (row == table.getSelectedRow()) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                // Nếu không có dòng nào được chọn, kiểm tra từ khóa tìm kiếm
                else if (!keyword.isEmpty() && maKM.toLowerCase().contains(keyword)) {
                    c.setBackground(Color.LIGHT_GRAY);
                    c.setForeground(Color.BLACK);
                }
                // Nếu không, giữ màu mặc định
                else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }
                return c;
            }
        });

        // Kiểu chữ cho tiêu đề bảng
        kmTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        kmTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(kmTable);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Hiển thị danh sách khuyến mãi ban đầu
        for (KhuyenMai km : danhSachKhuyenMai) {
            kmTableModel.addRow(new Object[]{
                    km.getMaKhuyenMai(),
                    km.getTenKhuyenMai(),
                    km.getGiaTriKhuyenMai(),
                    km.getNgayBatDau() != null ? dateFormat.format(km.getNgayBatDau()) : "",
                    km.getNgayKetThuc() != null ? dateFormat.format(km.getNgayKetThuc()) : ""
            });
        }

        // MouseListener để điền dữ liệu vào form khi click dòng
        kmTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = kmTable.getSelectedRow();
                if (selectedRow >= 0) {
                    txtMaKM.setText((String) kmTableModel.getValueAt(selectedRow, 0));
                    txtTenKM.setText((String) kmTableModel.getValueAt(selectedRow, 1));
                    txtGiaTriKM.setText(String.valueOf(kmTableModel.getValueAt(selectedRow, 2)));
                    txtNgayBatDau.setText((String) kmTableModel.getValueAt(selectedRow, 3));
                    txtNgayKetThuc.setText((String) kmTableModel.getValueAt(selectedRow, 4));
                    txtMaKM.setEditable(false); // Không cho phép sửa mã khuyến mãi
                }
            }
        });

        // ActionListener cho nút Tìm kiếm
        btnTimKM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = txtTimKM.getText().trim().toLowerCase();
                if (keyword.isEmpty()) {
                    // Nếu ô tìm kiếm trống, hiển thị tất cả khuyến mãi
                    kmTableModel.setRowCount(0);
                    for (KhuyenMai km : danhSachKhuyenMai) {
                        kmTableModel.addRow(new Object[]{
                                km.getMaKhuyenMai(),
                                km.getTenKhuyenMai(),
                                km.getGiaTriKhuyenMai(),
                                km.getNgayBatDau() != null ? dateFormat.format(km.getNgayBatDau()) : "",
                                km.getNgayKetThuc() != null ? dateFormat.format(km.getNgayKetThuc()) : ""
                        });
                    }
                    return;
                }

                // Tìm kiếm bằng DAO
                List<KhuyenMai> ketQuaTimKiem = khuyenMaiDAO.timKiemKhuyenMai(keyword);
                
                // Hiển thị kết quả tìm kiếm
                kmTableModel.setRowCount(0);
                if (ketQuaTimKiem.isEmpty()) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Không tìm thấy khuyến mãi với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (KhuyenMai km : ketQuaTimKiem) {
                        kmTableModel.addRow(new Object[]{
                                km.getMaKhuyenMai(),
                                km.getTenKhuyenMai(),
                                km.getGiaTriKhuyenMai(),
                                km.getNgayBatDau() != null ? dateFormat.format(km.getNgayBatDau()) : "",
                                km.getNgayKetThuc() != null ? dateFormat.format(km.getNgayKetThuc()) : ""
                        });
                    }
                }
            }
        });

        // ActionListener cho nút Thêm
        btnThemKM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maKM = txtMaKM.getText().trim();
                String tenKM = txtTenKM.getText().trim();
                String giaTriKMStr = txtGiaTriKM.getText().trim();
                String ngayBatDauStr = txtNgayBatDau.getText().trim();
                String ngayKetThucStr = txtNgayKetThuc.getText().trim();

                // Kiểm tra dữ liệu nhập
                if (maKM.isEmpty() || tenKM.isEmpty() || giaTriKMStr.isEmpty() || ngayBatDauStr.isEmpty() || ngayKetThucStr.isEmpty()) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng nhập đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra định dạng mã khuyến mãi
                if (!MA_KM_PATTERN.matcher(maKM).matches()) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Mã khuyến mãi phải có định dạng KM + 3 chữ số (VD: KM123).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Kiểm tra định dạng tên khuyến mãi
                if (!TEN_KM_PATTERN.matcher(tenKM).matches()) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Tên khuyến mãi phải bắt đầu bằng 'Khuyến mãi' và chỉ chứa chữ cái, số, khoảng cách hoặc dấu gạch ngang (VD: Khuyến mãi Hè-2025).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double giaTriKM = Double.parseDouble(giaTriKMStr);
                    if (giaTriKM <= 0 || giaTriKM > 100) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải lớn hơn 0 và nhỏ hơn hoặc bằng 100.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Parse ngày
                    Date ngayBatDau = dateFormat.parse(ngayBatDauStr);
                    Date ngayKetThuc = dateFormat.parse(ngayKetThucStr);
                    if (ngayBatDau.after(ngayKetThuc)) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Kiểm tra mã khuyến mãi trùng lặp
                    if (khuyenMaiDAO.getKhuyenMaiTheoMa(maKM) != null) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Mã khuyến mãi đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Thêm khuyến mãi
                    KhuyenMai km = new KhuyenMai(maKM, tenKM, giaTriKM);
                    km.setNgayBatDau(ngayBatDau);
                    km.setNgayKetThuc(ngayKetThuc);
                    if (khuyenMaiDAO.themKhuyenMai(km)) {
                        danhSachKhuyenMai.add(km);
                        kmTableModel.addRow(new Object[]{
                                km.getMaKhuyenMai(),
                                km.getTenKhuyenMai(),
                                km.getGiaTriKhuyenMai(),
                                dateFormat.format(km.getNgayBatDau()),
                                dateFormat.format(km.getNgayKetThuc())
                        });

                        // Xóa form sau khi thêm
                        txtMaKM.setText("");
                        txtTenKM.setText("");
                        txtGiaTriKM.setText("");
                        txtNgayBatDau.setText("");
                        txtNgayKetThuc.setText("");
                        txtMaKM.setEditable(true);
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Thêm khuyến mãi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Lỗi khi thêm khuyến mãi vào CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải là số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ActionListener cho nút Xóa
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
                            txtMaKM.setText("");
                            txtTenKM.setText("");
                            txtGiaTriKM.setText("");
                            txtNgayBatDau.setText("");
                            txtNgayKetThuc.setText("");
                            txtMaKM.setEditable(true);
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

        // ActionListener cho nút Lưu
        btnLuuKM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = kmTable.getSelectedRow();
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

                    // Kiểm tra định dạng tên khuyến mãi
                    if (!TEN_KM_PATTERN.matcher(tenKM).matches()) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Tên khuyến mãi phải bắt đầu bằng 'Khuyến mãi' và chỉ chứa chữ cái, số, khoảng cách hoặc dấu gạch ngang (VD: Khuyến mãi Hè-2025).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        double giaTriKM = Double.parseDouble(giaTriKMStr);
                        if (giaTriKM <= 0 || giaTriKM > 100) {
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Giá trị khuyến mãi phải lớn hơn 0 và nhỏ hơn hoặc bằng 100.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Parse ngày
                        Date ngayBatDau = dateFormat.parse(ngayBatDauStr);
                        Date ngayKetThuc = dateFormat.parse(ngayKetThucStr);
                        if (ngayBatDau.after(ngayKetThuc)) {
                            JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Cập nhật khuyến mãi
                        KhuyenMai km = new KhuyenMai(maKM, tenKM, giaTriKM);
                        km.setNgayBatDau(ngayBatDau);
                        km.setNgayKetThuc(ngayKetThuc);
                        if (khuyenMaiDAO.capNhatKhuyenMai(km)) {
                            // Cập nhật danh sách trong bộ nhớ
                            for (int i = 0; i < danhSachKhuyenMai.size(); i++) {
                                if (danhSachKhuyenMai.get(i).getMaKhuyenMai().equals(maKM)) {
                                    danhSachKhuyenMai.set(i, km);
                                    break;
                                }
                            }
                            
                            // Cập nhật bảng
                            kmTableModel.setValueAt(tenKM, selectedRow, 1);
                            kmTableModel.setValueAt(giaTriKM, selectedRow, 2);
                            kmTableModel.setValueAt(dateFormat.format(ngayBatDau), selectedRow, 3);
                            kmTableModel.setValueAt(dateFormat.format(ngayKetThuc), selectedRow, 4);

                            // Xóa form sau khi lưu
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
                    } catch (ParseException ex) {
                        JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng yyyy-MM-dd.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(KhuyenMaiPanel.this, "Vui lòng chọn một khuyến mãi để lưu.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // ActionListener cho nút Làm mới
        btnLamMoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xóa form
                txtMaKM.setText("");
                txtTenKM.setText("");
                txtGiaTriKM.setText("");
                txtNgayBatDau.setText("");
                txtNgayKetThuc.setText("");
                txtMaKM.setEditable(true);
            }
        });

        return pBorder;
    }
}