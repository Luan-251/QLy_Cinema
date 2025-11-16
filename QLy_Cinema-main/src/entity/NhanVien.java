package entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity NhanVien bám đúng bảng NhanVien trong CinemaDB.
 */
public class NhanVien {
    private String maNhanVien;              // VARCHAR(10) PK
    private String tenNhanVien;             // NVARCHAR(100) NOT NULL
    private String email;                   // VARCHAR(100) NOT NULL
    private String matKhau;                 // NVARCHAR(100) NOT NULL
    private LocalDateTime thoiGianTaoTaiKhoan; // DATETIME2(0) NOT NULL

    public NhanVien() {}

    public NhanVien(String maNhanVien, String tenNhanVien, String email,
                    String matKhau, LocalDateTime thoiGianTaoTaiKhoan) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.email = email;
        this.matKhau = matKhau;
        this.thoiGianTaoTaiKhoan = thoiGianTaoTaiKhoan;
    }

    // Getters/Setters
    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public LocalDateTime getThoiGianTaoTaiKhoan() { return thoiGianTaoTaiKhoan; }
    public void setThoiGianTaoTaiKhoan(LocalDateTime thoiGianTaoTaiKhoan) {
        this.thoiGianTaoTaiKhoan = thoiGianTaoTaiKhoan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NhanVien)) return false;
        NhanVien nv = (NhanVien) o;
        return Objects.equals(maNhanVien, nv.maNhanVien);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNhanVien);
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", tenNhanVien='" + tenNhanVien + '\'' +
                ", email='" + email + '\'' +
                ", thoiGianTaoTaiKhoan=" + thoiGianTaoTaiKhoan +
                '}';
    }
}