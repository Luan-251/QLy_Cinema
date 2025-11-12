-- =====================================
-- TẠO DATABASE QLyCinema
-- =====================================
DROP DATABASE IF EXISTS QLyCinema;
CREATE DATABASE QLyCinema;
GO
USE QLyCinema;
GO

-- =====================================
-- BẢNG: NhanVien
-- =====================================
CREATE TABLE NhanVien (
    maNhanVien VARCHAR(20) PRIMARY KEY,
    tenNhanVien NVARCHAR(100) NOT NULL,
    email NVARCHAR(100),
    matKhau NVARCHAR(100)
);
GO

INSERT INTO NhanVien VALUES
('NV01', N'Nguyễn Văn An', 'an@qlcinema.vn', '123456'),
('NV02', N'Trần Thị Bình', 'binh@qlcinema.vn', '654321'),
('NV03', N'Lê Quốc Cường', 'cuong@qlcinema.vn', 'abc123');
GO

-- =====================================
-- BẢNG: Phim
-- =====================================
CREATE TABLE Phim (
    maPhim VARCHAR(20) PRIMARY KEY,
    tenPhim NVARCHAR(100) NOT NULL,
    thoiLuong INT,
    tenDaoDien NVARCHAR(100),
    tenDienVien NVARCHAR(200),
    theLoai NVARCHAR(100),
    giaPhim FLOAT
);
GO

INSERT INTO Phim VALUES
('P01', N'Avengers: Endgame', 180, N'Anthony Russo', N'Robert Downey Jr, Chris Evans', N'Hành động', 120000),
('P02', N'Your Name', 110, N'Makoto Shinkai', N'Ryunosuke Kamiki, Mone Kamishiraishi', N'Tình cảm', 100000),
('P03', N'Spiderman: No Way Home', 150, N'Jon Watts', N'Tom Holland, Zendaya', N'Phiêu lưu', 110000);
GO

-- =====================================
-- BẢNG: LoaiGhe
-- =====================================
CREATE TABLE LoaiGhe (
    maLoaiGhe VARCHAR(20) PRIMARY KEY,
    tenLoaiGhe NVARCHAR(50)
);
GO

INSERT INTO LoaiGhe VALUES
('GHE_THUONG', N'Ghế thường'),
('GHE_VIP', N'Ghế VIP'),
('GHE_DOI', N'Ghế đôi');
GO

-- =====================================
-- BẢNG: TrangThai
-- =====================================
CREATE TABLE TrangThai (
    maTrangThai VARCHAR(20) PRIMARY KEY,
    tenTrangThai NVARCHAR(50)
);
GO

INSERT INTO TrangThai VALUES
('TRONG', N'Trống'),
('DANG_CHO', N'Đang chờ'),
('DA_DAT', N'Đã đặt');
GO

-- =====================================
-- BẢNG: Phong
-- =====================================
CREATE TABLE Phong (
    maPhong VARCHAR(20) PRIMARY KEY,
    tenPhong NVARCHAR(50),
    soPhong INT,
    soGhe INT
);
GO

INSERT INTO Phong VALUES
('PH01', N'Phòng 1', 1, 45),
('PH02', N'Phòng 2', 2, 45),
('PH03', N'Phòng 3', 3, 45);
GO

-- =====================================
-- BẢNG: Ghe
-- =====================================
CREATE TABLE Ghe (
    maGhe VARCHAR(20) PRIMARY KEY,
    tenGhe NVARCHAR(10),
    giaGhe FLOAT,
    maLoaiGhe VARCHAR(20),
    maTrangThai VARCHAR(20) DEFAULT 'TRONG',
    maPhong VARCHAR(20),
    FOREIGN KEY (maLoaiGhe) REFERENCES LoaiGhe(maLoaiGhe),
    FOREIGN KEY (maTrangThai) REFERENCES TrangThai(maTrangThai),
    FOREIGN KEY (maPhong) REFERENCES Phong(maPhong)
);
GO

-- Tạo 45 ghế tự động cho mỗi phòng (5 hàng x 9 ghế)
DECLARE @phong INT = 1;
DECLARE @hang CHAR(1);
DECLARE @so INT;
DECLARE @maGhe VARCHAR(20);
DECLARE @tenGhe NVARCHAR(10);
DECLARE @gia FLOAT;
DECLARE @maLoaiGhe VARCHAR(20);

WHILE @phong <= 3
BEGIN
    SET @hang = 'A';
    WHILE @hang <= 'E'
    BEGIN
        SET @so = 1;
        WHILE @so <= 9
        BEGIN
            SET @maGhe = CONCAT('G', @phong, '_', @hang, @so);
            SET @tenGhe = CONCAT(@hang, @so);

            IF @hang IN ('A', 'B')
                BEGIN
                    SET @maLoaiGhe = 'GHE_THUONG';
                    SET @gia = 80000;
                END
            ELSE IF @hang IN ('C', 'D')
                BEGIN
                    SET @maLoaiGhe = 'GHE_VIP';
                    SET @gia = 100000;
                END
            ELSE
                BEGIN
                    SET @maLoaiGhe = 'GHE_DOI';
                    SET @gia = 150000;
                END

            INSERT INTO Ghe (maGhe, tenGhe, giaGhe, maLoaiGhe, maPhong)
            VALUES (@maGhe, @tenGhe, @gia, @maLoaiGhe, CONCAT('PH0', @phong));

            SET @so += 1;
        END
        SET @hang = CHAR(ASCII(@hang) + 1);
    END
    SET @phong += 1;
END
GO

-- =====================================
-- BẢNG: LoaiSuatChieu
-- =====================================
CREATE TABLE LoaiSuatChieu (
    maLoaiSuat VARCHAR(20) PRIMARY KEY,
    tenLoaiSuat NVARCHAR(100)
);
GO

INSERT INTO LoaiSuatChieu VALUES
('SUAT_SOM', N'Suất chiếu sớm'),
('SUAT_THUONG', N'Suất chiếu thường'),
('SUAT_DACBIET', N'Suất chiếu đặc biệt');
GO

-- =====================================
-- BẢNG: SuatChieu
-- =====================================
CREATE TABLE SuatChieu (
    maSuatChieu VARCHAR(20) PRIMARY KEY,
    thoiGianBatDau DATETIME,
    thoiGianKetThuc DATETIME,
    maPhim VARCHAR(20),
    maPhong VARCHAR(20),
    maLoaiSuat VARCHAR(20),
    FOREIGN KEY (maPhim) REFERENCES Phim(maPhim),
    FOREIGN KEY (maPhong) REFERENCES Phong(maPhong),
    FOREIGN KEY (maLoaiSuat) REFERENCES LoaiSuatChieu(maLoaiSuat)
);
GO

INSERT INTO SuatChieu VALUES
('SC01', '2025-10-30 08:00:00', '2025-10-30 10:30:00', 'P01', 'PH01', 'SUAT_SOM'),
('SC02', '2025-10-30 14:00:00', '2025-10-30 16:00:00', 'P02', 'PH02', 'SUAT_THUONG'),
('SC03', '2025-10-30 20:00:00', '2025-10-30 22:30:00', 'P03', 'PH03', 'SUAT_DACBIET');
GO

-- =====================================
-- BẢNG: Ve
-- =====================================
CREATE TABLE Ve (
    maVe VARCHAR(20) PRIMARY KEY,
    giaVe FLOAT,
    maSuatChieu VARCHAR(20),
    maGhe VARCHAR(20),
    FOREIGN KEY (maSuatChieu) REFERENCES SuatChieu(maSuatChieu),
    FOREIGN KEY (maGhe) REFERENCES Ghe(maGhe)
);
GO

INSERT INTO Ve VALUES
('V01', 90000, 'SC01', 'G1_A3'),
('V02', 90000, 'SC01', 'G1_A6'),
('V03', 100000, 'SC02', 'G2_B3'),
('V04', 100000, 'SC02', 'G2_C6'),
('V05', 150000, 'SC03', 'G3_D3');
GO

-- Cập nhật trạng thái ghế đã đặt
UPDATE Ghe
SET maTrangThai = 'DA_DAT'
WHERE maGhe IN ('G1_A3','G1_A6','G2_B3','G2_C6','G3_D3');
GO

-- =====================================
-- BẢNG: HoaDon
-- =====================================
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(20) PRIMARY KEY,
    ngayLap DATETIME,
    tongTien FLOAT,
    soVe INT,
    maNhanVien VARCHAR(20),
    maVe VARCHAR(20),
    FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    FOREIGN KEY (maVe) REFERENCES Ve(maVe)
);
GO

INSERT INTO HoaDon VALUES
('HD01', '2025-10-30 09:00:00', 90000, 1, 'NV01', 'V01'),
('HD02', '2025-10-30 09:05:00', 90000, 1, 'NV02', 'V02'),
('HD03', '2025-10-30 15:00:00', 100000, 1, 'NV03', 'V03'),
('HD04', '2025-10-30 15:10:00', 100000, 1, 'NV01', 'V04'),
('HD05', '2025-10-30 20:30:00', 150000, 1, 'NV02', 'V05');
GO
