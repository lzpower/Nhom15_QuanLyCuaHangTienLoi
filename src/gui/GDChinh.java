package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import connectDB.ConnectDB;
import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GDChinh extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JButton btnBanHang;
	private JButton btnQuanLiSP;
	private JButton btnThongKeDoanhThu;
	private JButton btnKhuyenMai;
	private JButton btnQuanLiNV;
	private JButton btnQuanLiKH;
	private JButton btnDangXuat;
	private JButton btnNutTam;
	private String vaitro;
	private String tenNhanVien; // Added to store employee name

	public GDChinh(String tenDangNhap) {
		this.vaitro = "admin"; // Default role
		this.tenNhanVien = ""; // Default empty name

		// Get actual role and employee name from database if available
		if (tenDangNhap != null && !tenDangNhap.isEmpty()) {
			try {
				ConnectDB.getInstance().connect();
				TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
				NhanVienDAO nhanVienDAO = new NhanVienDAO();
				
				TaiKhoan tk = taiKhoanDAO.getTaiKhoanTheoTenDangNhap(tenDangNhap);
				if (tk != null) {
					this.vaitro = tk.getVaiTro();
					
					// Get employee name
					if (tk.getNhanVien() != null) {
						NhanVien nv = nhanVienDAO.getNhanVienTheoMa(tk.getNhanVien().getMaNhanVien());
						if (nv != null) {
							this.tenNhanVien = nv.getTenNhanVien();
						}
					}
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
		leftPanel.setBackground(new Color(99, 166, 148));

		// Tạo panel logo không dùng FlowLayout
		JPanel logoPanel = new JPanel();
		logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
		logoPanel.setBackground(new Color(99, 166, 148)); // Cùng màu với leftPanel
		logoPanel.setMaximumSize(new Dimension(200, 72)); // Giới hạn chiều cao chính xác bằng logo

		// Tải logo với kích thước chính xác
		ImageIcon originalIcon = new ImageIcon("src/img/circlek.png");
		Image img = originalIcon.getImage();
		Image resizedImg = img.getScaledInstance(186, 72, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImg);

		// Tạo label và căn giữa
		JLabel lblimg = new JLabel(resizedIcon);
		lblimg.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Thêm label vào panel logo
		logoPanel.add(lblimg);

		// Thêm vào panel chính
		leftPanel.add(logoPanel);
		leftPanel.add(Box.createVerticalStrut(10)); // Khoảng cách với các button
		
		btnBanHang = createButton("Bán Hàng");
		btnQuanLiSP = createButton("Quản Lý Sản Phẩm");
		btnThongKeDoanhThu = createButton("Thống Kê Doanh Thu");
		btnKhuyenMai = createButton("Khuyến Mãi");
		btnQuanLiNV = createButton("Quản Lý Nhân Viên");
		btnQuanLiKH = createButton("Quản Lý Khách Hàng");
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
		leftPanel.add(btnDangXuat);
		leftPanel.add(Box.createVerticalStrut(15));

		leftPanel.add(Box.createVerticalGlue());
		leftPanel.setBackground((new Color(99, 166, 148)));
		// Add in4 panel at the bottom
		JPanel in4 = new JPanel();
		in4.setBackground(new Color(85, 133, 120));
		JLabel timeLabel = new JLabel();
		timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		in4.add(timeLabel);

		// Update time every second
		JLabel lbTenNV = new JLabel("Tên nhân viên: " + tenNhanVien); 
		JLabel lbQuyen = new JLabel("Quyền: " + vaitro);
		in4.add(lbTenNV);
		in4.add(lbQuyen);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Timer timer = new Timer(1000, e -> timeLabel.setText(sdf
				.format(new Date())));
		timer.start();

		leftPanel.add(in4);
		// Content panel for right side
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

		// Add panels to frame
		add(leftPanel, BorderLayout.WEST);
		add(contentPanel, BorderLayout.CENTER);

		// Add action listeners
		btnBanHang.addActionListener(this);
        btnQuanLiSP.addActionListener(this);
        btnThongKeDoanhThu.addActionListener(this);
        btnKhuyenMai.addActionListener(this);
        btnQuanLiNV.addActionListener(this);
        btnQuanLiKH.addActionListener(this);
        btnDangXuat.addActionListener(this);
		btnBanHang.doClick();
	}
	
	private void doiMauNutHienTai(JButton btn) {
	    if (btnNutTam != null) {
	        btnNutTam.setBackground(new Color(99, 166, 148)); // trả lại màu cũ
	    }
	    btn.setBackground(new Color(30, 89, 57)); // tô màu mới
	    btnNutTam = btn; // cập nhật nút hiện tại
	}

	private JButton createButton(String text) {
	    JButton button = new JButton(text);
	    button.setPreferredSize(new Dimension(200, 40));
	    button.setMaximumSize(new Dimension(200, 40));
	    button.setMinimumSize(new Dimension(200, 40));
	    
	    button.setBackground(new Color(99, 166, 148));
	    button.setForeground(Color.WHITE); 
	    
	    button.setHorizontalAlignment(SwingConstants.LEFT);
	    button.setHorizontalTextPosition(SwingConstants.RIGHT);
	    button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
	    
	    button.setFocusPainted(false);
	    button.setBorderPainted(false);
	    
	    button.setFont(new Font("Arial", Font.BOLD, 14));
	    button.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    return button;
	}
	private void showPanel(JPanel panel) {
		contentPanel.removeAll();
		contentPanel.add(panel, BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    Object o = e.getSource();
	    
	    try {
	        if (o == btnBanHang) {
	        	doiMauNutHienTai(btnBanHang);
	            showPanel(new BanHangPanel()); 
	        } 
	        else if (o == btnQuanLiSP) {
	        	doiMauNutHienTai(btnQuanLiSP);
	            showPanel(new QuanLiSPPanel());
	        } 
	        else if (o == btnThongKeDoanhThu) {
	        	doiMauNutHienTai(btnThongKeDoanhThu);
	            Connection conn = ConnectDB.getInstance().getConnection();
	            showPanel(new ThongKeDoanhThuPanel(conn));
	        } 
	        else if (o == btnKhuyenMai) {
	        	doiMauNutHienTai(btnKhuyenMai);
	            showPanel(new KhuyenMaiPanel());
	        } 
	        else if (o == btnQuanLiNV) {
	            if (vaitro.equalsIgnoreCase("admin")) {
	            	doiMauNutHienTai(btnQuanLiNV);
	                showPanel(new QuanLiNVPanel());
	            } else {
	                JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập dưới quyền admin");
	            }
	        } 
	        else if (o == btnQuanLiKH) {
	        	doiMauNutHienTai(btnQuanLiKH);
	            showPanel(new QuanLiKHPanel());
	        }
	        else if (o == btnDangXuat) {
	        	doiMauNutHienTai(btnDangXuat);
	            int confirm = JOptionPane.showConfirmDialog(
	                this, 
	                "Bạn có muốn đăng xuất?", 
	                "Xác nhận", 
	                JOptionPane.YES_NO_OPTION
	            );
	            
	            if (confirm == JOptionPane.YES_OPTION) {
	                new Start().setVisible(true);
	                dispose();
	            }
	        }
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + ex.getMessage());
	        ex.printStackTrace();
	    }
	}
}