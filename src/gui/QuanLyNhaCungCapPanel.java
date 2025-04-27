package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import dao.NhaCungCapDAO;
import entity.NhaCungCap;

public class QuanLyNhaCungCapPanel extends JPanel implements MouseListener {
    private DefaultTableModel nccTableModel;
    private JTable nccTable;
    private JTextField txtTimNCC, txtMaNCC, txtTenNCC, txtDiaChi, txtSoDienThoai, txtEmail;
    private List<NhaCungCap> danhSachNhaCungCap;
    private NhaCungCapDAO nhaCungCapDAO;

    public QuanLyNhaCungCapPanel() {
        setLayout(new BorderLayout());

        // Khởi tạo DAO
        nhaCungCapDAO = new NhaCungCapDAO();
        
        // Lấy danh sách nhà cung cấp từ CSDL
        danhSachNhaCungCap = nhaCungCapDAO.getAllNhaCungCap();

        // Tạo giao diện
        JPanel formQuanLyNCC = createFormQuanLyNCC();
        add(formQuanLyNCC, BorderLayout.CENTER);
    }

    private JPanel createFormQuanLyNCC() {
        JPanel pBorder = new JPanel(new BorderLayout());
        pBorder.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Phần trên: Tìm kiếm nhà cung cấp
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBorder(BorderFactory.createTitledBorder("Tìm nhà cung cấp"));
        txtTimNCC = new JTextField(20);
        JButton btnTimNCC = new JButton("Tìm kiếm");
        pTop.add(txtTimNCC);
        pTop.add(btnTimNCC);
        pBorder.add(pTop, BorderLayout.NORTH);

        // Phần giữa: Bảng nhà cung cấp
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã NCC", "Tên NCC", "Địa chỉ", "Số điện thoại", "Email"};
        nccTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        nccTable = new JTable(nccTableModel);
        JScrollPane scrollPane = new JScrollPane(nccTable);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Phần dưới: Form nhập liệu và nút
        JPanel pBottom = new JPanel(new BorderLayout());
        JPanel pNhap = new JPanel();
        pNhap.setLayout(new BoxLayout(pNhap, BoxLayout.Y_AXIS));
        pNhap.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Mã NCC
        JPanel pMaNCC = new JPanel(new BorderLayout());
        JLabel lblMaNCC = new JLabel("Mã nhà cung cấp:");
        lblMaNCC.setPreferredSize(new Dimension(120, 20));
        txtMaNCC = new JTextField();
        pMaNCC.add(lblMaNCC, BorderLayout.WEST);
        pMaNCC.add(txtMaNCC, BorderLayout.CENTER);
        pNhap.add(pMaNCC);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tên NCC
        JPanel pTenNCC = new JPanel(new BorderLayout());
        JLabel lblTenNCC = new JLabel("Tên nhà cung cấp:");
        lblTenNCC.setPreferredSize(new Dimension(120, 20));
        txtTenNCC = new JTextField();
        pTenNCC.add(lblTenNCC, BorderLayout.WEST);
        pTenNCC.add(txtTenNCC, BorderLayout.CENTER);
        pNhap.add(pTenNCC);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Địa chỉ
        JPanel pDiaChi = new JPanel(new BorderLayout());
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setPreferredSize(new Dimension(120, 20));
        txtDiaChi = new JTextField();
        pDiaChi.add(lblDiaChi, BorderLayout.WEST);
        pDiaChi.add(txtDiaChi, BorderLayout.CENTER);
        pNhap.add(pDiaChi);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Số điện thoại
        JPanel pSoDienThoai = new JPanel(new BorderLayout());
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
        lblSoDienThoai.setPreferredSize(new Dimension(120, 20));
        txtSoDienThoai = new JTextField();
        pSoDienThoai.add(lblSoDienThoai, BorderLayout.WEST);
        pSoDienThoai.add(txtSoDienThoai, BorderLayout.CENTER);
        pNhap.add(pSoDienThoai);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Email
        JPanel pEmail = new JPanel(new BorderLayout());
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setPreferredSize(new Dimension(120, 20));
        txtEmail = new JTextField();
        pEmail.add(lblEmail, BorderLayout.WEST);
        pEmail.add(txtEmail, BorderLayout.CENTER);
        pNhap.add(pEmail);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Các nút
        JPanel pBtn = new JPanel();
        JButton btnThemNCC = new JButton("Thêm");
        JButton btnXoaNCC = new JButton("Xóa");
        JButton btnXoaTrang = new JButton("Xoá trắng");
        JButton btnSuaNCC = new JButton("Sửa");
        pBtn.add(btnThemNCC);
        pBtn.add(btnXoaNCC);
        pBtn.add(btnXoaTrang);
        pBtn.add(btnSuaNCC);

        pBottom.add(pNhap, BorderLayout.CENTER);
        pBottom.add(pBtn, BorderLayout.SOUTH);
        pBorder.add(pBottom, BorderLayout.SOUTH);

        // Hiển thị danh sách nhà cung cấp ban đầu
        for (NhaCungCap ncc : danhSachNhaCungCap) {
            nccTableModel.addRow(new Object[]{
                    ncc.getMaNhaCungCap(),
                    ncc.getTenNhaCungCap(),
                    ncc.getDiaChi(),
                    ncc.getSoDienThoai(),
                    ncc.getEmail()
            });
        }
        
        // Thêm sự kiện cho bảng
        nccTable.addMouseListener(this);
        
        // Thêm sự kiện cho nút tìm kiếm
        txtTimNCC.addActionListener(e -> btnTimNCC.doClick());
        
        btnTimNCC.addActionListener(e -> {
            String keyword = txtTimNCC.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                // Hiển thị tất cả nhà cung cấp
                nccTableModel.setRowCount(0);
                for (NhaCungCap ncc : danhSachNhaCungCap) {
                    nccTableModel.addRow(new Object[]{
                            ncc.getMaNhaCungCap(),
                            ncc.getTenNhaCungCap(),
                            ncc.getDiaChi(),
                            ncc.getSoDienThoai(),
                            ncc.getEmail()
                    });
                }
                return;
            }

            // Tìm kiếm sử dụng DAO
            List<NhaCungCap> ketQuaTimKiem = nhaCungCapDAO.timKiemNhaCungCap(keyword);
            
            // Hiển thị kết quả tìm kiếm
            nccTableModel.setRowCount(0);
            if (ketQuaTimKiem.isEmpty()) {
                JOptionPane.showMessageDialog(QuanLyNhaCungCapPanel.this, "Không tìm thấy nhà cung cấp với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                txtTimNCC.setText("");
                loadAllData();
            } else {
                txtTimNCC.setText("");
                for (NhaCungCap ncc : ketQuaTimKiem) {
                    nccTableModel.addRow(new Object[]{
                            ncc.getMaNhaCungCap(),
                            ncc.getTenNhaCungCap(),
                            ncc.getDiaChi(),
                            ncc.getSoDienThoai(),
                            ncc.getEmail()
                    });
                }
            }
        });
        
        // Sự kiện xóa trắng
        btnXoaTrang.addActionListener(e -> clearForm());
        
        // Sự kiện thêm nhà cung cấp
        btnThemNCC.addActionListener(e -> {
            String maNCC = txtMaNCC.getText().trim();
            String tenNCC = txtTenNCC.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String soDienThoai = txtSoDienThoai.getText().trim();
            String email = txtEmail.getText().trim();

            try {
                // Kiểm tra dữ liệu trống
                if (maNCC.isEmpty() || tenNCC.isEmpty() || soDienThoai.isEmpty()) {
                    throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin bắt buộc (mã, tên, số điện thoại)!");
                }

                // Kiểm tra định dạng số điện thoại
                if (!soDienThoai.matches("\\d{10}")) {
                    throw new IllegalArgumentException("Số điện thoại phải là 10 chữ số!");
                }

                // Kiểm tra mã nhà cung cấp trùng
                if (nhaCungCapDAO.getNhaCungCapTheoMa(maNCC) != null) {
                    throw new IllegalArgumentException("Mã nhà cung cấp đã tồn tại!");
                }

                // Tạo nhà cung cấp mới
                NhaCungCap ncc = new NhaCungCap(maNCC, tenNCC, diaChi, soDienThoai, email);
                if (nhaCungCapDAO.themNhaCungCap(ncc)) {
                    danhSachNhaCungCap.add(ncc);
                    nccTableModel.addRow(new Object[]{
                            ncc.getMaNhaCungCap(),
                            ncc.getTenNhaCungCap(),
                            ncc.getDiaChi(),
                            ncc.getSoDienThoai(),
                            ncc.getEmail()
                    });

                    // Xóa form
                    clearForm();
                    JOptionPane.showMessageDialog(QuanLyNhaCungCapPanel.this, "Thêm nhà cung cấp thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new IllegalArgumentException("Lỗi khi thêm nhà cung cấp vào CSDL!");
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(QuanLyNhaCungCapPanel.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sự kiện xóa nhà cung cấp
        btnXoaNCC.addActionListener(e -> {
            int selectedRow = nccTable.getSelectedRow();
            if (selectedRow >= 0) {
                String maNCC = (String) nccTableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(QuanLyNhaCungCapPanel.this, "Bạn có chắc muốn xóa nhà cung cấp " + maNCC + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (nhaCungCapDAO.xoaNhaCungCap(maNCC)) {
                        danhSachNhaCungCap.removeIf(ncc -> ncc.getMaNhaCungCap().equals(maNCC));
                        nccTableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(QuanLyNhaCungCapPanel.this, "Xóa nhà cung cấp thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(QuanLyNhaCungCapPanel.this, "Không thể xóa nhà cung cấp này vì đã có phiếu nhập liên quan!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(QuanLyNhaCungCapPanel.this, "Vui lòng chọn một nhà cung cấp để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Sự kiện sửa nhà cung cấp
        btnSuaNCC.addActionListener(e -> {
            int selectedRow = nccTable.getSelectedRow();
            if (selectedRow >= 0) {
                String maNCC = txtMaNCC.getText().trim();
                String tenNCC = txtTenNCC.getText().trim();
                String diaChi = txtDiaChi.getText().trim();
                String soDienThoai = txtSoDienThoai.getText().trim();
                String email = txtEmail.getText().trim();

                try {
                    // Kiểm tra dữ liệu trống
                    if (tenNCC.isEmpty() || soDienThoai.isEmpty()) {
                        throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin bắt buộc (tên, số điện thoại)!");
                    }

                    // Kiểm tra định dạng số điện thoại
                    if (!soDienThoai.matches("\\d{10}")) {
                        throw new IllegalArgumentException("Số điện thoại phải là 10 chữ số!");
                    }

                    // Cập nhật nhà cung cấp
                    NhaCungCap ncc = new NhaCungCap(maNCC, tenNCC, diaChi, soDienThoai, email);
                    if (nhaCungCapDAO.capNhatNhaCungCap(ncc)) {
                        // Cập nhật danh sách trong bộ nhớ
                        for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                            if (danhSachNhaCungCap.get(i).getMaNhaCungCap().equals(maNCC)) {
                                danhSachNhaCungCap.set(i, ncc);
                                break;
                            }
                        }
                        
                        // Cập nhật bảng
                        nccTableModel.setValueAt(tenNCC, selectedRow, 1);
                        nccTableModel.setValueAt(diaChi, selectedRow, 2);
                        nccTableModel.setValueAt(soDienThoai, selectedRow, 3);
                        nccTableModel.setValueAt(email, selectedRow, 4);

                        // Xóa form
                        clearForm();
                        txtMaNCC.setEditable(true);
                        JOptionPane.showMessageDialog(QuanLyNhaCungCapPanel.this, "Cập nhật nhà cung cấp thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        throw new IllegalArgumentException("Lỗi khi cập nhật nhà cung cấp trong CSDL!");
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(QuanLyNhaCungCapPanel.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(QuanLyNhaCungCapPanel.this, "Vui lòng chọn một nhà cung cấp để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        return pBorder;
    }

    private void clearForm() {
        txtTimNCC.setText("");
        txtMaNCC.setText("");
        txtTenNCC.setText("");
        txtDiaChi.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        nccTable.clearSelection();
        txtMaNCC.setEditable(true);
    }
    
    private void loadAllData() {
        nccTableModel.setRowCount(0);
        for (NhaCungCap ncc : danhSachNhaCungCap) {
            nccTableModel.addRow(new Object[]{
                    ncc.getMaNhaCungCap(),
                    ncc.getTenNhaCungCap(),
                    ncc.getDiaChi(),
                    ncc.getSoDienThoai(),
                    ncc.getEmail()
            });
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = nccTable.getSelectedRow();
        if (row >= 0) {
            txtMaNCC.setText(nccTableModel.getValueAt(row, 0).toString());
            txtTenNCC.setText(nccTableModel.getValueAt(row, 1).toString());
            txtDiaChi.setText(nccTableModel.getValueAt(row, 2) != null ? nccTableModel.getValueAt(row, 2).toString() : "");
            txtSoDienThoai.setText(nccTableModel.getValueAt(row, 3).toString());
            txtEmail.setText(nccTableModel.getValueAt(row, 4) != null ? nccTableModel.getValueAt(row, 4).toString() : "");
            txtMaNCC.setEditable(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}