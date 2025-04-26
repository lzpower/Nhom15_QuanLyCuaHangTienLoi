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

import dao.KhachHangDAO;
import entity.KhachHang;

public class QuanLiKHPanel extends JPanel implements MouseListener {
    private DefaultTableModel khTableModel;
    private JTable khTable;
    private JTextField txtTimKH, txtMaKH, txtTenKH, txtSoDienThoai, txtSoDiem;
    private List<KhachHang> danhSachKhachHang;
    private KhachHangDAO khachHangDAO;

    public QuanLiKHPanel() {
        setLayout(new BorderLayout());

        // Khởi tạo DAO
        khachHangDAO = new KhachHangDAO();
        
        // Lấy danh sách khách hàng từ CSDL
        danhSachKhachHang = khachHangDAO.getAllKhachHang();
        if (danhSachKhachHang.isEmpty()) {
            // Nếu không có dữ liệu, thêm dữ liệu mẫu
            danhSachKhachHang.add(new KhachHang("KH001", "Nguyễn Thị X", "0901234567", 100));
            danhSachKhachHang.add(new KhachHang("KH002", "Trần Văn Y", "0912345678", 50));
            danhSachKhachHang.add(new KhachHang("KH003", "Lê Thị Z", "0923456789", 200));
            danhSachKhachHang.add(new KhachHang("KH004", "Phạm Văn T", "0934567890", 0));
            
            // Lưu dữ liệu mẫu vào CSDL
            for (KhachHang kh : danhSachKhachHang) {
                khachHangDAO.themKhachHang(kh);
            }
        }

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
        JButton btnTimKH = new JButton("Tìm kiếm");
        pTop.add(txtTimKH);
        pTop.add(btnTimKH);
        pBorder.add(pTop, BorderLayout.NORTH);

        // Phần giữa: Bảng khách hàng
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã KH", "Tên KH", "Số điện thoại", "Số điểm"};
        khTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        khTable = new JTable(khTableModel);
        JScrollPane scrollPane = new JScrollPane(khTable);
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
        lblMaKH.setPreferredSize(new Dimension(120, 20));
        txtMaKH = new JTextField();
        pMaKH.add(lblMaKH, BorderLayout.WEST);
        pMaKH.add(txtMaKH, BorderLayout.CENTER);
        pNhap.add(pMaKH);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tên KH
        JPanel pTenKH = new JPanel(new BorderLayout());
        JLabel lblTenKH = new JLabel("Tên khách hàng:");
        lblTenKH.setPreferredSize(new Dimension(120, 20));
        txtTenKH = new JTextField();
        pTenKH.add(lblTenKH, BorderLayout.WEST);
        pTenKH.add(txtTenKH, BorderLayout.CENTER);
        pNhap.add(pTenKH);
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

        // Số điểm
        JPanel pSoDiem = new JPanel(new BorderLayout());
        JLabel lblSoDiem = new JLabel("Số điểm:");
        lblSoDiem.setPreferredSize(new Dimension(120, 20));
        txtSoDiem = new JTextField();
        pSoDiem.add(lblSoDiem, BorderLayout.WEST);
        pSoDiem.add(txtSoDiem, BorderLayout.CENTER);
        pNhap.add(pSoDiem);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Các nút
        JPanel pBtn = new JPanel();
        JButton btnThemKH = new JButton("Thêm");
        JButton btnXoaKH = new JButton("Xóa");
        JButton btnXoaTrang = new JButton("Xoá trắng");
        //JButton btnSuaKH = new JButton("Sửa");
        JButton btnSuaKH = new JButton("Sửa");
        pBtn.add(btnThemKH);
        pBtn.add(btnXoaKH);
        pBtn.add(btnXoaTrang);
        //pBtn.add(btnSuaKH);
        pBtn.add(btnSuaKH);

        pBottom.add(pNhap, BorderLayout.CENTER);
        pBottom.add(pBtn, BorderLayout.SOUTH);
        pBorder.add(pBottom, BorderLayout.SOUTH);

        // Hiển thị danh sách khách hàng ban đầu
        for (KhachHang kh : danhSachKhachHang) {
            khTableModel.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getTenKH(),
                    kh.getSoDT(),
                    kh.getSoDiem()
            });
        }
        txtTimKH.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnTimKH.doClick();
			}
		});
        // Sự kiện tìm kiếm
        btnTimKH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = txtTimKH.getText().trim().toLowerCase();
                if (keyword.isEmpty()) {
                    // Hiển thị tất cả khách hàng
                    khTableModel.setRowCount(0);
                    for (KhachHang kh : danhSachKhachHang) {
                        khTableModel.addRow(new Object[]{
                                kh.getMaKH(),
                                kh.getTenKH(),
                                kh.getSoDT(),
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
                khTableModel.setRowCount(0);
                if (ketQuaTimKiem.isEmpty()) {
                    JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Không tìm thấy khách hàng với từ khóa: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    txtTimKH.setText("");
                    dienvaoTable();
                } else {
                	txtTimKH.setText("");
                    for (KhachHang kh : ketQuaTimKiem) {
                        khTableModel.addRow(new Object[]{
                                kh.getMaKH(),
                                kh.getTenKH(),
                                kh.getSoDT(),
                                kh.getSoDiem()
                        });
                    }
                }
            }
        });
      //mouselistener
        khTable.addMouseListener(this);
        // Sự kiện xóa trắng
        btnXoaTrang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        // Sự kiện thêm khách hàng
        btnThemKH.addActionListener(new ActionListener() {
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
                        khTableModel.addRow(new Object[]{
                                kh.getMaKH(),
                                kh.getTenKH(),
                                kh.getSoDT(),
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
        btnXoaKH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = khTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKH = (String) khTableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(QuanLiKHPanel.this, "Bạn có chắc muốn xóa khách hàng " + maKH + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (khachHangDAO.xoaKhachHang(maKH)) {
                            danhSachKhachHang.removeIf(kh -> kh.getMaKH().equals(maKH));
                            khTableModel.removeRow(selectedRow);
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
//        btnSuaKH.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int selectedRow = khTable.getSelectedRow();
//                if (selectedRow >= 0) {
////                    String maKH = (String) khTableModel.getValueAt(selectedRow, 0);
////                    txtMaKH.setText(maKH);
////                    txtTenKH.setText((String) khTableModel.getValueAt(selectedRow, 1));
////                    txtSoDienThoai.setText((String) khTableModel.getValueAt(selectedRow, 2));
////                    txtSoDiem.setText(String.valueOf(khTableModel.getValueAt(selectedRow, 3)));
//                    txtMaKH.setEditable(false); // Không cho sửa mã KH
//                } else {
//                    JOptionPane.showMessageDialog(QuanLiKHPanel.this, "Vui lòng chọn một khách hàng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                }
//            }
//        });

        // Sự kiện lưu khách hàng
        btnSuaKH.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = khTable.getSelectedRow();
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
                                if (danhSachKhachHang.get(i).getMaKH().equals(maKH)) {
                                    danhSachKhachHang.set(i, kh);
                                    break;
                                }
                            }
                            
                            // Cập nhật bảng
                            khTableModel.setValueAt(tenKH, selectedRow, 1);
                            khTableModel.setValueAt(soDienThoai, selectedRow, 2);
                            khTableModel.setValueAt(soDiem, selectedRow, 3);

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

    private void clearForm() {
        txtTimKH.setText("");
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSoDienThoai.setText("");
        txtSoDiem.setText("");
        khTable.clearSelection();
        txtMaKH.setEditable(true);
    }
    public void dienvaoTable() {
    	khTableModel.setRowCount(0);
        for (KhachHang kh : danhSachKhachHang) {
            khTableModel.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getTenKH(),
                    kh.getSoDT(),
                    kh.getSoDiem()
            });
        }
    }
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int row=khTable.getSelectedRow();
		txtMaKH.setText(khTable.getValueAt(row, 0).toString());
		txtTenKH.setText(khTable.getValueAt(row, 1).toString());
		txtSoDienThoai.setText(khTable.getValueAt(row, 2).toString());
		txtSoDiem.setText(khTable.getValueAt(row, 3).toString());
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