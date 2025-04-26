package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import connectDB.ConnectDB;
import dao.NhanVienDAO;

public class Start extends JFrame implements ActionListener {
    private JTextField txtTK;
    private JPasswordField txtMK;
    private JButton btnDN;
    private JButton btnTogglePassword;
    private boolean check = false;
    private NhanVienDAO nhanVienDAO;

    public Start() {
        super("Đăng Nhập");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Kết nối CSDL
        try {
            ConnectDB.getInstance().connect();
            nhanVienDAO = new NhanVienDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage());
            e.printStackTrace();
        }

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();

        // Tiêu đề
        JLabel lblTD = new JLabel("ĐĂNG NHẬP TÀI KHOẢN", SwingConstants.CENTER);
        lblTD.setForeground(new Color(0, 153, 255)); // Bright blue color
        lblTD.setFont(new Font("Arial", Font.BOLD, 22));

        // Nhãn và trường nhập
        JLabel lblTK = new JLabel("Tài khoản:", SwingConstants.RIGHT);
        lblTK.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel lblMK = new JLabel("Mật khẩu:", SwingConstants.RIGHT);
        lblMK.setFont(new Font("Arial", Font.PLAIN, 14));

        // Text fields with border
        txtTK = new JTextField(20);
        txtTK.setPreferredSize(new Dimension(200, 30));
        txtTK.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        txtMK = new JPasswordField(20);
        txtMK.setPreferredSize(new Dimension(200, 30));
        txtMK.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        txtMK.setEchoChar('*');

        // Buttons with modern styling
        btnTogglePassword = new JButton("Hiện");
        btnTogglePassword.setPreferredSize(new Dimension(70, 30));
        btnTogglePassword.setBackground(new Color(230, 230, 230));
        btnTogglePassword.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btnTogglePassword.setFocusPainted(false);

        btnDN = new JButton("Đăng nhập");
        btnDN.setPreferredSize(new Dimension(120, 30));
        btnDN.setBackground(new Color(230, 230, 230));
        btnDN.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btnDN.setFocusPainted(false);

        // Add title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(lblTD, gbc);

        // Add username label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 0, 5, 10);
        mainPanel.add(lblTK, gbc);

        // Add username field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        mainPanel.add(txtTK, gbc);

        // Add password label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 0, 5, 10);
        mainPanel.add(lblMK, gbc);

        // Add password field
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 10);
        mainPanel.add(txtMK, gbc);

        // Add toggle password button
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);
        mainPanel.add(btnTogglePassword, gbc);

        // Add login button (centered)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 0, 0, 0);
        mainPanel.add(btnDN, gbc);

        // Sự kiện toggle mật khẩu
        btnTogglePassword.addActionListener(e -> {
            if (txtMK.getEchoChar() == '*') {
                txtMK.setEchoChar((char) 0);
                btnTogglePassword.setText("Ẩn");
                check = true;
            } else {
                txtMK.setEchoChar('*');
                btnTogglePassword.setText("Hiện");
                check = false;
            }
        });

        // Sự kiện đăng nhập
        btnDN.addActionListener(this);
        txtTK.addActionListener(e -> txtMK.requestFocus());
        txtMK.addActionListener(e -> btnDN.doClick());
        
        // Try to set window icon if available
        try {
            setIconImage(new ImageIcon(getClass().getResource("/img/logo.png")).getImage());
        } catch (Exception e) {
            // Icon not found, continue without it
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String tk = txtTK.getText().trim();
        String mk = new String(txtMK.getPassword()).trim();

        if (tk.isEmpty() || mk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra đăng nhập từ CSDL
        if (nhanVienDAO.kiemTraDangNhap(tk, mk)) {
            SwingUtilities.invokeLater(() -> {
                new GDChinh(tk).setVisible(true);
                dispose(); // Đóng form đăng nhập
            });
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            new Start().setVisible(true);
        });
    }
}
