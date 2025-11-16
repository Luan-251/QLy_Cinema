------------------------------------------------------------
-- TẠO DATABASE (nếu cần)
------------------------------------------------------------
IF DB_ID('CinemaDB') IS NULL
BEGIN
    CREATE DATABASE CinemaDB;
END;
GO

USE CinemaDB;
GO

------------------------------------------------------------
-- XÓA BẢNG CŨ NẾU TỒN TẠI (để chạy lại script cho sạch)
------------------------------------------------------------
IF OBJECT_ID('ChiTietHoaDon', 'U') IS NOT NULL DROP TABLE ChiTietHoaDon;
IF OBJECT_ID('Ve', 'U') IS NOT NULL DROP TABLE Ve;
IF OBJECT_ID('SuatChieu', 'U') IS NOT NULL DROP TABLE SuatChieu;
IF OBJECT_ID('Ghe', 'U') IS NOT NULL DROP TABLE Ghe;
IF OBJECT_ID('Phong', 'U') IS NOT NULL DROP TABLE Phong;
IF OBJECT_ID('Phim', 'U') IS NOT NULL DROP TABLE Phim;
IF OBJECT_ID('HoaDon', 'U') IS NOT NULL DROP TABLE HoaDon;
IF OBJECT_ID('NhanVien', 'U') IS NOT NULL DROP TABLE NhanVien;
IF OBJECT_ID('TrangThai', 'U') IS NOT NULL DROP TABLE TrangThai;
IF OBJECT_ID('LoaiGhe', 'U') IS NOT NULL DROP TABLE LoaiGhe;
GO

------------------------------------------------------------
-- ENUM: LOAIGHE
------------------------------------------------------------
CREATE TABLE LoaiGhe (
    MaLoaiGhe VARCHAR(20) NOT NULL PRIMARY KEY
    -- GHE_THUONG, GHE_VIP, GHE_DOI
);
GO

------------------------------------------------------------
-- ENUM: TRANGTHAI (TRẠNG THÁI VÉ)
------------------------------------------------------------
CREATE TABLE TrangThai (
    MaTrangThai VARCHAR(20) NOT NULL PRIMARY KEY
    -- TRONG, DANG_CHO, DA_DAT
);
GO

------------------------------------------------------------
-- BẢNG NHÂN VIÊN
------------------------------------------------------------
CREATE TABLE NhanVien (
    MaNhanVien          VARCHAR(10)     NOT NULL PRIMARY KEY,
    TenNhanVien         NVARCHAR(100)   NOT NULL,
    Email               VARCHAR(100)    NOT NULL,
    MatKhau             NVARCHAR(100)   NOT NULL,
    ThoiGianTaoTaiKhoan DATETIME2(0)    NOT NULL
);
GO

------------------------------------------------------------
-- BẢNG PHÒNG
------------------------------------------------------------
CREATE TABLE Phong (
    MaPhong  VARCHAR(10)   NOT NULL PRIMARY KEY,
    TenPhong NVARCHAR(100) NOT NULL,
    SoGhe    INT           NOT NULL
);
GO

------------------------------------------------------------
-- BẢNG PHIM
------------------------------------------------------------
CREATE TABLE Phim (
    MaPhim         VARCHAR(10)    NOT NULL PRIMARY KEY,
    TenPhim        NVARCHAR(200)  NOT NULL,
    ThoiLuong      INT            NOT NULL,   -- phút
    TenDaoDien     NVARCHAR(100)  NOT NULL,
    TenCacDienVien NVARCHAR(255)  NOT NULL,
    TheLoai        NVARCHAR(100)  NOT NULL,
    GiaPhim        DECIMAL(18,0)  NOT NULL
);
GO

------------------------------------------------------------
-- BẢNG GHẾ
-- PK: (MaGhe, MaPhong) để có thể trùng A1 ở nhiều phòng khác nhau
------------------------------------------------------------
CREATE TABLE Ghe (
    MaGhe      VARCHAR(10)    NOT NULL,
    MaPhong    VARCHAR(10)    NOT NULL,
    GiaGhe     DECIMAL(18,0)  NOT NULL,
    MaLoaiGhe  VARCHAR(20)    NOT NULL,
    CONSTRAINT PK_Ghe PRIMARY KEY (MaGhe, MaPhong),
    CONSTRAINT FK_Ghe_Phong FOREIGN KEY (MaPhong)
        REFERENCES Phong(MaPhong),
    CONSTRAINT FK_Ghe_LoaiGhe FOREIGN KEY (MaLoaiGhe)
        REFERENCES LoaiGhe(MaLoaiGhe)
);
GO

------------------------------------------------------------
-- BẢNG SUẤT CHIẾU
------------------------------------------------------------
CREATE TABLE SuatChieu (
    MaSuatChieu     VARCHAR(20)  NOT NULL PRIMARY KEY,
    ThoiGianBatDau  DATETIME2(0) NOT NULL,
    MaPhong         VARCHAR(10)  NOT NULL,
    MaPhim          VARCHAR(10)  NOT NULL,
    CONSTRAINT FK_SuatChieu_Phong FOREIGN KEY (MaPhong)
        REFERENCES Phong(MaPhong),
    CONSTRAINT FK_SuatChieu_Phim FOREIGN KEY (MaPhim)
        REFERENCES Phim(MaPhim)
);
GO

------------------------------------------------------------
-- BẢNG VÉ
-- Mỗi vé = 1 ghế + 1 suất chiếu + trạng thái
------------------------------------------------------------
CREATE TABLE Ve (
    MaVe         VARCHAR(20)    NOT NULL PRIMARY KEY,
    MaSuatChieu  VARCHAR(20)    NOT NULL,
    MaGhe        VARCHAR(10)    NOT NULL,
    MaPhong      VARCHAR(10)    NOT NULL, -- để tham chiếu đúng ghế
    TrangThai    VARCHAR(20)    NOT NULL,
    CONSTRAINT FK_Ve_SuatChieu FOREIGN KEY (MaSuatChieu)
        REFERENCES SuatChieu(MaSuatChieu),
    CONSTRAINT FK_Ve_Ghe FOREIGN KEY (MaGhe, MaPhong)
        REFERENCES Ghe(MaGhe, MaPhong),
    CONSTRAINT FK_Ve_TrangThai FOREIGN KEY (TrangThai)
        REFERENCES TrangThai(MaTrangThai),
    CONSTRAINT UQ_Ve_SuatChieu_Ghe UNIQUE (MaSuatChieu, MaGhe, MaPhong)
);
GO

------------------------------------------------------------
-- BẢNG HÓA ĐƠN
------------------------------------------------------------
CREATE TABLE HoaDon (
    MaHoaDon   VARCHAR(20)   NOT NULL PRIMARY KEY,
    NgayLap    DATETIME2(0)  NOT NULL,
    MaNhanVien VARCHAR(10)   NOT NULL,
    Thue       DECIMAL(5,2)  NOT NULL,
    TienNhan   DECIMAL(18,0) NOT NULL,
    TongTien   DECIMAL(18,0) NOT NULL,
    TienThua   DECIMAL(18,0) NOT NULL,
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (MaNhanVien)
        REFERENCES NhanVien(MaNhanVien)
);
GO

------------------------------------------------------------
-- BẢNG CHI TIẾT HÓA ĐƠN
------------------------------------------------------------
CREATE TABLE ChiTietHoaDon (
    MaHoaDon VARCHAR(20) NOT NULL,
    MaVe     VARCHAR(20) NOT NULL,
    CONSTRAINT PK_ChiTietHoaDon PRIMARY KEY (MaHoaDon, MaVe),
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (MaHoaDon)
        REFERENCES HoaDon(MaHoaDon),
    CONSTRAINT FK_CTHD_Ve FOREIGN KEY (MaVe)
        REFERENCES Ve(MaVe)
);
GO

------------------------------------------------------------
-- INSERT GIÁ TRỊ ENUM
------------------------------------------------------------
INSERT INTO LoaiGhe (MaLoaiGhe) VALUES
('GHE_THUONG'),
('GHE_VIP'),
('GHE_DOI');
GO

INSERT INTO TrangThai (MaTrangThai) VALUES
('TRONG'),
('DANG_CHO'),
('DA_DAT');
GO

------------------------------------------------------------
-- INSERT PHÒNG
------------------------------------------------------------
INSERT INTO Phong (MaPhong, TenPhong, SoGhe) VALUES
('P001', N'Phòng 1', 50),
('P002', N'Phòng 2', 50),
('P003', N'Phòng 3', 50);
GO

------------------------------------------------------------
-- INSERT NHÂN VIÊN
------------------------------------------------------------
INSERT INTO NhanVien (MaNhanVien, TenNhanVien, Email, MatKhau, ThoiGianTaoTaiKhoan)
VALUES
('NV001', N'Nguyễn Văn A', 'a@example.com', N'123456', '2025-01-01T08:00:00'),
('NV002', N'Trần Thị B',   'b@example.com', N'123456', '2025-01-01T08:05:00'),
('NV003', N'Lê Văn C',     'c@example.com', N'123456', '2025-01-01T08:10:00');
GO

------------------------------------------------------------
-- INSERT PHIM
------------------------------------------------------------
INSERT INTO Phim (MaPhim, TenPhim, ThoiLuong, TenDaoDien, TenCacDienVien, TheLoai, GiaPhim)
VALUES
('M001', N'Hành Trình Ánh Sáng', 120, N'Đạo diễn 1',
        N'Diễn viên 1, Diễn viên 2', N'Hành động', 90000),
('M002', N'Đêm Trong Thành Phố', 110, N'Đạo diễn 2',
        N'Diễn viên 3, Diễn viên 4', N'Tâm lý', 85000),
('M003', N'Kẻ Đánh Cắp Thời Gian', 130, N'Đạo diễn 3',
        N'Diễn viên 5, Diễn viên 6', N'Viễn tưởng', 95000);
GO

------------------------------------------------------------
-- INSERT GHẾ CHO PHÒNG 1 (P001) - 50 GHẾ
-- Hàng A–E, mỗi hàng 10 ghế (A1..E10)
-- Giá:
--   GHE_THUONG: 50,000
--   GHE_DOI   : 60,000 ( +20% )
--   GHE_VIP   : 65,000 ( +30% )
--
-- VIP: B5, B6, C5, C6, D5 (5 ghế)
-- ĐÔI: D10, E1, E2, E3, E4, E9, E10 (7 ghế)
------------------------------------------------------------

-- Hàng A: tất cả ghế thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('A01', 'P001', 50000, 'GHE_THUONG'),
('A02', 'P001', 50000, 'GHE_THUONG'),
('A03', 'P001', 50000, 'GHE_THUONG'),
('A04', 'P001', 50000, 'GHE_THUONG'),
('A05', 'P001', 50000, 'GHE_THUONG'),
('A06', 'P001', 50000, 'GHE_THUONG'),
('A07', 'P001', 50000, 'GHE_THUONG'),
('A08', 'P001', 50000, 'GHE_THUONG'),
('A09', 'P001', 50000, 'GHE_THUONG'),
('A10','P001', 50000, 'GHE_THUONG');

-- Hàng B: B5, B6 VIP; còn lại thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('B01', 'P001', 50000, 'GHE_THUONG'),
('B02', 'P001', 50000, 'GHE_THUONG'),
('B03', 'P001', 50000, 'GHE_THUONG'),
('B04', 'P001', 50000, 'GHE_THUONG'),
('B05', 'P001', 65000, 'GHE_VIP'),
('B06', 'P001', 65000, 'GHE_VIP'),
('B07', 'P001', 50000, 'GHE_THUONG'),
('B08', 'P001', 50000, 'GHE_THUONG'),
('B09', 'P001', 50000, 'GHE_THUONG'),
('B10','P001', 50000, 'GHE_THUONG');

-- Hàng C: C5, C6 VIP; còn lại thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('C01', 'P001', 50000, 'GHE_THUONG'),
('C02', 'P001', 50000, 'GHE_THUONG'),
('C03', 'P001', 50000, 'GHE_THUONG'),
('C04', 'P001', 50000, 'GHE_THUONG'),
('C05', 'P001', 65000, 'GHE_VIP'),
('C06', 'P001', 65000, 'GHE_VIP'),
('C07', 'P001', 50000, 'GHE_THUONG'),
('C08', 'P001', 50000, 'GHE_THUONG'),
('C09', 'P001', 50000, 'GHE_THUONG'),
('C10','P001', 50000, 'GHE_THUONG');

-- Hàng D: D5 VIP, D10 ghế đôi
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('D01', 'P001', 50000, 'GHE_THUONG'),
('D02', 'P001', 50000, 'GHE_THUONG'),
('D03', 'P001', 50000, 'GHE_THUONG'),
('D04', 'P001', 50000, 'GHE_THUONG'),
('D05', 'P001', 65000, 'GHE_VIP'),
('D06', 'P001', 50000, 'GHE_THUONG'),
('D07', 'P001', 50000, 'GHE_THUONG'),
('D08', 'P001', 50000, 'GHE_THUONG'),
('D09', 'P001', 50000, 'GHE_THUONG'),
('D10','P001', 60000, 'GHE_DOI');

-- Hàng E: E1,E2,E3,E4,E9,E10 ghế đôi; E5–E8 thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('E01', 'P001', 60000, 'GHE_DOI'),
('E02', 'P001', 60000, 'GHE_DOI'),
('E03', 'P001', 60000, 'GHE_DOI'),
('E04', 'P001', 60000, 'GHE_DOI'),
('E05', 'P001', 50000, 'GHE_THUONG'),
('E06', 'P001', 50000, 'GHE_THUONG'),
('E07', 'P001', 50000, 'GHE_THUONG'),
('E08', 'P001', 50000, 'GHE_THUONG'),
('E09', 'P001', 60000, 'GHE_DOI'),
('E10','P001', 60000, 'GHE_DOI');
GO

------------------------------------------------------------
-- INSERT GHẾ CHO PHÒNG 2 (P002) - 50 GHẾ
-- Hàng A–E, mỗi hàng 10 ghế (A1..E10)
-- Giá:
--   GHE_THUONG: 50,000
--   GHE_DOI   : 60,000 ( +20% )
--   GHE_VIP   : 65,000 ( +30% )
-- VIP: B5, B6, C5, C6, D5 (5 ghế)
-- ĐÔI: D10, E1, E2, E3, E4, E9, E10 (7 ghế)
------------------------------------------------------------

-- Hàng A: tất cả ghế thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('A01', 'P002', 50000, 'GHE_THUONG'),
('A02', 'P002', 50000, 'GHE_THUONG'),
('A03', 'P002', 50000, 'GHE_THUONG'),
('A04', 'P002', 50000, 'GHE_THUONG'),
('A05', 'P002', 50000, 'GHE_THUONG'),
('A06', 'P002', 50000, 'GHE_THUONG'),
('A07', 'P002', 50000, 'GHE_THUONG'),
('A08', 'P002', 50000, 'GHE_THUONG'),
('A09', 'P002', 50000, 'GHE_THUONG'),
('A10','P002', 50000, 'GHE_THUONG');

-- Hàng B: B5, B6 VIP; còn lại thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('B01', 'P002', 50000, 'GHE_THUONG'),
('B02', 'P002', 50000, 'GHE_THUONG'),
('B03', 'P002', 50000, 'GHE_THUONG'),
('B04', 'P002', 50000, 'GHE_THUONG'),
('B05', 'P002', 65000, 'GHE_VIP'),
('B06', 'P002', 65000, 'GHE_VIP'),
('B07', 'P002', 50000, 'GHE_THUONG'),
('B08', 'P002', 50000, 'GHE_THUONG'),
('B09', 'P002', 50000, 'GHE_THUONG'),
('B10','P002', 50000, 'GHE_THUONG');

-- Hàng C: C5, C6 VIP; còn lại thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('C01', 'P002', 50000, 'GHE_THUONG'),
('C02', 'P002', 50000, 'GHE_THUONG'),
('C03', 'P002', 50000, 'GHE_THUONG'),
('C04', 'P002', 50000, 'GHE_THUONG'),
('C05', 'P002', 65000, 'GHE_VIP'),
('C06', 'P002', 65000, 'GHE_VIP'),
('C07', 'P002', 50000, 'GHE_THUONG'),
('C08', 'P002', 50000, 'GHE_THUONG'),
('C09', 'P002', 50000, 'GHE_THUONG'),
('C10','P002', 50000, 'GHE_THUONG');

-- Hàng D: D5 VIP, D10 ghế đôi
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('D01', 'P002', 50000, 'GHE_THUONG'),
('D02', 'P002', 50000, 'GHE_THUONG'),
('D03', 'P002', 50000, 'GHE_THUONG'),
('D04', 'P002', 50000, 'GHE_THUONG'),
('D05', 'P002', 65000, 'GHE_VIP'),
('D06', 'P002', 50000, 'GHE_THUONG'),
('D07', 'P002', 50000, 'GHE_THUONG'),
('D08', 'P002', 50000, 'GHE_THUONG'),
('D09', 'P002', 50000, 'GHE_THUONG'),
('D10','P002', 60000, 'GHE_DOI');

-- Hàng E: E1,E2,E3,E4,E9,E10 ghế đôi; E5–E8 thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('E01', 'P002', 60000, 'GHE_DOI'),
('E02', 'P002', 60000, 'GHE_DOI'),
('E03', 'P002', 60000, 'GHE_DOI'),
('E04', 'P002', 60000, 'GHE_DOI'),
('E05', 'P002', 50000, 'GHE_THUONG'),
('E06', 'P002', 50000, 'GHE_THUONG'),
('E07', 'P002', 50000, 'GHE_THUONG'),
('E08', 'P002', 50000, 'GHE_THUONG'),
('E09', 'P002', 60000, 'GHE_DOI'),
('E10','P002', 60000, 'GHE_DOI');
GO

------------------------------------------------------------
-- INSERT GHẾ CHO PHÒNG 3 (P003) - 50 GHẾ
-- Hàng A–E, mỗi hàng 10 ghế (A1..E10)
-- Giá:
--   GHE_THUONG: 50,000
--   GHE_DOI   : 60,000 ( +20% )
--   GHE_VIP   : 65,000 ( +30% )
-- VIP: B5, B6, C5, C6, D5 (5 ghế)
-- ĐÔI: D10, E1, E2, E3, E4, E9, E10 (7 ghế)
------------------------------------------------------------

-- Hàng A: tất cả ghế thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('A01', 'P003', 50000, 'GHE_THUONG'),
('A02', 'P003', 50000, 'GHE_THUONG'),
('A03', 'P003', 50000, 'GHE_THUONG'),
('A04', 'P003', 50000, 'GHE_THUONG'),
('A05', 'P003', 50000, 'GHE_THUONG'),
('A06', 'P003', 50000, 'GHE_THUONG'),
('A07', 'P003', 50000, 'GHE_THUONG'),
('A08', 'P003', 50000, 'GHE_THUONG'),
('A09', 'P003', 50000, 'GHE_THUONG'),
('A10','P003', 50000, 'GHE_THUONG');

-- Hàng B: B5, B6 VIP; còn lại thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('B01', 'P003', 50000, 'GHE_THUONG'),
('B02', 'P003', 50000, 'GHE_THUONG'),
('B03', 'P003', 50000, 'GHE_THUONG'),
('B04', 'P003', 50000, 'GHE_THUONG'),
('B05', 'P003', 65000, 'GHE_VIP'),
('B06', 'P003', 65000, 'GHE_VIP'),
('B07', 'P003', 50000, 'GHE_THUONG'),
('B08', 'P003', 50000, 'GHE_THUONG'),
('B09', 'P003', 50000, 'GHE_THUONG'),
('B10','P003', 50000, 'GHE_THUONG');

-- Hàng C: C5, C6 VIP; còn lại thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('C01', 'P003', 50000, 'GHE_THUONG'),
('C02', 'P003', 50000, 'GHE_THUONG'),
('C03', 'P003', 50000, 'GHE_THUONG'),
('C04', 'P003', 50000, 'GHE_THUONG'),
('C05', 'P003', 65000, 'GHE_VIP'),
('C06', 'P003', 65000, 'GHE_VIP'),
('C07', 'P003', 50000, 'GHE_THUONG'),
('C08', 'P003', 50000, 'GHE_THUONG'),
('C09', 'P003', 50000, 'GHE_THUONG'),
('C10','P003', 50000, 'GHE_THUONG');

-- Hàng D: D5 VIP, D10 ghế đôi
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('D01', 'P003', 50000, 'GHE_THUONG'),
('D02', 'P003', 50000, 'GHE_THUONG'),
('D03', 'P003', 50000, 'GHE_THUONG'),
('D04', 'P003', 50000, 'GHE_THUONG'),
('D05', 'P003', 65000, 'GHE_VIP'),
('D06', 'P003', 50000, 'GHE_THUONG'),
('D07', 'P003', 50000, 'GHE_THUONG'),
('D08', 'P003', 50000, 'GHE_THUONG'),
('D09', 'P003', 50000, 'GHE_THUONG'),
('D10','P003', 60000, 'GHE_DOI');

-- Hàng E: E1,E2,E3,E4,E9,E10 ghế đôi; E5–E8 thường
INSERT INTO Ghe (MaGhe, MaPhong, GiaGhe, MaLoaiGhe) VALUES
('E01', 'P003', 60000, 'GHE_DOI'),
('E02', 'P003', 60000, 'GHE_DOI'),
('E03', 'P003', 60000, 'GHE_DOI'),
('E04', 'P003', 60000, 'GHE_DOI'),
('E05', 'P003', 50000, 'GHE_THUONG'),
('E06', 'P003', 50000, 'GHE_THUONG'),
('E07', 'P003', 50000, 'GHE_THUONG'),
('E08', 'P003', 50000, 'GHE_THUONG'),
('E09', 'P003', 60000, 'GHE_DOI'),
('E10','P003', 60000, 'GHE_DOI');
GO

------------------------------------------------------------
-- SUẤT CHIẾU
-- 3 phim, mỗi phim 7 suất trong 1 ngày (2025-01-02)
--
-- M001 ở P001
-- M002 ở P002
-- M003 ở P003
------------------------------------------------------------
INSERT INTO SuatChieu (MaSuatChieu, ThoiGianBatDau, MaPhong, MaPhim) VALUES
-- Phim 1 - Phòng 1
('SC101', '2025-01-02T09:00:00', 'P001', 'M001'),
('SC102', '2025-01-02T11:00:00', 'P001', 'M001'),
('SC103', '2025-01-02T13:00:00', 'P001', 'M001'),
('SC104', '2025-01-02T15:00:00', 'P001', 'M001'),
('SC105', '2025-01-02T17:00:00', 'P001', 'M001'),
('SC106', '2025-01-02T19:00:00', 'P001', 'M001'),
('SC107', '2025-01-02T21:00:00', 'P001', 'M001'),

-- Phim 2 - Phòng 2
('SC201', '2025-01-02T09:30:00', 'P002', 'M002'),
('SC202', '2025-01-02T11:30:00', 'P002', 'M002'),
('SC203', '2025-01-02T13:30:00', 'P002', 'M002'),
('SC204', '2025-01-02T15:30:00', 'P002', 'M002'),
('SC205', '2025-01-02T17:30:00', 'P002', 'M002'),
('SC206', '2025-01-02T19:30:00', 'P002', 'M002'),
('SC207', '2025-01-02T21:30:00', 'P002', 'M002'),

-- Phim 3 - Phòng 3
('SC301', '2025-01-02T09:15:00', 'P003', 'M003'),
('SC302', '2025-01-02T11:15:00', 'P003', 'M003'),
('SC303', '2025-01-02T13:15:00', 'P003', 'M003'),
('SC304', '2025-01-02T15:15:00', 'P003', 'M003'),
('SC305', '2025-01-02T17:15:00', 'P003', 'M003'),
('SC306', '2025-01-02T19:15:00', 'P003', 'M003'),
('SC307', '2025-01-02T21:15:00', 'P003', 'M003');
GO

------------------------------------------------------------
-- TẠO 10 VÉ ĐÃ ĐẶT (DA_DAT) CHO PHIM 1 Ở PHÒNG 1
-- Mỗi vé 1 ghế khác nhau, rải trên các suất chiếu
------------------------------------------------------------
INSERT INTO Ve (MaVe, MaSuatChieu, MaGhe, MaPhong, TrangThai) VALUES
('V0001', 'SC101', 'A01',  'P001', 'DA_DAT'),
('V0002', 'SC101', 'A02',  'P001', 'DA_DAT'),
('V0003', 'SC102', 'B05',  'P001', 'DA_DAT'), -- VIP
('V0004', 'SC103', 'C05',  'P001', 'DA_DAT'), -- VIP
('V0005', 'SC104', 'D05',  'P001', 'DA_DAT'), -- VIP
('V0006', 'SC105', 'E01',  'P001', 'DA_DAT'), -- ĐÔI
('V0007', 'SC106', 'E02',  'P001', 'DA_DAT'), -- ĐÔI
('V0008', 'SC107', 'C06',  'P001', 'DA_DAT'), -- VIP
('V0009', 'SC107', 'C07',  'P001', 'DA_DAT'), -- THƯỜNG
('V0010', 'SC107', 'A10', 'P001', 'DA_DAT'); -- THƯỜNG
GO

------------------------------------------------------------
-- 10 HÓA ĐƠN (mỗi hóa đơn 1 vé, thuế = 0 cho dễ tính)
-- Giá theo loại ghế:
--   GHE_THUONG: 50,000
--   GHE_DOI   : 60,000
--   GHE_VIP   : 65,000
------------------------------------------------------------

-- HD0001: V0001 - ghế A01 (thường)
INSERT INTO HoaDon (MaHoaDon, NgayLap, MaNhanVien, Thue, TienNhan, TongTien, TienThua)
VALUES ('HD0001', '2025-01-02T08:30:00', 'NV001', 0.00, 50000, 50000, 0);

-- HD0002: V0002 - ghế A02 (thường)
INSERT INTO HoaDon VALUES
('HD0002', '2025-01-02T08:35:00', 'NV001', 0.00, 50000, 50000, 0);

-- HD0003: V0003 - ghế B05 (VIP)
INSERT INTO HoaDon VALUES
('HD0003', '2025-01-02T09:30:00', 'NV001', 0.00, 65000, 65000, 0);

-- HD0004: V0004 - ghế C05 (VIP)
INSERT INTO HoaDon VALUES
('HD0004', '2025-01-02T10:00:00', 'NV002', 0.00, 65000, 65000, 0);

-- HD0005: V0005 - ghế D05 (VIP)
INSERT INTO HoaDon VALUES
('HD0005', '2025-01-02T11:30:00', 'NV002', 0.00, 65000, 65000, 0);

-- HD0006: V0006 - ghế E01 (ĐÔI)
INSERT INTO HoaDon VALUES
('HD0006', '2025-01-02T12:00:00', 'NV002', 0.00, 60000, 60000, 0);

-- HD0007: V0007 - ghế E02 (ĐÔI)
INSERT INTO HoaDon VALUES
('HD0007', '2025-01-02T13:00:00', 'NV003', 0.00, 60000, 60000, 0);

-- HD0008: V0008 - ghế C06 (VIP)
INSERT INTO HoaDon VALUES
('HD0008', '2025-01-02T14:00:00', 'NV003', 0.00, 65000, 65000, 0);

-- HD0009: V0009 - ghế C07 (THƯỜNG)
INSERT INTO HoaDon VALUES
('HD0009', '2025-01-02T15:00:00', 'NV003', 0.00, 50000, 50000, 0);

-- HD0010: V0010 - ghế A10 (THƯỜNG)
INSERT INTO HoaDon VALUES
('HD0010', '2025-01-02T16:00:00', 'NV001', 0.00, 50000, 50000, 0);
GO

------------------------------------------------------------
-- CHI TIẾT HÓA ĐƠN (mỗi hóa đơn 1 vé tương ứng)
------------------------------------------------------------
INSERT INTO ChiTietHoaDon (MaHoaDon, MaVe) VALUES
('HD0001', 'V0001'),
('HD0002', 'V0002'),
('HD0003', 'V0003'),
('HD0004', 'V0004'),
('HD0005', 'V0005'),
('HD0006', 'V0006'),
('HD0007', 'V0007'),
('HD0008', 'V0008'),
('HD0009', 'V0009'),
('HD0010', 'V0010');
GO


