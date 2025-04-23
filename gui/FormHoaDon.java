package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class FormHoaDon extends JFrame{
	private DefaultTableModel modaltable;
	private JTable table;
	public FormHoaDon() {
		super("Hoá Đơn");
		setSize(400,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//pNorth
		JPanel pNorth=new JPanel();
		pNorth.setLayout(new GridLayout(1, 2));
		ImageIcon img=new ImageIcon("src/img/logo.png");
		JLabel lblimg=new JLabel(img);
		JPanel p1 = new JPanel(new GridLayout(3, 1,0,10));
		JLabel lblNhom=new JLabel("NHÓM 15",SwingConstants.CENTER);
		JLabel lblMH=new JLabel("LTHSK",SwingConstants.CENTER);
		JLabel lblLop=new JLabel("DHKTPM19B",SwingConstants.CENTER);
	
		p1.add(lblNhom);
		p1.add(lblMH);
		p1.add(lblLop);
		
		pNorth.add(lblimg);
		pNorth.add(p1);
		add(pNorth,BorderLayout.NORTH);
		
		//pCenter
		
		//td
		JPanel pCenter=new JPanel(new BorderLayout());
		JLabel lblHD=new JLabel("HOÁ ĐƠN THANH TOÁN",SwingConstants.CENTER);
		lblHD.setPreferredSize(new Dimension(50, 60));
		lblHD.setFont(new Font("Arial", Font.BOLD, 20));
		add(pCenter,BorderLayout.CENTER);
		//mã hd, ngày,nhân viên ,tên kh
		JPanel pInfor=new JPanel();
		pInfor.setLayout(new GridLayout(2,2,0,0));
		
		JLabel lblMaHD=new JLabel("Mã HD:",SwingConstants.LEFT);
		JLabel lblNgay=new JLabel("Ngày:",SwingConstants.LEFT);
		JLabel lblTG=new JLabel("Thời gian:",SwingConstants.LEFT);
		JLabel lblKH=new JLabel("Khách hàng:",SwingConstants.LEFT);
		
		JLabel lblMaHDtxt=new JLabel("J97");
		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime =LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedDate = currentDate.format(formatter);
        String formattedTime = currentTime.format(timeFormatter);
        JLabel lblNgaytxt=new JLabel(formattedDate);
        JLabel lblTGtxt=new JLabel(formattedTime);
        JLabel lblKHtxt=new JLabel("J97");
        
        JPanel p11=new JPanel(); p11.add(lblMaHD);p11.add(lblMaHDtxt);
        JPanel p12=new JPanel();p12.add(lblNgay);p12.add(lblNgaytxt);
        JPanel p13=new JPanel();p13.add(lblTG);p13.add(lblTGtxt);
        JPanel p14=new JPanel();p14.add(lblKH);p14.add(lblKHtxt);
        pInfor.add(p11);pInfor.add(p12);pInfor.add(p13);pInfor.add(p14);
        
        JPanel pTop=new JPanel(new GridLayout(2,1,2,2));
        pTop.add(lblHD);
        pTop.add(pInfor);
        
        pCenter.add(pTop,BorderLayout.NORTH);
        
        //table
        String cols[]= {"STT","Tên SP","SL","Đơn Giá","Thành tiền"};
        modaltable= new DefaultTableModel(cols,5);
        table=new JTable(modaltable);
        // Chỉnh kích thước cột
        table.getColumnModel().getColumn(0).setPreferredWidth(25); 
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(25);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

       	JScrollPane sc=new JScrollPane(table);
       //pCenter.add(sc,BorderLayout.CENTER);
		
       	JPanel pThanhTien=new JPanel(new GridLayout(6,2,0,0));
		
		JLabel lblTongTien=new JLabel("Tổng tiền");
		JLabel lblTongTientxt=new JLabel("5 củ",SwingConstants.RIGHT);  //setText
		JLabel lblGiamGia=new JLabel("Giảm giá");
		JLabel lblGiamGiatxt=new JLabel("1 củ",SwingConstants.RIGHT);  //setText
		JLabel lblThanhTien=new JLabel("Thành tiền");
		JLabel lblThanhTientxt=new JLabel("4 củ",SwingConstants.RIGHT);  //setText
		JLabel lblTienMat=new JLabel("Thanh toán tiền mặt");
		JLabel lblTienMattxt=new JLabel("4 củ",SwingConstants.RIGHT);  //setText
		JLabel lblTienNhan=new JLabel("Tiền nhận");
		JLabel lblTienNhantxt=new JLabel("6 củ",SwingConstants.RIGHT);  //setText
		JLabel lblTienThua=new JLabel("Tiền thừa");
		JLabel lblTienThuatxt=new JLabel("2 củ",SwingConstants.RIGHT);  //setText
		
		pThanhTien.add(lblTongTien);pThanhTien.add(lblTongTientxt);
		pThanhTien.add(lblGiamGia);pThanhTien.add(lblGiamGiatxt);
		pThanhTien.add(lblThanhTien);pThanhTien.add(lblThanhTientxt);
		pThanhTien.add(lblTienMat);pThanhTien.add(lblTienMattxt);
		pThanhTien.add(lblTienNhan);pThanhTien.add(lblTienNhantxt);
		pThanhTien.add(lblTienThua);pThanhTien.add(lblTienThuatxt);
		
		//pCenter.add(pThanhTien,BorderLayout.SOUTH);
		JPanel ptable=new JPanel(new GridLayout(2,1,0,0));
		ptable.add(sc);
		ptable.add(pThanhTien);
		pCenter.add(ptable,BorderLayout.CENTER);
		
	}
	public static void main(String[] args) {
		new FormHoaDon().setVisible(true);
	}
}
