package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Kết nối CSDL
        try {
            ConnectDB.getInstance().connect();
            nhanVienDAO = new NhanVienDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage());
            e.printStackTrace();
        }

        // Tiêu đề
        JLabel lblTD = new JLabel("ĐĂNG NHẬP TÀI KHOẢN", SwingConstants.CENTER);
        lblTD.setForeground(new Color(33, 150, 243));
        lblTD.setFont(new Font("Arial", Font.BOLD, 22));
        lblTD.setPreferredSize(new Dimension(50, 55));

        // Nhãn và trường nhập
        JLabel lblTK = new JLabel("Tài khoản:");
        lblTK.setFont(new Font("Arial", Font.BOLD, 15));
        JLabel lblMK = new JLabel("Mật khẩu:");
        lblMK.setFont(new Font("Arial", Font.BOLD, 15));

        btnDN = new JButton("Đăng nhập");
        btnTogglePassword = new JButton("Hiện");
        btnTogglePassword.setPreferredSize(new Dimension(60, 35));

        Dimension d2 = new Dimension(10, 35);
        txtTK = new JTextField(27);
        txtMK = new JPasswordField(20);
        txtTK.setPreferredSize(d2);
        txtMK.setPreferredSize(d2);

        add(lblTD, BorderLayout.NORTH);

        // Panel chính
        JPanel pCenter = new JPanel();
        pCenter.setLayout(new GridLayout(3, 1, 0, 10));
        add(pCenter);

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel(new FlowLayout());
        JPanel p3 = new JPanel();

        p1.add(lblTK);
        p1.add(txtTK);
        p2.add(lblMK);
        p2.add(txtMK);
        p2.add(btnTogglePassword);
        p3.add(btnDN);

        pCenter.add(p1);
        pCenter.add(p2);
        pCenter.add(p3);

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
                new DGChinh().setVisible(true);
                dispose(); // Đóng form đăng nhập
            });
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Start().setVisible(true));
    }
}