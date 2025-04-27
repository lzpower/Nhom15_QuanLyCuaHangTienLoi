package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import connectDB.ConnectDB;
import dao.TaiKhoanDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GDChinh extends JFrame {
	private JPanel contentPanel;
	private JButton btnBanHang;
	private JButton btnQuanLiSP;
	private JButton btnThongKeDoanhThu;
	private JButton btnKhuyenMai;
	private JButton btnQuanLiNV;
	private JButton btnQuanLiKH;
	private JButton btnNhapHang; // Add this button
	private JButton btnQuanLyNhaCungCap; // Add this button
	private JButton btnQuanLyPhieuNhap; // Add this button
	private JButton btnDangXuat;
	private String vaitro;

	public GDChinh(String tenDangNhap) {
		this.vaitro = "admin"; // Default role

		// Get actual role from database if available
		if (tenDangNhap != null && !tenDangNhap.isEmpty()) {
			try {
				ConnectDB.getInstance().connect();
				TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
				entity.TaiKhoan tk = taiKhoanDAO
						.getTaiKhoanTheoTenDangNhap(tenDangNhap);
				if (tk != null) {
					this.vaitro = tk.getVaiTro();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		setTitle("Cửa Hàng Tiện Lợi - Hệ Thống Quản Lý");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Left panel for buttons
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		leftPanel.setPreferredSize(new Dimension(200, getHeight()));
		leftPanel.setBackground(new Color(240, 240, 240));

		// Initialize buttons
		btnBanHang = createButton("Bán Hàng");
		btnQuanLiSP = createButton("Quản Lý Sản Phẩm");
		btnThongKeDoanhThu = createButton("Thống Kê Doanh Thu");
		btnKhuyenMai = createButton("Khuyến Mãi");
		btnQuanLiNV = createButton("Quản Lý Nhân Viên");
		btnQuanLiKH = createButton("Quản Lý Khách Hàng");
		btnNhapHang = createButton("Nhập Hàng"); // New button
		btnQuanLyNhaCungCap = createButton("Quản Lý Nhà Cung Cấp"); // New
																	// button
		btnQuanLyPhieuNhap = createButton("Quản Lý Phiếu Nhập"); // New button
		btnDangXuat = createButton("Đăng Xuất");

		// Add buttons to left panel
		leftPanel.add(btnBanHang);
		leftPanel.add(Box.createVerticalStrut(15));
		leftPanel.add(btnQuanLiSP);
		leftPanel.add(Box.createVerticalStrut(15));
		leftPanel.add(btnThongKeDoanhThu);
		leftPanel.add(Box.createVerticalStrut(15));
		leftPanel.add(btnKhuyenMai);
		leftPanel.add(Box.createVerticalStrut(15));
		leftPanel.add(btnQuanLiNV);
		leftPanel.add(Box.createVerticalStrut(15));
		leftPanel.add(btnQuanLiKH);
		leftPanel.add(Box.createVerticalStrut(15));
		leftPanel.add(btnNhapHang); // Add new button
		leftPanel.add(Box.createVerticalStrut(15));
		leftPanel.add(btnQuanLyNhaCungCap); // Add new button
		leftPanel.add(Box.createVerticalStrut(15));
		leftPanel.add(btnQuanLyPhieuNhap); // Add new button
		leftPanel.add(Box.createVerticalStrut(15));
		leftPanel.add(btnDangXuat);
		leftPanel.add(Box.createVerticalStrut(15));

		leftPanel.add(Box.createVerticalGlue());

		// Add in4 panel at the bottom
		JPanel in4 = new JPanel();
		JLabel timeLabel = new JLabel();
		timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		in4.add(timeLabel);

		// Update time every second
		JLabel lbTenNV = new JLabel("Tên nhân viên: " + tenDangNhap);
		in4.add(lbTenNV);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Timer timer = new Timer(1000, e -> timeLabel.setText(sdf
				.format(new Date())));
		timer.start();

		leftPanel.add(in4);
		// Content panel for right side
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		// Add panels to frame
		add(leftPanel, BorderLayout.WEST);
		add(contentPanel, BorderLayout.CENTER);

		// Add action listeners
		addButtonListeners(tenDangNhap);
		btnBanHang.doClick();
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setMaximumSize(new Dimension(210, 100));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setFont(new Font("Arial", Font.PLAIN, 16));
		return button;
	}

	private void addButtonListeners(String tenDangNhap) {
		btnBanHang.addActionListener(e -> {
			try {
				showPanel(new BanHangPanel());
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(this,
						"Lỗi kết nối CSDL: " + ex.getMessage());
				ex.printStackTrace();
			}
		});

		btnQuanLiSP.addActionListener(e -> showPanel(new QuanLiSPPanel()));

		btnThongKeDoanhThu.addActionListener(e -> {
			Connection conn = ConnectDB.getInstance().getConnection(); // Lấy
																		// kết
																		// nối
																		// từ
																		// ConnectDB
				showPanel(new ThongKeDoanhThuPanel(conn)); // Truyền kết nối vào
															// panel
			});

		btnKhuyenMai.addActionListener(e -> showPanel(new KhuyenMaiPanel()));

		btnQuanLiNV.addActionListener(e -> {
			if (vaitro.equalsIgnoreCase("admin")) {
				showPanel(new QuanLiNVPanel());
			} else {
				JOptionPane.showMessageDialog(this,
						"Vui lòng đăng nhập dưới quyền admin");
			}
		});

		btnQuanLiKH.addActionListener(e -> showPanel(new QuanLiKHPanel()));

		// Add listeners for the new buttons
		btnNhapHang.addActionListener(e -> showPanel(new NhapHangPanel()));

		btnQuanLyNhaCungCap
				.addActionListener(e -> showPanel(new QuanLyNhaCungCapPanel()));

		btnQuanLyPhieuNhap
				.addActionListener(e -> showPanel(new QuanLyPhieuNhapPanel()));

		btnDangXuat.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(this,
					"Bạn có muốn đăng xuất?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				SwingUtilities.invokeLater(() -> {
					new Start().setVisible(true);
					dispose(); // Đóng MainFrame
					});
			}
		});
	}

	private void showPanel(JPanel panel) {
		contentPanel.removeAll();
		contentPanel.add(panel, BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}
}
