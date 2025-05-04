package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import connectDB.ConnectDB;
import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;

public class QuanLiTaiKhoanPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtTimTK, txtTenDangNhap, txtMatKhau;
    private JComboBox<String> cbxVaiTro;
    private List<TaiKhoan> danhSachTaiKhoan;
    private TaiKhoanDAO taiKhoanDAO;
    private NhanVienDAO nhanVienDAO;
    private List<NhanVien> danhSachNhanVien;
    private JButton btnXoaTrang;
    private JButton btnTim;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnSua;
    private JTextField txtNhanVien;

    public QuanLiTaiKhoanPanel() {
        setLayout(new BorderLayout());

        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage());
        }
        taiKhoanDAO = new TaiKhoanDAO();
        nhanVienDAO = new NhanVienDAO();
        danhSachTaiKhoan = taiKhoanDAO.getAllTaiKhoan();
        danhSachNhanVien = nhanVienDAO.getAllNhanVien();

        JPanel formQuanLiTK = createFormQuanLiTK();
        add(formQuanLiTK, BorderLayout.CENTER);
    }

    private JPanel createFormQuanLiTK() {
        JPanel pBorder = new JPanel(new BorderLayout());
        pBorder.setBorder(new EmptyBorder(10, 10, 10, 10));

        //north
        JPanel pTop = new JPanel();
        pTop.setLayout(new BorderLayout());
        pTop.setBorder(BorderFactory.createTitledBorder("Thông tin tài khoản"));

        //textfield
        JPanel pFields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập:");
        lblTenDangNhap.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblTenDangNhap, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTenDangNhap = new JTextField(30);
        txtTenDangNhap.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtTenDangNhap, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblMatKhau, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtMatKhau = new JTextField(30);
        txtMatKhau.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtMatKhau, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel lblVaiTro = new JLabel("Vai trò:");
        lblVaiTro.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblVaiTro, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] vaiTroOptions = {"Quản lý", "Nhân viên bán hàng","Nhân viên kho"};
        cbxVaiTro = new JComboBox<>(vaiTroOptions);
        cbxVaiTro.setPreferredSize(new Dimension(400, 25));
        pFields.add(cbxVaiTro, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        JLabel lblNhanVien = new JLabel("Nhân viên:");
        lblNhanVien.setPreferredSize(new Dimension(120, 20));
        pFields.add(lblNhanVien, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNhanVien = new JTextField(30);
        txtNhanVien.setPreferredSize(new Dimension(400, 25));
        pFields.add(txtNhanVien, gbc);
        txtNhanVien.setEditable(false);
        
        //panel button
        JPanel pButtons = new JPanel();
        pButtons.setLayout(new BoxLayout(pButtons, BoxLayout.Y_AXIS));
        pButtons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

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
        JLabel lblTimTK = new JLabel("Tìm tài khoản:");
        lblTimTK.setPreferredSize(new Dimension(120, 20));
        pSearch.add(lblTimTK);
        pSearch.add(Box.createHorizontalStrut(1));
        txtTimTK = new JTextField(15);
        txtTimTK.setPreferredSize(new Dimension(200, 25));
        txtTimTK.setHorizontalAlignment(JTextField.LEFT);
        pSearch.add(txtTimTK);
        pSearch.add(Box.createRigidArea(new Dimension(5, 0)));
        btnTim = new JButton("Tìm kiếm");
        styleButton(btnTim, new Color(41, 128, 185));
        btnTim.setPreferredSize(new Dimension(120, 25));
        pSearch.add(btnTim);
        pTop.add(pSearch, BorderLayout.SOUTH);
        
        // Thêm panel search vào panel top
        JPanel pTopContainer = new JPanel();
        pTopContainer.setLayout(new BoxLayout(pTopContainer, BoxLayout.Y_AXIS));
        pTopContainer.add(pTop);
        pSearch.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pBorder.add(pTopContainer, BorderLayout.NORTH);

        // table center
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Tên đăng nhập", "Mật khẩu", "Vai trò", "Mã nhân viên", "Tên nhân viên"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 17));
        table.setFont(new Font("Tahoma", Font.PLAIN, 15));
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        pBorder.add(pCenter, BorderLayout.CENTER);
        
        // Hiển thị danh sách tài khoản ban đầu
        loadTableData();

        // even
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String tenNhanVien="";
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String tenDangNhap = tableModel.getValueAt(row, 0).toString();
                        String matKhau = tableModel.getValueAt(row, 1).toString();
                        String vaiTro = tableModel.getValueAt(row, 2).toString();
                        String maNhanVien = tableModel.getValueAt(row, 3).toString();
                        tenNhanVien = tableModel.getValueAt(row, 4).toString();
                        txtTenDangNhap.setText(tenDangNhap);
                        txtMatKhau.setText(matKhau);
                        
                        // Set vai trò
                        for (int i = 0; i < cbxVaiTro.getItemCount(); i++) {
                            if (cbxVaiTro.getItemAt(i).equals(vaiTro)) {
                                cbxVaiTro.setSelectedIndex(i);
                                break;
                            }
                        }
                        txtNhanVien.setText(maNhanVien+" - "+tenNhanVien);
                        txtTenDangNhap.setEditable(false);
                    }
                }
            }
        });

        txtTimTK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTim.doClick();
            }
        });
        
        btnXoaTrang.addActionListener(this);
        btnTim.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);
        
        return pBorder;
    }
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        for (TaiKhoan tk : danhSachTaiKhoan) {
            String maNV = tk.getNhanVien() != null ? tk.getNhanVien().getMaNhanVien() : "";
            String tenNV = tk.getNhanVien() != null ? tk.getNhanVien().getTenNhanVien() : "";
            
            tableModel.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getMatKhau(),
                    tk.getVaiTro(),
                    maNV,
                    tenNV
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnXoaTrang)) {
            clearForm();
            loadTableData();
        } else if (o.equals(btnTim)) {
            timKiemTaiKhoan();
        } else if (o.equals(btnXoa)) {
            xoaTaiKhoan();
        } else if (o.equals(btnSua)) {
            suaTaiKhoan();
        }
    }
    
    private void timKiemTaiKhoan() {
        String keyword = txtTimTK.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            tableModel.setRowCount(0);
            loadTableData();
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTimTK.requestFocus();
            return;
        }
        
        tableModel.setRowCount(0);
        boolean found = false;
        
        for (TaiKhoan tk : danhSachTaiKhoan) {
            if (tk.getTenDangNhap().toLowerCase().contains(keyword) || 
                (tk.getNhanVien() != null && tk.getNhanVien().getTenNhanVien().toLowerCase().contains(keyword))) {
                
                String maNV = tk.getNhanVien() != null ? tk.getNhanVien().getMaNhanVien() : "";
                String tenNV = tk.getNhanVien() != null ? tk.getNhanVien().getTenNhanVien() : "";
                
                tableModel.addRow(new Object[]{
                        tk.getTenDangNhap(),
                        tk.getMatKhau(),
                        tk.getVaiTro(),
                        maNV,
                        tenNV
                });
                found = true;
            }
        }
        
        if (!found) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            txtTimTK.setText("");
            loadTableData();
        } else {
            txtTimTK.setText("");
        }
    }
    
    private void clearForm() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        cbxVaiTro.setSelectedItem(null);
        txtTenDangNhap.setEditable(true);
        txtNhanVien.setText("");
        table.clearSelection();
        loadTableData();
    }
    
    private boolean validateInput() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = txtMatKhau.getText().trim();
        String nhanVienText = txtNhanVien.getText().trim();
        
        if (tenDangNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenDangNhap.requestFocus();
            return false;
        }
        
        if (matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.requestFocus();
            return false;
        }
        
        if (nhanVienText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void xoaTaiKhoan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String tenDangNhap = tableModel.getValueAt(selectedRow, 0).toString();
        
        if (tenDangNhap.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản admin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn xóa tài khoản '" + tenDangNhap + "'?", 
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = taiKhoanDAO.xoaTaiKhoan(tenDangNhap);
            if (success) {
                danhSachTaiKhoan.removeIf(tk -> tk.getTenDangNhap().equals(tenDangNhap));
                tableModel.removeRow(selectedRow);
                
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void suaTaiKhoan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) {
            return;
        }
        
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = txtMatKhau.getText().trim();
        String vaiTro = cbxVaiTro.getSelectedItem().toString();
        
        String nhanVienText = txtNhanVien.getText().trim();
        if (nhanVienText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String maNhanVien = nhanVienText.split(" - ")[0];
        
        if (tenDangNhap.equalsIgnoreCase("admin") && !vaiTro.equalsIgnoreCase("Quản lý")) {
            JOptionPane.showMessageDialog(this, "Không thể thay đổi vai trò của tài khoản admin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(maNhanVien);
        if (nhanVien == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên với mã: " + maNhanVien, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        for (TaiKhoan tk : danhSachTaiKhoan) {
            if (tk.getNhanVien() != null && 
                tk.getNhanVien().getMaNhanVien().equals(maNhanVien) && 
                !tk.getTenDangNhap().equals(tenDangNhap)) {
                JOptionPane.showMessageDialog(this, "Nhân viên này đã có tài khoản khác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        TaiKhoan taiKhoanCapNhat = new TaiKhoan(tenDangNhap, matKhau, vaiTro, nhanVien);
        boolean success = taiKhoanDAO.capNhatTaiKhoan(taiKhoanCapNhat);
        if (success) {
            for (int i = 0; i < danhSachTaiKhoan.size(); i++) {
                if (danhSachTaiKhoan.get(i).getTenDangNhap().equals(tenDangNhap)) {
                    danhSachTaiKhoan.set(i, taiKhoanCapNhat);
                    break;
                }
            }
            
            tableModel.setValueAt(matKhau, selectedRow, 1);
            tableModel.setValueAt(vaiTro, selectedRow, 2);
            tableModel.setValueAt(maNhanVien, selectedRow, 3);
            tableModel.setValueAt(nhanVien.getTenNhanVien(), selectedRow, 4);
            
            JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
}