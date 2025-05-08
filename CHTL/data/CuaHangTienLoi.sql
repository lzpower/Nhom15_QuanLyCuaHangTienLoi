CREATE DATABASE CuaHangTienLoi
GO

USE CuaHangTienLoi
GO

-- 1. Loại sản phẩm
CREATE TABLE LoaiSanPham (
    maLoaiSanPham NVARCHAR(6) PRIMARY KEY,
    tenLoaiSanPham NVARCHAR(100) NOT NULL
)

-- 3. Sản phẩm
CREATE TABLE SanPham (
    maSanPham NVARCHAR(12) PRIMARY KEY,
    tenSanPham NVARCHAR(100) NOT NULL,
    maLoaiSanPham NVARCHAR(6) NOT NULL,
    soLuongHienCo INT NOT NULL,
    giaNhap DECIMAL(18,2) NOT NULL,
    giaBan DECIMAL(18,2) NOT NULL,
    urlHinhAnh NVARCHAR(255),
    FOREIGN KEY (maLoaiSanPham) REFERENCES LoaiSanPham(maLoaiSanPham)
)

-- 4. Chức vụ
CREATE TABLE ChucVu (
    maChucVu NVARCHAR(5) PRIMARY KEY,
    tenChucVu NVARCHAR(100) NOT NULL
)

-- 5. Nhân viên
CREATE TABLE NhanVien (
    maNhanVien NVARCHAR(5) PRIMARY KEY,
    tenNhanVien NVARCHAR(100) NOT NULL,
    maChucVu NVARCHAR(5) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL,
    FOREIGN KEY (maChucVu) REFERENCES ChucVu(maChucVu)
)

-- 6. Tài khoản
CREATE TABLE TaiKhoan (
    tenDangNhap NVARCHAR(50) PRIMARY KEY,
    matKhau NVARCHAR(50) NOT NULL,
    vaiTro NVARCHAR(50) NOT NULL,
    maNhanVien NVARCHAR(5),
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
)

-- 7. Khách hàng
CREATE TABLE KhachHang (
    maKhachHang NVARCHAR(5) PRIMARY KEY,
    tenKhachHang NVARCHAR(100) NOT NULL,
    soDienThoai NVARCHAR(10) NOT NULL,
    soDiem INT DEFAULT 0
)

-- 8. Khuyến mãi
CREATE TABLE KhuyenMai (
    maKhuyenMai NVARCHAR(5) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(100) NOT NULL,
    giaTriKhuyenMai DECIMAL(5,2) NOT NULL CHECK (giaTriKhuyenMai > 0 AND giaTriKhuyenMai <= 100),
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL
)

-- 9. Hóa đơn
CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(10) PRIMARY KEY,
    ngayLap DATETIME NOT NULL,
    maNhanVien NVARCHAR(5) NOT NULL,
    maKhachHang NVARCHAR(5),
    maKhuyenMai NVARCHAR(5),
    tongTien DECIMAL(18,2) NOT NULL DEFAULT 0,
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang),
    FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai)
)

-- 10. Chi tiết hóa đơn
CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(10),
    maSanPham NVARCHAR(12),
    soLuong INT NOT NULL,
    donGia DECIMAL(18,2) NOT NULL,
    PRIMARY KEY (maHoaDon, maSanPham),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)
)



-- LoaiSanPham
INSERT INTO LoaiSanPham (maLoaiSanPham, tenLoaiSanPham) VALUES
('LSP001', N'Hàng tổng hợp'),
('LSP002', N'Hóa mỹ phẩm'),
('LSP003', N'Thực phẩm khô'),
('LSP004', N'Kem'),
('LSP005', N'Sữa & Sản phẩm từ sữa'),
('LSP006', N'Snack'),
('LSP007', N'Bánh kẹo'),
('LSP008', N'Nước giải khát lạnh'),
('LSP009', N'Bia - rượu')

-- ChucVu
INSERT INTO ChucVu (maChucVu, tenChucVu) VALUES
('CV001', N'Quản lý'),
('CV002', N'Nhân viên bán hàng'),
('CV003', N'Nhân viên kho')

-- NhanVien
INSERT INTO NhanVien (maNhanVien, tenNhanVien, maChucVu, soDienThoai) VALUES
('NV001', N'Nguyễn Văn Tân', 'CV001', '0376543210'),
('NV002', N'Trần Thanh Trường', 'CV002', '0851234567'),
('NV003', N'Hồ Thị Kim Xuyến', 'CV003', '0912345678'),
('NV004', N'Huỳnh Anh Trọng', 'CV002', '0598765432')

-- TaiKhoan
INSERT INTO TaiKhoan (tenDangNhap, matKhau, vaiTro, maNhanVien) VALUES
('admin', 'admin', N'Quản lý', 'NV001'),
('ttt', '123', N'Nhân viên bán hàng', 'NV002'),
('htkx', '123', N'Nhân viên kho', 'NV003'),
('hat', '123', N'Nhân viên bán hàng', 'NV004')

-- KhachHang
INSERT INTO KhachHang (maKhachHang, tenKhachHang, soDienThoai, soDiem) VALUES
('KH001', N'Nguyễn Thị X', '0901234567', 100),
('KH002', N'Trần Văn Y', '0912345678', 50),
('KH003', N'Lê Thị Z', '0923456789', 200),
('KH004', N'Phạm Văn T', '0934567890', 0),
('KH005', N'Hoàng Thị U', '0945678901', 150)

-- KhuyenMai
INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, giaTriKhuyenMai, ngayBatDau, ngayKetThuc) VALUES
('KM001', N'Giảm giá mùa thi lại', 10, '2025-05-01', '2025-05-10'),
('KM002', N'Khuyến mãi Tết cho sinh viên rớt môn', 20, '2025-01-20', '2025-02-10'),
('KM003', N'Black Friday cho sinh viên FA', 15, '2025-11-25', '2025-11-30'),
('KM004', N'FAN JACK', 25, '2025-06-01', '2025-06-15');

-- SanPham
INSERT INTO SanPham (maSanPham, tenSanPham, maLoaiSanPham, soLuongHienCo, giaNhap, giaBan, urlHinhAnh) VALUES
('893024581736', N'Vỉ 4 viên pin AA Con Ó', 'LSP001', 100, 15200, 22800, 'HangTongHop1.jpg'),
('893147205981', N'Vỉ 2 viên pin tiểu AAA Panasonic', 'LSP001', 100, 11300, 16950, 'HangTongHop2.jpg'),
('893659328174', N'Khăn giấy ăn PREMIESR VinaTissue 1 lớp 100 tờ', 'LSP001', 100, 12000, 18000, 'HangTongHop3.jpg'),
('893710492385', N'Khăn ướt em bé Bobby không mùi gói 100 miếng', 'LSP001', 100, 42000, 63000, 'HangTongHop4.jpg'),
('893843217906', N'Bột giặt OMO Comfort tinh dầu thơm nồng nàn túi 350g', 'LSP001', 100, 22000, 33000, 'HangTongHop5.jpg'),
('893932485170', N'Bột giặt nhiệt Aba hương nước hoa túi 350g', 'LSP001', 100, 19500, 29250, 'HangTongHop6.jpg'),
('893518204637', N'Khẩu trang kháng khuẩn Famapro 4D 4 lớp gói 10 cái', 'LSP001', 100, 14600, 21900, 'HangTongHop7.jpg'),
('893608391524', N'Khẩu trang y tế cao cấp Famapro Extra 4 lớp hộp 50 cái', 'LSP001', 100, 34000, 51000, 'HangTongHop8.jpg'),
('893790421683', N'Bông tẩy trang Mihoo dạng tròn 150 miếng', 'LSP002', 100, 47000, 70500, 'HoaMyPham1.jpg'),
('893286319740', N'Nước tẩy trang Simple Micellar Water Gently Removes Make Up & Hydrates làm sạch lớp trang điểm 400ml', 'LSP002', 100, 159000, 238500, 'HoaMyPham2.jpg'),
('893104592837', N'Kem đánh răng P/S trắng răng than hoạt tính 230g', 'LSP002', 100, 35000, 52500, 'HoaMyPham3.jpg'),
('893375801294', N'Kem đánh răng Sensodyne trắng răng tự nhiên 100g', 'LSP002', 100, 74000, 111000, 'HoaMyPham4.jpg'),
('893421790865', N'Sữa chống nắng Sunplay Skin Aqua Clear White dưỡng da trắng mịn SPF 50+/PA++++ 25g', 'LSP002', 100, 132000, 198000, 'HoaMyPham5.jpg'),
('893916284530', N'Sáp vuốt tóc Romano Clay Wax giữ nếp lâu', 'LSP002', 100, 82000, 123000, 'HoaMyPham6.jpg'),
('893635728149', N'Ngũ cốc ăn sáng Nestlé Milo gói 70g', 'LSP003', 100, 31000, 46500, 'ThucPhamKho1.jpg'),
('893752490381', N'Gói nước cốt cà phê sữa NesCafé 75ml', 'LSP003', 100, 8000, 12000, 'ThucPhamKho2.jpg'),
('893382917045', N'Mì Hảo Hảo vị tôm chua cay gói 75g', 'LSP003', 100, 4400, 6600, 'ThucPhamKho3.jpg'),
('893509273186', N'Mì xào khô Indomie Mi Goreng vị đặc biệt gói 85g', 'LSP003', 100, 6000, 9000, 'ThucPhamKho4.jpg'),
('893678214953', N'Mì Modern lẩu Thái tôm ly 65g', 'LSP003', 100, 8200, 12300, 'ThucPhamKho5.jpg'),
('893801357692', N'Mì trộn Cung Đình Kool vị sườn nướng tô 99g', 'LSP003', 100, 15300, 22950, 'ThucPhamKho6.jpg'),
('893215074839', N'Kem que Topten Vanila Wall''s cây 60g', 'LSP004', 100, 10000, 15000, 'Kem1.jpg'),
('893947260318', N'Kem bánh mochi dâu Joyday gói 45ml', 'LSP004', 100, 6000, 9000, 'Kem2.jpg'),
('893062173489', N'Kem vani socola Merino Super Teen cây 60g', 'LSP004', 100, 9000, 13500, 'Kem3.jpg'),
('893439528176', N'Kem trứng muối dừa Hùng Linh Snow baby premium cây 60ml', 'LSP004', 100, 15000, 22500, 'Kem4.jpg'),
('893124708935', N'Sữa tươi tiệt trùng ít đường Vinamilk 100% Sữa tươi bịch 220ml', 'LSP005', 100, 8600, 12900, 'Sua1.jpg'),
('893673905812', N'Hộp sữa tươi tiệt trùng có đường TH true MILK 180ml', 'LSP005', 100, 9000, 13500, 'Sua2.jpg'),
('893298471650', N'Hộp sữa lúa mạch Milo 180ml', 'LSP005', 100, 8000, 12000, 'Sua3.jpg'),
('893540296781', N'Hộp sữa chua không đường Vinamilk 100g', 'LSP005', 100, 5800, 8700, 'Sua4.jpg'),
('893826107943', N'Chai sữa uống lên men Yakult 65ml', 'LSP005', 100, 5000, 7500, 'Sua5.jpg'),
('893015384672', N'Sữa uống lên men hương cam Betagen chai 700ml', 'LSP005', 100, 30000, 45000, 'Sua6.jpg'),
('893274509316', N'Snack tôm cay đặc biệt Oishi gói 32g', 'LSP006', 100, 4000, 6000, 'Snack1.jpg'),
('893364801295', N'Snack que nhân vị kem Tiramisu Oishi Akiko gói 140g', 'LSP006', 100, 20000, 30000, 'Snack2.jpg'),
('893703421586', N'Snack vị bò lúc lắc Poca gói 65g', 'LSP006', 100, 10000, 15000, 'Snack3.jpg'),
('893892640731', N'Snack vị kim chi Hàn Quốc O''Star gói 152g', 'LSP006', 100, 21000, 31500, 'Snack4.jpg'),
('893678209354', N'Snack khoai tây tôm hùm nướng ngũ vị Lay''s Stax lon 150g', 'LSP006', 100, 40000, 60000, 'Snack5.jpg'),
('893197630458', N'Bánh gạo nướng vị tảo biển Orion An gói 111.3g', 'LSP007', 100, 15000, 22500, 'BanhKeo1.jpg'),
('893903726184', N'Bánh chocopie Orion hộp 198g (6 cái)', 'LSP007', 100, 30000, 45000, 'BanhKeo2.jpg'),
('893086492317', N'Kẹo hương bạc hà nhân socola Dynamite Big Bang gói 120g', 'LSP007', 100, 12000, 18000, 'BanhKeo3.jpg'),
('893425087163', N'Kẹo dẻo lợi khuẩn vị trái cây Bibica Huro Probiotics gói 24g', 'LSP007', 100, 6000, 9000, 'BanhKeo4.jpg'),
('893713945280', N'Kẹo ngậm không đường Mentos Clean Breath hương đào bạc hà hộp 35g', 'LSP007', 100, 28800, 43200, 'BanhKeo5.jpg'),
('893592014738', N'Nước ngọt 7 Up vị chanh lon 320ml', 'LSP008', 100, 6200, 9300, 'Nuoc1.jpg'),
('893280143697', N'Nước ngọt Pepsi không calo chai 390ml', 'LSP008', 100, 7000, 10500, 'Nuoc2.jpg'),
('893491703652', N'Nước tăng lực Redbull Thái kẽm và vitamin 250ml', 'LSP008', 100, 9000, 13500, 'Nuoc3.jpg'),
('893869204517', N'Nước tăng lực Monster Energy 355ml', 'LSP008', 100, 20000, 30000, 'Nuoc4.jpg'),
('893351209478', N'Nước tinh khiết Aquafina 355ml', 'LSP008', 100, 5000, 7500, 'Nuoc5.jpg'),
('893173046892', N'Nước giải khát có ga Aquafina Soda 320ml', 'LSP008', 100, 8000, 12000, 'Nuoc6.jpg'),
('893692174058', N'Sữa trái cây Nutriboost hương bánh quy kem 297ml', 'LSP008', 100, 9000, 13500, 'Nuoc7.jpg'),
('893948731025', N'Bia Sài Gòn Lager 330ml', 'LSP009', 100, 10000, 15000, 'BiaRuou1.jpg'),
('893064792158', N'Bia Tiger lon 250ml', 'LSP009', 100, 10600, 15900, 'BiaRuou2.jpg'),
('893210385749', N'Rượu soju Korice hương việt quất 12% chai 360ml', 'LSP009', 100, 50000, 75000, 'BiaRuou3.jpg'),
('893758291360', N'Rượu vang đỏ Sài Gòn Classic 12.5% chai 750ml', 'LSP009', 100, 90000, 135000, 'BiaRuou4.jpg'),
('893147328059', N'Nước trái cây lên men Chill Blueberry Vodka vị việt quất lon 330ml', 'LSP009', 100, 18000, 27000, 'BiaRuou5.jpg')
-- Insert data for HoaDon
INSERT INTO HoaDon (maHoaDon, ngayLap, maNhanVien, maKhachHang, maKhuyenMai, tongTien) VALUES
('HD006', '2025-01-15 09:00:00', 'NV001', 'KH001', NULL, 125000),
('HD007', '2025-02-10 14:20:00', 'NV002', 'KH001', NULL, 183600),
('HD008', '2025-03-22 11:45:00', 'NV003', 'KH001', NULL, 267000),
('HD009', '2025-04-05 16:30:00', 'NV001', 'KH001', NULL, 174600),
('HD010', '2025-05-18 08:55:00', 'NV003', NULL, NULL, 82000),
('HD011', '2025-06-09 13:10:00', 'NV002', 'KH001', NULL, 149500),
('HD012', '2025-07-25 15:40:00', 'NV004', 'KH001', NULL, 219600),
('HD013', '2025-08-13 10:05:00', 'NV001', 'KH002', NULL, 297000),
('HD014', '2025-09-07 17:30:00', 'NV003', NULL, NULL, 96000),
('HD015', '2025-10-19 09:15:00', 'NV004', 'KH001', NULL, 132000),
('HD016', '2025-11-11 12:25:00', 'NV002', 'KH001', NULL, 189000),
('HD017', '2025-12-03 18:50:00', 'NV001', 'KH001', NULL, 245700);


-- ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHoaDon, maSanPham, soLuong, donGia) VALUES
('HD006', '893215074839', 2, 15000),
('HD006', '893903726184', 1, 45000),
('HD007', '893518204637', 3, 21900),
('HD007', '893124708935', 6, 12900),
('HD008', '893024581736', 2, 22800),
('HD008', '893286319740', 1, 221400),
('HD009', '893421790865', 1, 198000),
('HD009', '893635728149', 2, 46500),
('HD010', '893382917045', 4, 6600),
('HD010', '893592014738', 2, 9300),
('HD011', '893024581736', 3, 22800),
('HD012', '893286319740', 2, 238500),
('HD012', '893518204637', 1, 21900),
('HD013', '893124708935', 6, 12900),
('HD013', '893421790865', 1, 219600),
('HD014', '893215074839', 3, 15000),
('HD015', '893903726184', 2, 45000),
('HD016', '893635728149', 2, 46500),
('HD016', '893382917045', 5, 6600),
('HD017', '893286319740', 1, 238500),
('HD017', '893518204637', 1, 21900);
