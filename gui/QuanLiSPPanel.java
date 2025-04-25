package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.LoaiSanPhamDAO;
import dao.SanPhamDAO;
import entity.LoaiSanPham;
import entity.SanPham;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.List;

public class QuanLiSPPanel extends JPanel implements ActionListener, MouseListener {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton btnTaoHD;
    private JButton btnCapNhat;
    private JButton btnThongKe;
    private JTextField txtTim;
    private JButton btnTim;
    private JButton btnLoc;
    private JComboBox<String> cbxLoc;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnSua;
    private JButton btnLuu;
    private JTextField txtMa;
    private JTextField txtTen;
    private JComboBox<String> cbxLoai;
    private JTextField txtSLHienCo;
    private JTextField txtSLThem;
    private JTextField txtGiaNhap;
    private JTextField txtGiaBan;
    private JTextField txtUrlHinhAnh;
    
    private SanPhamDAO sanPhamDAO;
    private LoaiSanPhamDAO loaiSanPhamDAO;
    private List<LoaiSanPham> danhSachLoaiSP;

    public QuanLiSPPanel() {
        setLayout(new BorderLayout());

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
        
        

        // Trung tam: Tim SP va Bang SP
        JPanel pCenter = new JPanel();
        pCenter.setLayout(new BorderLayout());
        // Tim SP
        JPanel pTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTim.setBorder(BorderFactory.createTitledBorder("Tìm sản phẩm"));
        txtTim = new JTextField(20);
        btnTim = new JButton("Tìm kiếm");
        
        // Tạo danh sách loại sản phẩm cho combobox
        String[] loaiSPArray = new String[danhSachLoaiSP.size() + 1];
        loaiSPArray[0] = "Tất cả";
        for (int i = 0; i < danhSachLoaiSP.size(); i++) {
            loaiSPArray[i + 1] = danhSachLoaiSP.get(i).getTenLoaiSP();
        }
        
        cbxLoc = new JComboBox<>(loaiSPArray);
        btnLoc = new JButton("Lọc theo loại");
        
        pTim.add(txtTim);
        pTim.add(btnTim);
        pTim.add(cbxLoc);
        pTim.add(btnLoc);
        pCenter.add(pTim, BorderLayout.NORTH);

        // Bang SP
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
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //Tắt tự động điều chỉnh kích thước cột
        productTable.setRowHeight(150);

        // Điều chỉnh chiều rộng các cột
        productTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Cột "Hình ảnh"
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Cột "Mã sản phẩm"
        productTable.getColumnModel().getColumn(2).setPreferredWidth(600); // Cột "Tên sản phẩm"
        productTable.getColumnModel().getColumn(3).setPreferredWidth(250);  // Cột "Loại"
        productTable.getColumnModel().getColumn(4).setPreferredWidth(150);  // Cột "Số lượng"
        productTable.getColumnModel().getColumn(5).setPreferredWidth(150);  // Cột "Giá nhập"
        productTable.getColumnModel().getColumn(6).setPreferredWidth(150);  // Cột "Giá bán"
        JScrollPane scrollPane = new JScrollPane(productTable);
        pCenter.add(scrollPane, BorderLayout.CENTER);
        
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Nhap sp + cac button
        JPanel pBottom = new JPanel();
        pBottom.setLayout(new BorderLayout());
        // Nhap sp
        JPanel pNhap = new JPanel();
        pNhap.setLayout(new BoxLayout(pNhap, BoxLayout.Y_AXIS));
        pNhap.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Ma SP
        JPanel pMa = new JPanel(new BorderLayout());
        JLabel lblMa = new JLabel("Mã sản phẩm:");
        lblMa.setPreferredSize(new Dimension(100, 20));
        txtMa = new JTextField();
        pMa.add(lblMa, BorderLayout.WEST);
        pMa.add(txtMa, BorderLayout.CENTER);
        pNhap.add(pMa);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Ten SP
        JPanel pTen = new JPanel(new BorderLayout());
        JLabel lblTen = new JLabel("Tên sản phẩm:");
        lblTen.setPreferredSize(new Dimension(100, 20));
        txtTen = new JTextField();
        pTen.add(lblTen, BorderLayout.WEST);
        pTen.add(txtTen, BorderLayout.CENTER);
        pNhap.add(pTen);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Loai + SL
        JPanel pLoaiSL = new JPanel();
        pLoaiSL.setLayout(new BoxLayout(pLoaiSL, BoxLayout.X_AXIS));
        pNhap.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Loai
        JPanel pLoai = new JPanel(new BorderLayout());
        JLabel lblLoai = new JLabel("Loại:");
        lblLoai.setPreferredSize(new Dimension(100, 20));
        cbxLoai = new JComboBox<>(loaiSPArray);
        pLoai.add(lblLoai, BorderLayout.WEST);
        pLoai.add(cbxLoai, BorderLayout.CENTER);
        pLoaiSL.add(pLoai);
        pLoaiSL.add(Box.createRigidArea(new Dimension(10, 10)));
        
        // SL Hien Co
        JPanel pSLHienCo = new JPanel(new BorderLayout());
        JLabel lblSLHienCo = new JLabel("Số lượng hiện có:");
        lblSLHienCo.setPreferredSize(new Dimension(110, 20));
        txtSLHienCo = new JTextField();
        txtSLHienCo.setEditable(false);
        pSLHienCo.add(lblSLHienCo, BorderLayout.WEST);
        pSLHienCo.add(txtSLHienCo, BorderLayout.CENTER);
        pLoaiSL.add(pSLHienCo);
        pLoaiSL.add(Box.createRigidArea(new Dimension(10, 10)));
        
        // SL Them
        JPanel pSLThem = new JPanel(new BorderLayout());
        JLabel lblSLThem = new JLabel("Số lượng thêm:");
        lblSLThem.setPreferredSize(new Dimension(100, 20));
        txtSLThem = new JTextField();
        pSLThem.add(lblSLThem, BorderLayout.WEST);
        pSLThem.add(txtSLThem, BorderLayout.CENTER);
        pLoaiSL.add(pSLThem);
        pLoaiSL.add(Box.createRigidArea(new Dimension(0, 10)));
        
        pNhap.add(pLoaiSL);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Gia
        JPanel pGia = new JPanel();
        pGia.setLayout(new GridLayout(1, 2, 5, 5));
        
        // Gia Nhap
        JPanel pGiaNhap = new JPanel(new BorderLayout());
        JLabel lblGiaNhap = new JLabel("Giá nhập:");
        lblGiaNhap.setPreferredSize(new Dimension(100, 20));
        txtGiaNhap = new JTextField();
        pGiaNhap.add(lblGiaNhap, BorderLayout.WEST);
        pGiaNhap.add(txtGiaNhap, BorderLayout.CENTER);
        pGia.add(pGiaNhap);
        
        // Gia Ban
        JPanel pGiaBan = new JPanel(new BorderLayout());
        JLabel lblGiaBan = new JLabel("Giá bán:");
        lblGiaBan.setPreferredSize(new Dimension(100, 20));
        txtGiaBan = new JTextField(); // Khởi tạo txtGiaBan
        txtGiaBan.setEditable(false); // Đặt không cho phép chỉnh sửa
        pGiaBan.add(lblGiaBan, BorderLayout.WEST);
        pGiaBan.add(txtGiaBan, BorderLayout.CENTER);
        pGia.add(pGiaBan);
        
        pNhap.add(pGia);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Hinh anh
        JPanel pHinhAnh = new JPanel(new BorderLayout());
        JLabel lblHinhAnh = new JLabel("URL hình ảnh:");
        lblHinhAnh.setPreferredSize(new Dimension(100, 20));
        txtUrlHinhAnh = new JTextField();
        pHinhAnh.add(lblHinhAnh, BorderLayout.WEST);
        pHinhAnh.add(txtUrlHinhAnh, BorderLayout.CENTER);
        pNhap.add(pHinhAnh);
        pNhap.add(Box.createRigidArea(new Dimension(0, 10)));

        // Cac button
        JPanel pNut = new JPanel(new GridLayout(4, 0, 10, 10));
        btnThem = new JButton("THÊM");
        btnXoa = new JButton("XÓA");
        btnSua = new JButton("SỬA");
        btnLuu = new JButton("LƯU");

        pNut.add(btnThem);
        pNut.add(btnXoa);
        pNut.add(btnSua);
        pNut.add(btnLuu);

        // Add bottom panels to a container
        pBottom.add(pNhap, BorderLayout.CENTER);
        pBottom.add(pNut, BorderLayout.EAST);

        pBorder.add(pBottom, BorderLayout.NORTH);
        
        // Su kien
        btnTim.addActionListener(this);
        btnLoc.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);
        btnLuu.addActionListener(this);
        productTable.addMouseListener(this);
        
        // Load dữ liệu sản phẩm từ CSDL
        loadSanPhamData();
    }
    
    private void loadSanPhamData() {
        tableModel.setRowCount(0);
        List<SanPham> danhSachSP = sanPhamDAO.getAllSanPham();
        if (danhSachSP == null || danhSachSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm nào trong cơ sở dữ liệu!");
            return;
        }
        for (SanPham sp : danhSachSP) {
            ImageIcon icon = loadImageIcon(sp.getUrlHinhAnh());
            tableModel.addRow(new Object[]{
                icon,
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getLoaiSP().getTenLoaiSP(),
                sp.getSlHienCo(),
                sp.getGiaNhap(),
                sp.getGiaBan()
            });
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
                    System.err.println("Không tìm thấy hình ảnh: " + mappedUrl);
                }
            } catch (Exception ex) {
                System.err.println("Lỗi tải hình ảnh " + mappedUrl + ": " + ex.getMessage());
            }
        }
        if (icon == null) {
            try {
                URL defaultUrl = getClass().getResource("/img/default.png");
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

    public void xoaTextField() {
        txtMa.setText("");
        txtTen.setText("");
        cbxLoai.setSelectedIndex(0);
        txtSLHienCo.setText("");
        txtSLThem.setText("");
        txtGiaNhap.setText("");
        txtGiaBan.setText("");
        txtMa.setEditable(true);
        txtUrlHinhAnh.setText("");
    }

    private boolean validData() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String slThem = txtSLThem.getText().trim();
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
        if (slThem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số lượng thêm không được để trống.");
            return false;
        }
        if (giaNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá nhập không được để trống.");
            return false;
        }
        if (urlHinhAnh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "URL hình ảnh không được để trống.");
            return false;
        }

        // Kiểm tra số lượng và giá nhập là số hợp lệ
        try {
            int sl = Integer.parseInt(slThem);
            if (sl < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng thêm không được âm.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng thêm phải là số nguyên.");
            return false;
        }

        try {
            double gia = Double.parseDouble(giaNhap);
            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Giá nhập phải lớn hơn 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá nhập phải là số thực.");
            return false;
        }

        // Kiểm tra URL hình ảnh hợp lệ
        if (!urlHinhAnh.matches(".*\\.(png|jpg|jpeg|gif)$")) {
            JOptionPane.showMessageDialog(this, "URL hình ảnh phải là tệp .png, .jpg, .jpeg hoặc .gif.");
            return false;
        }
        // Kiểm tra hình ảnh tồn tại
        String mappedUrl = "/img/" + urlHinhAnh;
        URL imageUrl = getClass().getResource(mappedUrl);
        if (imageUrl == null) {
            JOptionPane.showMessageDialog(this, "Hình ảnh " + urlHinhAnh + " không tồn tại trong thư mục /img/.");
            return false;
        }

        return true;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = productTable.getSelectedRow();
        if (row >= 0) {
            txtMa.setText(productTable.getValueAt(row, 1).toString());
            txtTen.setText(productTable.getValueAt(row, 2).toString());

            String tenLoai = productTable.getValueAt(row, 3).toString();
            for (int i = 0; i < cbxLoai.getItemCount(); i++) {
                if (cbxLoai.getItemAt(i).equals(tenLoai)) {
                    cbxLoai.setSelectedIndex(i);
                    break;
                }
            }

            txtSLHienCo.setText(productTable.getValueAt(row, 4).toString());
            txtSLThem.setText("0");
            txtGiaNhap.setText(productTable.getValueAt(row, 5).toString());
            txtGiaBan.setText(productTable.getValueAt(row, 6).toString());
            txtMa.setEditable(false);

            SanPham sp = sanPhamDAO.getSanPhamTheoMa(txtMa.getText().trim());
            if (sp != null) {
                txtUrlHinhAnh.setText(sp.getUrlHinhAnh());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnThem)) {
            if (!validData()) return;

            String maSP = txtMa.getText().trim();
            String tenSP = txtTen.getText().trim();
            String tenLoaiSP = cbxLoai.getSelectedItem().toString();
            int soLuong = Integer.parseInt(txtSLThem.getText().trim());
            double giaNhap = Double.parseDouble(txtGiaNhap.getText().trim());
            String urlHinhAnh = txtUrlHinhAnh.getText().trim();

            String maLoaiSP = null;
            for (LoaiSanPham loaiSP : danhSachLoaiSP) {
                if (loaiSP.getTenLoaiSP().equals(tenLoaiSP)) {
                    maLoaiSP = loaiSP.getMaLoaiSP();
                    break;
                }
            }

            if (maLoaiSP == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy loại sản phẩm!");
                return;
            }

            if (sanPhamDAO.getSanPhamTheoMa(maSP) != null) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm đã tồn tại!");
                return;
            }

            LoaiSanPham loaiSP = new LoaiSanPham(maLoaiSP, tenLoaiSP);
            SanPham sp = new SanPham(maSP, tenSP, loaiSP, soLuong, giaNhap, urlHinhAnh);

            if (sanPhamDAO.themSanPham(sp)) {
            	ImageIcon icon = loadImageIcon(sp.getUrlHinhAnh());
                // Thêm sản phẩm vào bảng
                tableModel.addRow(new Object[]{
                    icon,
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getLoaiSP().getTenLoaiSP(),
                    sp.getSlHienCo(),
                    sp.getGiaNhap(),
                    sp.getGiaBan()
                });

                xoaTextField();
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm vào CSDL!");
            }
        } else if (o.equals(btnXoa)) {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                String maSP = tableModel.getValueAt(row, 1).toString();
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (sanPhamDAO.xoaSanPham(maSP)) {
                        tableModel.removeRow(row);
                        xoaTextField();
                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm từ CSDL!");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm để xóa.");
            }
        } else if (o.equals(btnSua)) {
            int row = productTable.getSelectedRow();
            if (row >= 0) {
                txtMa.setEditable(false);
                JOptionPane.showMessageDialog(this, "Đã chọn sản phẩm để sửa. Vui lòng cập nhật thông tin và nhấn Lưu.");
            } else {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm để sửa.");
            }
        } else if (o.equals(btnLuu)) {
            if (!validData()) return;

            int row = productTable.getSelectedRow();
            if (row >= 0) {
                String maSP = txtMa.getText().trim();
                String tenSP = txtTen.getText().trim();
                String tenLoaiSP = cbxLoai.getSelectedItem().toString();
                int slHienCo = Integer.parseInt(txtSLHienCo.getText().trim());
                int slThem = Integer.parseInt(txtSLThem.getText().trim());
                int slMoi = slHienCo + slThem;
                double giaNhap = Double.parseDouble(txtGiaNhap.getText().trim());
                String urlHinhAnh = txtUrlHinhAnh.getText().trim();

                String maLoaiSP = null;
                for (LoaiSanPham loaiSP : danhSachLoaiSP) {
                    if (loaiSP.getTenLoaiSP().equals(tenLoaiSP)) {
                        maLoaiSP = loaiSP.getMaLoaiSP();
                        break;
                    }
                }

                if (maLoaiSP == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy loại sản phẩm!");
                    return;
                }

                LoaiSanPham loaiSP = new LoaiSanPham(maLoaiSP, tenLoaiSP);
                SanPham sp = new SanPham(maSP, tenSP, loaiSP, slMoi, giaNhap, urlHinhAnh);

                if (sanPhamDAO.capNhatSanPham(sp)) {
                	ImageIcon icon = loadImageIcon(sp.getUrlHinhAnh());
                    // Cập nhật sản phẩm trong bảng
                    tableModel.setValueAt(icon, row, 0);
                    tableModel.setValueAt(tenSP, row, 2);
                    tableModel.setValueAt(tenLoaiSP, row, 3);
                    tableModel.setValueAt(slMoi, row, 4);
                    tableModel.setValueAt(giaNhap, row, 5);
                    tableModel.setValueAt(sp.getGiaBan(), row, 6);

                    xoaTextField();
                    JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật sản phẩm trong CSDL!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chọn sản phẩm để lưu.");
            }
        } else if (o.equals(btnTim)) {
            String tuKhoa = txtTim.getText().trim();
            if (tuKhoa.isEmpty()) {
                loadSanPhamData();
                return;
            }

            List<SanPham> ketQuaTimKiem = sanPhamDAO.timKiemSanPham(tuKhoa);

            tableModel.setRowCount(0);
            if (ketQuaTimKiem.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với từ khóa: " + tuKhoa);
            } else {
                for (SanPham sp : ketQuaTimKiem) {
                	ImageIcon icon = loadImageIcon(sp.getUrlHinhAnh());
                    
                    tableModel.addRow(new Object[]{
                        icon,
                        sp.getMaSP(),
                        sp.getTenSP(),
                        sp.getLoaiSP().getTenLoaiSP(),
                        sp.getSlHienCo(),
                        sp.getGiaNhap(),
                        sp.getGiaBan()
                    });
                }
            }
        } else if (o.equals(btnLoc)) {
            String tenLoaiSP = cbxLoc.getSelectedItem().toString();
            if (tenLoaiSP.equals("Tất cả")) {
                loadSanPhamData();
                return;
            }

            String maLoaiSP = null;
            for (LoaiSanPham loaiSP : danhSachLoaiSP) {
                if (loaiSP.getTenLoaiSP().equals(tenLoaiSP)) {
                    maLoaiSP = loaiSP.getMaLoaiSP();
                    break;
                }
            }

            if (maLoaiSP == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy loại sản phẩm!");
                return;
            }

            List<SanPham> ketQuaLoc = sanPhamDAO.getSanPhamTheoLoai(maLoaiSP);

            tableModel.setRowCount(0);
            if (ketQuaLoc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có sản phẩm thuộc loại: " + tenLoaiSP);
            } else {
                for (SanPham sp : ketQuaLoc) {
                	ImageIcon icon = loadImageIcon(sp.getUrlHinhAnh());

                    tableModel.addRow(new Object[]{
                        icon,
                        sp.getMaSP(),
                        sp.getTenSP(),
                        sp.getLoaiSP().getTenLoaiSP(),
                        sp.getSlHienCo(),
                        sp.getGiaNhap(),
                        sp.getGiaBan()
                    });
                }
            }
        }
    }
}