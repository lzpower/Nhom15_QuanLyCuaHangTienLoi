package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.KhuyenMaiDAO;
import dao.NhanVienDAO;
import dao.SanPhamDAO;
import dao.TaiKhoanDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.SanPham;
import entity.TaiKhoan;

import java.io.File;
import java.net.URL;

public class BanHangPanel extends JPanel implements ActionListener {
	private JPanel p;
	private JButton btnTaoHoaDon;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnLamRong;
	private JButton btnInHD;
	private JButton btnTimKhachHang;
	private DefaultTableModel model;
	private JTable tb;
	private JTextField txtMaVach;
	private JTextField txtMaKhuyenMai;
	private JTextField txtTongCong;
	private JTextField txtGiamGia;
	private JTextField txtSuDungDiem;
	private JTextField txtTongTien;
	private JTextField txtSoLuong;
	private JTextField txtMaKhachHang;
	private JTextField txtTenKhachHang;
	private JTextField txtSoDienThoai;
	private JTextField txtMaHoaDon;
	private JTextField txtNhanVien;
	private JTextField txtNgayTao;
	private JScrollPane jsc;

	private SanPhamDAO sanPhamDAO;
	private HoaDonDAO hoaDonDAO;
	private ChiTietHoaDonDAO chiTietHoaDonDAO;
	private KhuyenMaiDAO khuyenMaiDAO;
	private KhachHangDAO khachHangDAO;
	private NhanVienDAO nhanVienDAO;
	private String maHoaDonHienTai;
	private String maNhanVienHienTai = "NV001"; // Giả sử mã nhân viên mặc định
	private boolean daCoHoaDon = false;
	private double tongCong = 0;

	public BanHangPanel() {
		setLayout(new BorderLayout());
		p = new JPanel(new BorderLayout());
//		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(new EmptyBorder(10, 10, 15, 10));
		add(p, BorderLayout.CENTER);

		sanPhamDAO = new SanPhamDAO();
		hoaDonDAO = new HoaDonDAO();
		chiTietHoaDonDAO = new ChiTietHoaDonDAO();
		khuyenMaiDAO = new KhuyenMaiDAO();
		khachHangDAO = new KhachHangDAO();
		nhanVienDAO = new NhanVienDAO();
		
		UIManager.put("Label.font", new Font("Time New Roman", Font.BOLD, 17));
		UIManager.put("TextField.font", new Font("Time New Roman", Font.PLAIN,
				17));
		UIManager.put("Button.font", new Font("Time New Roman", Font.BOLD, 12));

		JPanel pn = new JPanel(new GridBagLayout());
		pn.setPreferredSize(new Dimension(0,80));
		GridBagConstraints gbc = new GridBagConstraints();
		pn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		pn.add(btnTaoHoaDon = new JButton("Tạo hóa đơn"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		pn.add(new JLabel("Mã hóa đơn:"), gbc);

		gbc.gridx = 2;
		gbc.gridy = 0;
		pn.add(txtMaHoaDon = new JTextField(10), gbc);
		txtMaHoaDon.setEditable(false);

		gbc.gridx = 3;
		gbc.gridy = 0;
		pn.add(new JLabel("Nhân viên:"), gbc);

		gbc.gridx = 4;
		gbc.gridy = 0;
		pn.add(txtNhanVien = new JTextField(10), gbc);
		txtNhanVien.setEditable(false);

		gbc.gridx = 5;
		gbc.gridy = 0;
		pn.add(new JLabel("Ngày tạo:"), gbc);

		gbc.gridx = 6;
		gbc.gridy = 0;
		pn.add(txtNgayTao = new JTextField(10), gbc);
		txtNgayTao.setEditable(false);
		p.add(pn,BorderLayout.NORTH);
		


		JPanel pc = new JPanel(new BorderLayout());
		JPanel pch = new JPanel();
		pch.add(new JLabel("Nhập mã vạch:"));
		pch.add(txtMaVach = new JTextField(10));
		pch.add(new JLabel("Nhập số lượng:"));
		pch.add(txtSoLuong = new JTextField(10));
		pch.add(btnThem = new JButton("Thêm"));
		pch.add(btnXoa = new JButton("Xóa"));
		pch.add(btnLamRong = new JButton("Làm Rỗng"));
		pc.add(pch, BorderLayout.NORTH);
		
		
		String[] colName = { "STT", "Hình ảnh", "Mã SP", "Tên sản phẩm",
				"Số lượng", "Giá", "Thành Tiền" };
		Object[][] data = {};
		model = new DefaultTableModel(data, colName);
		tb = new JTable(model);
		tb.setRowHeight(80);
//		tb.setPreferredSize(new Dimension(0, 400));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < tb.getColumnCount(); i++) {
			tb.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		tb.getColumnModel().getColumn(1)
				.setCellRenderer(new DefaultTableCellRenderer() {
					@Override
					public void setValue(Object value) {
						if (value instanceof ImageIcon) {
							setIcon((ImageIcon) value);
							setText(""); // Không in text nữa
						} else {
							super.setValue(value);
						}
					}
				});
		tb.setFont(new Font("Arial", Font.PLAIN, 16));
		jsc= new JScrollPane(tb);
		jsc.setPreferredSize(new Dimension(0,100));
		pc.add(jsc, BorderLayout.CENTER);
		
		
		JPanel pcs = new JPanel(new BorderLayout());
		JPanel pcsw = new JPanel(new GridBagLayout());
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		pcsw.setPreferredSize(new Dimension(400, 0));

		gbc.gridx = 0;
		gbc.gridy = 0;
		pcsw.add(new JLabel("Mã khách hàng:"), gbc);
		gbc.gridx = 1;
		pcsw.add(txtMaKhachHang = new JTextField(10), gbc);
		gbc.gridx = 2;
		pcsw.add(btnTimKhachHang = new JButton("Tìm"), gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		pcsw.add(new JLabel("Tên khách hàng:"), gbc);
		gbc.gridx = 1;
		pcsw.add(txtTenKhachHang = new JTextField(10), gbc);
		txtTenKhachHang.setEditable(false);

		gbc.gridx = 0;
		gbc.gridy = 2;
		pcsw.add(new JLabel("Số điện thoại:"), gbc);
		gbc.gridx = 1;
		pcsw.add(txtSoDienThoai = new JTextField(10), gbc);
		txtSoDienThoai.setEditable(false);

		gbc.gridx = 0;
		gbc.gridy = 3;
		pcsw.add(new JLabel("Mã khuyến mãi:"), gbc);
		gbc.gridx = 1;
		txtMaKhuyenMai = new JTextField(10);
		txtMaKhuyenMai.setPreferredSize(new Dimension(150, 25));
		txtMaKhuyenMai.setMaximumSize(new Dimension(150, 25));
		txtMaKhuyenMai.setMinimumSize(new Dimension(150, 25));
		pcsw.add(txtMaKhuyenMai, gbc);

		JPanel pcsc = new JPanel(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		pcsc.add(new JLabel("Tổng cộng:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		txtTongCong = new JTextField(10);
		txtTongCong.setEditable(false);
		pcsc.add(txtTongCong, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		pcsc.add(new JLabel("Giảm giá:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		txtGiamGia = new JTextField(10);
		txtGiamGia.setEditable(false);
		pcsc.add(txtGiamGia, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;
		pcsc.add(new JLabel("Sử dụng điểm:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		txtSuDungDiem = new JTextField(10);
		pcsc.add(txtSuDungDiem, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0;
		pcsc.add(new JLabel("Tổng tiền:"), gbc);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		txtTongTien = new JTextField(10);
		txtTongTien.setEditable(false);
		pcsc.add(txtTongTien, gbc);

		gbc.gridy = 5;
		pcsc.add(btnInHD = new JButton("In hóa đơn"), gbc);

		pcs.add(pcsw, BorderLayout.WEST);
		pcs.add(pcsc, BorderLayout.CENTER);
		pc.add(pcs, BorderLayout.SOUTH);
		p.add(pc,BorderLayout.CENTER);

		setEnabledInputFields(false);

		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLamRong.addActionListener(this);
		btnTaoHoaDon.addActionListener(this);
		btnInHD.addActionListener(this);
		btnTimKhachHang.addActionListener(this);
	}

	private void setEnabledInputFields(boolean enabled) {
		txtMaVach.setEnabled(enabled);
		txtSoLuong.setEnabled(enabled);
		btnThem.setEnabled(enabled);
		btnXoa.setEnabled(enabled);
		btnLamRong.setEnabled(enabled);
		txtMaKhachHang.setEnabled(enabled);
		txtMaKhuyenMai.setEnabled(enabled);
		btnInHD.setEnabled(enabled);
		btnTimKhachHang.setEnabled(enabled);
	}

	// Phương thức để tạo hóa đơn mới
	private void taoHoaDonMoi() {
		maHoaDonHienTai = hoaDonDAO.taoMaHoaDonMoi();
		NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(maNhanVienHienTai);
		String tenNhanVien = nhanVien != null ? nhanVien.getTenNV()
				: "Không xác định";

		// Lấy ngày giờ hiện tại
		Date ngayHienTai = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");
		String ngayGioHienTai = dateFormat.format(ngayHienTai);

		// Hiển thị thông tin hóa đơn
		txtMaHoaDon.setText(maHoaDonHienTai);
		txtNhanVien.setText(tenNhanVien);
		txtNgayTao.setText(ngayGioHienTai);

		// Lưu hóa đơn vào database
		HoaDon hoaDon = new HoaDon(maHoaDonHienTai, ngayHienTai,
				maNhanVienHienTai);
		hoaDonDAO.themHoaDon(hoaDon);

		setEnabledInputFields(true);
		// Đánh dấu đã có hóa đơn
		daCoHoaDon = true;
		// Làm mới bảng
		model.setRowCount(0);
		tongCong = 0;
		updateTongTien();
	}

	private void themSanPhamVaoHoaDon() {
		String maVach = txtMaVach.getText().trim();
		String soLuongStr = txtSoLuong.getText().trim();
		if (maVach.isEmpty() || soLuongStr.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Vui lòng nhập mã vạch và số lượng!");
			return;
		}

		try {
			int soLuong = Integer.parseInt(soLuongStr);
			if (soLuong <= 0) {
				JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
				return;
			}

			// Lấy thông tin sản phẩm từ database
			SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(maVach);
			if (sanPham == null) {
				JOptionPane.showMessageDialog(this,
						"Không tìm thấy sản phẩm với mã vạch: " + maVach);
				return;
			}

			// Kiểm tra số lượng tồn kho
			if (sanPham.getSlHienCo() < soLuong) {
				JOptionPane.showMessageDialog(this,
						"Số lượng sản phẩm trong kho không đủ! Hiện có: "
								+ sanPham.getSlHienCo());
				return;
			}

			// Tính giá bán (giả sử giá bán = giá nhập * 1.2)
			double giaBan = sanPham.getGiaNhap() * 1.2;
			double thanhTien = giaBan * soLuong;

			// Thêm vào bảng
			int rowCount = model.getRowCount();
			ImageIcon icon = loadImageIcon(sanPham.getUrlHinhAnh());
			model.addRow(new Object[] { rowCount + 1, icon, sanPham.getMaSP(),
					sanPham.getTenSP(), soLuong, String.format("%.2f", giaBan),
					String.format("%.2f", thanhTien) });

			// Thêm chi tiết hóa đơn vào database
			ChiTietHoaDon chiTiet = new ChiTietHoaDon(maHoaDonHienTai,
					sanPham.getMaSP(), soLuong, giaBan);
			chiTietHoaDonDAO.themChiTietHoaDon(chiTiet);

			// Cập nhật số lượng sản phẩm trong kho
			sanPhamDAO.capNhatSoLuongSanPham(sanPham.getMaSP(),
					sanPham.getSlHienCo() - soLuong);

			// Cập nhật tổng tiền
			tongCong += thanhTien;
			updateTongTien();

			// Xóa nội dung các trường nhập liệu
			txtMaVach.setText("");
			txtSoLuong.setText("");
			txtMaVach.requestFocus();

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
		}
	}

	// Phương thức để xóa sản phẩm khỏi hóa đơn
	private void xoaSanPhamKhoiHoaDon() {
		int selectedRow = tb.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this,
					"Vui lòng chọn sản phẩm cần xóa!");
			return;
		}

		String maSP = model.getValueAt(selectedRow, 1).toString();
		int soLuong = Integer.parseInt(model.getValueAt(selectedRow, 3)
				.toString());
		double thanhTien = Double.parseDouble(model.getValueAt(selectedRow, 5)
				.toString().replace(",", ""));

		// Xóa chi tiết hóa đơn khỏi database
		chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maSP);

		// Cập nhật lại số lượng sản phẩm trong kho
		SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(maSP);
		if (sanPham != null) {
			sanPhamDAO.capNhatSoLuongSanPham(maSP, sanPham.getSlHienCo()
					+ soLuong);
		}

		// Xóa dòng khỏi bảng
		model.removeRow(selectedRow);

		// Cập nhật lại STT
		for (int i = 0; i < model.getRowCount(); i++) {
			model.setValueAt(i + 1, i, 0);
		}

		// Cập nhật tổng tiền
		tongCong -= thanhTien;
		updateTongTien();
	}

	// Phương thức để làm rỗng hóa đơn
	private void lamRongHoaDon() {
		// Xóa tất cả chi tiết hóa đơn khỏi database
		for (int i = 0; i < model.getRowCount(); i++) {
			String maSP = model.getValueAt(i, 1).toString();
			int soLuong = Integer.parseInt(model.getValueAt(i, 3).toString());

			// Cập nhật lại số lượng sản phẩm trong kho
			SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(maSP);
			if (sanPham != null) {
				sanPhamDAO.capNhatSoLuongSanPham(maSP, sanPham.getSlHienCo()
						+ soLuong);
			}

			chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maSP);
		}

		// Xóa tất cả dòng khỏi bảng
		model.setRowCount(0);

		// Cập nhật tổng tiền
		tongCong = 0;
		updateTongTien();
	}

	private void timKhachHang() {
		String maKhachHang = txtMaKhachHang.getText().trim();
		if (maKhachHang.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng!");
			return;
		}

		// Tìm khách hàng trong database
		KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(maKhachHang);
		if (khachHang != null) {
			// Hiển thị thông tin khách hàng
			txtTenKhachHang.setText(khachHang.getTenKH());
			txtSoDienThoai.setText(khachHang.getSoDT());
		} else {
			JOptionPane.showMessageDialog(this,
					"Không tìm thấy khách hàng với mã: " + maKhachHang);
			txtTenKhachHang.setText("");
			txtSoDienThoai.setText("");
		}
	}

	// Phương thức để cập nhật tổng tiền
	private void updateTongTien() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		txtTongCong.setText(df.format(tongCong));

		// Tính giảm giá nếu có mã khuyến mãi
		double giamGia = 0;
		String maKM = txtMaKhuyenMai.getText().trim();
		if (!maKM.isEmpty()) {
			KhuyenMai khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(maKM);
			if (khuyenMai != null) {
				giamGia = tongCong * (khuyenMai.getGiaTriKM() / 100.0);
				txtGiamGia.setText(df.format(giamGia));
			}
		}

		// Tính tổng tiền sau khi giảm giá
		double tongTien = tongCong - giamGia;
		txtTongTien.setText(df.format(tongTien));
	}

	// Phương thức để đặt lại trạng thái sau khi in hóa đơn
	private void resetHoaDon() {
		// Xóa thông tin hóa đơn
		txtMaHoaDon.setText("");
		txtNhanVien.setText("");
		txtNgayTao.setText("");

		// Xóa bảng
		model.setRowCount(0);

		// Xóa các trường nhập liệu
		txtMaVach.setText("");
		txtSoLuong.setText("");
		txtMaKhachHang.setText("");
		txtTenKhachHang.setText("");
		txtSoDienThoai.setText("");
		txtMaKhuyenMai.setText("");
		txtTongCong.setText("");
		txtGiamGia.setText("");
		txtSuDungDiem.setText("");
		txtTongTien.setText("");

		// Vô hiệu hóa các trường nhập liệu
		setEnabledInputFields(false);

		// Đặt lại trạng thái
		daCoHoaDon = false;
		tongCong = 0;
	}

	// Sửa lại phương thức inHoaDon()
	private void inHoaDon() {
	    if (!daCoHoaDon || maHoaDonHienTai == null || maHoaDonHienTai.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Chưa có hóa đơn để in!");
	        return;
	    }

	    try {
	        // Đường dẫn thư mục hiện tại
	        String thuMucLuu = System.getProperty("user.dir") + "/hoadon/";
	        File folder = new File(thuMucLuu);
	        if (!folder.exists()) {
	            folder.mkdirs();
	        }

	        File fileToSave = new File(thuMucLuu + maHoaDonHienTai + ".pdf");

	        System.out.println("File sẽ lưu vào: " + fileToSave.getAbsolutePath());

	        String giamGiaStr = txtGiamGia.getText().replace(",", "").trim();
	        String tongTienStr = txtTongTien.getText().replace(",", "").trim();

	        double giamGia = giamGiaStr.isEmpty() ? 0 : Double.parseDouble(giamGiaStr);
	        double tongTien = tongTienStr.isEmpty() ? 0 : Double.parseDouble(tongTienStr);

	        FormHoaDon formHoaDon = new FormHoaDon(maHoaDonHienTai);

	        System.out.println("Đang gọi xuatHoaDonPDF...");

	        boolean thanhCong = formHoaDon.xuatHoaDonPDF(fileToSave.getAbsolutePath(), giamGia, tongTien);

	        if (thanhCong) {
	            JOptionPane.showMessageDialog(this, "In hóa đơn thành công!\nĐã lưu tại: " + fileToSave.getAbsolutePath());
	            resetHoaDon();
	        } else {
	            JOptionPane.showMessageDialog(this, "In hóa đơn thất bại!");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + e.getMessage());
	    }
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnTaoHoaDon) {
			taoHoaDonMoi();
		} else if (e.getSource() == btnThem) {
			themSanPhamVaoHoaDon();
		} else if (e.getSource() == btnXoa) {
			xoaSanPhamKhoiHoaDon();
		} else if (e.getSource() == btnLamRong) {
			lamRongHoaDon();
		} else if (e.getSource() == btnInHD) {
			inHoaDon();
		} else if (e.getSource() == btnTimKhachHang) {
			timKhachHang();
		}
	}
	 private ImageIcon loadImageIcon(String urlHinhAnh) {
	        ImageIcon icon = null;
	        if (urlHinhAnh != null && !urlHinhAnh.isEmpty()) {
	            String mappedUrl = "/img/" + urlHinhAnh;
	            try {
	                URL imageUrl = getClass().getResource(mappedUrl);
	                if (imageUrl != null) {
	                    icon = new ImageIcon(imageUrl);
	                    Image image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
	                    icon = new ImageIcon(image);
	                } else {
	                	System.out.println("Đường dẫn hình ảnh: " + mappedUrl + ", URL: " + imageUrl);
	                }
	            } catch (Exception ex) {
	                System.err.println("Lỗi tải hình ảnh " + mappedUrl + ": " + ex.getMessage());
	            }
	        }
	        if (icon == null) {
	            try {
	                URL defaultUrl = getClass().getResource("/img/default.jpg");
	                if (defaultUrl != null) {
	                    icon = new ImageIcon(defaultUrl);
	                    Image image = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
	                    icon = new ImageIcon(image);
	                } else {
	                    System.err.println("Không tìm thấy hình ảnh mặc định: /img/default.png");
	                }
	            } catch (Exception ex) {
	                System.err.println("Lỗi tải hình ảnh mặc định: " + ex.getMessage());
	            }
	        }
	        return icon;
	    }


	public static void main(String[] args) {
		JFrame frame = new JFrame("Bán Hàng");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 700);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(new BanHangPanel()); // Gán panel vào frame
		frame.setVisible(true);
	}
}