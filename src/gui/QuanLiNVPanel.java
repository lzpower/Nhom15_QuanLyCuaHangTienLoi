package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.ChucVu;
import entity.NhanVien;
import entity.TaiKhoan;

public class QuanLiNVPanel extends JPanel implements ActionListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtTimNV, txtMaNV, txtTenNV, txtSoDienThoai;
    private JComboBox<String> cbxChucVu;
    private List<NhanVien> danhSachNhanVien;
    private NhanVienDAO nhanVienDAO;
    private TaiKhoanDAO taiKhoanDAO;
    private JButton btnXoaTrang;
    private JButton btnTim;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnSua;

    public QuanLiNVPanel() {
        setLayout(new BorderLayout());
        nhanVienDAO = new NhanVienDAO();
        taiKhoanDAO = new TaiKhoanDAO();
        danhSachNhanVien = nhanVienDAO.getAllNhanVien();
        JPanel formQuanLiNV = createFormQuanLiNV();
        add(formQuanLiNV, BorderLayout.CENTER);
    }

    private JPanel createFormQuanLiNV() {
        JPanel pBorder = new JPanel(new BorderLayout());
        pBorder.setBorder(new EmptyBorder(10, 10, 10, 10));

        //north
        JPanel pTop = new JPanel();
        pTop.setLayout(new BorderLayout());
        pTop.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        //textfield
        JPanel pFields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel lblMaNV = new JLabel("Mã nhân viên:");
        lblMaNV.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblMaNV, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtMaNV = new JTextField(30);
        txtMaNV.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtMaNV, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel lblTenNV = new JLabel("Tên nhân viên:");
        lblTenNV.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblTenNV, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTenNV = new JTextField(30);
        txtTenNV.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtTenNV, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblChucVu, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] chucVuOptions = {"Quản lý", "Nhân viên bán hàng"};
        cbxChucVu = new JComboBox<>(chucVuOptions);
        cbxChucVu.setPreferredSize(new Dimension(400, 25));
        pFields.add(cbxChucVu, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
        lblSoDienThoai.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblSoDienThoai, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSoDienThoai = new JTextField(30);
        txtSoDienThoai.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtSoDienThoai, gbc);

        //panel button
        JPanel pButtons = new JPanel();
        pButtons.setLayout(new BoxLayout(pButtons, BoxLayout.Y_AXIS));
        pButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        btnThem = new JButton("Thêm");
        styleButton(btnThem, new Color(46, 204, 113));
        btnThem.setPreferredSize(new Dimension(120, 25));
        btnThem.setMaximumSize(new Dimension(120, 40));
        btnThem.setAlignmentX(Component.LEFT_ALIGNMENT);
        pButtons.add(btnThem);
        pButtons.add(Box.createRigidArea(new Dimension(0, 10)));

        btnXoa = new JButton("Xóa");
        styleButton(btnXoa, new Color(231, 76, 60));
        btnXoa.setPreferredSize(new Dimension(120, 25));
        btnXoa.setMaximumSize(new Dimension(120, 40));
        btnXoa.setAlignmentX(Component.LEFT_ALIGNMENT);
        pButtons.add(btnXoa);
        pButtons.add(Box.createRigidArea(new Dimension(0, 10)));

        btnXoaTrang = new JButton("Xóa trắng");
        styleButton(btnXoaTrang, new Color(52, 152, 219));
        btnXoaTrang.setPreferredSize(new Dimension(120, 25));
        btnXoaTrang.setMaximumSize(new Dimension(120, 40));
        btnXoaTrang.setAlignmentX(Component.LEFT_ALIGNMENT);
        pButtons.add(btnXoaTrang);
        pButtons.add(Box.createRigidArea(new Dimension(0, 10)));

        btnSua = new JButton("Sửa");
        styleButton(btnSua, new Color(41, 128, 185));
        btnSua.setPreferredSize(new Dimension(120, 25));
        btnSua.setMaximumSize(new Dimension(120, 40));
        btnSua.setAlignmentX(Component.LEFT_ALIGNMENT);
        pButtons.add(btnSua);

        pTop.add(pFields, BorderLayout.CENTER);
        pTop.add(pButtons, BorderLayout.EAST);

        //south
        JPanel pSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pSearch.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel lblTimNV = new JLabel("Tìm nhân viên:");
        lblTimNV.setPreferredSize(new Dimension(120, 20));
        pSearch.add(lblTimNV);
        pSearch.add(Box.createHorizontalStrut(1));
        txtTimNV = new JTextField(15);
        txtTimNV.setPreferredSize(new Dimension(200, 25));
        txtTimNV.setHorizontalAlignment(JTextField.LEFT);
        pSearch.add(txtTimNV);
        pSearch.add(Box.createRigidArea(new Dimension(5, 0)));
        btnTim = new JButton("Tìm kiếm");
        styleButton(btnTim, new Color(41, 128, 185));
        btnTim.setPreferredSize(new Dimension(120, 25));
        pSearch.add(btnTim);
        pTop.add(pSearch,BorderLayout.SOUTH);
        
        // Thêm panel search vào panel top
        JPanel pTopContainer = new JPanel();
        pTopContainer.setLayout(new BoxLayout(pTopContainer, BoxLayout.Y_AXIS));
        pTopContainer.add(pTop);
        pSearch.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pBorder.add(pTopContainer, BorderLayout.NORTH);
        

        // table center
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã NV", "Tên NV", "Chức vụ", "Số điện thoại"};
        tableModel = new DefaultTableModel(columnNames, 0) {
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
        // Hiển thị danh sách nhân viên ban đầu
        dienvaotable();

        // even
        table.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (row >= 0 && column >= 0) {
                    String maNV = (String) tableModel.getValueAt(row, 0);
                    String tenNV = (String) tableModel.getValueAt(row, 1);
                    String tenChucVu = (String) tableModel.getValueAt(row, 2);
                    String soDienThoai = (String) tableModel.getValueAt(row, 3);
                    
                    // Validate the data based on which column was edited
                    if (column == 1 && !tenNV.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, 
                            "Tên nhân viên chỉ được chứa chữ cái và khoảng trắng!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        dienvaotable(); // Reset table data
                        return;
                    }
                    
                    if (column == 2 && !tenChucVu.equals("Quản lý") && !tenChucVu.equals("Nhân viên bán hàng")) {
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, 
                            "Chức vụ không hợp lệ! Chỉ chấp nhận 'Quản lý' hoặc 'Nhân viên bán hàng'", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        dienvaotable(); // Reset table data
                        return;
                    }
                    
                    if (column == 3 && !soDienThoai.matches("\\d{10}")) {
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, 
                            "Số điện thoại phải là 10 chữ số!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        dienvaotable(); // Reset table data
                        return;
                    }
                    
                    // Create ChucVu object based on the selected position
                    String maChucVu;
                    if (tenChucVu.equals("Quản lý")) {
                        maChucVu = "CV001";
                    } else {
                        maChucVu = "CV002";
                    }
                    ChucVu chucVu = new ChucVu(maChucVu, tenChucVu);
                    
                    // Create NhanVien object with updated data
                    NhanVien nhanVien = new NhanVien(maNV, tenNV, chucVu, soDienThoai);
                    
                    // Update the employee in the database
                    if (nhanVienDAO.capNhatNhanVien(nhanVien)) {
                        // Update the employee in the list
                        for (int i = 0; i < danhSachNhanVien.size(); i++) {
                            if (danhSachNhanVien.get(i).getMaNhanVien().equals(maNV)) {
                                danhSachNhanVien.set(i, nhanVien);
                                break;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, 
                            "Lỗi khi cập nhật nhân viên trong CSDL!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        dienvaotable(); // Reset table data
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
                        txtMaNV.setText(table.getValueAt(row, 0).toString());
                        txtTenNV.setText(table.getValueAt(row, 1).toString());
                        String chucVu = table.getValueAt(row, 2).toString();
                        cbxChucVu.setSelectedItem(chucVu);
                        txtSoDienThoai.setText(table.getValueAt(row, 3).toString());
                        txtMaNV.setEditable(false);
                    }
                }
            }
        });

        txtTimNV.addActionListener(new ActionListener() {
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
            dienvaotable();
        } else if (o.equals(btnTim)) {
            String keyword = txtTimNV.getText().trim();
            if (keyword.isEmpty()) {
                tableModel.setRowCount(0);
                dienvaotable();
                JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Vui lòng nhập từ khóa tìm kiếm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtTimNV.requestFocus();
                return;
            }

            List<NhanVien> ketQuaTimKiem = nhanVienDAO.timKiemNhanVien(keyword);
            tableModel.setRowCount(0);
            if (ketQuaTimKiem.isEmpty()) {
                JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Không tìm thấy nhân viên với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                txtTimNV.setText("");
                dienvaotable();
            } else {
                txtTimNV.setText("");
                for (NhanVien nv : ketQuaTimKiem) {
                    tableModel.addRow(new Object[]{
                            nv.getMaNhanVien(),
                            nv.getTenNhanVien(),
                            nv.getChucVu().getTenChucVu(),
                            nv.getSoDienThoai()
                    });
                }
            }
        } else if (o.equals(btnThem)) {
            if (!validata()) {
                return;
            }
            try {
                String maNV = txtMaNV.getText().trim();
                String tenNV = txtTenNV.getText().trim();
                String tenChucVu = cbxChucVu.getSelectedItem().toString();
                String soDienThoai = txtSoDienThoai.getText().trim();
                for (NhanVien nv : danhSachNhanVien) {
                    if (nv.getMaNhanVien().equalsIgnoreCase(maNV)) {
                        JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        txtMaNV.requestFocus();
                        return;
                    }
                }
                String maChucVu;
                if (tenChucVu.equals("Quản lý")) {
                    maChucVu = "CV001";
                } else {
                    maChucVu = "CV002";
                }
                
                // Tạo đối tượng ChucVu và NhanVien
                ChucVu chucVu = new ChucVu(maChucVu, tenChucVu);
                NhanVien nhanVien = new NhanVien(maNV, tenNV, chucVu, soDienThoai);
                
                // Thêm nhân viên vào CSDL
                if (nhanVienDAO.themNhanVien(nhanVien)) {
                    // Thêm vào danh sách và cập nhật bảng
                    danhSachNhanVien.add(nhanVien);
                    tableModel.addRow(new Object[]{
                            nhanVien.getMaNhanVien(),
                            nhanVien.getTenNhanVien(),
                            nhanVien.getChucVu().getTenChucVu(),
                            nhanVien.getSoDienThoai()
                    });
                    
                    // Tạo tài khoản tự động cho nhân viên mới
                    String tenDangNhap = maNV; // Sử dụng mã nhân viên làm tên đăng nhập
                    String matKhau = "123"; // Mật khẩu mặc định
                    String vaiTro = tenChucVu.equals("Quản lý") ? "Admin" : "User"; // Vai trò dựa trên chức vụ
                    
                    TaiKhoan taiKhoan = new TaiKhoan(tenDangNhap, matKhau, vaiTro, nhanVien);
                    
                    if (taiKhoanDAO.themTaiKhoan(taiKhoan)) {
                        JOptionPane.showMessageDialog(this, 
                            "Thêm nhân viên thành công và đã tạo tài khoản với tên đăng nhập: " + tenDangNhap + 
                            ", mật khẩu mặc định: 123", 
                            "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Thêm nhân viên thành công nhưng không thể tạo tài khoản!", 
                            "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                    
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else if (o.equals(btnXoa)) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String maNV = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(QuanLiNVPanel.this, "Bạn có chắc muốn xóa nhân viên " + maNV + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (nhanVienDAO.xoaNhanVien(maNV)) {
                        danhSachNhanVien.removeIf(nv -> nv.getMaNhanVien().equalsIgnoreCase(maNV));
                        tableModel.removeRow(selectedRow);
                        clearForm();
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Xóa nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Không thể xóa nhân viên này. Nhân viên có thể đã có dữ liệu liên quan!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Vui lòng chọn nhân viên để xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } else if (o.equals(btnSua)) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Kiểm tra xem form có hợp lệ không
                if (!validata()) {
                    return;
                }
                
                try {
                    String maNV = txtMaNV.getText().trim();
                    String tenNV = txtTenNV.getText().trim();
                    String tenChucVu = cbxChucVu.getSelectedItem().toString();
                    String soDienThoai = txtSoDienThoai.getText().trim();
                    
                    // Tạo mã chức vụ dựa trên tên chức vụ
                    String maChucVu;
                    if (tenChucVu.equals("Quản lý")) {
                        maChucVu = "CV001";
                    } else {
                        maChucVu = "CV002";
                    }
                    
                    // Tạo đối tượng ChucVu và NhanVien
                    ChucVu chucVu = new ChucVu(maChucVu, tenChucVu);
                    NhanVien nhanVien = new NhanVien(maNV, tenNV, chucVu, soDienThoai);
                    
                    if (nhanVienDAO.capNhatNhanVien(nhanVien)) {
                        // Cập nhật danh sách và bảng
                        for (int i = 0; i < danhSachNhanVien.size(); i++) {
                            if (danhSachNhanVien.get(i).getMaNhanVien().equals(maNV)) {
                                danhSachNhanVien.set(i, nhanVien);
                                break;
                            }
                        }
                        
                        tableModel.setValueAt(tenNV, selectedRow, 1);
                        tableModel.setValueAt(tenChucVu, selectedRow, 2);
                        tableModel.setValueAt(soDienThoai, selectedRow, 3);

                        clearForm();
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Cập nhật nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Cập nhật nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Vui lòng chọn một nhân viên để cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void styleButton(JButton button, Color bgColor) {
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setForeground(Color.white);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); //hover
        button.setFocusPainted(false); // bỏ viền
    }

    public void clearForm() {
        txtTimNV.setText("");
        txtMaNV.setText("");
        txtTenNV.setText("");
        cbxChucVu.setSelectedIndex(0);
        txtSoDienThoai.setText("");
        table.clearSelection();
        txtMaNV.setEditable(true);
        
        // Tự động tạo mã nhân viên mới
        String maNVMoi = nhanVienDAO.taoMaNhanVienMoi();
        txtMaNV.setText(maNVMoi);
    }

    public void dienvaotable() {
        tableModel.setRowCount(0);
        for (NhanVien nv : danhSachNhanVien) {
            tableModel.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getTenNhanVien(),
                    nv.getChucVu().getTenChucVu(),
                    nv.getSoDienThoai()
            });
        }
    }
    public boolean validata() {
        if (!checkfield()) {
            return false;
        }
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
        if (!maNV.matches("^NV\\d{3}$")) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên phải có định dạng NV và 3 chữ số (VD: NV001)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaNV.requestFocus();
            return false;
        }
        if (!tenNV.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên chỉ được chứa chữ cái và khoảng trắng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenNV.requestFocus();
            return false;
        }
        if (!soDienThoai.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là 10 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        return true;
    }
    public boolean checkfield() {
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        String chucVu = (String) cbxChucVu.getSelectedItem();
        String soDienThoai = txtSoDienThoai.getText().trim();

        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaNV.requestFocus();
            return false;
        }
        if (tenNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenNV.requestFocus();
            return false;
        }
        if (chucVu == null) {
            JOptionPane.showMessageDialog(this, "Chức vụ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            cbxChucVu.requestFocus();
            return false;
        }
        if (soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        return true;
    }
}