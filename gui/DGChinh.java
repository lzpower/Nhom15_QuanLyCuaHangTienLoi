package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DGChinh extends JFrame {
    private JPanel contentPanel;
    private JButton btnBanHang;
    private JButton btnQuanLiSP;
    private JButton btnThongKeDoanhThu;
    private JButton btnKhuyenMai;
    private JButton btnQuanLiNV;
    private JButton btnQuanLiKH;
    private JButton btnDangXuat;

    public DGChinh() {
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
        btnDangXuat = createButton("Đăng Xuất");

        // Add buttons to left panel
        leftPanel.add(btnBanHang);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(btnQuanLiSP);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(btnThongKeDoanhThu);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(btnKhuyenMai);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(btnQuanLiNV);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(btnQuanLiKH);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(btnDangXuat);

        // Content panel for right side
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Add action listeners
        addButtonListeners();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }

    private void addButtonListeners() {
        btnBanHang.addActionListener(e -> showPanel(new BanHangPanel()));
        btnQuanLiSP.addActionListener(e -> showPanel(new QuanLiSPPanel()));
        btnThongKeDoanhThu.addActionListener(e -> showPanel(new ThongKeDoanhThuPanel()));
        btnKhuyenMai.addActionListener(e -> showPanel(new KhuyenMaiPanel()));
        btnQuanLiNV.addActionListener(e -> showPanel(new QuanLiNVPanel()));
        btnQuanLiKH.addActionListener(e -> showPanel(new QuanLiKHPanel()));
        btnDangXuat.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
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