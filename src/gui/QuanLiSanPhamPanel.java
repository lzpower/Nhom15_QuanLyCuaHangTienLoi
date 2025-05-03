package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.LoaiSanPhamDAO;
import dao.SanPhamDAO;
import entity.LoaiSanPham;
import entity.SanPham;

public class QuanLiSanPhamPanel extends JPanel implements ActionListener {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton btnTim;
    private JButton btnLoc;
    private JTextField txtTim;
    private JComboBox<String> cbxLoc;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnCapNhat;
    private JButton btnChonAnh;
    private JTextField txtMa;
    private JTextField txtTen;
    private JComboBox<String> cbxLoai;
    private JTextField txtSLHienCo;
    private JTextField txtGiaNhap;
    private JTextField txtGiaBan;
    private JTextField txtUrlHinhAnh;
    private JLabel lblHinhAnh;

    private SanPhamDAO sanPhamDAO;
    private LoaiSanPhamDAO loaiSanPhamDAO;
    private List<LoaiSanPham> danhSachLoaiSP;

    private ExecutorService executorService;
    private List<SanPham> cachedProducts;
	private JButton btnXoaTrang;
	private JTextField txtSLThem;

    public QuanLiSanPhamPanel() {
        setLayout(new BorderLayout());

		// Khởi tạo nhóm luồng cho các tác vụ nền
        executorService = Executors.newFixedThreadPool(2);

        // Khởi tạo DAO
        sanPhamDAO = new SanPhamDAO();
        loaiSanPhamDAO = new LoaiSanPhamDAO();

        // Lấy danh sách loại sản phẩm
        danhSachLoaiSP = loaiSanPhamDAO.getAllLoaiSanPham();
        if (danhSachLoaiSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có loại sản phẩm nào trong CSDL!");
        }

        JPanel pBorder = new JPanel(new BorderLayout());
        pBorder.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(pBorder);

        // Trung tâm: Tìm SP và Bảng SP
        JPanel pCenter = new JPanel();
        pCenter.setLayout(new BorderLayout());
        
        // Tìm SP
        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTim.setBorder(BorderFactory.createTitledBorder("Tìm sản phẩm"));
        txtTim = new JTextField(20);
        btnTim = new JButton("Tìm kiếm");

        // Tạo danh sách loại sản phẩm cho combobox
        String[] loaiSPArray = new String[danhSachLoaiSP.size() + 1];
        loaiSPArray[0] = "Tất cả";
        for (int i = 0; i < danhSachLoaiSP.size(); i++) {
            loaiSPArray[i + 1] = danhSachLoaiSP.get(i).getTenLoaiSanPham();
        }

        cbxLoc = new JComboBox<>(loaiSPArray);
        btnLoc = new JButton("Lọc theo loại");

        pTim.add(txtTim);
        pTim.add(btnTim);
        pTim.add(cbxLoc);
        pTim.add(btnLoc);
        pCenter.add(pTim, BorderLayout.NORTH);

        // Bảng SP 
        String[] columnNames = {"Hình ảnh", "Mã vạch", "Tên sản phẩm", "Loại", "Số lượng", "Giá nhập", "Giá bán"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return ImageIcon.class;
                return super.getColumnClass(columnIndex);
            }
        };
        productTable = new JTable(tableModel);
        productTable.setRowHeight(150);
        
        // Điều chỉnh chiều rộng các cột
        productTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Hình ảnh
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Mã sản phẩm
        productTable.getColumnModel().getColumn(2).setPreferredWidth(600); // Tên sản phẩm
        productTable.getColumnModel().getColumn(3).setPreferredWidth(250); // Loại
        productTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Số lượng
        productTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Giá nhập
        productTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Giá bán
      
        //Căn giữa các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        //Tiêu đề
        ((DefaultTableCellRenderer)productTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        productTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Mã sản phẩm
        productTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Loại
        productTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Số lượng
        productTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Giá nhập
        productTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Giá bán
        
        productTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 17));
        productTable.setFont(new Font("Tahoma", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(productTable);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Panel chính chứa hình ảnh, nhập liệu và nút
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel Hình ảnh 
        JPanel pHinhAnh = new JPanel(new BorderLayout());
        pHinhAnh.setPreferredSize(new Dimension(200, 200));

        // Panel nhập URL hình ảnh và nút chọn
        JPanel pUrlHinhAnh = new JPanel(new BorderLayout());
        JLabel lblUrlHinhAnh = new JLabel("URL hình ảnh:");
        txtUrlHinhAnh = new JTextField();
        btnChonAnh = new JButton("Chọn ảnh");
        pUrlHinhAnh.add(lblUrlHinhAnh, BorderLayout.WEST);
        pUrlHinhAnh.add(btnChonAnh, BorderLayout.EAST);

        // Panel preview hình
        JPanel pPreviewHinhAnh = new JPanel(new BorderLayout());
        lblHinhAnh = new JLabel();
        lblHinhAnh.setPreferredSize(new Dimension(150, 150));
        lblHinhAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblHinhAnh.setHorizontalAlignment(SwingConstants.CENTER);
        pPreviewHinhAnh.add(lblHinhAnh, BorderLayout.CENTER);

        pHinhAnh.add(pUrlHinhAnh, BorderLayout.NORTH);
        pHinhAnh.add(pPreviewHinhAnh, BorderLayout.CENTER);

        // Panel Nhập liệu (ở giữa)
        JPanel pNhap = new JPanel();
        pNhap.setLayout(new BoxLayout(pNhap, BoxLayout.Y_AXIS));
        pNhap.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Set chiều rộng
        int textFieldWidth = 300;
        Dimension textFieldDimension = new Dimension(textFieldWidth, 25);
        Dimension halfTextFieldDimension = new Dimension((textFieldWidth - 10) / 2, 25);

        // Mã SP
        JPanel pMa = new JPanel(new BorderLayout());
        JLabel lblMa = new JLabel("Mã sản phẩm:");
        lblMa.setPreferredSize(new Dimension(120, 20));
        txtMa = new JTextField();
        txtMa.setPreferredSize(textFieldDimension);
        pMa.add(lblMa, BorderLayout.WEST);
        pMa.add(txtMa, BorderLayout.CENTER);
        pNhap.add(pMa);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tên SP
        JPanel pTen = new JPanel(new BorderLayout());
        JLabel lblTen = new JLabel("Tên sản phẩm:");
        lblTen.setPreferredSize(new Dimension(120, 20));
        txtTen = new JTextField();
        txtTen.setPreferredSize(textFieldDimension);
        pTen.add(lblTen, BorderLayout.WEST);
        pTen.add(txtTen, BorderLayout.CENTER);
        pNhap.add(pTen);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Loại + SL Hiện có + SL Thêm
        JPanel pLoaiSL = new JPanel();
        pLoaiSL.setLayout(new BoxLayout(pLoaiSL, BoxLayout.X_AXIS));

        // Loại
        JPanel pLoai = new JPanel(new BorderLayout());
        JLabel lblLoai = new JLabel("Loại:");
        lblLoai.setPreferredSize(new Dimension(120, 20));
        cbxLoai = new JComboBox<>(loaiSPArray);
        cbxLoai.setPreferredSize(halfTextFieldDimension);
        cbxLoai.setMinimumSize(halfTextFieldDimension);
        cbxLoai.setMaximumSize(halfTextFieldDimension);
        pLoai.add(lblLoai, BorderLayout.WEST);
        pLoai.add(cbxLoai, BorderLayout.CENTER);
        pLoaiSL.add(pLoai);
        pLoaiSL.add(Box.createRigidArea(new Dimension(10, 0)));

        // SL Hiện có
        JPanel pSLHienCo = new JPanel(new BorderLayout());
        JLabel lblSLHienCo = new JLabel("Số lượng hiện có:");
        lblSLHienCo.setPreferredSize(new Dimension(180, 20));
        txtSLHienCo = new JTextField();
        txtSLHienCo.setEditable(false);
        // Force the text field to have the same width as other components
        txtSLHienCo.setPreferredSize(halfTextFieldDimension);
        txtSLHienCo.setMinimumSize(halfTextFieldDimension);
        txtSLHienCo.setMaximumSize(halfTextFieldDimension);
        pSLHienCo.add(lblSLHienCo, BorderLayout.WEST);
        pSLHienCo.add(txtSLHienCo, BorderLayout.CENTER);
        pLoaiSL.add(pSLHienCo);
        pLoaiSL.add(Box.createRigidArea(new Dimension(10, 0)));
        
        // SL thêm
        JPanel pSLThem = new JPanel(new BorderLayout());
        JLabel lblSLThem = new JLabel("Số lượng thêm:");
        lblSLThem.setPreferredSize(new Dimension(150, 20));
        txtSLThem = new JTextField();
        // Force the text field to have the same width as other components
        txtSLThem.setPreferredSize(halfTextFieldDimension);
        txtSLThem.setMinimumSize(halfTextFieldDimension);
        txtSLThem.setMaximumSize(halfTextFieldDimension);
        pSLThem.add(lblSLThem, BorderLayout.WEST);
        pSLThem.add(txtSLThem, BorderLayout.CENTER);
        pLoaiSL.add(pSLThem);

        //Thêm vào panel chính
        pNhap.add(pLoaiSL);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Giá nhập
        JPanel pGiaNhap = new JPanel(new BorderLayout());
        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setPreferredSize(new Dimension(120, 20));
        txtGiaNhap = new JTextField();
        txtGiaNhap.setPreferredSize(textFieldDimension);
        pGiaNhap.add(lblGiaNhap, BorderLayout.WEST);
        pGiaNhap.add(txtGiaNhap, BorderLayout.CENTER);
        pNhap.add(pGiaNhap);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Giá bán
        JPanel pGiaBan = new JPanel(new BorderLayout());
        JLabel lblGiaBan = new JLabel("Giá bán:");
        lblGiaBan.setPreferredSize(new Dimension(120, 20));
        txtGiaBan = new JTextField();
        txtGiaBan.setPreferredSize(textFieldDimension);
        txtGiaBan.setEditable(false);
        pGiaBan.add(lblGiaBan, BorderLayout.WEST);
        pGiaBan.add(txtGiaBan, BorderLayout.CENTER);
        pNhap.add(pGiaBan);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));
        
        


        //Panel Nút (bên phải)
        JPanel pNut = new JPanel(new GridLayout(4, 1, 10, 10));
        pNut.setPreferredSize(new Dimension(120, 150));
        btnThem = new JButton("THÊM");
        btnXoaTrang = new JButton("XÓA TRẮNG");
        btnXoa = new JButton("XÓA");
        btnCapNhat = new JButton("CẬP NHẬT");

        pNut.add(btnThem);
        pNut.add(btnXoa);
        pNut.add(btnXoaTrang);
        pNut.add(btnCapNhat);
        
        dinhDangNut(btnThem, new Color(46, 204, 113));
        dinhDangNut(btnXoa, new Color(231, 76, 60));
        dinhDangNut(btnXoaTrang, new Color(52, 152, 219));
        dinhDangNut(btnCapNhat, new Color(41, 128, 185));
        dinhDangNut(btnTim, new Color(41, 128, 185));
        dinhDangNut(btnChonAnh, new Color(41, 128, 185));
        dinhDangNut(btnLoc, new Color(46, 204, 113));
       
        

        // Gộp các panel vào pBottom
        pBottom.add(pHinhAnh, BorderLayout.WEST);
        pBottom.add(pNhap, BorderLayout.CENTER);
        pBottom.add(pNut, BorderLayout.EAST);

        // Add vào giao diện chính
        pBorder.add(pBottom, BorderLayout.NORTH);

        // Sự kiện
        btnTim.addActionListener(this);
        btnLoc.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnCapNhat.addActionListener(this);
        btnChonAnh.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        

        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = productTable.getSelectedRow();
                    if (row >= 0) {
                        txtMa.setText(productTable.getValueAt(row, 1).toString()); // Mã sản phẩm
                        txtTen.setText(productTable.getValueAt(row, 2).toString()); // Tên sản phẩm
                        String tenLoai = productTable.getValueAt(row, 3).toString(); // Loại
                        for (int i = 0; i < cbxLoai.getItemCount(); i++) {
                            if (cbxLoai.getItemAt(i).equals(tenLoai)) {
                                cbxLoai.setSelectedIndex(i);
                                break;
                            }
                        }
                        txtSLHienCo.setText(productTable.getValueAt(row, 4).toString()); // Số lượng
                        txtSLThem.setText("0");
                        executorService.submit(() -> {
                            try {
                                SanPham sp = sanPhamDAO.getSanPhamTheoMa(txtMa.getText().trim());
                                if (sp != null) {
                                    SwingUtilities.invokeLater(() -> {
                                        txtUrlHinhAnh.setText(sp.getUrlHinhAnh());
                                        txtGiaNhap.setText(String.valueOf(sp.getGiaNhap()));
                                        txtGiaBan.setText(String.valueOf(sp.getGiaBan())); 
                                        capNhatXemTruocHinhAnh();
                                    });
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });

                     // Tải thông tin chi tiết sản phẩm trong nền
                        executorService.submit(() -> {
                            try {
                                SanPham sp = sanPhamDAO.getSanPhamTheoMa(txtMa.getText().trim());
                                if (sp != null) {
                                    SwingUtilities.invokeLater(() -> {
                                        txtUrlHinhAnh.setText(sp.getUrlHinhAnh()); 
                                        capNhatXemTruocHinhAnh(); // Hiển thị hình ảnh khi click vào sản phẩm
                                    });
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                }
            }
        });
        
        // Sự kiện cho URL hình ảnh để hiển thị 
        txtUrlHinhAnh.addActionListener(e -> capNhatXemTruocHinhAnh());

        // Load dữ liệu sản phẩm từ CSDL 
        taiDuLieuSanPhamTuCSDL();
    }
    
    private void taiDuLieuSanPhamTuCSDL() {
    	// Hiển thị chỉ báo tải
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        executorService.submit(() -> {
            try {
            	// Lưu trữ sản phẩm để truy cập nhanh hơn
                cachedProducts = sanPhamDAO.getAllSanPham();
                
                if (cachedProducts == null || cachedProducts.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, 
                            "Không có sản phẩm nào trong cơ sở dữ liệu!");
                        setCursor(Cursor.getDefaultCursor());
                    });
                    return;
                }
                
             // Cập nhật UI theo từng đợt 
                final int BATCH_SIZE = 20;
                for (int i = 0; i < cachedProducts.size(); i += BATCH_SIZE) {
                    final int start = i;
                    final int end = Math.min(i + BATCH_SIZE, cachedProducts.size());
                    
                    SwingUtilities.invokeLater(() -> {
                        for (int j = start; j < end; j++) {
                            updateTableRow(-1, cachedProducts.get(j), true);
                        }
                        
                        // Nếu là đợt cuối cùng, đặt lại con trỏ
                        if (end == cachedProducts.size()) {
                            setCursor(Cursor.getDefaultCursor());
                        }
                    });
                    
                 // Độ trễ nhỏ để cho phép UI cập nhật
                    Thread.sleep(50);
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, 
                        "Lỗi khi tải dữ liệu: " + e.getMessage());
                    setCursor(Cursor.getDefaultCursor());
                });
                e.printStackTrace();
            }
        });
    }
    
    private void capNhatXemTruocHinhAnh() {
        String urlHinhAnh = txtUrlHinhAnh.getText().trim();
        if (!urlHinhAnh.isEmpty()) {
        	// Tải hình ảnh trong nền
            executorService.submit(() -> {
                ImageIcon icon = taiBieuTuongHinhAnh(urlHinhAnh);
                SwingUtilities.invokeLater(() -> {
                    if (icon != null) {
                        lblHinhAnh.setIcon(icon);
                        lblHinhAnh.setText("");
                    } else {
                        lblHinhAnh.setIcon(null);
                        lblHinhAnh.setText("Không tìm thấy hình ảnh");
                    }
                });
            });
        } else {
            lblHinhAnh.setIcon(null);
            lblHinhAnh.setText("");
        }
    }

    private ImageIcon taiBieuTuongHinhAnh(String urlHinhAnh) {
        ImageIcon icon = null;
        if (urlHinhAnh != null && !urlHinhAnh.isEmpty()) {
            String mappedUrl = "/img/" + urlHinhAnh;
            try {
                URL imageUrl = getClass().getResource(mappedUrl);
                if (imageUrl != null) {
                    icon = new ImageIcon(imageUrl);
                    Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(image);
                } else {
                    JOptionPane.showMessageDialog(this, "Đường dẫn hình ảnh: " + mappedUrl + ", URL: " + imageUrl);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi tải hình ảnh " + mappedUrl + ": " + ex.getMessage());
            }
        }
        if (icon == null) {
            try {
                URL defaultUrl = getClass().getResource("/img/default.jpg");
                if (defaultUrl != null) {
                    icon = new ImageIcon(defaultUrl);
                    Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(image);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hình ảnh mặc định: /img/default.png");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi tải hình ảnh mặc định: " + ex.getMessage());
            }
        }
        return icon;
    }

    public void xoaTextField() {
    	txtMa.setText("");
        txtTen.setText("");
        cbxLoai.setSelectedIndex(0);
        txtSLHienCo.setText("");
        txtSLThem.setText(""); 
        txtGiaNhap.setText("");
        txtGiaBan.setText("");
        txtUrlHinhAnh.setText("");
        lblHinhAnh.setIcon(null);
        lblHinhAnh.setText("");
        txtMa.setEditable(true);
    }
    
    public void dinhDangNut(JButton button, Color bgColor) {
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setForeground(Color.white);
    }

    private boolean kiemTraUrlHinhAnh(String urlHinhAnh) {
        if (urlHinhAnh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "URL hình ảnh không được để trống.");
            return false;
        }
        if (!urlHinhAnh.matches(".*\\.(png|jpg|jpeg|gif)$")) {
            JOptionPane.showMessageDialog(this, "URL hình ảnh phải là tệp .png, .jpg, .jpeg hoặc .gif.");
            return false;
        }
        
        // Kiểm tra xem có tồn tại trong thư mục /img/ không
        String mappedUrl = "/img/" + urlHinhAnh;
        URL imageUrl = getClass().getResource(mappedUrl);
        if (imageUrl == null) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Hình ảnh " + urlHinhAnh + " không tồn tại trong thư mục /img/.\nBạn có muốn tiếp tục?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION);
            return confirm == JOptionPane.YES_OPTION;
        }
        return true;
    }

    private boolean validData() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String giaNhapText = txtGiaNhap.getText().trim();
        String urlHinhAnh = txtUrlHinhAnh.getText().trim();
        
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm không được để trống.");
            return false;
        }
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống.");
            return false;
        }
        if (giaNhapText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá nhập không được để trống.");
            return false;
        }
        if (!kiemTraUrlHinhAnh(urlHinhAnh)) {
            return false;
        }

        if(!ma.matches("^893[0-9]{9}$")) {
        	JOptionPane.showMessageDialog(this, "Mã sản phẩm phải bắt đầu bằng 893 và các số, tối đa 12 số!");
        	return false;
        }
        if(!ten.matches(".*\\S.*")) {
        	JOptionPane.showMessageDialog(this, "Tên sản phẩm phải bao gồm các kí tự!");
        	return false;
        }
        
        try {
            double giaNhap = Double.parseDouble(giaNhapText);

            if (giaNhap <= 0) {
                JOptionPane.showMessageDialog(this, "Giá nhập phải lớn hơn 0.");
                return false;
            }

            // Tính giá bán = giá nhập * 1.5 và hiển thị dạng số bình thường
            double giaBan = giaNhap * 1.5;
            txtGiaBan.setText(String.valueOf(giaBan));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá nhập phải là số hợp lệ.");
            return false;
        }

        try {
            double soLuong = Double.parseDouble(txtSLThem.getText().trim().toString());
            
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng nhập phải lớn hơn 0.");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng thêm phải hợp lệ.");
            return false;
        }
        
        if (cbxLoai.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm.");
            return false;
        }
        
        
        return true;
    }


    private String getMaLoaiSPFromTen(String tenLoaiSP) {
        for (LoaiSanPham loaiSP : danhSachLoaiSP) {
            if (loaiSP.getTenLoaiSanPham().equals(tenLoaiSP)) {
                return loaiSP.getMaLoaiSanPham();
            }
        }
        JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm!");
        return null;
    }

    private void updateTableRow(int row, SanPham sp, boolean isAdd) {
    	// Tải hình ảnh trong nền 
        executorService.submit(() -> {
            ImageIcon icon = taiBieuTuongHinhAnh(sp.getUrlHinhAnh());
            DecimalFormat df = new DecimalFormat("#,###đ");
            SwingUtilities.invokeLater(() -> {
            	Object[] rowData = new Object[]{
            		    icon,
            		    sp.getMaSanPham(),
            		    sp.getTenSanPham(),
            		    sp.getLoaiSanPham().getTenLoaiSanPham(),
            		    sp.getSoLuongHienCo(),
            		    df.format(sp.getGiaNhap()),  
                        df.format(sp.getGiaBan())  
            		};
                
                if (isAdd) {
                    tableModel.addRow(rowData);
                } else {
                    for (int i = 0; i < rowData.length; i++) {
                        tableModel.setValueAt(rowData[i], row, i);
                    }
                }
            });
        });
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnThem)) {
            if (!validData()) return;

            String maSP = txtMa.getText().trim();
            String tenSP = txtTen.getText().trim();
            String tenLoaiSP = cbxLoai.getSelectedItem().toString();
            int soLuong = 0;
            try {
                soLuong = Integer.parseInt(txtSLThem.getText().trim());
                if (soLuong < 0)
                    throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số lượng thêm phải là số nguyên không âm.");
                return;
            }
            double giaNhap = Double.parseDouble(txtGiaNhap.getText().trim());
            String urlHinhAnh = txtUrlHinhAnh.getText().trim();

            String maLoaiSP = getMaLoaiSPFromTen(tenLoaiSP);
            if (maLoaiSP == null) return;

            if (sanPhamDAO.getSanPhamTheoMa(maSP) != null) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm đã tồn tại!");
                return;
            }

            LoaiSanPham loaiSP = new LoaiSanPham(maLoaiSP, tenLoaiSP);
            SanPham sp = new SanPham(maSP, tenSP, loaiSP, soLuong, giaNhap, urlHinhAnh);

            executorService.submit(() -> {
                boolean success = sanPhamDAO.themSanPham(sp);
                
                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        updateTableRow(-1, sp, true);
                        if (cachedProducts != null) {
                            cachedProducts.add(sp);
                        }
                        xoaTextField();
                        JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, "Thêm sản phẩm thành công!");
                    } else {
                        JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, "Lỗi khi thêm sản phẩm vào CSDL!");
                    }
                });
            });
        } else if (o.equals(btnXoaTrang)) {
            xoaTextField();
        } else if (o.equals(btnXoa)) {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                String maSP = tableModel.getValueAt(row, 1).toString();
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                	// Thực hiện thao tác CSDL trong luồng nền
                    executorService.submit(() -> {
                        boolean success = sanPhamDAO.xoaSanPham(maSP);
                        
                        SwingUtilities.invokeLater(() -> {
                            if (success) {
                                tableModel.removeRow(row);
                                if (cachedProducts != null) {
                                    cachedProducts.removeIf(sp -> sp.getMaSanPham().equals(maSP));
                                }
                                xoaTextField();
                                JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, "Xóa sản phẩm thành công!");
                            } else {
                                JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, "Lỗi khi xóa sản phẩm từ CSDL!");
                            }
                        });
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm để xóa.");
            }
        } else if (o.equals(btnCapNhat)) {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                String maSP = txtMa.getText().trim();
                String tenSP = txtTen.getText().trim();
                String tenLoaiSP = cbxLoai.getSelectedItem().toString();
                int slHienCo = Integer.parseInt(txtSLHienCo.getText().trim());
                int slThem = 0;
                try {
                    slThem = Integer.parseInt(txtSLThem.getText().trim());
                    if (slThem < 0)
                        throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Số lượng thêm phải là số nguyên không âm.");
                    return;
                }

                int slMoi = slHienCo + slThem;
                double giaNhap = Double.parseDouble(txtGiaNhap.getText().trim());
                double giaBan = giaNhap * 1.5;
                txtGiaBan.setText(String.format("%.2f", giaBan));
                String urlHinhAnh = txtUrlHinhAnh.getText().trim();

                String maLoaiSP = getMaLoaiSPFromTen(tenLoaiSP);
                if (maLoaiSP == null) return;

                LoaiSanPham loaiSP = new LoaiSanPham(maLoaiSP, tenLoaiSP);
                SanPham sp = new SanPham(maSP, tenSP, loaiSP, slMoi, giaNhap, urlHinhAnh);

                // Thực hiện thao tác CSDL trong luồng nền
                executorService.submit(() -> {
                    boolean success = sanPhamDAO.capNhatSanPham(sp);
                    
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            updateTableRow(row, sp, false);
                            if (cachedProducts != null) {
                                for (int i = 0; i < cachedProducts.size(); i++) {
                                    if (cachedProducts.get(i).getMaSanPham().equals(maSP)) {
                                        cachedProducts.set(i, sp);
                                        break;
                                    }
                                }
                            }
                            if (!validData()) return;
                            xoaTextField();
                            JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, "Cập nhật sản phẩm thành công!");
                        } else {
                            JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, "Lỗi khi cập nhật sản phẩm trong CSDL!");
                        }
                    });
                });
            } else {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm để cập nhật.");
            }
        } else if (o.equals(btnTim)) {
            String tuKhoa = txtTim.getText().trim();
            if (tuKhoa.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa sản phẩm cần tìm!");
                return;
            }

            // Thực hiện tìm kiếm trong luồng nền
            executorService.submit(() -> {
                List<SanPham> ketQuaTimKiem = sanPhamDAO.timKiemSanPham(tuKhoa);
                
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    if (ketQuaTimKiem.isEmpty()) {
                        JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, "Không tìm thấy sản phẩm với từ khóa: " + tuKhoa);
                    } else {
                        for (SanPham sp : ketQuaTimKiem) {
                            updateTableRow(-1, sp, true);
                        }
                    }
                });
            });
        } else if (o.equals(btnLoc)) {
        	txtTim.setText("");
            String tenLoaiSP = cbxLoc.getSelectedItem().toString();
            if (tenLoaiSP.equals("Tất cả")) {
                taiDuLieuSanPhamTuCSDL();
                return;
            }

            String maLoaiSP = getMaLoaiSPFromTen(tenLoaiSP);
            if (maLoaiSP == null) return;

            // Thực hiện lọc trong luồng nền
            executorService.submit(() -> {
                List<SanPham> ketQuaLoc = sanPhamDAO.getSanPhamTheoLoai(maLoaiSP);
                
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    if (ketQuaLoc.isEmpty()) {
                        JOptionPane.showMessageDialog(QuanLiSanPhamPanel.this, "Không có sản phẩm thuộc loại: " + tenLoaiSP);
                    } else {
                        for (SanPham sp : ketQuaLoc) {
                            updateTableRow(-1, sp, true);
                        }
                    }
                });
            });
        } else if (o.equals(btnChonAnh)) {
            // Hiển thị dialog chọn ảnh
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn hình ảnh");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            
            
            // Đặt thư mục ban đầu thành src/img
            try {
                File projectDir = new File(System.getProperty("user.dir"));
                File imgDir = new File(projectDir, "src/img");
                if (imgDir.exists() && imgDir.isDirectory()) {
                    fileChooser.setCurrentDirectory(imgDir);
                } else {
                    JOptionPane.showMessageDialog(this, "Directory not found: " + imgDir.getAbsolutePath());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error setting initial directory: " + ex.getMessage());
            }
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getName();
                
                // Kiểm tra định dạng file
                if (fileName.toLowerCase().endsWith(".jpg") || 
                    fileName.toLowerCase().endsWith(".jpeg") || 
                    fileName.toLowerCase().endsWith(".png") || 
                    fileName.toLowerCase().endsWith(".gif")) {
                    
                    txtUrlHinhAnh.setText(fileName);
                    capNhatXemTruocHinhAnh();
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn file hình ảnh (jpg, jpeg, png, gif)");
                }
            }
        }
    }
    
    // Xóa dữ liệu
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}