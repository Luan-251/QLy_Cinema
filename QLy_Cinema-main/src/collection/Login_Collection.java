package collection;

import dao.NhanVienDAO;
import entity.NhanVien;

import java.util.regex.Pattern;

/**
 * Login_Collection: xử lý đăng nhập dựa trên bảng NhanVien (CinemaDB).
 * Hỗ trợ username là Email hoặc MaNhanVien, và (tùy chọn) TenNhanVien.
 */
public class Login_Collection {
    private final NhanVienDAO nvDAO = new NhanVienDAO();

    // Cho phép đăng nhập bằng Tên nhân viên hay không (mặc định: có thể bật/tắt)
    private final boolean allowLoginByName;

    public Login_Collection() {
        this(true); // mặc định cho phép fallback by name
    }

    public Login_Collection(boolean allowLoginByName) {
        this.allowLoginByName = allowLoginByName;
    }

    public NhanVien login(String username, String plainPassword) {
        if (username == null || plainPassword == null) return null;

        String u = username.trim();
        String p = plainPassword; // nếu dùng hash: hash ở DAO
        if (u.isEmpty() || p.isEmpty()) return null;

        // 1) Thử như MaNhanVien (VD NV001)
        NhanVien nv = nvDAO.loginById(u, p);
        if (nv != null) return nv;

        // 2) Fallback: TenNhanVien (tùy chọn)
        if (allowLoginByName) {
            return nvDAO.loginByName(u, p);
        }

        return null;
    }
}