package gui;

import collection.NhanVien_Collection;
import entity.NhanVien;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NhanVien_GUI extends JFrame {

    private final NhanVien_Collection collection = new NhanVien_Collection();

    // Bảng & model
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"Mã NV", "Tên NV", "Email", "Mật khẩu", "Thời gian tạo"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private JTable tbl;

    // Form ở phía trên
    private JTextField txtMa, txtTen, txtEmail, txtMatKhau;

    // Tìm kiếm & nút thao tác (phía dưới)
    private JTextField txtSearch;
    private JButton btnThem, btnCapNhat, btnXoa, btnLamMoi, btnTim;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public NhanVien_GUI() {
        setTitle("Quản lý Nhân Viên - CinemaDB");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        initLayout();
        initEvents();

        // Nạp dữ liệu ban đầu
        loadTable(collection.getAll());
    }

    private void initLayout() {
        // Khung chính với BorderLayout
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        // ============ PHÍA TRÊN: FORM THÔNG TIN NHÂN VIÊN (NORTH) ============
        JPanel pnTop = new JPanel(new BorderLayout());
        pnTop.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        // Lưới form trái-phải
        JPanel formGrid = new JPanel(new GridLayout(2, 4, 10, 10));
        txtMa = new JTextField();
        txtTen = new JTextField();
        txtEmail = new JTextField();
        txtMatKhau = new JTextField();

        formGrid.add(new JLabel("Mã nhân viên:"));
        formGrid.add(txtMa);
        formGrid.add(new JLabel("Tên nhân viên:"));
        formGrid.add(txtTen);

        formGrid.add(new JLabel("Email:"));
        formGrid.add(txtEmail);
        formGrid.add(new JLabel("Mật khẩu:"));
        formGrid.add(txtMatKhau);

        pnTop.add(formGrid, BorderLayout.CENTER);
        root.add(pnTop, BorderLayout.NORTH);

        // ============ PHÍA DƯỚI: DANH SÁCH (CENTER) + THANH NÚT (SOUTH) ============
        // Khung dưới gồm 2 phần: CENTER = bảng cuộn, SOUTH = nút thao tác & tìm kiếm
        JPanel pnBottom = new JPanel(new BorderLayout(10, 10));

        // Bảng cuộn ở trung tâm phía dưới
        tbl = new JTable(tableModel);
        tbl.setRowHeight(26);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.getSelectionModel().addListSelectionListener(this::onTableSelection);

        JScrollPane scroll = new JScrollPane(tbl);
        pnBottom.add(scroll, BorderLayout.CENTER);

        // Thanh nút thao tác & tìm kiếm ở SOUTH
        JPanel pnActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnThem = new JButton("Thêm");
        btnCapNhat = new JButton("Cập nhật");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        txtSearch = new JTextField(22);
        btnTim = new JButton("Tìm");

        pnActions.add(btnThem);
        pnActions.add(btnCapNhat);
        pnActions.add(btnXoa);
        pnActions.add(btnLamMoi);
        pnActions.add(new JLabel("Tìm theo tên:"));
        pnActions.add(txtSearch);
        pnActions.add(btnTim);

        pnBottom.add(pnActions, BorderLayout.SOUTH);

        // Gắn phần dưới vào CENTER của root
        root.add(pnBottom, BorderLayout.CENTER);
    }

    private void initEvents() {
        btnThem.addActionListener(e -> onThem());
        btnCapNhat.addActionListener(e -> onCapNhat());
        btnXoa.addActionListener(e -> onXoa());
        btnLamMoi.addActionListener(e -> onLamMoi());
        btnTim.addActionListener(e -> onTim());
    }

    // ================== SỰ KIỆN ==================
    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = tbl.getSelectedRow();
        if (row >= 0) {
            txtMa.setText(val(row, 0));
            txtTen.setText(val(row, 1));
            txtEmail.setText(val(row, 2));
            txtMatKhau.setText(val(row, 3));
        }
    }

    private void onThem() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String email = txtEmail.getText().trim();
        String mk = txtMatKhau.getText().trim();

        if (ten.isEmpty() || email.isEmpty() || mk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Tên, Email, Mật khẩu!");
            return;
        }
        if (ma.isEmpty()) {
            ma = collection.generateNextId(); // tự tạo NVxxx nếu chưa nhập
            txtMa.setText(ma);
        }

        NhanVien nv = new NhanVien(ma, ten, email, mk, LocalDateTime.now());
        boolean ok = collection.add(nv);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            loadTable(collection.getAll());
            selectRowById(ma);
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại. Kiểm tra trùng mã hoặc kết nối DB.");
        }
    }

    private void onCapNhat() {
        int row = tbl.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần cập nhật!");
            return;
        }
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String email = txtEmail.getText().trim();
        String mk = txtMatKhau.getText().trim();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được rỗng!");
            return;
        }

        NhanVien nv = new NhanVien(ma, ten, email, mk, LocalDateTime.now());
        boolean ok = collection.update(nv);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadTable(collection.getAll());
            selectRowById(ma);
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void onXoa() {
        int row = tbl.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
            return;
        }
        String ma = val(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa nhân viên " + ma + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = collection.remove(ma);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadTable(collection.getAll());
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void onLamMoi() {
        collection.refresh();
        loadTable(collection.getAll());
        clearForm();
    }

    private void onTim() {
        String kw = txtSearch.getText().trim();
        List<NhanVien> kq = kw.isEmpty() ? collection.getAll() : collection.filterByName(kw);
        loadTable(kq);
    }

    // ================== TIỆN ÍCH ==================
    private void loadTable(List<NhanVien> data) {
        tableModel.setRowCount(0);
        for (NhanVien nv : data) {
            tableModel.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getTenNhanVien(),
                    nv.getEmail(),
                    nv.getMatKhau(),
                    nv.getThoiGianTaoTaiKhoan() != null ? fmt.format(nv.getThoiGianTaoTaiKhoan()) : ""
            });
        }
    }

    private void selectRowById(String ma) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (ma.equals(tableModel.getValueAt(i, 0))) {
                tbl.setRowSelectionInterval(i, i);
                tbl.scrollRectToVisible(tbl.getCellRect(i, 0, true));
                break;
            }
        }
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtEmail.setText("");
        txtMatKhau.setText("");
        txtSearch.setText("");
        tbl.clearSelection();
    }

    private String val(int row, int col) {
        Object o = tableModel.getValueAt(row, col);
        return o == null ? "" : o.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NhanVien_GUI().setVisible(true));
    }
}