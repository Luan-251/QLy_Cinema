package gui;

import collection.Phim_Collection;
import connectDB.connectDB;
import entity.Phim;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Phim_GUI extends JFrame {

    private Phim_Collection phimCollection;

    private JTextField txtMaPhim, txtTenPhim, txtThoiLuong, txtTenDaoDien,
            txtTenDienVien, txtTheLoai, txtGiaPhim, txtSuatChieu;
    private JButton btnThem, btnXoa, btnSua, btnChiTiet, btnRefresh;
    private JTable table;
    private DefaultTableModel tableModel;

    private ArrayList<Phim> cachedPhimList = new ArrayList<>();

	private JButton btnTim;

	private JTextField txtTimMa;

    public Phim_GUI() {
        phimCollection = new Phim_Collection();
        initLookAndFeel();
        initComponents();
        loadAllPhimToTable();
        addEventHandlers();
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private void initComponents() {
        setTitle("Quản lý Phim");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(main, BorderLayout.CENTER);

        // ======= TOP =======
        JPanel top = new JPanel(new BorderLayout(8, 8));
        main.add(top, BorderLayout.NORTH);

        // Buttons
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        btnThem = makeButton("Thêm");
        btnXoa = makeButton("Xóa");
        btnSua = makeButton("Sửa");
        btnRefresh = makeButton("Làm mới");
        leftButtons.add(btnThem);
        leftButtons.add(btnXoa);
        leftButtons.add(btnSua);
        leftButtons.add(btnRefresh);
        top.add(leftButtons, BorderLayout.WEST);

        // ======= FORM =======
        JPanel form = new JPanel();
        GroupLayout gl = new GroupLayout(form);
        form.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        JLabel lblMa = new JLabel("Mã Phim:");
        JLabel lblTen = new JLabel("Tên Phim:");
        JLabel lblSuat = new JLabel("Suất Chiếu:");
        JLabel lblThoi = new JLabel("Thời Lượng:");
        JLabel lblDao = new JLabel("Tên Đạo Diễn:");
        JLabel lblDien = new JLabel("Tên Diễn Viên:");
        JLabel lblLoai = new JLabel("Thể Loại:");
        JLabel lblGia = new JLabel("Giá Phim:");

        txtMaPhim = new JTextField(12);
        txtTenPhim = new JTextField(18);
        txtSuatChieu = new JTextField(18);
        //txtSuatChieu.setEditable(false);
        txtThoiLuong = new JTextField(6);
        txtTenDaoDien = new JTextField(12);
        txtTenDienVien = new JTextField(12);
        txtTheLoai = new JTextField(12);
        txtGiaPhim = new JTextField(8);

        // Layout
        gl.setHorizontalGroup(
            gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(lblMa).addComponent(lblTen).addComponent(lblThoi).addComponent(lblDao))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtMaPhim).addComponent(txtTenPhim)
                        .addComponent(txtThoiLuong).addComponent(txtTenDaoDien))
                .addGap(20)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(lblSuat).addComponent(lblDien)
                        .addComponent(lblLoai).addComponent(lblGia))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(txtSuatChieu).addComponent(txtTenDienVien)
                        .addComponent(txtTheLoai).addComponent(txtGiaPhim))
        );

        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMa).addComponent(txtMaPhim)
                        .addComponent(lblSuat).addComponent(txtSuatChieu))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTen).addComponent(txtTenPhim)
                        .addComponent(lblDien).addComponent(txtTenDienVien))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblThoi).addComponent(txtThoiLuong)
                        .addComponent(lblLoai).addComponent(txtTheLoai))
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblDao).addComponent(txtTenDaoDien)
                        .addComponent(lblGia).addComponent(txtGiaPhim))
        );

        top.add(form, BorderLayout.CENTER);

        // ======= TABLE =======
        String[] cols = {"Mã Phim", "Tên Phim", "Thời Lượng", "Đạo Diễn",
                "Diễn Viên", "Thể Loại", "Giá"};
        tableModel = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        main.add(scroll, BorderLayout.CENTER);

        // ======= BOTTOM =======
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        txtTimMa = new JTextField(12);
        btnTim = new JButton("Tìm kiếm");
        bottom.add(txtTimMa);
        bottom.add(btnTim);
        txtTimMa.setPreferredSize(new Dimension(160, 36));
        btnTim.setPreferredSize(new Dimension(160, 36));
        btnChiTiet = new JButton("Chi tiết Phim");
        btnChiTiet.setPreferredSize(new Dimension(160, 36));
        bottom.add(btnChiTiet);
        main.add(bottom, BorderLayout.SOUTH);
    }

    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(100, 36));
        return b;
    }

    private void loadAllPhimToTable() {
        SwingUtilities.invokeLater(() -> {
            try {
                cachedPhimList = phimCollection.getAllPhim();
                refreshTableFromList(cachedPhimList);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi load phim: " + ex.getMessage());
            }
        });
    }

    private void refreshTableFromList(ArrayList<Phim> list) {
        tableModel.setRowCount(0);
        if (list == null) return;
        for (Phim p : list) {
            tableModel.addRow(new Object[]{
                    p.getMaPhim(), p.getTenPhim(), p.getThoiLuong(),
                    p.getTenDaoDien(), p.getTenDienVien(),
                    p.getTheLoai(), p.getGiaPhim()
            });
        }
    }

    private void addEventHandlers() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int viewRow = table.getSelectedRow();
                if (viewRow < 0) return;
                int modelRow = table.convertRowIndexToModel(viewRow);
                if (modelRow < 0 || modelRow >= tableModel.getRowCount()) return;

                // điền dữ liệu vào form
                txtMaPhim.setText(tableModel.getValueAt(modelRow, 0).toString());
                txtTenPhim.setText(tableModel.getValueAt(modelRow, 1).toString());
                txtThoiLuong.setText(tableModel.getValueAt(modelRow, 2).toString());
                txtTenDaoDien.setText(tableModel.getValueAt(modelRow, 3).toString());
                txtTenDienVien.setText(tableModel.getValueAt(modelRow, 4).toString());
                txtTheLoai.setText(tableModel.getValueAt(modelRow, 5).toString());
                txtGiaPhim.setText(tableModel.getValueAt(modelRow, 6).toString());

                // lấy suất chiếu và hiển thị
                String maPhim = txtMaPhim.getText().trim();
                ArrayList<String> dsSuat = phimCollection.getSuatChieuCuaPhim(maPhim);
                if (dsSuat.isEmpty()) {
                    txtSuatChieu.setText("Chưa có suất chiếu");
                } else {
                    txtSuatChieu.setText(String.join(", ", dsSuat));
                }
            }
        });

        // Thêm
        btnThem.addActionListener(e -> {
            Phim p = readPhimFromForm();
            if (p == null) return;
            try {
                if (phimCollection.insertPhim(p)) {
                    JOptionPane.showMessageDialog(this, "Thêm phim thành công");
                    loadAllPhimToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage());
            }
        });

        // Xóa
        btnXoa.addActionListener(e -> {
            String ma = txtMaPhim.getText().trim();
            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập mã phim để xóa");
                return;
            }
            int r = JOptionPane.showConfirmDialog(this, "Xóa phim " + ma + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                if (phimCollection.deletePhim(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    loadAllPhimToTable();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại");
                }
            }
        });

        // Sửa
        btnSua.addActionListener(e -> {
            Phim p = readPhimFromForm();
            if (p == null) return;
            try {
                if (phimCollection.updatePhim(p)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                    loadAllPhimToTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage());
            }
        });

        // Làm mới
        btnRefresh.addActionListener(e -> {
            clearForm();
            loadAllPhimToTable();
        });
        
     // ----- Sự kiện tìm kiếm -----
        btnTim.addActionListener(e -> {
            String maCanTim = txtTimMa.getText().trim();
            if (maCanTim.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập mã phim để tìm kiếm");
                return;
            }

            ArrayList<Phim> ketQua = new ArrayList<>();
            for (Phim p : cachedPhimList) {
                if (p.getMaPhim().equalsIgnoreCase(maCanTim)) {
                    ketQua.add(p);
                }
            }

            if (ketQua.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phim có mã: " + maCanTim);
                return;
            }

            refreshTableFromList(ketQua); // Hiển thị kết quả lên bảng
        });
        
     // Chi tiết Phim
        btnChiTiet.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phim để xem chi tiết!");
                return;
            }

            String ma = tableModel.getValueAt(row, 0).toString();
            String ten = tableModel.getValueAt(row, 1).toString();
            String thoiLuong = tableModel.getValueAt(row, 2).toString();
            String daoDien = tableModel.getValueAt(row, 3).toString();
            String dienVien = tableModel.getValueAt(row, 4).toString();
            String theLoai = tableModel.getValueAt(row, 5).toString();
            String gia = tableModel.getValueAt(row, 6).toString();

            ArrayList<String> dsSuat = phimCollection.getSuatChieuCuaPhim(ma);
            String suatChieu = dsSuat.isEmpty() ? "Chưa có suất chiếu" : String.join(", ", dsSuat);

            // Dữ liệu cho JTable
            String[][] data = {
                {"Mã Phim", ma},
                {"Tên Phim", ten},
                {"Thời Lượng", thoiLuong},
                {"Thể Loại", theLoai},
                {"Đạo Diễn", daoDien},
                {"Diễn Viên", dienVien},
                {"Suất Chiếu", suatChieu},
                {"Giá Phim", gia}
            };

            String[] columnNames = {"Thông Tin", "Chi Tiết"};

            JTable tableChiTiet = new JTable(data, columnNames);
            tableChiTiet.setEnabled(false); // Không cho chỉnh sửa
            tableChiTiet.setRowHeight(25);
            tableChiTiet.getColumnModel().getColumn(0).setPreferredWidth(100);
            tableChiTiet.getColumnModel().getColumn(1).setPreferredWidth(300);

            JScrollPane scrollPane = new JScrollPane(tableChiTiet);
            scrollPane.setPreferredSize(new Dimension(450, 200)); // Kích thước popup

            // Hiển thị với nút "Đóng"
            Object[] options = {"Đóng"};
            JOptionPane.showOptionDialog(
                this,
                scrollPane,
                "Chi tiết Phim",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );
        });

    }

    private Phim readPhimFromForm() {
        String ma = txtMaPhim.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phim không được rỗng");
            return null;
        }
        String ten = txtTenPhim.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên phim không được rỗng");
            return null;
        }
        int thoi = 0;
        try {
            thoi = Integer.parseInt(txtThoiLuong.getText().trim());
        } catch (Exception ignored) {}
        String dao = txtTenDaoDien.getText().trim();
        String dien = txtTenDienVien.getText().trim();
        String loai = txtTheLoai.getText().trim();
        double gia = 0;
        try {
            gia = Double.parseDouble(txtGiaPhim.getText().trim());
        } catch (Exception ignored) {}
        return new Phim(ma, ten, thoi, dao, dien, loai, gia);
    }

    private void clearForm() {
        txtMaPhim.setText("");
        txtTenPhim.setText("");
        txtThoiLuong.setText("");
        txtTenDaoDien.setText("");
        txtTenDienVien.setText("");
        txtTheLoai.setText("");
        txtGiaPhim.setText("");
        txtSuatChieu.setText("");
        txtTimMa.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Phim_GUI().setVisible(true));
    }
}
