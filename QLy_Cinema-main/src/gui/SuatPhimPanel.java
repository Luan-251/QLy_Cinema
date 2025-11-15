package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.SuatChieuDAO;
import entity.SuatChieu;
import entity.Phim;
import entity.Phong;
import connectDB.connectDB;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

public class SuatPhimPanel extends JPanel implements ActionListener {

    private JTextField txtMaSuat;
    private JComboBox<String> cboPhim, cboPhong;
    private JComboBox<String> cboMaPhim, cboMaPhong;
    private JComboBox<String> cboGioBatDau;
    private JSpinner spNgayChieu;
    private JButton btnThem, btnSua, btnXoa, btnTaiLai, btnTimKiem;
    private JTextField txtTimKiem;
    private JTable table;
    private DefaultTableModel model;

    private SuatChieuDAO dao;

    private SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

    private Map<String, Phim> mapTenPhim = new HashMap<>();
    private Map<String, Phim> mapMaPhim = new HashMap<>();
    private Map<String, Phong> mapTenPhong = new HashMap<>();
    private Map<String, Phong> mapMaPhong = new HashMap<>();

    public SuatPhimPanel() {
        dao = new SuatChieuDAO();
        setLayout(new BorderLayout(10, 10));

        // ===== NORTH: Tìm kiếm =====
        JPanel pnlSearch = new JPanel();
        txtTimKiem = new JTextField(30);
        btnTimKiem = new JButton("Tìm kiếm");
        pnlSearch.add(new JLabel("Nhập từ khóa:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);
        add(pnlSearch, BorderLayout.NORTH);

        // ===== CENTER: Input + nút + table =====
        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));

        // Input 2 cột
        JPanel pnlInput = new JPanel();
        pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.Y_AXIS));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel pnlColumns = new JPanel(new GridLayout(1, 2, 20, 0));

        // Cột trái
        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        txtMaSuat = new JTextField(20);
        txtMaSuat.setEditable(false);
        cboPhim = new JComboBox<>();
        cboMaPhim = new JComboBox<>();
        setComponentWidth(txtMaSuat);
        setComponentWidth(cboPhim);
        setComponentWidth(cboMaPhim);
        pnlLeft.add(createInputRow("Mã Suất Chiếu:", txtMaSuat));
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(createInputRow("Tên Phim:", cboPhim));
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(createInputRow("Mã Phim:", cboMaPhim));

        // Cột phải
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
        cboPhong = new JComboBox<>();
        cboMaPhong = new JComboBox<>();
        cboGioBatDau = new JComboBox<>(new String[]{
                "08:00", "10:00", "12:00", "14:00",
                "16:00", "18:00", "20:00", "22:00"
        });
        spNgayChieu = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spNgayChieu, "yyyy-MM-dd");
        spNgayChieu.setEditor(dateEditor);
        setComponentWidth(cboPhong);
        setComponentWidth(cboMaPhong);
        setComponentWidth(cboGioBatDau);
        setComponentWidth(spNgayChieu);
        pnlRight.add(createInputRow("Tên Phòng:", cboPhong));
        pnlRight.add(Box.createVerticalStrut(10));
        pnlRight.add(createInputRow("Mã Phòng:", cboMaPhong));
        pnlRight.add(Box.createVerticalStrut(10));
        pnlRight.add(createInputRow("Ngày Chiếu:", spNgayChieu));
        pnlRight.add(Box.createVerticalStrut(10));
        pnlRight.add(createInputRow("Giờ Chiếu:", cboGioBatDau));

        pnlColumns.add(pnlLeft);
        pnlColumns.add(pnlRight);
        pnlInput.add(pnlColumns);

        // Nút chức năng
        JPanel pnlButton = new JPanel();
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnTaiLai = new JButton("Tải Lại");
        pnlButton.add(btnThem);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
        pnlButton.add(btnTaiLai);
        pnlInput.add(Box.createVerticalStrut(10));
        pnlInput.add(pnlButton);

        pnlCenter.add(pnlInput, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{
                "Mã Suất", "Mã Phim", "Tên Phim",
                "Mã Phòng", "Tên Phòng", "Ngày Chiếu", "Giờ Chiếu"
        }, 0);
        table = new JTable(model);
        pnlCenter.add(new JScrollPane(table), BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

        // ===== LOAD DATA =====
        loadComboTenPhim();
        loadComboMaPhim();
        loadComboTenPhong();
        loadComboMaPhong();
        loadTable();
        generateNextMaSuat();

        // ===== SYNC TÊN ↔ MÃ =====
        cboPhim.addActionListener(e -> {
            String ten = (String) cboPhim.getSelectedItem();
            if (ten != null && mapTenPhim.containsKey(ten))
                cboMaPhim.setSelectedItem(mapTenPhim.get(ten).getMaPhim());
        });
        cboMaPhim.addActionListener(e -> {
            String ma = (String) cboMaPhim.getSelectedItem();
            if (ma != null && mapMaPhim.containsKey(ma))
                cboPhim.setSelectedItem(mapMaPhim.get(ma).getTenPhim());
        });
        cboPhong.addActionListener(e -> {
            String ten = (String) cboPhong.getSelectedItem();
            if (ten != null && mapTenPhong.containsKey(ten))
                cboMaPhong.setSelectedItem(mapTenPhong.get(ten).getMaPhong());
        });
        cboMaPhong.addActionListener(e -> {
            String ma = (String) cboMaPhong.getSelectedItem();
            if (ma != null && mapMaPhong.containsKey(ma))
                cboPhong.setSelectedItem(mapMaPhong.get(ma).getTenPhong());
        });

        // Click table -> đổ ngược lên input
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtMaSuat.setText(model.getValueAt(row, 0).toString());
                cboMaPhim.setSelectedItem(model.getValueAt(row, 1));
                cboMaPhong.setSelectedItem(model.getValueAt(row, 3));
                spNgayChieu.setValue(model.getValueAt(row, 5) != null ?
                        java.sql.Date.valueOf(model.getValueAt(row, 5).toString()) : new Date());
                cboGioBatDau.setSelectedItem(model.getValueAt(row, 6));
            }
        });

        // ACTION
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnTaiLai.addActionListener(this);
        btnTimKiem.addActionListener(e -> searchTable());
    }

    private void setComponentWidth(JComponent comp) {
        Dimension dim = new Dimension(200, 28);
        comp.setPreferredSize(dim);
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
    }

    private JPanel createInputRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.add(new JLabel(labelText), BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        return row;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnThem) xuLyThem();
        else if (e.getSource() == btnSua) xuLySua();
        else if (e.getSource() == btnXoa) xuLyXoa();
        else if (e.getSource() == btnTaiLai) {
            loadTable();
            generateNextMaSuat();
        }
    }

    // ================== GENERATE UNIQUE CODE ==================
    private void generateNextMaSuat() {
        try {
            Set<String> existingCodes = new HashSet<>();
            for (SuatChieu sc : dao.getAll())
                existingCodes.add(sc.getMaSuatChieu());

            int i = 1;
            String nextCode;
            do {
                nextCode = String.format("SC%03d", i++);
            } while (existingCodes.contains(nextCode));

            txtMaSuat.setText(nextCode);
        } catch (Exception e) {
            e.printStackTrace();
            txtMaSuat.setText("");
        }
    }

    // ================== CHECK DUPLICATE ==================
    private boolean checkDuplicateShow(Phong phong, Timestamp thoiGian, String excludeMaSuat) {
        String timeStr = sdfFull.format(thoiGian).substring(0, 16);
        for (SuatChieu sc : dao.getAll()) {
            if (excludeMaSuat != null && sc.getMaSuatChieu().equals(excludeMaSuat)) continue;
            if (sc.getMaPhong().getMaPhong().equals(phong.getMaPhong())) {
                String existingTime = sdfFull.format(sc.getThoiGianBatDau()).substring(0, 16);
                if (timeStr.equals(existingTime)) return true;
            }
        }
        return false;
    }

    private boolean checkDuplicateMaSuat(String maSuat) {
        for (SuatChieu sc : dao.getAll()) {
            if (maSuat.equalsIgnoreCase(sc.getMaSuatChieu())) return true;
        }
        return false;
    }

    // ================== CRUD ==================
    private void xuLyThem() {
        try {
            String maSuat = txtMaSuat.getText().trim();
            Phim phim = mapMaPhim.get(cboMaPhim.getSelectedItem());
            Phong phong = mapMaPhong.get(cboMaPhong.getSelectedItem());
            String gio = (String) cboGioBatDau.getSelectedItem();
            Date ngay = (Date) spNgayChieu.getValue();
            Timestamp fullTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(sdfDate.format(ngay) + " " + gio + ":00").getTime());

            if (checkDuplicateMaSuat(maSuat)) {
                JOptionPane.showMessageDialog(this, "Mã suất chiếu đã tồn tại!");
                return;
            }
            if (checkDuplicateShow(phong, fullTime, null)) {
                JOptionPane.showMessageDialog(this, "Phòng này đã có suất chiếu cùng giờ!");
                return;
            }

            SuatChieu sc = new SuatChieu(maSuat, fullTime, phim, phong);
            if (dao.insert(sc)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadTable();
                generateNextMaSuat();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
        }
    }

    private void xuLySua() {
        try {
            String maSuat = txtMaSuat.getText().trim();
            if (maSuat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chọn suất chiếu để sửa!");
                return;
            }
            Phim phim = mapMaPhim.get(cboMaPhim.getSelectedItem());
            Phong phong = mapMaPhong.get(cboMaPhong.getSelectedItem());
            String gio = (String) cboGioBatDau.getSelectedItem();
            Date ngay = (Date) spNgayChieu.getValue();
            Timestamp fullTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(sdfDate.format(ngay) + " " + gio + ":00").getTime());

            if (checkDuplicateShow(phong, fullTime, maSuat)) {
                JOptionPane.showMessageDialog(this, "Phòng này đã có suất chiếu cùng giờ!");
                return;
            }

            SuatChieu sc = new SuatChieu(maSuat, fullTime, phim, phong);
            if (dao.update(sc)) {
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
                loadTable();
                generateNextMaSuat();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + e.getMessage());
        }
    }

    private void xuLyXoa() {
        String ma = txtMaSuat.getText();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn suất chiếu để xóa!");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (dao.delete(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadTable();
                generateNextMaSuat();
            }
        }
    }

    // ================== LOAD COMBO ==================
    private void loadComboTenPhim() {
        mapTenPhim.clear();
        cboPhim.removeAllItems();
        try (Connection con = connectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT MaPhim, TenPhim FROM Phim")) {
            while (rs.next()) {
                Phim p = new Phim(rs.getString(1), rs.getString(2));
                mapTenPhim.put(p.getTenPhim(), p);
                cboPhim.addItem(p.getTenPhim());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadComboMaPhim() {
        mapMaPhim.clear();
        cboMaPhim.removeAllItems();
        try (Connection con = connectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT MaPhim, TenPhim FROM Phim")) {
            while (rs.next()) {
                Phim p = new Phim(rs.getString(1), rs.getString(2));
                mapMaPhim.put(p.getMaPhim(), p);
                cboMaPhim.addItem(p.getMaPhim());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadComboTenPhong() {
        mapTenPhong.clear();
        cboPhong.removeAllItems();
        try (Connection con = connectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT MaPhong, TenPhong FROM Phong")) {
            while (rs.next()) {
                Phong p = new Phong(rs.getString(1), rs.getString(2));
                mapTenPhong.put(p.getTenPhong(), p);
                cboPhong.addItem(p.getTenPhong());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadComboMaPhong() {
        mapMaPhong.clear();
        cboMaPhong.removeAllItems();
        try (Connection con = connectDB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT MaPhong, TenPhong FROM Phong")) {
            while (rs.next()) {
                Phong p = new Phong(rs.getString(1), rs.getString(2));
                mapMaPhong.put(p.getMaPhong(), p);
                cboMaPhong.addItem(p.getMaPhong());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadTable() {
        model.setRowCount(0);
        List<SuatChieu> list = dao.getAll();
        for (SuatChieu sc : list) {
            String date = sc.getThoiGianBatDau() != null ? sdfDate.format(sc.getThoiGianBatDau()) : "";
            String time = sc.getThoiGianBatDau() != null ? sdfTime.format(sc.getThoiGianBatDau()) : "";
            model.addRow(new Object[]{
                    sc.getMaSuatChieu(),
                    sc.getMaPhim().getMaPhim(),
                    sc.getMaPhim().getTenPhim(),
                    sc.getMaPhong().getMaPhong(),
                    sc.getMaPhong().getTenPhong(),
                    date,
                    time
            });
        }
    }

    private void searchTable() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadTable();
            return;
        }
        model.setRowCount(0);
        for (SuatChieu sc : dao.getAll()) {
            if (sc.getMaSuatChieu().toLowerCase().contains(keyword)
                    || sc.getMaPhim().getTenPhim().toLowerCase().contains(keyword)
                    || sc.getMaPhong().getTenPhong().toLowerCase().contains(keyword)) {
                String date = sc.getThoiGianBatDau() != null ? sdfDate.format(sc.getThoiGianBatDau()) : "";
                String time = sc.getThoiGianBatDau() != null ? sdfTime.format(sc.getThoiGianBatDau()) : "";
                model.addRow(new Object[]{
                        sc.getMaSuatChieu(),
                        sc.getMaPhim().getMaPhim(),
                        sc.getMaPhim().getTenPhim(),
                        sc.getMaPhong().getMaPhong(),
                        sc.getMaPhong().getTenPhong(),
                        date,
                        time
                });
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Quản Lý Suất Chiếu");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.setContentPane(new SuatPhimPanel());
            f.setVisible(true);
        });
    }
}
