package collection;

import dao.NhanVienDAO;
import entity.NhanVien;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Bộ sưu tập nhân viên: tải, thêm/sửa/xoá, lọc/sắp xếp, đồng bộ với DB qua DAO.
 */
public class NhanVien_Collection {
    private final NhanVienDAO dao;
    private final List<NhanVien> cache;

    public NhanVien_Collection() {
        this.dao = new NhanVienDAO();
        this.cache = new ArrayList<>();
        refresh();
    }

    // Tải lại từ DB
    public void refresh() {
        cache.clear();
        cache.addAll(dao.getAll());
    }

    // Lấy danh sách hiện tại
    public List<NhanVien> getAll() {
        return Collections.unmodifiableList(cache);
    }

    // Thêm NV (đồng bộ DB + cache)
    public boolean add(NhanVien nv) {
        boolean ok = dao.insert(nv);
        if (ok) {
            cache.add(nv);
            sortByCreatedDesc(); // để hiển thị mới nhất lên đầu (tuỳ bạn)
        }
        return ok;
    }

    // Cập nhật NV
    public boolean update(NhanVien nv) {
        boolean ok = dao.update(nv);
        if (ok) {
            int idx = indexOf(nv.getMaNhanVien());
            if (idx >= 0) cache.set(idx, nv);
        }
        return ok;
    }

    // Xoá NV
    public boolean remove(String maNV) {
        boolean ok = dao.delete(maNV);
        if (ok) {
            int idx = indexOf(maNV);
            if (idx >= 0) cache.remove(idx);
        }
        return ok;
    }

    // Tìm vị trí trong cache
    private int indexOf(String maNV) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getMaNhanVien().equals(maNV)) return i;
        }
        return -1;
    }

    // Lọc theo predicate
    public List<NhanVien> filter(Predicate<NhanVien> p) {
        return cache.stream().filter(p).collect(Collectors.toList());
    }

    // Lọc theo tên (contains, Case-insensitive)
    public List<NhanVien> filterByName(String keyword) {
        String kw = keyword.toLowerCase();
        return filter(nv -> nv.getTenNhanVien() != null &&
                nv.getTenNhanVien().toLowerCase().contains(kw));
    }

    // Sắp xếp theo thời gian tạo (giảm dần)
    public void sortByCreatedDesc() {
        cache.sort(Comparator.comparing(NhanVien::getThoiGianTaoTaiKhoan,
                Comparator.nullsLast(Comparator.naturalOrder())).reversed());
    }

    // Lấy danh sách theo domain email (ví dụ "@example.com")
    public List<NhanVien> byEmailDomain(String domain) {
        String d = domain.toLowerCase();
        return filter(nv -> nv.getEmail() != null && nv.getEmail().toLowerCase().endsWith(d));
    }

    // Tạo mã NV mới đơn giản (NVxxx) — demo
    public String generateNextId() {
        int max = 0;
        for (NhanVien nv : cache) {
            String ma = nv.getMaNhanVien();
            if (ma != null && ma.startsWith("NV")) {
                try { max = Math.max(max, Integer.parseInt(ma.substring(2))); }
                catch (NumberFormatException ignore) {}
            }
        }
        return String.format("NV%03d", max + 1);
    }

    // Tạo nhanh NV demo
    public NhanVien newNhanVien(String ten, String email, String matKhau) {
        return new NhanVien(generateNextId(), ten, email, matKhau, LocalDateTime.now());
    }
}