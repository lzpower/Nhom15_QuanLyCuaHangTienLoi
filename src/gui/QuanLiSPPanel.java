package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.LoaiSanPhamDAO;
import dao.SanPhamDAO;
import entity.LoaiSanPham;
import entity.SanPham;

public class QuanLiSPPanel extends JPanel implements ActionListener {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton btnTim;
    private JButton btnLoc;
    private JTextField txtTim;
    private JComboBox<String> cbxLoc;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnLuu;
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
    
    // For optimization
    private ExecutorService executorService;
    private List<SanPham> cachedProducts;

    public QuanLiSPPanel() {
        setLayout(new BorderLayout());

        // Initialize thread pool for background tasks
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

        // Bảng SP - Optimized with lazy loading
        String[] columnNames = {"Hình ảnh", "Mã sản phẩm", "Tên sản phẩm", "Loại", "Số lượng", "Giá nhập", "Giá bán"};
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
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        productTable.setRowHeight(150);

        // Điều chỉnh chiều rộng các cột
        productTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Hình ảnh
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Mã sản phẩm
        productTable.getColumnModel().getColumn(2).setPreferredWidth(600); // Tên sản phẩm
        productTable.getColumnModel().getColumn(3).setPreferredWidth(250); // Loại
        productTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Số lượng
        productTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Giá nhập
        productTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Giá bán

        JScrollPane scrollPane = new JScrollPane(productTable);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Panel chính chứa hình ảnh, nhập liệu và nút
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setBorder(new EmptyBorder(10, 10, 10, 10));

        // ========== Panel Hình ảnh (bên trái) ==========
        JPanel pHinhAnh = new JPanel(new BorderLayout());
        pHinhAnh.setPreferredSize(new Dimension(200, 200));

        // Panel nhập URL hình ảnh và nút chọn
        JPanel pUrlHinhAnh = new JPanel(new BorderLayout());
        JLabel lblUrlHinhAnh = new JLabel("URL hình ảnh:");

        txtUrlHinhAnh = new JTextField();
        txtUrlHinhAnh.setEditable(false);
        btnChonAnh = new JButton("Chọn ảnh");
        pUrlHinhAnh.add(lblUrlHinhAnh, BorderLayout.WEST);
        pUrlHinhAnh.add(txtUrlHinhAnh, BorderLayout.CENTER);
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

        // ========== Panel Nhập liệu (ở giữa) ==========
        JPanel pNhap = new JPanel();
        pNhap.setLayout(new BoxLayout(pNhap, BoxLayout.Y_AXIS));
        pNhap.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Set a consistent width for all text fields
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

        // Loại + SL Hiện có (on the same row)
        JPanel pLoaiSL = new JPanel();
        pLoaiSL.setLayout(new BoxLayout(pLoaiSL, BoxLayout.X_AXIS));

        // Loại
        JPanel pLoai = new JPanel(new BorderLayout());
        JLabel lblLoai = new JLabel("Loại:");
        lblLoai.setPreferredSize(new Dimension(120, 20));
        cbxLoai = new JComboBox<>(loaiSPArray);
        // Force the combobox to have the same width as other components
        cbxLoai.setPreferredSize(halfTextFieldDimension);
        cbxLoai.setMinimumSize(halfTextFieldDimension);
        cbxLoai.setMaximumSize(halfTextFieldDimension);
        pLoai.add(lblLoai, BorderLayout.WEST);
        pLoai.add(cbxLoai, BorderLayout.CENTER);
        pLoaiSL.add(pLoai);
        pLoaiSL.add(Box.createRigidArea(new Dimension(10, 0)));

        // SL Hiện có
        JPanel pSLHienCo = new JPanel(new BorderLayout());
        JLabel lblSLHienCo = new JLabel("SL hiện có:");
        lblSLHienCo.setPreferredSize(new Dimension(120, 20));
        txtSLHienCo = new JTextField();
        txtSLHienCo.setEditable(false);
        // Force the text field to have the same width as other components
        txtSLHienCo.setPreferredSize(halfTextFieldDimension);
        txtSLHienCo.setMinimumSize(halfTextFieldDimension);
        txtSLHienCo.setMaximumSize(halfTextFieldDimension);
        pSLHienCo.add(lblSLHienCo, BorderLayout.WEST);
        pSLHienCo.add(txtSLHienCo, BorderLayout.CENTER);
        pLoaiSL.add(pSLHienCo);

        // Add the row to the main panel
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


        // ========== Panel Nút thao tác (bên phải) ==========
        JPanel pNut = new JPanel(new GridLayout(4, 1, 10, 10));
        pNut.setPreferredSize(new Dimension(100, 150));
        btnThem = new JButton("THÊM");
        btnXoa = new JButton("XÓA");
        btnLuu = new JButton("CẬP NHẬT");

        pNut.add(btnThem);
        pNut.add(btnXoa);
        pNut.add(btnLuu);

        // ========== Gộp các panel vào pBottom ==========
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
        btnLuu.addActionListener(this);
        btnChonAnh.addActionListener(this);
        
        // Use ListSelectionListener instead of MouseListener for better performance
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
                        txtGiaNhap.setText(productTable.getValueAt(row, 5).toString()); // Giá nhập
                        txtGiaBan.setText(productTable.getValueAt(row, 6).toString()); // Giá bán
                        txtMa.setEditable(false);

                        // Load product details in background thread
                        executorService.submit(() -> {
                            try {
                                SanPham sp = sanPhamDAO.getSanPhamTheoMa(txtMa.getText().trim());
                                if (sp != null) {
                                    SwingUtilities.invokeLater(() -> {
                                        txtUrlHinhAnh.setText(sp.getUrlHinhAnh()); // Đặt đúng URL
                                        updateImagePreview(); // Hiển thị hình ảnh khi click vào sản phẩm
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
        
        // Thêm sự kiện cho URL hình ảnh để hiển thị preview
        txtUrlHinhAnh.addActionListener(e -> updateImagePreview());

        // Load dữ liệu sản phẩm từ CSDL (optimized with background thread)
        loadSanPhamDataAsync();
    }
    
    // Optimized asynchronous data loading
    private void loadSanPhamDataAsync() {
        // Show loading indicator
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        executorService.submit(() -> {
            try {
                // Cache products for faster access
                cachedProducts = sanPhamDAO.getAllSanPham();
                
                if (cachedProducts == null || cachedProducts.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(QuanLiSPPanel.this, 
                            "Không có sản phẩm nào trong cơ sở dữ liệu!");
                        setCursor(Cursor.getDefaultCursor());
                    });
                    return;
                }
                
                // Update UI in batches for better performance
                final int BATCH_SIZE = 20;
                for (int i = 0; i < cachedProducts.size(); i += BATCH_SIZE) {
                    final int start = i;
                    final int end = Math.min(i + BATCH_SIZE, cachedProducts.size());
                    
                    SwingUtilities.invokeLater(() -> {
                        for (int j = start; j < end; j++) {
                            updateTableRow(-1, cachedProducts.get(j), true);
                        }
                        
                        // If this is the last batch, reset cursor
                        if (end == cachedProducts.size()) {
                            setCursor(Cursor.getDefaultCursor());
                        }
                    });
                    
                    // Small delay to allow UI to update
                    Thread.sleep(50);
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(QuanLiSPPanel.this, 
                        "Lỗi khi tải dữ liệu: " + e.getMessage());
                    setCursor(Cursor.getDefaultCursor());
                });
                e.printStackTrace();
            }
        });
    }
    
    private void updateImagePreview() {
        String urlHinhAnh = txtUrlHinhAnh.getText().trim();
        if (!urlHinhAnh.isEmpty()) {
            // Load image in background thread
            executorService.submit(() -> {
                ImageIcon icon = loadImageIcon(urlHinhAnh);
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

    private ImageIcon loadImageIcon(String urlHinhAnh) {
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
                    Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
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

    public void xoaTextField() {
        txtMa.setText("");
        txtTen.setText("");
        cbxLoai.setSelectedIndex(0);
        txtSLHienCo.setText("");
        txtGiaNhap.setText("");
        txtGiaBan.setText("");
        txtUrlHinhAnh.setText("");
        lblHinhAnh.setIcon(null);
        lblHinhAnh.setText("");
        txtMa.setEditable(true);
    }

    private boolean isValidImageUrl(String urlHinhAnh) {
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
        String giaNhap = txtGiaNhap.getText().trim();
        String urlHinhAnh = txtUrlHinhAnh.getText().trim();

        // Check for empty fields
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm không được để trống.");
            return false;
        }
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống.");
            return false;
        }
        if (giaNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá nhập không được để trống.");
            return false;
        }
        if (!isValidImageUrl(urlHinhAnh)) {
            return false;
        }

        // Kiểm tra số lượng và giá nhập là số hợp lệ
        
        try {
            double gia = Double.parseDouble(giaNhap);
            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Giá nhập phải lớn hơn 0.");
                return false;
            }
            
            // Tự động tính giá bán = giá nhập * 1.5
            double giaBan = gia * 1.5;
            txtGiaBan.setText(String.valueOf(giaBan));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá nhập phải là số thực.");
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
        JOptionPane.showMessageDialog(this, "Không tìm thấy loại sản phẩm!");
        return null;
    }

    private void updateTableRow(int row, SanPham sp, boolean isAdd) {
        // Load image in background thread to improve performance
        executorService.submit(() -> {
            ImageIcon icon = loadImageIcon(sp.getUrlHinhAnh());
            
            SwingUtilities.invokeLater(() -> {
                Object[] rowData = new Object[]{
                    icon,
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getLoaiSanPham().getTenLoaiSanPham(),
                    sp.getSoLuongHienCo(),
                    sp.getGiaNhap(),
                    sp.getGiaBan()
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
            int soLuong = Integer.parseInt(txtSLHienCo.getText().trim());
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

            // Perform database operation in background thread
            executorService.submit(() -> {
                boolean success = sanPhamDAO.themSanPham(sp);
                
                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        updateTableRow(-1, sp, true);
                        // Update cache
                        if (cachedProducts != null) {
                            cachedProducts.add(sp);
                        }
                        xoaTextField();
                        JOptionPane.showMessageDialog(QuanLiSPPanel.this, "Thêm sản phẩm thành công!");
                    } else {
                        JOptionPane.showMessageDialog(QuanLiSPPanel.this, "Lỗi khi thêm sản phẩm vào CSDL!");
                    }
                });
            });
        } else if (o.equals(btnXoa)) {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                String maSP = tableModel.getValueAt(row, 1).toString();
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Perform database operation in background thread
                    executorService.submit(() -> {
                        boolean success = sanPhamDAO.xoaSanPham(maSP);
                        
                        SwingUtilities.invokeLater(() -> {
                            if (success) {
                                tableModel.removeRow(row);
                                // Update cache
                                if (cachedProducts != null) {
                                    cachedProducts.removeIf(sp -> sp.getMaSanPham().equals(maSP));
                                }
                                xoaTextField();
                                JOptionPane.showMessageDialog(QuanLiSPPanel.this, "Xóa sản phẩm thành công!");
                            } else {
                                JOptionPane.showMessageDialog(QuanLiSPPanel.this, "Lỗi khi xóa sản phẩm từ CSDL!");
                            }
                        });
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm để xóa.");
            }
        } else if (o.equals(btnLuu)) {
            if (!validData()) return;

            int row = productTable.getSelectedRow();
            if (row >= 0) {
                String maSP = txtMa.getText().trim();
                String tenSP = txtTen.getText().trim();
                String tenLoaiSP = cbxLoai.getSelectedItem().toString();
                int slHienCo = Integer.parseInt(txtSLHienCo.getText().trim());
                double giaNhap = Double.parseDouble(txtGiaNhap.getText().trim());
                String urlHinhAnh = txtUrlHinhAnh.getText().trim();

                String maLoaiSP = getMaLoaiSPFromTen(tenLoaiSP);
                if (maLoaiSP == null) return;

                LoaiSanPham loaiSP = new LoaiSanPham(maLoaiSP, tenLoaiSP);
                SanPham sp = new SanPham(maSP, tenSP, loaiSP, slHienCo, giaNhap, urlHinhAnh);

                // Perform database operation in background thread
                executorService.submit(() -> {
                    boolean success = sanPhamDAO.capNhatSanPham(sp);
                    
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            updateTableRow(row, sp, false);
                            // Update cache
                            if (cachedProducts != null) {
                                for (int i = 0; i < cachedProducts.size(); i++) {
                                    if (cachedProducts.get(i).getMaSanPham().equals(maSP)) {
                                        cachedProducts.set(i, sp);
                                        break;
                                    }
                                }
                            }
                            xoaTextField();
                            JOptionPane.showMessageDialog(QuanLiSPPanel.this, "Cập nhật sản phẩm thành công!");
                        } else {
                            JOptionPane.showMessageDialog(QuanLiSPPanel.this, "Lỗi khi cập nhật sản phẩm trong CSDL!");
                        }
                    });
                });
            } else {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm để lưu.");
            }
        } else if (o.equals(btnTim)) {
            String tuKhoa = txtTim.getText().trim();
            if (tuKhoa.isEmpty()) {
                loadSanPhamDataAsync();
                return;
            }

            // Perform search in background thread
            executorService.submit(() -> {
                List<SanPham> ketQuaTimKiem = sanPhamDAO.timKiemSanPham(tuKhoa);
                
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    if (ketQuaTimKiem.isEmpty()) {
                        JOptionPane.showMessageDialog(QuanLiSPPanel.this, "Không tìm thấy sản phẩm với từ khóa: " + tuKhoa);
                    } else {
                        for (SanPham sp : ketQuaTimKiem) {
                            updateTableRow(-1, sp, true);
                        }
                    }
                });
            });
        } else if (o.equals(btnLoc)) {
            String tenLoaiSP = cbxLoc.getSelectedItem().toString();
            if (tenLoaiSP.equals("Tất cả")) {
                loadSanPhamDataAsync();
                return;
            }

            String maLoaiSP = getMaLoaiSPFromTen(tenLoaiSP);
            if (maLoaiSP == null) return;

            // Perform filtering in background thread
            executorService.submit(() -> {
                List<SanPham> ketQuaLoc = sanPhamDAO.getSanPhamTheoLoai(maLoaiSP);
                
                SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    if (ketQuaLoc.isEmpty()) {
                        JOptionPane.showMessageDialog(QuanLiSPPanel.this, "Không có sản phẩm thuộc loại: " + tenLoaiSP);
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
            
            
         // Set the initial directory to src/img
            try {
                // Get the project directory
                File projectDir = new File(System.getProperty("user.dir"));
                // Navigate to src/img
                File imgDir = new File(projectDir, "src/img");
                
                // Check if directory exists
                if (imgDir.exists() && imgDir.isDirectory()) {
                    fileChooser.setCurrentDirectory(imgDir);
                } else {
                    System.out.println("Directory not found: " + imgDir.getAbsolutePath());
                }
            } catch (Exception ex) {
                System.err.println("Error setting initial directory: " + ex.getMessage());
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
                    updateImagePreview();
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn file hình ảnh (jpg, jpeg, png, gif)");
                }
            }
        }
    }
    
    // Clean up resources when panel is removed
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}