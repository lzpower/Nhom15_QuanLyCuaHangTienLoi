package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.KhachHangDAO;
import entity.KhachHang;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class QuanLiKHPanel extends JPanel implements MouseListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtTimKH, txtMaKH, txtTenKH, txtSoDienThoai, txtSoDiem;
    private List<KhachHang> danhSachKhachHang;
    private KhachHangDAO khachHangDAO;

    public QuanLiKHPanel() {
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

        // Phần trên: Tìm kiếm khách hàng
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBorder(BorderFactory.createTitledBorder("Tìm khách hàng"));
        txtTimKH = new JTextField(20);
        JButton btnTim = new JButton("Tìm kiếm");
        pTop.add(txtTimKH);
        pTop.add(btnTim);
        pBorder.add(pTop, BorderLayout.NORTH);

        // Phần giữa: Bảng khách hàng
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã KH", "Tên KH", "Số điện thoại", "Số điểm"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            /**
			 * 
			 */
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

        // Phần dưới: Form nhập liệu và nút
        JPanel pBottom = new JPanel(new BorderLayout());
        JPanel pNhap = new JPanel();
        pNhap.setLayout(new BoxLayout(pNhap, BoxLayout.Y_AXIS));
        pNhap.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Mã KH
        JPanel pMaKH = new JPanel(new BorderLayout());
        JLabel lblMaKH = new JLabel("Mã khách hàng:");
        lblMaKH.setPreferredSize(new Dimension(140, 20));
        txtMaKH = new JTextField();
        pMaKH.add(lblMaKH, BorderLayout.WEST);
        pMaKH.add(txtMaKH, BorderLayout.CENTER);
        pNhap.add(pMaKH);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tên KH
        JPanel pTenKH = new JPanel(new BorderLayout());
        JLabel lblTenKH = new JLabel("Tên khách hàng:");
        lblTenKH.setPreferredSize(new Dimension(140, 20));
        txtTenKH = new JTextField();
        pTenKH.add(lblTenKH, BorderLayout.WEST);
        pTenKH.add(txtTenKH, BorderLayout.CENTER);
        pNhap.add(pTenKH);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Số điện thoại
        JPanel pSoDienThoai = new JPanel(new BorderLayout());
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
        lblSoDienThoai.setPreferredSize(new Dimension(140, 20));
        txtSoDienThoai = new JTextField();
        pSoDienThoai.add(lblSoDienThoai, BorderLayout.WEST);
        pSoDienThoai.add(txtSoDienThoai, BorderLayout.CENTER);
        pNhap.add(pSoDienThoai);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Số điểm
        JPanel pSoDiem = new JPanel(new BorderLayout());
        JLabel lblSoDiem = new JLabel("Số điểm:");
        lblSoDiem.setPreferredSize(new Dimension(140, 20));
        txtSoDiem = new JTextField();
        pSoDiem.add(lblSoDiem, BorderLayout.WEST);
        pSoDiem.add(txtSoDiem, BorderLayout.CENTER);
        pNhap.add(pSoDiem);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Các nút
        JPanel pBtn = new JPanel();
        JButton btnThem = new JButton("Thêm");
        JButton btnXoa = new JButton("Xóa");
        JButton btnXoaTrang = new JButton("Xoá trắng");
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

        // Hiển thị danh sách khách hàng ban đầu
        for (KhachHang kh : danhSachKhachHang) {
            tableModel.addRow(new Object[]{
                    kh.getMaKhachHang(),
                    kh.getTenKhachHang(),
                    kh.getSoDienThoai(),
                    kh.getSoDiem()
            });
        }
        txtTimKH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnTim.doClick();
            }
        });
        
        // Sự kiện tìm kiếm
        btnTim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = txtTimKH.getText().trim().toLowerCase();
                if (keyword.isEmpty()) {
                    // Hiển thị tất cả khách hàng
                    tableModel.setRowCount(0);
                    for (KhachHang kh : danhSachKhachHang) {
                        tableModel.addRow(new Object[]{
                                kh.getMaKhachHang(),
                                kh.getTenKhachHang(),
                                kh.getSoDienThoai(),
                                kh.getSoDiem()
                        });
                    }
                    JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Vui lòng nhập mã khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    txtTimKH.requestFocus();
                    return;
                }

                // Tìm kiếm sử dụng DAO
                List<KhachHang> ketQuaTimKiem = khachHangDAO.timKiemKhachHang(keyword);
                
                // Hiển thị kết quả tìm kiếm
                tableModel.setRowCount(0);
                if (ketQuaTimKiem.isEmpty()) {
                    JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Không tìm thấy khách hàng với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
            }
        });
        
        // mouselistener
        table.addMouseListener(this);
        
        // Sự kiện xóa trắng
        btnXoaTrang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        // Sự kiện thêm khách hàng
        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maKH = txtMaKH.getText().trim();
                String tenKH = txtTenKH.getText().trim();
                String soDienThoai = txtSoDienThoai.getText().trim();
                String soDiemStr = txtSoDiem.getText().trim();

                try {
                    // Kiểm tra dữ liệu trống
                    if (maKH.isEmpty() || tenKH.isEmpty() || soDienThoai.isEmpty() || soDiemStr.isEmpty()) {
                        throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
                    }

                    // Kiểm tra số điểm
                    int soDiem;
                    try {
                        soDiem = Integer.parseInt(soDiemStr);
                    } catch (NumberFormatException ex) {
                        throw new IllegalArgumentException("Số điểm phải là số nguyên!");
                    }

                    // Kiểm tra mã khách hàng trùng
                    if (khachHangDAO.getKhachHangTheoMa(maKH) != null) {
                        throw new IllegalArgumentException("Mã khách hàng đã tồn tại!");
                    }

                    // Tạo khách hàng mới
                    KhachHang kh = new KhachHang(maKH, tenKH, soDienThoai, soDiem);
                    if (khachHangDAO.themKhachHang(kh)) {
                        danhSachKhachHang.add(kh);
                        tableModel.addRow(new Object[]{
                                kh.getMaKhachHang(),
                                kh.getTenKhachHang(),
                                kh.getSoDienThoai(),
                                kh.getSoDiem()
                        });

                        // Xóa form
                        clearForm();
                        JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Thêm khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        throw new IllegalArgumentException("Lỗi khi thêm khách hàng vào CSDL!");
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(QuanLiKHPanel.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Sự kiện xóa khách hàng
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKH = (String) tableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(QuanLiKHPanel.this, "Bạn có chắc muốn xóa khách hàng " + maKH + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (khachHangDAO.xoaKhachHang(maKH)) {
                            danhSachKhachHang.removeIf(kh -> kh.getMaKhachHang().equals(maKH));
                            tableModel.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Xóa khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Khách hàng đã có hoá đơn không thể xoá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Vui lòng chọn một khách hàng để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Sự kiện sửa khách hàng
        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKH = txtMaKH.getText().trim();
                    String tenKH = txtTenKH.getText().trim();
                    String soDienThoai = txtSoDienThoai.getText().trim();
                    String soDiemStr = txtSoDiem.getText().trim();

                    try {
                        // Kiểm tra dữ liệu trống
                        if (tenKH.isEmpty() || soDienThoai.isEmpty() || soDiemStr.isEmpty()) {
                            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
                        }

                        // Kiểm tra số điểm
                        int soDiem;
                        try {
                            soDiem = Integer.parseInt(soDiemStr);
                        } catch (NumberFormatException ex) {
                            throw new IllegalArgumentException("Số điểm phải là số nguyên!");
                        }

                        // Cập nhật khách hàng
                        KhachHang kh = new KhachHang(maKH, tenKH, soDienThoai, soDiem);
                        if (khachHangDAO.capNhatKhachHang(kh)) {
                            // Cập nhật danh sách trong bộ nhớ
                            for (int i = 0; i < danhSachKhachHang.size(); i++) {
                                if (danhSachKhachHang.get(i).getMaKhachHang().equals(maKH)) {
                                    danhSachKhachHang.set(i, kh);
                                    break;
                                }
                            }
                            
                            // Cập nhật bảng
                            tableModel.setValueAt(tenKH, selectedRow, 1);
                            tableModel.setValueAt(soDienThoai, selectedRow, 2);
                            tableModel.setValueAt(soDiem, selectedRow, 3);

                            // Xóa form
                            clearForm();
                            txtMaKH.setEditable(true);
                            JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Cập nhật khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            throw new IllegalArgumentException("Lỗi khi cập nhật khách hàng trong CSDL!");
                        }
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(QuanLiKHPanel.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Vui lòng chọn một khách hàng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        return pBorder;
    }

    public void styleButton(JButton button, Color bgColor) {
		button.setContentAreaFilled(false);
		button.setOpaque(true);
		button.setBackground(bgColor);
		button.setForeground(Color.white);
	}
    private void clearForm() {
        txtTimKH.setText("");
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSoDienThoai.setText("");
        txtSoDiem.setText("");
        table.clearSelection();
        txtMaKH.setEditable(true);
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
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        txtMaKH.setText(table.getValueAt(row, 0).toString());
        txtTenKH.setText(table.getValueAt(row, 1).toString());
        txtSoDienThoai.setText(table.getValueAt(row, 2).toString());
        txtSoDiem.setText(table.getValueAt(row, 3).toString());
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