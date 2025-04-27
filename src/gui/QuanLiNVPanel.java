package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import dao.NhanVienDAO;
import entity.ChucVu;
import entity.NhanVien;

public class QuanLiNVPanel extends JPanel implements MouseListener {
    private DefaultTableModel nvTableModel;
    private JTable nvTable;
    private JTextField txtTimNV, txtMaNV, txtTenNV, txtChucVu, txtSoDienThoai, txtTenDangNhap;
    private JTextField txtMatKhau;
    private List<NhanVien> danhSachNhanVien;
    private NhanVienDAO nhanVienDAO;

    public QuanLiNVPanel() {
        setLayout(new BorderLayout());

        // Khởi tạo DAO
        nhanVienDAO = new NhanVienDAO();
        
        // Lấy danh sách nhân viên từ CSDL
        danhSachNhanVien = nhanVienDAO.getAllNhanVien();
        // Tạo giao diện
        JPanel formQuanLiNV = createFormQuanLiNV();
        add(formQuanLiNV, BorderLayout.CENTER);
    }

    private JPanel createFormQuanLiNV() {
        JPanel pBorder = new JPanel(new BorderLayout());
        pBorder.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Phần trên: Tìm kiếm nhân viên
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBorder(BorderFactory.createTitledBorder("Tìm nhân viên"));
        txtTimNV = new JTextField(20);
        JButton btnTimNV = new JButton("Tìm kiếm");
        pTop.add(txtTimNV);
        pTop.add(btnTimNV);
        pBorder.add(pTop, BorderLayout.NORTH);

        // Phần giữa: Bảng nhân viên
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã NV", "Tên NV", "Chức vụ", "Số điện thoại"};
        nvTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        nvTable = new JTable(nvTableModel);
        JScrollPane scrollPane = new JScrollPane(nvTable);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Phần dưới: Form nhập liệu và nút
        JPanel pBottom = new JPanel(new BorderLayout());
        JPanel pNhap = new JPanel();
        pNhap.setLayout(new BoxLayout(pNhap, BoxLayout.Y_AXIS));
        pNhap.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Mã NV
        JPanel pMaNV = new JPanel(new BorderLayout());
        JLabel lblMaNV = new JLabel("Mã nhân viên:");
        lblMaNV.setPreferredSize(new Dimension(120, 20));
        txtMaNV = new JTextField();
        pMaNV.add(lblMaNV, BorderLayout.WEST);
        pMaNV.add(txtMaNV, BorderLayout.CENTER);
        pNhap.add(pMaNV);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tên NV
        JPanel pTenNV = new JPanel(new BorderLayout());
        JLabel lblTenNV = new JLabel("Tên nhân viên:");
        lblTenNV.setPreferredSize(new Dimension(120, 20));
        txtTenNV = new JTextField();
        pTenNV.add(lblTenNV, BorderLayout.WEST);
        pTenNV.add(txtTenNV, BorderLayout.CENTER);
        pNhap.add(pTenNV);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Chức vụ
        JPanel pChucVu = new JPanel(new BorderLayout());
        JLabel lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setPreferredSize(new Dimension(120, 20));
        txtChucVu = new JTextField();
        pChucVu.add(lblChucVu, BorderLayout.WEST);
        pChucVu.add(txtChucVu, BorderLayout.CENTER);
        pNhap.add(pChucVu);
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

        // Tên đăng nhập
        JPanel pTenDangNhap = new JPanel(new BorderLayout());
        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập:");
        lblTenDangNhap.setPreferredSize(new Dimension(120, 20));
        txtTenDangNhap = new JTextField();
        pTenDangNhap.add(lblTenDangNhap, BorderLayout.WEST);
        pTenDangNhap.add(txtTenDangNhap, BorderLayout.CENTER);
        pNhap.add(pTenDangNhap);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Mật khẩu
        JPanel pMatKhau = new JPanel(new BorderLayout());
        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setPreferredSize(new Dimension(120, 20));
        txtMatKhau = new JTextField();
        pMatKhau.add(lblMatKhau, BorderLayout.WEST);
        pMatKhau.add(txtMatKhau, BorderLayout.CENTER);
        pNhap.add(pMatKhau);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Các nút
        JPanel pBtn = new JPanel();
        JButton btnThemNV = new JButton("Thêm");
        JButton btnXoaNV = new JButton("Xóa");
        JButton btnXoaTrang = new JButton("Xoá trắng");
        JButton btnSuaNV = new JButton("Sửa");
        pBtn.add(btnThemNV);
        pBtn.add(btnXoaNV);
        pBtn.add(btnXoaTrang);
        pBtn.add(btnSuaNV);

        pBottom.add(pNhap, BorderLayout.CENTER);
        pBottom.add(pBtn, BorderLayout.SOUTH);
        pBorder.add(pBottom, BorderLayout.SOUTH);

        // Hiển thị danh sách nhân viên ban đầu
        for (NhanVien nv : danhSachNhanVien) {
            nvTableModel.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getTenNhanVien(),
                    nv.getChucVu(),
                    nv.getSoDienThoai()
            });
        }
        //mouselistener
        nvTable.addMouseListener(this);
        txtTimNV.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnTimNV.doClick();
			}
		}	);
        // Xóa trắng
        btnXoaTrang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        // Sự kiện tìm kiếm
        btnTimNV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = txtTimNV.getText().trim().toLowerCase();
                if (keyword.isEmpty()) {
                    // Hiển thị tất cả nhân viên
                    nvTableModel.setRowCount(0);
                    for (NhanVien nv : danhSachNhanVien) {
                        nvTableModel.addRow(new Object[]{
                        		nv.getMaNhanVien(),
                                nv.getTenNhanVien(),
                                nv.getChucVu(),
                                nv.getSoDienThoai()
                        });
                    }
                    JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Vui lòng nhập mã nhân viên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    txtTimNV.requestFocus();
                    return;
                }

                // Tìm kiếm sử dụng DAO
                List<NhanVien> ketQuaTimKiem = nhanVienDAO.timKiemNhanVien(keyword);
                
                // Hiển thị kết quả tìm kiếm
                nvTableModel.setRowCount(0);
                if (ketQuaTimKiem.isEmpty()) {
                    JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Không tìm thấy nhân viên với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    txtTimNV.setText("");
                    dienvaotable();
                } else {
                	txtTimNV.setText("");
                    for (NhanVien nv : ketQuaTimKiem) {
                        nvTableModel.addRow(new Object[]{
                                nv.getMaNhanVien(),
                                nv.getTenNhanVien(),
                                nv.getChucVu().getTenChucVu(),
                                nv.getSoDienThoai()
                        });
                    }
                }
            }
        });

        // Sự kiện thêm nhân viên
        btnThemNV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maNV = txtMaNV.getText().trim();
                String tenNV = txtTenNV.getText().trim();
                String chucVu = txtChucVu.getText().trim();
                String soDienThoai = txtSoDienThoai.getText().trim();

                // Kiểm tra dữ liệu
                try {
                    // Kiểm tra trống (được xử lý trong setter của NhanVien)
                    if (maNV.isEmpty() || tenNV.isEmpty() || chucVu.isEmpty() || soDienThoai.isEmpty()) {
                        throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
                    }

                    // Kiểm tra định dạng số điện thoại
                    if (!soDienThoai.matches("\\d{10}")) {
                        throw new IllegalArgumentException("Số điện thoại phải là 10 chữ số!");
                    }

                    // Kiểm tra mã nhân viên trùng
                    if (nhanVienDAO.getNhanVienTheoMa(maNV) != null) {
                        throw new IllegalArgumentException("Mã nhân viên đã tồn tại!");
                    }

                    // Thêm nhân viên
                    NhanVien nv = new NhanVien(maNV, tenNV, new ChucVu(chucVu), soDienThoai);
                    if (nhanVienDAO.themNhanVien(nv)) {
                        danhSachNhanVien.add(nv);
                        nvTableModel.addRow(new Object[]{
                        		nv.getMaNhanVien(),
                                nv.getTenNhanVien(),
                                nv.getChucVu(),
                                nv.getSoDienThoai()
                        });

                        // Xóa form
                        clearForm();
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Thêm nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        throw new IllegalArgumentException("Lỗi khi thêm nhân viên vào CSDL!");
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(QuanLiNVPanel.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Sự kiện xóa nhân viên
        btnXoaNV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = nvTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String maNV = (String) nvTableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(QuanLiNVPanel.this, "Bạn có chắc muốn xóa nhân viên " + maNV + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (nhanVienDAO.xoaNhanVien(maNV)) {
                            danhSachNhanVien.removeIf(nv -> nv.getMaNhanVien().equalsIgnoreCase(maNV));
                            nvTableModel.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Xóa nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Nhân viên đã có hoá đơn, không thể xoá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Vui lòng chọn nhân viên để xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Sự kiện sửa nhân viên
//        btnSuaNV.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int selectedRow = nvTable.getSelectedRow();
//                if (selectedRow >= 0) {
//                    String maNV = (String) nvTableModel.getValueAt(selectedRow, 0);
//                    txtMaNV.setText(maNV);
//                    txtTenNV.setText((String) nvTableModel.getValueAt(selectedRow, 1));
//                    txtChucVu.setText((String) nvTableModel.getValueAt(selectedRow, 2));
//                    txtSoDienThoai.setText((String) nvTableModel.getValueAt(selectedRow, 3));
//                    txtTenDangNhap.setText((String) nvTableModel.getValueAt(selectedRow, 4));
//                    txtMatKhau.setText((String) nvTableModel.getValueAt(selectedRow, 5));
//                    txtMaNV.setEditable(false); // Không cho sửa mã NV
//                    txtTenDangNhap.setEditable(false); // Không cho sửa tên đăng nhập
//                } else {
//                    JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Vui lòng chọn một nhân viên để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                }
//            }
//        });

        // Sự kiện lưu nhân viên
        btnSuaNV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = nvTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String maNV = txtMaNV.getText().trim();
                    String tenNV = txtTenNV.getText().trim();
                    String chucVu = txtChucVu.getText().trim();
                    String soDienThoai = txtSoDienThoai.getText().trim();

                    // Kiểm tra dữ liệu
                    try {
                        if (tenNV.isEmpty() || chucVu.isEmpty() || soDienThoai.isEmpty()) {
                            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
                        }

                        // Kiểm tra định dạng số điện thoại
                        if (!soDienThoai.matches("\\d{10}")) {
                            throw new IllegalArgumentException("Số điện thoại phải là 10 chữ số!");
                        }

                        // Cập nhật nhân viên
                        NhanVien nv = new NhanVien(maNV, tenNV, new ChucVu(chucVu), soDienThoai);
                        if (nhanVienDAO.capNhatNhanVien(nv)) {
                            // Cập nhật danh sách trong bộ nhớ
                            for (int i = 0; i < danhSachNhanVien.size(); i++) {
                                if (danhSachNhanVien.get(i).getMaNhanVien().equals(maNV)) {
                                    danhSachNhanVien.set(i, nv);
                                    break;
                                }
                            }
                            
                            // Cập nhật bảng
                            nvTableModel.setValueAt(tenNV, selectedRow, 1);
                            nvTableModel.setValueAt(chucVu, selectedRow, 2);
                            nvTableModel.setValueAt(soDienThoai, selectedRow, 3);

                            // Xóa form
                            clearForm();
                            txtMaNV.setEditable(true);
                            txtTenDangNhap.setEditable(true);
                            JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Cập nhật nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            throw new IllegalArgumentException("Nhân viên đã có hoá đơn, không thể xoá!");
                        }
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Vui lòng chọn một nhân viên để lưu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        return pBorder;
    }

    public void clearForm() {
        txtTimNV.setText("");
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtChucVu.setText("");
        txtSoDienThoai.setText("");
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        nvTable.clearSelection();
        txtMaNV.setEditable(true);
        txtTenDangNhap.setEditable(true);
    }
    public void dienvaotable() {
    	nvTableModel.setRowCount(0);
    	for (NhanVien nv : danhSachNhanVien) {
            nvTableModel.addRow(new Object[]{
            		nv.getMaNhanVien(),
                    nv.getTenNhanVien(),
                    nv.getChucVu().getTenChucVu(),
                    nv.getSoDienThoai()
            });
        }
    }
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int row=nvTable.getSelectedRow();
		txtMaNV.setText(nvTable.getValueAt(row, 0).toString());
		txtTenNV.setText(nvTable.getValueAt(row, 1).toString());
		txtChucVu.setText(nvTable.getValueAt(row, 2).toString());
		txtSoDienThoai.setText(nvTable.getValueAt(row, 3).toString());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}