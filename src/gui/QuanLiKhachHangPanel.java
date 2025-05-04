package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import dao.KhachHangDAO;
import entity.KhachHang;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class QuanLiKhachHangPanel extends JPanel implements ActionListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtTimKH, txtMaKH, txtTenKH, txtSoDienThoai, txtSoDiem;
    private List<KhachHang> danhSachKhachHang;
    private KhachHangDAO khachHangDAO;
    private JButton btnXoaTrang;
    private JButton btnTim;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnSua;

    public QuanLiKhachHangPanel() {
        setLayout(new BorderLayout());

        // Khởi tạo DAO
        khachHangDAO = new KhachHangDAO();
        
        // Lấy danh sách khách hàng từ CSDL
        danhSachKhachHang = khachHangDAO.getAllKhachHang();
        
        // Tạo giao diện
        JPanel formQuanLiKH = createFormQuanLiKH();
        add(formQuanLiKH, BorderLayout.CENTER);
    }

    private JPanel createFormQuanLiKH() {
        JPanel pBorder = new JPanel(new BorderLayout());
        pBorder.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Phần trên: Nhập liệu, các nút, và tìm kiếm
        JPanel pTop = new JPanel();
        pTop.setLayout(new BorderLayout());
        pTop.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));

        // Panel cho text fields
        JPanel pFields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Mã KH
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel lblMaKH = new JLabel("Mã khách hàng:");
        lblMaKH.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblMaKH, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtMaKH = new JTextField(30);
        txtMaKH.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtMaKH, gbc);

        // Tên KH
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel lblTenKH = new JLabel("Tên khách hàng:");
        lblTenKH.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblTenKH, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTenKH = new JTextField(30);
        txtTenKH.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtTenKH, gbc);

        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
        lblSoDienThoai.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblSoDienThoai, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSoDienThoai = new JTextField(30);
        txtSoDienThoai.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtSoDienThoai, gbc);

        // Số điểm
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        JLabel lblSoDiem = new JLabel("Số điểm:");
        lblSoDiem.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblSoDiem, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSoDiem = new JTextField(30);
        txtSoDiem.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtSoDiem, gbc);

        // Panel cho các nút
        JPanel pButtons = new JPanel();
        pButtons.setLayout(new BoxLayout(pButtons, BoxLayout.Y_AXIS));
        pButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Nút Thêm
        btnThem = new JButton("Thêm");
        styleButton(btnThem, new Color(46, 204, 113));
        btnThem.setPreferredSize(new Dimension(120, 25));
        btnThem.setMaximumSize(new Dimension(120, 40));
        btnThem.setAlignmentX(Component.LEFT_ALIGNMENT);
        pButtons.add(btnThem);
        pButtons.add(Box.createRigidArea(new Dimension(0, 10)));

        // Nút Xóa
        btnXoa = new JButton("Xóa");
        styleButton(btnXoa, new Color(231, 76, 60));
        btnXoa.setPreferredSize(new Dimension(120, 25));
        btnXoa.setMaximumSize(new Dimension(120, 40));
        btnXoa.setAlignmentX(Component.LEFT_ALIGNMENT);
        pButtons.add(btnXoa);
        pButtons.add(Box.createRigidArea(new Dimension(0, 10)));

        // Nút Xóa trắng
        btnXoaTrang = new JButton("Xóa trắng");
        styleButton(btnXoaTrang, new Color(52, 152, 219));
        btnXoaTrang.setPreferredSize(new Dimension(120, 25));
        btnXoaTrang.setMaximumSize(new Dimension(120, 40));
        btnXoaTrang.setAlignmentX(Component.LEFT_ALIGNMENT);
        pButtons.add(btnXoaTrang);
        pButtons.add(Box.createRigidArea(new Dimension(0, 10)));

        // Nút Sửa
        btnSua = new JButton("Sửa");
        styleButton(btnSua, new Color(41, 128, 185));
        btnSua.setPreferredSize(new Dimension(120, 25));
        btnSua.setMaximumSize(new Dimension(120, 40));
        btnSua.setAlignmentX(Component.LEFT_ALIGNMENT);
        pButtons.add(btnSua);

        // Thêm panel fields và buttons vào panel top
        pTop.add(pFields, BorderLayout.CENTER);
        pTop.add(pButtons, BorderLayout.EAST);

        // Panel cho tìm kiếm
        JPanel pSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSearch.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Tìm kiếm khách hàng
        JLabel lblTimKH = new JLabel("Tìm khách hàng:");
        lblTimKH.setPreferredSize(new Dimension(120, 20));
        pSearch.add(lblTimKH);
        pSearch.add(Box.createHorizontalStrut(1));

        txtTimKH = new JTextField(15);
        txtTimKH.setPreferredSize(new Dimension(200, 25));
        txtTimKH.setHorizontalAlignment(JTextField.LEFT);
        pSearch.add(txtTimKH);

        pSearch.add(Box.createRigidArea(new Dimension(5, 0)));

        btnTim = new JButton("Tìm kiếm");
        styleButton(btnTim, new Color(41, 128, 185));
        btnTim.setPreferredSize(new Dimension(120, 25));
        pSearch.add(btnTim);
        
        pTop.add(pSearch, BorderLayout.SOUTH);

        JPanel pTopContainer = new JPanel();
        pTopContainer.setLayout(new BoxLayout(pTopContainer, BoxLayout.Y_AXIS));
        pTopContainer.add(pTop);
        pSearch.setAlignmentX(Component.LEFT_ALIGNMENT);

        pBorder.add(pTopContainer, BorderLayout.NORTH);

        //table
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã KH", "Tên KH", "Số điện thoại", "Số điểm"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return column==1||column==2||column==3;
            }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 17));
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Hiển thị danh sách khách hàng ban đầu
        dienvaoTable();

        // Sự kiện
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (row >= 0 && column >= 0) {
                        String maKH = (String) tableModel.getValueAt(row, 0);
                        String tenKH = (String) tableModel.getValueAt(row, 1);
                        String soDienThoai = (String) tableModel.getValueAt(row, 2);
                        int soDiem = Integer.parseInt(tableModel.getValueAt(row, 3).toString());
                        
                        // Validate the data
                        if (column == 1 && !tenKH.matches("^[a-zA-ZÀ-ỹ\\s]{1,100}$")) {
                            JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, 
                                "Tên khách hàng chỉ được chứa chữ cái và khoảng trắng! Tối đa 100 kí tự", 
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                            dienvaoTable(); // Reset table data
                            return;
                        }
                        
                        if (column == 2 && !soDienThoai.matches("\\d{10}")) {
                            JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, 
                                "Số điện thoại phải là 10 chữ số!", 
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                            dienvaoTable(); // Reset table data
                            return;
                        }
                        
                        if (column == 2 && khachHangDAO.kiemTraSoDienThoaiTonTai(soDienThoai, maKH)) {
                            JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, 
                                "Số điện thoại này đã tồn tại cho khách hàng khác!", 
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                            dienvaoTable(); // Reset table data
                            return;
                        }
                        
                        if (column == 3) {
                            try {
                                if (soDiem < 0) {
                                    JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, 
                                        "Số điểm phải lớn hơn hoặc bằng 0!", 
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                                    dienvaoTable(); // Reset table data
                                    return;
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, 
                                    "Số điểm phải là số nguyên!", 
                                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                                dienvaoTable(); // Reset table data
                                return;
                            }
                        }
                        
                        // Update the customer in the database
                        KhachHang khachHang = new KhachHang(maKH, tenKH, soDienThoai, soDiem);
                        if (khachHangDAO.capNhatKhachHang(khachHang)) {
                            // Update the customer in the list
                            for (int i = 0; i < danhSachKhachHang.size(); i++) {
                                if (danhSachKhachHang.get(i).getMaKhachHang().equals(maKH)) {
                                    danhSachKhachHang.set(i, khachHang);
                                    break;
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, 
                                "Lỗi khi cập nhật khách hàng trong CSDL!", 
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                            dienvaoTable(); // Reset table data
                        }
                    }
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        txtMaKH.setText(table.getValueAt(row, 0).toString());
                        txtMaKH.setEditable(false);
                        txtTenKH.setText(table.getValueAt(row, 1).toString());
                        txtSoDienThoai.setText(table.getValueAt(row, 2).toString());
                        txtSoDiem.setText(table.getValueAt(row, 3).toString());
                    }
                }
            }
        });

        txtTimKH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTim.doClick();
            }
        });

        btnXoaTrang.addActionListener(this);
        btnTim.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);

        return pBorder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnXoaTrang)) {
            clearForm();
            dienvaoTable();
        } else if (o.equals(btnTim)) {
            String keyword = txtTimKH.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                dienvaoTable();
                JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, "Vui lòng nhập mã khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtTimKH.requestFocus();
                return;
            }

            List<KhachHang> ketQuaTimKiem = khachHangDAO.timKiemKhachHang(keyword);
            tableModel.setRowCount(0);
            if (ketQuaTimKiem.isEmpty()) {
                JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, "Không tìm thấy khách hàng với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                txtTimKH.setText("");
                dienvaoTable();
            } else {
                txtTimKH.setText("");
                for (KhachHang kh : ketQuaTimKiem) {
                    tableModel.addRow(new Object[]{
                            kh.getMaKhachHang(),
                            kh.getTenKhachHang(),
                            kh.getSoDienThoai(),
                            kh.getSoDiem()
                    });
                }
            }
        } else if (o.equals(btnThem)) {
            if (!validata()) {
                return;
            }
            try {
                String maKH = txtMaKH.getText().trim();
                String tenKH = txtTenKH.getText().trim();
                String soDienThoai = txtSoDienThoai.getText().trim();
                String soDiemStr = txtSoDiem.getText().trim();
                for (KhachHang kh : danhSachKhachHang) {
                    if (kh.getMaKhachHang().equalsIgnoreCase(maKH)) {
                        JOptionPane.showMessageDialog(this, "Mã khách hàng đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        txtMaKH.requestFocus();
                        return;
                    }
                }
                int soDiem;
                try {
                    soDiem = Integer.parseInt(soDiemStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số điểm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    txtSoDiem.requestFocus();
                    return;
                }
                KhachHang khachHang = new KhachHang(maKH, tenKH, soDienThoai, soDiem);
                if (khachHangDAO.themKhachHang(khachHang)) {
                    danhSachKhachHang.add(khachHang);
                    tableModel.addRow(new Object[]{
                            khachHang.getMaKhachHang(),
                            khachHang.getTenKhachHang(),
                            khachHang.getSoDienThoai(),
                            khachHang.getSoDiem()
                    });
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else if (o.equals(btnXoa)) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String maKH = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(QuanLiKhachHangPanel.this, "Bạn có chắc muốn xóa khách hàng " + maKH + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (khachHangDAO.xoaKhachHang(maKH)) {
                        danhSachKhachHang.removeIf(kh -> kh.getMaKhachHang().equals(maKH));
                        tableModel.removeRow(selectedRow);
                        clearForm();
                        JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, "Xóa khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, "Khách hàng đã có hoá đơn không thể xoá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, "Vui lòng chọn một khách hàng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } else if (o.equals(btnSua)) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String maKH = txtMaKH.getText().trim();
                String tenKH = txtTenKH.getText().trim();
                String soDienThoai = txtSoDienThoai.getText().trim();
                String soDiemStr = txtSoDiem.getText().trim();

                try {
                    if (!validata()) {
                        return;
                    }

                    int soDiem;
                    try {
                        soDiem = Integer.parseInt(soDiemStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Số điểm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        txtSoDiem.requestFocus();
                        return;
                    }

                    KhachHang kh = new KhachHang(maKH, tenKH, soDienThoai, soDiem);
                    if (khachHangDAO.capNhatKhachHang(kh)) {
                        for (int i = 0; i < danhSachKhachHang.size(); i++) {
                            if (danhSachKhachHang.get(i).getMaKhachHang().equals(maKH)) {
                                danhSachKhachHang.set(i, kh);
                                break;
                            }
                        }
                        
                        tableModel.setValueAt(tenKH, selectedRow, 1);
                        tableModel.setValueAt(soDienThoai, selectedRow, 2);
                        tableModel.setValueAt(soDiem, selectedRow, 3);

                        clearForm();
                        txtMaKH.setEditable(true);
                        JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, "Cập nhật khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, "Lỗi khi cập nhật khách hàng trong CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(QuanLiKhachHangPanel.this, "Vui lòng chọn một khách hàng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void styleButton(JButton button, Color bgColor) {
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
    }

    private void clearForm() {
        txtTimKH.setText("");
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSoDienThoai.setText("");
        txtSoDiem.setText("");
        table.clearSelection();
        txtMaKH.setEditable(true);
        
        String maKHMoi = khachHangDAO.taoKhachHangMoi();
        txtMaKH.setText(maKHMoi);
    }

    public void dienvaoTable() {
        tableModel.setRowCount(0);
        for (KhachHang kh : danhSachKhachHang) {
            tableModel.addRow(new Object[]{
                    kh.getMaKhachHang(),
                    kh.getTenKhachHang(),
                    kh.getSoDienThoai(),
                    kh.getSoDiem()
            });
        }
    }

    public boolean checkfield() {
        String maKH = txtMaKH.getText().trim();
        String tenKH = txtTenKH.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        String soDiem = txtSoDiem.getText().trim();

        if (maKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaKH.requestFocus();
            return false;
        }
        if (tenKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenKH.requestFocus();
            return false;
        }
        if (soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        if (soDiem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điểm không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDiem.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validata() {
        if (!checkfield()) {
            return false;
        }
        String maKH = txtMaKH.getText().trim();
        String tenKH = txtTenKH.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        String soDiemStr = txtSoDiem.getText().trim();

        if (!maKH.matches("^KH\\d{3}$")) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng phải có định dạng KH và 3 chữ số (VD: KH001)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaKH.requestFocus();
            return false;
        }
        if (!tenKH.matches("^[a-zA-ZÀ-ỹ\\s]{1,100}$")) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng chỉ được chứa chữ cái và khoảng trắng! Tối đa 100 kí tự", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenKH.requestFocus();
            return false;
        }
        if (!soDienThoai.matches("^0[35789]\\d{8}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại bắt đầu là số 0 tiếp theo là một trong các chữ số (3|8|7|5|9) còn lại 8 chữ số trong khoảng [0-9]! Tổng cộng 10 số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        int soDiem;
        try {
            soDiem = Integer.parseInt(soDiemStr);
            if (soDiem < 0) {
                JOptionPane.showMessageDialog(this, "Số điểm phải lớn hơn hoặc bằng 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSoDiem.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số điểm phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDiem.requestFocus();
            return false;
        }
        return true;
    }
}