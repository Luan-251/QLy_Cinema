package collection;
import java.util.ArrayList;
import entity.SuatChieu;

public class SuatPhim_Collection {
    private ArrayList<SuatChieu> danhSach;

    public SuatPhim_Collection() {
        danhSach = new ArrayList<>();
    }

    public void add(SuatChieu sp) {
        danhSach.add(sp);
    }

    public boolean remove(String maSuatChieu) {
        return danhSach.removeIf(sp -> sp.getMaSuatChieu().equals(maSuatChieu));
    }

    public SuatChieu find(String maSuat) {
        for (SuatChieu sp : danhSach) {
            if (sp.getMaSuatChieu().equals(maSuat))
                return sp;
        }
        return null;
    }

    public ArrayList<SuatChieu> getAll() {
        return danhSach;
    }
}
