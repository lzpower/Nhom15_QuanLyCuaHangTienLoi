package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
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

public class BanHangPanel extends JPanel implements ActionListener {
    private JPanel p;
    private JButton btnTaoHoaDon;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnLamRong;
    private JButton btnInHD;
    private JButton btnTimKhachHang;
    private JButton btnDung;
    private JComboBox<String> cbbMaVach;
    private JComboBox<String> cbbMaKhuyenMai;
    private DefaultTableModel model;
    private JTable tb;
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
    private JTextField txtDiem;
    private JScrollPane jsc;

    private SanPhamDAO sanPhamDAO;
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private KhuyenMaiDAO khuyenMaiDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    private TaiKhoanDAO taiKhoanDAO;
    
    private String maHoaDonHienTai;
    private String maNhanVienHienTai;
    private boolean daCoHoaDon = false;
    private double tongCong = 0;
    private KhachHang khachHangHienTai = null;
    private boolean isDungDiem = false;
    
    // Constructor without parameters
    public BanHangPanel() throws SQLException {
        ConnectDB.getInstance().connect(); 
        setLayout(new BorderLayout());
        p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 10, 15, 10));
        add(p, BorderLayout.CENTER);

        sanPhamDAO = new SanPhamDAO();
        hoaDonDAO = new HoaDonDAO();
        chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
        khachHangDAO = new KhachHangDAO();
        nhanVienDAO = new NhanVienDAO();
        taiKhoanDAO = new TaiKhoanDAO();
        
        // Default to first employee if no login info
        maNhanVienHienTai = "NV001";

        // Phần trên: Thông tin hóa đơn
        JPanel pn = new JPanel(new GridLayout(1, 7, 5, 5));
        pn.setBorder(BorderFactory.createEmptyBorder(5, 5, 20, 5));
        
        pn.add(btnTaoHoaDon = new JButton("Tạo hóa đơn"));
        pn.add(new JLabel("Mã hóa đơn:"));
        pn.add(txtMaHoaDon = new JTextField(10));
        txtMaHoaDon.setEditable(false);
        pn.add(new JLabel("Nhân viên:"));
        pn.add(txtNhanVien = new JTextField(10));
        txtNhanVien.setEditable(false);
        pn.add(new JLabel("Ngày tạo:"));
        pn.add(txtNgayTao = new JTextField(10));
        txtNgayTao.setEditable(false);
        p.add(pn, BorderLayout.NORTH);

        // Phần giữa: Thêm sản phẩm và bảng sản phẩm
        JPanel pc = new JPanel(new BorderLayout());
        JPanel pch = new JPanel(new GridLayout(1, 7, 5, 5));
        pch.add(new JLabel("Nhập mã vạch:"));
        pch.add(cbbMaVach = new JComboBox<String>());
        cbbMaVach.setEditable(true);
        pch.add(new JLabel("Nhập số lượng:"));
        pch.add(txtSoLuong = new JTextField(10));
        pch.add(btnThem = new JButton("Thêm"));
        pch.add(btnXoa = new JButton("Xóa"));
        pch.add(btnLamRong = new JButton("Làm Rỗng"));
        pc.add(pch, BorderLayout.NORTH);
        setupMaVachComboBox();
        
        // Bảng sản phẩm
        String[] colName = {"STT", "Hình ảnh", "Mã SP", "Tên sản phẩm", "Số lượng", "Giá", "Thành Tiền"};
        model = new DefaultTableModel(colName, 0);
        tb = new JTable(model);
        tb.setRowHeight(150);
        tb.setDefaultEditor(Object.class, null);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tb.getColumnCount(); i++) {
            tb.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        tb.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof ImageIcon) {
                    setIcon((ImageIcon) value);
                    setText("");
                } else {
                    super.setValue(value);
                }
            }
        });
        
        tb.setFont(new Font("Arial", Font.PLAIN, 16));
        jsc = new JScrollPane(tb);
        jsc.setPreferredSize(new Dimension(0, 100));
        pc.add(jsc, BorderLayout.CENTER);
        
        // Phần dưới: Thông tin khách hàng và thanh toán
        JPanel pcs = new JPanel(new BorderLayout());
        
        // Thông tin khách hàng
        JPanel pcsw = new JPanel(new GridLayout(5, 3, 5, 5));
        pcsw.setPreferredSize(new Dimension(400, 0));
        
        pcsw.add(new JLabel("Mã khuyến mãi:"));
        pcsw.add(cbbMaKhuyenMai = new JComboBox<String>());
        pcsw.add(new JLabel());
        
        pcsw.add(new JLabel("Mã khách hàng:"));
        pcsw.add(txtMaKhachHang = new JTextField(10));
        pcsw.add(btnTimKhachHang = new JButton("Tìm"));
        
        pcsw.add(new JLabel("Tên khách hàng:"));
        pcsw.add(txtTenKhachHang = new JTextField(10));
        txtTenKhachHang.setEditable(false);
        pcsw.add(new JLabel());
        
        pcsw.add(new JLabel("Số điện thoại:"));
        pcsw.add(txtSoDienThoai = new JTextField(10));
        txtSoDienThoai.setEditable(false);
        pcsw.add(new JLabel());
        
        pcsw.add(new JLabel("Điểm:"));
        pcsw.add(txtDiem = new JTextField(10));
        txtDiem.setEditable(false);
        pcsw.add(btnDung = new JButton("Dùng"));
        btnDung.setEnabled(false);
        
        
        // Thông tin thanh toán
        JPanel pcsc = new JPanel(new GridLayout(5, 2, 5, 5));
        
        pcsc.add(new JLabel("Tổng cộng:"));
        pcsc.add(txtTongCong = new JTextField(10));
        txtTongCong.setEditable(false);
        
        pcsc.add(new JLabel("Giảm giá:"));
        pcsc.add(txtGiamGia = new JTextField(10));
        txtGiamGia.setEditable(false);
        
        pcsc.add(new JLabel("Sử dụng điểm:"));
        pcsc.add(txtSuDungDiem = new JTextField(10));
        txtSuDungDiem.setEditable(false);
        
        pcsc.add(new JLabel("Tổng tiền:"));
        pcsc.add(txtTongTien = new JTextField(10));
        txtTongTien.setEditable(false);
        
        pcsc.add(new JLabel());
        pcsc.add(btnInHD = new JButton("In hóa đơn"));
        
        pcs.add(pcsw, BorderLayout.WEST);
        pcs.add(pcsc, BorderLayout.CENTER);
        pc.add(pcs, BorderLayout.SOUTH);
        p.add(pc, BorderLayout.CENTER);
        
        // Thiết lập ComboBox khuyến mãi
        loadKhuyenMai();
        
        // Vô hiệu hóa các trường nhập liệu ban đầu
        setEnabledInputFields(false);
        
        // Thêm sự kiện
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamRong.addActionListener(this);
        btnTaoHoaDon.addActionListener(this);
        btnInHD.addActionListener(this);
        btnTimKhachHang.addActionListener(this);
        btnDung.addActionListener(this);
        cbbMaKhuyenMai.addActionListener(this);
        
        // Thêm sự kiện cho txtSoLuong để nhấn Enter thêm sản phẩm
        txtSoLuong.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnThem.doClick();
                }
            }
        });
    }
    
    private void setupMaVachComboBox() {
        // Lấy danh sách mã sản phẩm từ CSDL
        List<SanPham> danhSachSP = sanPhamDAO.getAllSanPham();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement(" ");
        for (SanPham sp : danhSachSP) {
            model.addElement(sp.getMaSanPham());
        }
        
        cbbMaVach.setModel(model);
        
        // Thêm tính năng tìm kiếm tương đối
        JTextField textField = (JTextField) cbbMaVach.getEditor().getEditorComponent();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = textField.getText().toLowerCase();
                if (!input.isEmpty()) {
                    DefaultComboBoxModel<String> filteredModel = new DefaultComboBoxModel<>();
                    for (SanPham sp : danhSachSP) {
                        if (sp.getMaSanPham().toLowerCase().contains(input) || 
                            sp.getTenSanPham().toLowerCase().contains(input)) {
                            filteredModel.addElement(sp.getMaSanPham());
                        }
                    }
                    cbbMaVach.setModel(filteredModel);
                    cbbMaVach.setSelectedItem(input);
                    cbbMaVach.showPopup();
                }
            }
        });
    }
    
    private void loadKhuyenMai() {
        List<KhuyenMai> danhSachKM = khuyenMaiDAO.getAllKhuyenMai();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
        model.addElement("Không");//mặc định
        for (KhuyenMai km : danhSachKM) {
            model.addElement(km.getMaKhuyenMai() + " - " + km.getTenKhuyenMai());
        }
        cbbMaKhuyenMai.setModel(model);
    }

    private void setEnabledInputFields(boolean enabled) {
        cbbMaVach.setEnabled(enabled);
        txtSoLuong.setEnabled(enabled);
        btnThem.setEnabled(enabled);
        btnXoa.setEnabled(enabled);
        btnLamRong.setEnabled(enabled);
        txtMaKhachHang.setEnabled(enabled);
        btnInHD.setEnabled(enabled);
        btnTimKhachHang.setEnabled(enabled);
        cbbMaKhuyenMai.setEnabled(enabled);
    }

    // Phương thức để tạo hóa đơn mới
    private void taoHoaDonMoi() {
        maHoaDonHienTai = hoaDonDAO.taoMaHoaDonMoi();
        NhanVien nhanVien = nhanVienDAO.getNhanVienTheoMa(maNhanVienHienTai);
        String tenNhanVien = nhanVien != null ? nhanVien.getTenNhanVien() : "Không xác định";

        // Lấy ngày giờ hiện tại
        Date ngayHienTai = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String ngayGioHienTai = dateFormat.format(ngayHienTai);

        // Hiển thị thông tin hóa đơn
        txtMaHoaDon.setText(maHoaDonHienTai);
        txtNhanVien.setText(tenNhanVien);
        txtNgayTao.setText(ngayGioHienTai);

        // Lưu hóa đơn vào database
        HoaDon hoaDon = new HoaDon(maHoaDonHienTai, ngayHienTai, nhanVien);
        boolean success = hoaDonDAO.themHoaDon(hoaDon);
        
        if (!success) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tạo hóa đơn mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setEnabledInputFields(true);
        // Đánh dấu đã có hóa đơn
        daCoHoaDon = true;
        // Làm mới bảng
        model.setRowCount(0);
        tongCong = 0;
        updateTongTien();
        
        // Reset thông tin khách hàng
        khachHangHienTai = null;
        txtMaKhachHang.setText("");
        txtTenKhachHang.setText("");
        txtSoDienThoai.setText("");
        txtDiem.setText("");
        txtSuDungDiem.setText("");
        isDungDiem = false;
        btnDung.setText("Dùng");
        btnDung.setEnabled(false);
        
        // Đặt mặc định cho khuyến mãi là "Không"
        cbbMaKhuyenMai.setSelectedIndex(0);
        
        // Focus vào ô mã vạch
        cbbMaVach.requestFocus();
    }

    private void themSanPhamVaoHoaDon() {
        if (!daCoHoaDon) {
            JOptionPane.showMessageDialog(this, "Vui lòng tạo hóa đơn trước khi thêm sản phẩm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maVach = cbbMaVach.getSelectedItem() != null ? cbbMaVach.getSelectedItem().toString().trim() : "";
        String soLuongStr = txtSoLuong.getText().trim();
        
        if (maVach.isEmpty() || soLuongStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã vạch và số lượng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int soLuong = Integer.parseInt(soLuongStr);
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Lấy thông tin sản phẩm từ database
            SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(maVach);
            if (sanPham == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với mã vạch: " + maVach, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra số lượng tồn kho
            if (sanPham.getSoLuongHienCo() < soLuong) {
                JOptionPane.showMessageDialog(this, "Số lượng sản phẩm trong kho không đủ! Hiện có: " + sanPham.getSoLuongHienCo(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra xem sản phẩm đã có trong hóa đơn chưa
            boolean daTonTai = false;
            int rowIndex = -1;
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 2).toString().equals(maVach)) {
                    daTonTai = true;
                    rowIndex = i;
                    break;
                }
            }

            // Tính thành tiền
            double giaBan = sanPham.getGiaBan();
            double thanhTien = giaBan * soLuong;

            if (daTonTai) {
                // Cập nhật số lượng và thành tiền
                int soLuongCu = Integer.parseInt(model.getValueAt(rowIndex, 4).toString());
                int soLuongMoi = soLuongCu + soLuong;
                double thanhTienMoi = giaBan * soLuongMoi;
                
                // Kiểm tra lại số lượng tồn kho
                if (sanPham.getSoLuongHienCo() < soLuong) {
                    JOptionPane.showMessageDialog(this, "Số lượng sản phẩm trong kho không đủ! Hiện có: " + sanPham.getSoLuongHienCo(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                model.setValueAt(soLuongMoi, rowIndex, 4);
                model.setValueAt(String.format("%.2f", thanhTienMoi), rowIndex, 6);
                
                // Cập nhật chi tiết hóa đơn trong database
                ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHoaDonHienTai);
                chiTiet.setHoaDon(hoaDon);
                chiTiet.setSanPham(sanPham);
                chiTiet.setSoLuong(soLuongMoi);
                chiTiet.setDonGia(giaBan);
                
                // Xóa chi tiết cũ và thêm chi tiết mới
                chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maVach);
                chiTietHoaDonDAO.themChiTietHoaDon(chiTiet);
                
                // Cập nhật tổng tiền
                tongCong = tongCong - (giaBan * soLuongCu) + thanhTienMoi;
            } else {
                // Thêm vào bảng
                int rowCount = model.getRowCount();
                ImageIcon icon = loadImageIcon(sanPham.getUrlHinhAnh());
                model.addRow(new Object[] { 
                    rowCount + 1, 
                    icon, 
                    sanPham.getMaSanPham(),
                    sanPham.getTenSanPham(), 
                    soLuong, 
                    String.format("%.2f", giaBan),
                    String.format("%.2f", thanhTien) 
                });

                // Thêm chi tiết hóa đơn vào database
                ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHoaDonHienTai);
                chiTiet.setHoaDon(hoaDon);
                chiTiet.setSanPham(sanPham);
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(giaBan);
                
                chiTietHoaDonDAO.themChiTietHoaDon(chiTiet);
                
                // Cập nhật tổng tiền
                tongCong += thanhTien;
            }

            // Cập nhật số lượng sản phẩm trong kho
            sanPhamDAO.capNhatSoLuongSanPham(sanPham.getMaSanPham(), sanPham.getSoLuongHienCo() - soLuong);

            // Cập nhật tổng tiền
            updateTongTien();

            // Xóa nội dung các trường nhập liệu
            cbbMaVach.setSelectedIndex(-1);
            txtSoLuong.setText("");
            cbbMaVach.requestFocus();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức để xóa sản phẩm khỏi hóa đơn
    private void xoaSanPhamKhoiHoaDon() {
        int selectedRow = tb.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maSP = model.getValueAt(selectedRow, 2).toString();
        int soLuong = Integer.parseInt(model.getValueAt(selectedRow, 4).toString());
        double thanhTien = Double.parseDouble(model.getValueAt(selectedRow, 6).toString().replace(",", ""));

        // Xóa chi tiết hóa đơn khỏi database
        boolean success = chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maSP);
        if (!success) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm khỏi hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cập nhật lại số lượng sản phẩm trong kho
        SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(maSP);
        if (sanPham != null) {
            sanPhamDAO.capNhatSoLuongSanPham(maSP, sanPham.getSoLuongHienCo() + soLuong);
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
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn đã trống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tất cả sản phẩm khỏi hóa đơn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Xóa tất cả chi tiết hóa đơn khỏi database
        for (int i = 0; i < model.getRowCount(); i++) {
            String maSP = model.getValueAt(i, 2).toString();
            int soLuong = Integer.parseInt(model.getValueAt(i, 4).toString());

            // Cập nhật lại số lượng sản phẩm trong kho
            SanPham sanPham = sanPhamDAO.getSanPhamTheoMa(maSP);
            if (sanPham != null) {
                sanPhamDAO.capNhatSoLuongSanPham(maSP, sanPham.getSoLuongHienCo() + soLuong);
            }

            chiTietHoaDonDAO.xoaChiTietHoaDon(maHoaDonHienTai, maSP);
        }

        // Xóa tất cả dòng khỏi bảng
        model.setRowCount(0);

        // Cập nhật tổng tiền
        tongCong = 0;
        updateTongTien();
        
        // Reset thông tin khách hàng và điểm
        khachHangHienTai = null;
        txtMaKhachHang.setText("");
        txtTenKhachHang.setText("");
        txtSoDienThoai.setText("");
        txtDiem.setText("");
        txtSuDungDiem.setText("");
        isDungDiem = false;
        btnDung.setText("Dùng");
        btnDung.setEnabled(false);
        
        // Đặt mặc định cho khuyến mãi là "Không"
        cbbMaKhuyenMai.setSelectedIndex(0);
    }

    private void timKhachHang() {
        String maKhachHang = txtMaKhachHang.getText().trim();
        if (maKhachHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tìm khách hàng trong database
        KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(maKhachHang);
        if (khachHang != null) {
            // Hiển thị thông tin khách hàng
            txtTenKhachHang.setText(khachHang.getTenKhachHang());
            txtSoDienThoai.setText(khachHang.getSoDienThoai());
            txtDiem.setText(String.valueOf(khachHang.getSoDiem()));
            khachHangHienTai = khachHang;
            
            // Reset trạng thái sử dụng điểm
            isDungDiem = false;
            btnDung.setText("Dùng");
            txtSuDungDiem.setText("0");
            btnDung.setEnabled(khachHang.getSoDiem() > 0);
            updateTongTien();
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng với mã: " + maKhachHang, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            txtTenKhachHang.setText("");
            txtSoDienThoai.setText("");
            txtDiem.setText("");
            khachHangHienTai = null;
            btnDung.setEnabled(false);
        }
    }
    
    private void toggleDungDiem() {
        int soDiem = khachHangHienTai.getSoDiem();
        if (soDiem <= 0) {
            JOptionPane.showMessageDialog(this, "Khách hàng không có điểm để sử dụng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!isDungDiem) {
            isDungDiem = true;
            btnDung.setText("Bỏ Dùng");
            double giaTriDiem = soDiem * 1000;
            double tongCongHienTai = Double.parseDouble(txtTongCong.getText().replace(",", "").trim());
            double giamGia = Double.parseDouble(txtGiamGia.getText().replace(",", "").trim());
            double tongTienPhaiTra = tongCongHienTai - giamGia;
            if (giaTriDiem >= tongTienPhaiTra) {
                giaTriDiem = tongTienPhaiTra;
            }
            giaTriDiem = Math.floor(giaTriDiem / 1000) * 1000;
            int diemSuDung = (int)(giaTriDiem / 1000);
            txtSuDungDiem.setText(String.format("%,.0f", giaTriDiem));
            txtDiem.setText(String.valueOf(soDiem - diemSuDung));
        } else {
            isDungDiem = false;
            btnDung.setText("Dùng");
            txtSuDungDiem.setText("0");
            txtDiem.setText(String.valueOf(soDiem));
        }
        updateTongTien();
    }
    
    // Phương thức để cập nhật tổng tiền
    private void updateTongTien() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        txtTongCong.setText(df.format(tongCong));

        // Tính giảm giá nếu có mã khuyến mãi
        double giamGia = 0;
        String maKMSelection = cbbMaKhuyenMai.getSelectedItem() != null ? cbbMaKhuyenMai.getSelectedItem().toString() : "";
        
        if (!maKMSelection.isEmpty() && !maKMSelection.equals("Không")) {
            String maKM = maKMSelection.split(" - ")[0];
            KhuyenMai khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(maKM);
            if (khuyenMai != null) {
                giamGia = tongCong * (khuyenMai.getGiaTriKhuyenMai() / 100.0);
                txtGiamGia.setText(df.format(giamGia));
            }
        } else {
            txtGiamGia.setText("0.00");
        }
        
        double giamGiaDiem = 0;
        if (isDungDiem && khachHangHienTai != null) {
            String suDungDiemStr = txtSuDungDiem.getText().replace(",", "").trim();
            if (!suDungDiemStr.isEmpty() && !suDungDiemStr.equals("0")) {
                giamGiaDiem = Double.parseDouble(suDungDiemStr);
            }
        } else {
            txtSuDungDiem.setText("0.00");
        }

        // Tổng tiền sau khi giảm giá và sử dụng điểm
        double tongTien = tongCong - giamGia - giamGiaDiem;
        if (tongTien < 0) tongTien = 0; // Đảm bảo tổng tiền không âm
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
        cbbMaVach.setSelectedIndex(-1);
        txtSoLuong.setText("");
        txtMaKhachHang.setText("");
        txtTenKhachHang.setText("");
        txtSoDienThoai.setText("");
        txtDiem.setText("");
        cbbMaKhuyenMai.setSelectedIndex(0);
        txtTongCong.setText("");
        txtGiamGia.setText("");
        txtSuDungDiem.setText("");
        txtTongTien.setText("");

        // Vô hiệu hóa các trường nhập liệu
        setEnabledInputFields(false);

        // Đặt lại trạng thái
        daCoHoaDon = false;
        tongCong = 0;
        khachHangHienTai = null;
        isDungDiem = false;
        btnDung.setText("Dùng");
        btnDung.setEnabled(false);
    }

    // Phương thức in hóa đơn
    private void inHoaDon() {
        if (!daCoHoaDon || maHoaDonHienTai == null || maHoaDonHienTai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có hóa đơn để in!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn không có sản phẩm nào!", "Thông báo", JOptionPane.WARNING_MESSAGE);
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

            // Cập nhật thông tin hóa đơn trước khi in
            HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHoaDonHienTai);
            if (hoaDon != null) {
                // Cập nhật khách hàng nếu có
                if (khachHangHienTai != null) {
                    hoaDon.setKhachHang(khachHangHienTai);
                    
                    // Cập nhật điểm khách hàng nếu sử dụng điểm
                    if (isDungDiem) {
                        int diemHienTai = khachHangHienTai.getSoDiem();
                        String suDungDiemStr = txtSuDungDiem.getText().replace(",", "").trim();
                        if (!suDungDiemStr.isEmpty() && !suDungDiemStr.equals("0")) {
                            double giaTriDiem = Double.parseDouble(suDungDiemStr);
                            int diemSuDung = (int)(giaTriDiem / 1000);
                            khachHangHienTai.setSoDiem(diemHienTai - diemSuDung);
                            khachHangDAO.capNhatKhachHang(khachHangHienTai);
                        }
                    }
                    
                    // Thêm điểm cho khách hàng dựa trên tổng tiền
                    double tongTien = Double.parseDouble(txtTongTien.getText().replace(",", ""));
                    // Tính điểm tích lũy: 1% của tổng tiền chia cho 1000
                    int diemThuong = (int)(tongTien * 0.01 / 1000);
                    if (diemThuong > 0) {
                        khachHangHienTai.setSoDiem(khachHangHienTai.getSoDiem() + diemThuong);
                        khachHangDAO.capNhatKhachHang(khachHangHienTai);
                    }
                }
                
                // Cập nhật khuyến mãi nếu có
                String maKMSelection = cbbMaKhuyenMai.getSelectedItem() != null ? cbbMaKhuyenMai.getSelectedItem().toString() : "";
                if (!maKMSelection.isEmpty() && !maKMSelection.equals("Không")) {
                    String maKM = maKMSelection.split(" - ")[0];
                    KhuyenMai khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(maKM);
                    if (khuyenMai != null) {
                        hoaDon.setKhuyenMai(khuyenMai);
                    }
                }
                
                // Lưu cập nhật vào CSDL
                hoaDonDAO.capNhatHoaDon(hoaDon);
            }

            String giamGiaStr = txtGiamGia.getText().replace(",", "").trim();
            String tongTienStr = txtTongTien.getText().replace(",", "").trim();

            double giamGia = giamGiaStr.isEmpty() ? 0 : Double.parseDouble(giamGiaStr);
            double tongTien = tongTienStr.isEmpty() ? 0 : Double.parseDouble(tongTienStr);

            FormHoaDon formHoaDon = new FormHoaDon(maHoaDonHienTai);
            boolean thanhCong = formHoaDon.xuatHoaDonPDF(fileToSave.getAbsolutePath(), giamGia, tongTien);

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "In hóa đơn thành công!\nĐã lưu tại: " + fileToSave.getAbsolutePath(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
                resetHoaDon();
            } else {
                JOptionPane.showMessageDialog(this, "In hóa đơn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        } else if (e.getSource() == btnDung) {
            toggleDungDiem();
        } else if (e.getSource() == cbbMaKhuyenMai) {
            updateTongTien();
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

}