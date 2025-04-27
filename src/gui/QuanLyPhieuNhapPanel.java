package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.List;

import dao.ChiTietPhieuNhapDAO;
import dao.PhieuNhapDAO;
import entity.ChiTietPhieuNhap;
import entity.PhieuNhap;

public class QuanLyPhieuNhapPanel extends JPanel implements MouseListener {
    private DefaultTableModel pnTableModel;
    private DefaultTableModel ctTableModel;
    private JTable pnTable;
    private JTable ctTable;
    private JTextField txtTimPN;
    private List<PhieuNhap> danhSachPhieuNhap;
    private PhieuNhapDAO phieuNhapDAO;
    private ChiTietPhieuNhapDAO chiTietPhieuNhapDAO;

    public QuanLyPhieuNhapPanel() {
        setLayout(new BorderLayout());

        // Khởi tạo DAO
        phieuNhapDAO = new PhieuNhapDAO();
        chiTietPhieuNhapDAO = new ChiTietPhieuNhapDAO();
        
        // Lấy danh sách phiếu nhập từ CSDL
        danhSachPhieuNhap = phieuNhapDAO.getAllPhieuNhap();

        // Tạo giao diện
        JPanel formQuanLyPN = createFormQuanLyPN();
        add(formQuanLyPN, BorderLayout.CENTER);
    }

    private JPanel createFormQuanLyPN() {
        JPanel pBorder = new JPanel(new BorderLayout());
        pBorder.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Phần trên: Tìm kiếm phiếu nhập
        JPanel pTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pTop.setBorder(BorderFactory.createTitledBorder("Tìm phiếu nhập"));
        txtTimPN = new JTextField(20);
        JButton btnTimPN = new JButton("Tìm kiếm");
        JButton btnTaoMoi = new JButton("Tạo phiếu nhập mới");
        pTop.add(txtTimPN);
        pTop.add(btnTimPN);
        pTop.add(btnTaoMoi);
        pBorder.add(pTop, BorderLayout.NORTH);

        // Phần giữa: Bảng phiếu nhập và chi tiết
        JPanel pCenter = new JPanel(new BorderLayout());
        
        // Bảng phiếu nhập
        JPanel pPhieuNhap = new JPanel(new BorderLayout());
        pPhieuNhap.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập"));
        
        String[] pnColumnNames = {"Mã phiếu nhập", "Ngày nhập", "Nhân viên", "Nhà cung cấp", "Tổng tiền"};
        pnTableModel = new DefaultTableModel(pnColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pnTable = new JTable(pnTableModel);
        JScrollPane pnScrollPane = new JScrollPane(pnTable);
        pPhieuNhap.add(pnScrollPane, BorderLayout.CENTER);
        
        // Bảng chi tiết phiếu nhập
        JPanel pChiTiet = new JPanel(new BorderLayout());
        pChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu nhập"));
        
        String[] ctColumnNames = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
        ctTableModel = new DefaultTableModel(ctColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ctTable = new JTable(ctTableModel);
        JScrollPane ctScrollPane = new JScrollPane(ctTable);
        pChiTiet.add(ctScrollPane, BorderLayout.CENTER);
        
        // Chia màn hình thành 2 phần
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pPhieuNhap, pChiTiet);
        splitPane.setDividerLocation(300);
        pCenter.add(splitPane, BorderLayout.CENTER);
        
        pBorder.add(pCenter, BorderLayout.CENTER);

        // Hiển thị danh sách phiếu nhập ban đầu
        loadPhieuNhapData();
        
        // Thêm sự kiện cho bảng phiếu nhập
        pnTable.addMouseListener(this);
        
        // Thêm sự kiện cho nút tìm kiếm
        btnTimPN.addActionListener(e -> {
            String keyword = txtTimPN.getText().trim();
            if (keyword.isEmpty()) {
                loadPhieuNhapData();
                return;
            }
            
            // Tìm kiếm phiếu nhập theo mã
            PhieuNhap phieuNhap = phieuNhapDAO.getPhieuNhapTheoMa(keyword);
            pnTableModel.setRowCount(0);
            
            if (phieuNhap != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                double tongTien = chiTietPhieuNhapDAO.getTongTienPhieuNhap(phieuNhap.getMaPhieuNhap());
                
                pnTableModel.addRow(new Object[]{
                        phieuNhap.getMaPhieuNhap(),
                        sdf.format(phieuNhap.getNgayNhap()),
                        phieuNhap.getNhanVien().getTenNhanVien(),
                        phieuNhap.getNhaCungCap().getTenNhaCungCap(),
                        String.format("%,.0f", tongTien)
                });
                
                // Hiển thị chi tiết phiếu nhập
                loadChiTietPhieuNhap(phieuNhap.getMaPhieuNhap());
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu nhập với mã: " + keyword, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                ctTableModel.setRowCount(0);
            }
        });
        
        // Thêm sự kiện cho nút tạo mới
        btnTaoMoi.addActionListener(e -> {
            // Mở form nhập hàng
            JFrame frame = new JFrame("Nhập hàng");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new NhapHangPanel());
            frame.setVisible(true);
            
            // Thêm sự kiện khi đóng form nhập hàng thì cập nhật lại danh sách phiếu nhập
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    loadPhieuNhapData();
                }
            });
        });

        return pBorder;
    }
    
    private void loadPhieuNhapData() {
        pnTableModel.setRowCount(0);
        danhSachPhieuNhap = phieuNhapDAO.getAllPhieuNhap();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (PhieuNhap pn : danhSachPhieuNhap) {
            double tongTien = chiTietPhieuNhapDAO.getTongTienPhieuNhap(pn.getMaPhieuNhap());
            
            pnTableModel.addRow(new Object[]{
                    pn.getMaPhieuNhap(),
                    sdf.format(pn.getNgayNhap()),
                    pn.getNhanVien().getTenNhanVien(),
                    pn.getNhaCungCap().getTenNhaCungCap(),
                    String.format("%,.0f", tongTien)
            });
        }
        
        // Xóa chi tiết phiếu nhập
        ctTableModel.setRowCount(0);
    }
    
    private void loadChiTietPhieuNhap(String maPhieuNhap) {
        ctTableModel.setRowCount(0);
        List<ChiTietPhieuNhap> danhSachChiTiet = chiTietPhieuNhapDAO.getChiTietPhieuNhapTheoMaPN(maPhieuNhap);
        
        for (ChiTietPhieuNhap ct : danhSachChiTiet) {
            double thanhTien = ct.getSoLuong() * ct.getDonGia();
            
            ctTableModel.addRow(new Object[]{
                    ct.getSanPham().getMaSanPham(),
                    ct.getSanPham().getTenSanPham(),
                    ct.getSoLuong(),
                    String.format("%,.0f", ct.getDonGia()),
                    String.format("%,.0f", thanhTien)
            });
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = pnTable.getSelectedRow();
        if (row >= 0) {
            String maPhieuNhap = pnTableModel.getValueAt(row, 0).toString();
            loadChiTietPhieuNhap(maPhieuNhap);
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
}