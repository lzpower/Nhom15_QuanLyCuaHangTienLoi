package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.NhanVienDAO;
import entity.ChucVu;
import entity.NhanVien;

public class QuanLiNVPanel extends JPanel implements MouseListener,ActionListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtTimNV, txtMaNV, txtTenNV, txtChucVu, txtSoDienThoai;
    private List<NhanVien> danhSachNhanVien;
    private NhanVienDAO nhanVienDAO;
	private String tenNV;
	private String chucVu;
	private String soDienThoai;
	private String maNV;
	private JButton btnXoaTrang;
	private JButton btnTim;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnSua;

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
        JButton btnTim = new JButton("Tìm kiếm");
        pTop.add(txtTimNV);
        pTop.add(btnTim);
        pBorder.add(pTop, BorderLayout.NORTH);

        // Phần giữa: Bảng nhân viên
        JPanel pCenter = new JPanel(new BorderLayout());
        String[] columnNames = {"Mã NV", "Tên NV", "Chức vụ", "Số điện thoại"};
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
        JScrollPane scrollPane = new JScrollPane(table);
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
        lblMaNV.setPreferredSize(new Dimension(140, 20));
        txtMaNV = new JTextField();
        pMaNV.add(lblMaNV, BorderLayout.WEST);
        pMaNV.add(txtMaNV, BorderLayout.CENTER);
        pNhap.add(pMaNV);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tên NV
        JPanel pTenNV = new JPanel(new BorderLayout());
        JLabel lblTenNV = new JLabel("Tên nhân viên:");
        lblTenNV.setPreferredSize(new Dimension(140, 20));
        txtTenNV = new JTextField();
        pTenNV.add(lblTenNV, BorderLayout.WEST);
        pTenNV.add(txtTenNV, BorderLayout.CENTER);
        pNhap.add(pTenNV);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Chức vụ
        JPanel pChucVu = new JPanel(new BorderLayout());
        JLabel lblChucVu = new JLabel("Chức vụ:");
        lblChucVu.setPreferredSize(new Dimension(140, 20));
        txtChucVu = new JTextField();
        pChucVu.add(lblChucVu, BorderLayout.WEST);
        pChucVu.add(txtChucVu, BorderLayout.CENTER);
        pNhap.add(pChucVu);
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

        // Các nút
        JPanel pBtn = new JPanel();
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnXoaTrang = new JButton("Xoá trắng");
        btnSua = new JButton("Sửa");
        pBtn.add(btnThem);
        pBtn.add(btnXoa);
        pBtn.add(btnXoaTrang);
        pBtn.add(btnSua);

        styleButton(btnThem, new Color(46, 204, 113));
        styleButton(btnXoa, new Color(231, 76, 60));
        styleButton(btnXoaTrang, new Color(52, 152, 219));
        styleButton(btnSua, new Color(41, 128, 185));
        styleButton(btnTim, new Color(41, 128, 185));
        
        pBottom.add(pNhap, BorderLayout.CENTER);
        pBottom.add(pBtn, BorderLayout.SOUTH);
        pBorder.add(pBottom, BorderLayout.SOUTH);

        // Hiển thị danh sách nhân viên ban đầu
        for (NhanVien nv : danhSachNhanVien) {
            tableModel.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getTenNhanVien(),
                    nv.getChucVu(),
                    nv.getSoDienThoai()
            });
        }
        //even
        table.addMouseListener(this);
        txtTimNV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnTim.doClick();
			}
		}	);
        btnXoaTrang.addActionListener(this);
        btnTim.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);
        return pBorder;
    }
    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o=e.getSource();
		if(o.equals(btnXoaTrang)) {
			clearForm();
		}
		else if(o.equals(btnTim)) {
			String keyword = txtTimNV.getText().trim().toLowerCase();
            if (keyword.isEmpty()) {
                // Hiển thị tất cả nhân viên
                tableModel.setRowCount(0);
                for (NhanVien nv : danhSachNhanVien) {
                    tableModel.addRow(new Object[]{
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
		}
		else if(o.equals(btnThem)) {
//			if (validata()) {
//                String maNV = txtMaNV.getText().trim();
//                String tenNV = txtTenNV.getText().trim();
//                String chucVu = txtChucVu.getText().trim();
//                String soDienThoai = txtSoDienThoai.getText().trim();
//
//                try {
//                    NhanVien nv = new NhanVien(maNV, tenNV, new ChucVu(chucVu), soDienThoai);
//                    if (nhanVienDAO.themNhanVien(nv)) {
//                        danhSachNhanVien.add(nv);
//                        tableModel.addRow(new Object[]{
//                                nv.getMaNhanVien(),
//                                nv.getTenNhanVien(),
//                                nv.getChucVu().getTenChucVu(),
//                                nv.getSoDienThoai()
//                        });
//                        clearForm();
//                        JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//                    } else {
//                        JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhân viên vào CSDL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    }
//                } catch (SQLException ex) {
//                    if (ex.getMessage().contains("Violation of PRIMARY KEY")) {
//                        JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    } else {
//                        JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    }
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//                }
//            }
            
            // Kiểm tra dữ liệu
//            try {
//                if(validata()) {
//                	NhanVien nv = new NhanVien(maNV, tenNV, new ChucVu(chucVu), soDienThoai);
//                    if (nhanVienDAO.themNhanVien(nv)) {
//                        danhSachNhanVien.add(nv);
//                        tableModel.addRow(new Object[]{
//                        		nv.getMaNhanVien(),
//                                nv.getTenNhanVien(),
//                                nv.getChucVu(),
//                                nv.getSoDienThoai()
//                        });
//
//                        // Xóa form
//                        clearForm();
//                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Thêm nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//                    }
//                } else {
//                    throw new IllegalArgumentException("Lỗi khi thêm nhân viên vào CSDL!");
//                }
//            } catch (IllegalArgumentException ex) {
//                JOptionPane.showMessageDialog(QuanLiNVPanel.this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
		}
		else if(o.equals(btnXoa)) {
			int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String maNV = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(QuanLiNVPanel.this, "Bạn có chắc muốn xóa nhân viên " + maNV + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (nhanVienDAO.xoaNhanVien(maNV)) {
                        danhSachNhanVien.removeIf(nv -> nv.getMaNhanVien().equalsIgnoreCase(maNV));
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Xóa nhân viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Nhân viên đã có hoá đơn, không thể xoá!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(QuanLiNVPanel.this, "Vui lòng chọn nhân viên để xoá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
		}
		else if(o.equals(btnSua)) {
			int selectedRow = table.getSelectedRow();
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
                    if(!maNV.matches("^NV\\d{3}$")) {
                    	throw new IllegalArgumentException("Mã nhân viên phải bắt đầu bằng NV và 3 chữ số");
                    }
                    if(!tenNV.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
                    	throw new IllegalArgumentException("Tên nhân viên phải là các kí tự!");
                    }
                    if(!chucVu.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
                    	throw new IllegalArgumentException("Chức vụ phải là các kí tự!");
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
                        tableModel.setValueAt(tenNV, selectedRow, 1);
                        tableModel.setValueAt(chucVu, selectedRow, 2);
                        tableModel.setValueAt(soDienThoai, selectedRow, 3);

                        // Xóa form
                        clearForm();
                        txtMaNV.setEditable(true);
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
		}
    public void styleButton(JButton button, Color bgColor) {
		button.setContentAreaFilled(false);
		button.setOpaque(true);
		button.setBackground(bgColor);
		button.setForeground(Color.white);
	}
    public void clearForm() {
        txtTimNV.setText("");
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtChucVu.setText("");
        txtSoDienThoai.setText("");
        table.clearSelection();
        txtMaNV.setEditable(true);
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
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int row=table.getSelectedRow();
		txtMaNV.setText(table.getValueAt(row, 0).toString());
		txtTenNV.setText(table.getValueAt(row, 1).toString());
		txtChucVu.setText(table.getValueAt(row, 2).toString());
		txtSoDienThoai.setText(table.getValueAt(row, 3).toString());
	}
	public boolean validata() {
		String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        String chucVu = txtChucVu.getText().trim();
        String soDienThoai = txtSoDienThoai.getText().trim();
		if (tenNV.isEmpty() || chucVu.isEmpty() || soDienThoai.isEmpty()) {
			 JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
			 return false;
        }
        if(!maNV.matches("^NV\\d{3}$")) {
        	JOptionPane.showMessageDialog(this,"Mã nhân viên phải bắt đầu bằng NV và 3 chữ số");
        	return false;
        }
        if(!tenNV.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
        	JOptionPane.showMessageDialog(this,"Tên nhân viên phải là các kí tự!");
        	return false;
        }
        if(!chucVu.matches("^[a-zA-ZÀ-ỹ\\s]+$")) {
        	JOptionPane.showMessageDialog(this,"Chức vụ phải là các kí tự!");
        	return false;
        }	
        // Kiểm tra định dạng số điện thoại
        if (!soDienThoai.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,"Số điện thoại phải là 10 chữ số!");
        	return false;
        }
        return true;
	}
	public boolean checkfield() {
        String maNV = txtMaNV.getText().trim();
        String tenNV = txtTenNV.getText().trim();
        String chucVu = txtChucVu.getText().trim();
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
        if (chucVu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chức vụ không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtChucVu.requestFocus();
            return false;
        }
        if (soDienThoai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDienThoai.requestFocus();
            return false;
        }
        return true;
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