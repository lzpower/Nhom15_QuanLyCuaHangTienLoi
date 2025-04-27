package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import dao.NhaCungCapDAO;
import dao.NhanVienDAO;
import dao.PhieuNhapDAO;
import dao.ChiTietPhieuNhapDAO;
import dao.SanPhamDAO;
import entity.ChiTietPhieuNhap;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhap;
import entity.SanPham;

public class NhapHangPanel extends JPanel implements ActionListener {
    private JTextField txtMaPhieuNhap;
    private JTextField txtNgayNhap;
    private JComboBox<String> cbbNhanVien;
    private JComboBox<String> cbbNhaCungCap;
    private JComboBox<String> cbbSanPham;
    private JTextField txtSoLuong;
    private JTextField txtDonGia;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnLuu;
    private JButton btnTaoMoi;
    private DefaultTableModel modelChiTiet;
    private JTable tableChiTiet;
    
    private PhieuNhapDAO phieuNhapDAO;
    private ChiTietPhieuNhapDAO chiTietPhieuNhapDAO;
    private NhanVienDAO nhanVienDAO;
    private NhaCungCapDAO nhaCungCapDAO;
    private SanPhamDAO sanPhamDAO;
    
    private String maPhieuNhapHienTai;
    private List<ChiTietPhieuNhap> danhSachChiTiet;
    
    public NhapHangPanel() {
        try {
            ConnectDB.getInstance().connect();
            
            phieuNhapDAO = new PhieuNhapDAO();
            chiTietPhieuNhapDAO = new ChiTietPhieuNhapDAO();
            nhanVienDAO = new NhanVienDAO();
            nhaCungCapDAO = new NhaCungCapDAO();
            sanPhamDAO = new SanPhamDAO();
            
            danhSachChiTiet = new ArrayList<>();
            
            initUI();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Panel thông tin phiếu nhập
        JPanel pThongTin = new JPanel(new GridLayout(2, 4, 10, 10));
        pThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu nhập"));
        
        pThongTin.add(new JLabel("Mã phiếu nhập:"));
        pThongTin.add(txtMaPhieuNhap = new JTextField());
        txtMaPhieuNhap.setEditable(false);
        
        pThongTin.add(new JLabel("Ngày nhập:"));
        pThongTin.add(txtNgayNhap = new JTextField());
        txtNgayNhap.setEditable(false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        txtNgayNhap.setText(sdf.format(new Date()));
        
        pThongTin.add(new JLabel("Nhân viên:"));
        pThongTin.add(cbbNhanVien = new JComboBox<>());
        loadNhanVien();
        
        pThongTin.add(new JLabel("Nhà cung cấp:"));
        pThongTin.add(cbbNhaCungCap = new JComboBox<>());
        loadNhaCungCap();
        
        add(pThongTin, BorderLayout.NORTH);
        
        // Panel chi tiết phiếu nhập
        JPanel pChiTiet = new JPanel(new BorderLayout());
        pChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));
        
        // Panel nhập chi tiết
        JPanel pNhapChiTiet = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        pNhapChiTiet.add(new JLabel("Sản phẩm:"));
        pNhapChiTiet.add(cbbSanPham = new JComboBox<>());
        cbbSanPham.setPreferredSize(new Dimension(200, 25));
        loadSanPham();
        
        pNhapChiTiet.add(new JLabel("Số lượng:"));
        pNhapChiTiet.add(txtSoLuong = new JTextField(5));
        
        pNhapChiTiet.add(new JLabel("Đơn giá:"));
        pNhapChiTiet.add(txtDonGia = new JTextField(10));
        
        pNhapChiTiet.add(btnThem = new JButton("Thêm"));
        pNhapChiTiet.add(btnXoa = new JButton("Xóa"));
        
        pChiTiet.add(pNhapChiTiet, BorderLayout.NORTH);
        
        // Bảng chi tiết
        String[] cols = {"STT", "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
        modelChiTiet = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableChiTiet = new JTable(modelChiTiet);
        JScrollPane scrollPane = new JScrollPane(tableChiTiet);
        pChiTiet.add(scrollPane, BorderLayout.CENTER);
        
        add(pChiTiet, BorderLayout.CENTER);
        
        // Panel nút
        JPanel pButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pButton.add(btnTaoMoi = new JButton("Tạo mới"));
        pButton.add(btnLuu = new JButton("Lưu phiếu nhập"));
        
        add(pButton, BorderLayout.SOUTH);
        
        // Thêm sự kiện
        btnTaoMoi.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLuu.addActionListener(this);
        
        // Khởi tạo mã phiếu nhập mới
        maPhieuNhapHienTai = phieuNhapDAO.taoMaPhieuNhapMoi();
        txtMaPhieuNhap.setText(maPhieuNhapHienTai);
    }
    
    private void loadNhanVien() {
        List<NhanVien> danhSachNV = nhanVienDAO.getAllNhanVien();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        for (NhanVien nv : danhSachNV) {
            model.addElement(nv.getMaNhanVien() + " - " + nv.getTenNhanVien());
        }
        
        cbbNhanVien.setModel(model);
    }
    
    private void loadNhaCungCap() {
        List<NhaCungCap> danhSachNCC = nhaCungCapDAO.getAllNhaCungCap();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        for (NhaCungCap ncc : danhSachNCC) {
            model.addElement(ncc.getMaNhaCungCap() + " - " + ncc.getTenNhaCungCap());
        }
        
        cbbNhaCungCap.setModel(model);
    }
    
    private void loadSanPham() {
        List<SanPham> danhSachSP = sanPhamDAO.getAllSanPham();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        for (SanPham sp : danhSachSP) {
            model.addElement(sp.getMaSanPham() + " - " + sp.getTenSanPham());
        }
        
        cbbSanPham.setModel(model);
    }
    
    private void themChiTiet() {
        if (cbbSanPham.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String soLuongStr = txtSoLuong.getText().trim();
        String donGiaStr = txtDonGia.getText().trim();
        
        if (soLuongStr.isEmpty() || donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ số lượng và đơn giá!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int soLuong = Integer.parseInt(soLuongStr);
            double donGia = Double.parseDouble(donGiaStr);
            
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (donGia <= 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Lấy thông tin sản phẩm
            String spSelection = cbbSanPham.getSelectedItem().toString();
            String maSP = spSelection.split(" - ")[0];
            String tenSP = spSelection.split(" - ")[1];
            
            // Kiểm tra sản phẩm đã có trong danh sách chưa
            boolean daTonTai = false;
            int viTri = -1;
            
            for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
                if (modelChiTiet.getValueAt(i, 1).equals(maSP)) {
                    daTonTai = true;
                    viTri = i;
                    break;
                }
            }
            
            if (daTonTai) {
                // Cập nhật số lượng và thành tiền
                int soLuongCu = Integer.parseInt(modelChiTiet.getValueAt(viTri, 3).toString());
                double donGiaCu = Double.parseDouble(modelChiTiet.getValueAt(viTri, 4).toString());
                
                int soLuongMoi = soLuongCu + soLuong;
                double thanhTienMoi = soLuongMoi * donGiaCu;
                
                modelChiTiet.setValueAt(soLuongMoi, viTri, 3);
                modelChiTiet.setValueAt(String.format("%.2f", thanhTienMoi), viTri, 5);
                
                // Cập nhật danh sách chi tiết
                for (ChiTietPhieuNhap ct : danhSachChiTiet) {
                    if (ct.getSanPham().getMaSanPham().equals(maSP)) {
                        ct.setSoLuong(soLuongMoi);
                        break;
                    }
                }
            } else {
                // Thêm mới vào bảng
                double thanhTien = soLuong * donGia;
                
                modelChiTiet.addRow(new Object[] {
                    modelChiTiet.getRowCount() + 1,
                    maSP,
                    tenSP,
                    soLuong,
                    donGia,
                    String.format("%.2f", thanhTien)
                });
                
                // Thêm vào danh sách chi tiết
                SanPham sp = sanPhamDAO.getSanPhamTheoMa(maSP);
                PhieuNhap pn = new PhieuNhap();
                pn.setMaPhieuNhap(maPhieuNhapHienTai);
                
                ChiTietPhieuNhap ct = new ChiTietPhieuNhap(pn, sp, soLuong, donGia);
                danhSachChiTiet.add(ct);
            }
            
            // Xóa nội dung các trường nhập liệu
            cbbSanPham.setSelectedIndex(-1);
            txtSoLuong.setText("");
            txtDonGia.setText("");
            cbbSanPham.requestFocus();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng và đơn giá phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void xoaChiTiet() {
        int selectedRow = tableChiTiet.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maSP = modelChiTiet.getValueAt(selectedRow, 1).toString();
        
        // Xóa khỏi danh sách chi tiết
        danhSachChiTiet.removeIf(ct -> ct.getSanPham().getMaSanPham().equals(maSP));
        
        // Xóa khỏi bảng
        modelChiTiet.removeRow(selectedRow);
        
        // Cập nhật lại STT
        for (int i = 0; i < modelChiTiet.getRowCount(); i++) {
            modelChiTiet.setValueAt(i + 1, i, 0);
        }
    }
    
    private void luuPhieuNhap() {
        if (danhSachChiTiet.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phiếu nhập chưa có sản phẩm nào!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (cbbNhanVien.getSelectedIndex() == -1 || cbbNhaCungCap.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên và nhà cung cấp!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Lấy thông tin nhân viên
            String nvSelection = cbbNhanVien.getSelectedItem().toString();
            String maNV = nvSelection.split(" - ")[0];
            NhanVien nv = nhanVienDAO.getNhanVienTheoMa(maNV);
            
            // Lấy thông tin nhà cung cấp
            String nccSelection = cbbNhaCungCap.getSelectedItem().toString();
            String maNCC = nccSelection.split(" - ")[0];
            NhaCungCap ncc = nhaCungCapDAO.getNhaCungCapTheoMa(maNCC);
            
            // Tạo phiếu nhập
            PhieuNhap phieuNhap = new PhieuNhap(maPhieuNhapHienTai, new Date(), nv, ncc);
            
            // Lưu phiếu nhập vào CSDL
            boolean success = phieuNhapDAO.themPhieuNhap(phieuNhap);
            if (!success) {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Lưu chi tiết phiếu nhập
            for (ChiTietPhieuNhap ct : danhSachChiTiet) {
                ct.setPhieuNhap(phieuNhap);
                chiTietPhieuNhapDAO.themChiTietPhieuNhap(ct);
                
                // Cập nhật số lượng sản phẩm
                SanPham sp = ct.getSanPham();
                int soLuongMoi = sp.getSoLuongHienCo() + ct.getSoLuong();
                sanPhamDAO.capNhatSoLuongSanPham(sp.getMaSanPham(), soLuongMoi);
            }
            
            JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            
            // Tạo phiếu nhập mới
            taoMoi();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void taoMoi() {
        // Tạo mã phiếu nhập mới
        maPhieuNhapHienTai = phieuNhapDAO.taoMaPhieuNhapMoi();
        txtMaPhieuNhap.setText(maPhieuNhapHienTai);
        
        // Cập nhật ngày nhập
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        txtNgayNhap.setText(sdf.format(new Date()));
        
        // Xóa danh sách chi tiết
        danhSachChiTiet.clear();
        modelChiTiet.setRowCount(0);
        
        // Reset các trường nhập liệu
        cbbSanPham.setSelectedIndex(-1);
        txtSoLuong.setText("");
        txtDonGia.setText("");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThem) {
            themChiTiet();
        } else if (e.getSource() == btnXoa) {
            xoaChiTiet();
        } else if (e.getSource() == btnLuu) {
            luuPhieuNhap();
        } else if (e.getSource() == btnTaoMoi) {
            taoMoi();
        }
    }
}