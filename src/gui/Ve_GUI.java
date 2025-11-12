package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// ==========================================================
// CÁC LỚP GIẢ ĐỊNH (ENTITY VÀ DAO)
// ==========================================================
class Phim {
	private String tenPhim;

	public Phim(String tenPhim) {
		this.tenPhim = tenPhim;
	}

	@Override
	public String toString() {
		return tenPhim;
	}
}

class SuatChieu {
	private String maSuat;

	public SuatChieu(String maSuat) {
		this.maSuat = maSuat;
	}

	@Override
	public String toString() {
		return maSuat;
	}
}

class PhimDAO {
	public ArrayList<Phim> getAllPhim() {
		ArrayList<Phim> ds = new ArrayList<>();
		ds.add(new Phim("Dune: Part Two"));
		ds.add(new Phim("Avengers: Endgame"));
		return ds;
	}
}

class SuatChieuDAO {
	public ArrayList<SuatChieu> getSuatChieuByPhim(Phim phim) {
		ArrayList<SuatChieu> ds = new ArrayList<>();
		if (phim.toString().contains("Dune")) {
			ds.add(new SuatChieu("SC001 (10:00)"));
			ds.add(new SuatChieu("SC002 (14:00)"));
		} else {
			ds.add(new SuatChieu("SC003 (19:00)"));
		}
		return ds;
	}
}
// ==========================================================
// HẾT CÁC LỚP GIẢ ĐỊNH
// ==========================================================

public class Ve_GUI extends JPanel implements ActionListener {

	private JComboBox<Phim> cboTenPhim;
	private JComboBox<Object> cboPhong;
	private JComboBox<Object> cboSuatChieu;
	private JButton btnChonGhe;
	private JButton btnXacNhan;

	// Các JLabel hiển thị thông tin
	private JLabel lblGiaTriTenPhim;
	private JLabel lblGiaTriTenPhong;
	private JLabel lblGiaTriThoiLuong;
	private JLabel lblGiaTriTheLoai;
	private JLabel lblGiaTriTGBatDau;
	private JLabel lblGiaTriGheDaChon;

	private ArrayList<String> gheDaChon = new ArrayList<>();

	public Ve_GUI() {
		setLayout(new BorderLayout(10, 10));
		this.setBackground(Color.WHITE);

		// 2. PANEL CHÍNH (CENTER)
		JPanel pMain = new JPanel(new BorderLayout(10, 10));
		pMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pMain.setBackground(Color.WHITE);

		// --- PHẦN TRÊN: CHỌN PHIM VÀ SUẤT CHIẾU ---
		JPanel pChonPhimVaSuatChieu = new JPanel(new BorderLayout());
		pChonPhimVaSuatChieu.setBackground(Color.WHITE);

		// Bảng chọn (Phim, Phòng, Suất chiếu)
		JPanel pChon = new JPanel(new GridLayout(3, 2, 10, 10));
		pChon.setBackground(Color.WHITE);

		// --- Cột trái: Label ---
		JLabel lblTenPhim = new JLabel("Phim:");
		JLabel lblPhong = new JLabel("Phòng:");
		JLabel lblSuatChieu = new JLabel("Suất chiếu:");

		// --- Cột phải: ComboBox ---
		cboTenPhim = new JComboBox<>();
		cboTenPhim.setPreferredSize(new Dimension(300, 30));

		cboPhong = new JComboBox<>();
		cboPhong.setPreferredSize(new Dimension(300, 30));
		cboPhong.addItem("P002"); // Ví dụ phòng
		cboPhong.addItem("P001");

		cboSuatChieu = new JComboBox<>();
		cboSuatChieu.setPreferredSize(new Dimension(300, 30));
		cboSuatChieu.addItem("-- Chọn Suất chiếu --");

		pChon.add(lblTenPhim);
		pChon.add(cboTenPhim);
		pChon.add(lblPhong);
		pChon.add(cboPhong);
		pChon.add(lblSuatChieu);
		pChon.add(cboSuatChieu);

		// Nút CHỌN GHẾ
		btnChonGhe = new JButton("CHỌN GHẾ");
		btnChonGhe.setBackground(new Color(175, 23, 30));
		btnChonGhe.setForeground(Color.WHITE);
		btnChonGhe.setFont(new Font("Arial", Font.BOLD, 14));
		btnChonGhe.setPreferredSize(new Dimension(150, 40));

		JPanel pChonGheWrapper = new JPanel(new BorderLayout());
		pChonGheWrapper.setBackground(Color.WHITE);
		pChonGheWrapper.add(btnChonGhe, BorderLayout.EAST);

		pChonPhimVaSuatChieu.add(pChon, BorderLayout.WEST);
		pChonPhimVaSuatChieu.add(pChonGheWrapper, BorderLayout.EAST);
		pChonPhimVaSuatChieu.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);

		// --- PHẦN GIỮA: THÔNG TIN VỀ ---
		JPanel pThongTinVe = new JPanel(new GridLayout(6, 2, 5, 15));
		pThongTinVe.setBorder(BorderFactory.createTitledBorder("THÔNG TIN VỀ"));
		pThongTinVe.setBackground(Color.WHITE);

		Font infoFont = new Font("Arial", Font.PLAIN, 14);
		Font labelFont = new Font("Arial", Font.BOLD, 14);

		// Các hàng thông tin
		pThongTinVe.add(createLabel("Tên phim:", labelFont));
		lblGiaTriTenPhim = createLabel("", infoFont);
		pThongTinVe.add(lblGiaTriTenPhim);

		pThongTinVe.add(createLabel("Tên phòng:", labelFont));
		lblGiaTriTenPhong = createLabel("", infoFont);
		pThongTinVe.add(lblGiaTriTenPhong);

		pThongTinVe.add(createLabel("Thời lượng:", labelFont));
		lblGiaTriThoiLuong = createLabel("", infoFont);
		pThongTinVe.add(lblGiaTriThoiLuong);

		pThongTinVe.add(createLabel("Thể loại:", labelFont));
		lblGiaTriTheLoai = createLabel("", infoFont);
		pThongTinVe.add(lblGiaTriTheLoai);

		pThongTinVe.add(createLabel("Thời gian bắt đầu:", labelFont));
		lblGiaTriTGBatDau = createLabel("", infoFont);
		pThongTinVe.add(lblGiaTriTGBatDau);

		pThongTinVe.add(createLabel("Ghế đã chọn:", labelFont));
		lblGiaTriGheDaChon = createLabel("Chưa chọn ghế", infoFont);
		lblGiaTriGheDaChon.setForeground(Color.RED);
		pThongTinVe.add(lblGiaTriGheDaChon);

		// --- NÚT XÁC NHẬN ---
		btnXacNhan = new JButton("XÁC NHẬN ĐẶT VÉ");
		btnXacNhan.setBackground(new Color(175, 23, 30));
		btnXacNhan.setForeground(Color.WHITE);
		btnXacNhan.setFont(new Font("Arial", Font.BOLD, 16));
		btnXacNhan.setPreferredSize(new Dimension(200, 40));

		JPanel pSouthMain = new JPanel(new BorderLayout());
		pSouthMain.setBackground(Color.WHITE);
		pSouthMain.add(btnXacNhan, BorderLayout.EAST);

		pMain.add(pChonPhimVaSuatChieu, BorderLayout.NORTH);
		pMain.add(pThongTinVe, BorderLayout.CENTER);
		pMain.add(pSouthMain, BorderLayout.SOUTH);

		add(pMain, BorderLayout.CENTER);

		// 3. LOGIC XỬ LÝ DỮ LIỆU
		loadData();
		cboTenPhim.addActionListener(this);
		btnChonGhe.addActionListener(this);
		btnXacNhan.addActionListener(this);
		cboPhong.addActionListener(this); // Thêm sự kiện cho cboPhong
	}

	private JLabel createLabel(String text, Font font) {
		JLabel label = new JLabel(text);
		label.setFont(font);
		return label;
	}

	private void loadData() {
		PhimDAO phimDao = new PhimDAO();
		ArrayList<Phim> dsPhim = phimDao.getAllPhim();
		for (Phim phim : dsPhim) {
			cboTenPhim.addItem(phim);
		}

		if (!dsPhim.isEmpty()) {
			cboTenPhim.setSelectedIndex(0);
			loadSuatChieu(dsPhim.get(0));
		}
	}

	private void loadSuatChieu(Phim phim) {
		SuatChieuDAO dao = new SuatChieuDAO();
		ArrayList<SuatChieu> dsSuat = dao.getSuatChieuByPhim(phim);
		cboSuatChieu.removeAllItems();

		cboSuatChieu.addItem("-- Chọn Suất chiếu --");

		for (SuatChieu s : dsSuat) {
			cboSuatChieu.addItem(s);
		}

		// Reset ghế khi phim/suất chiếu thay đổi
		capNhatGheDaChon(new ArrayList<>());
	}

	// PHƯƠNG THỨC MỚI: Cập nhật ghế từ cửa sổ ChonGhe_GUI
	public void capNhatGheDaChon(ArrayList<String> dsGhe) {
		this.gheDaChon = dsGhe;
		Collections.sort(this.gheDaChon);
		if (dsGhe.isEmpty()) {
			lblGiaTriGheDaChon.setText("Chưa chọn ghế");
			lblGiaTriGheDaChon.setForeground(Color.RED);
		} else {
			lblGiaTriGheDaChon.setText(String.join(", ", dsGhe));
			lblGiaTriGheDaChon.setForeground(Color.BLUE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cboTenPhim) {
			Phim selectedPhim = (Phim) cboTenPhim.getSelectedItem();
			if (selectedPhim != null) {
				loadSuatChieu(selectedPhim);

				lblGiaTriTenPhim.setText(selectedPhim.toString());
				lblGiaTriThoiLuong.setText("175 phút");
				lblGiaTriTheLoai.setText("Khoa học viễn tưởng");
				lblGiaTriTenPhong.setText(cboPhong.getSelectedItem().toString());
				lblGiaTriTGBatDau.setText("10:00:00");

				capNhatGheDaChon(new ArrayList<>());
			}
		} else if (e.getSource() == cboPhong) {
			// Cập nhật thông tin phòng và reset ghế
			lblGiaTriTenPhong.setText(cboPhong.getSelectedItem().toString());
			capNhatGheDaChon(new ArrayList<>());

		} else if (e.getSource() == btnChonGhe) {

			// Kiểm tra đã chọn Phim và Phòng chưa
			if (cboTenPhim.getSelectedIndex() == -1 || cboPhong.getSelectedIndex() == -1
					|| cboSuatChieu.getSelectedIndex() <= 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn Phim và Suất chiếu trước khi chọn ghế!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
			String maPhong = cboPhong.getSelectedItem().toString();

			// Khởi tạo cửa sổ chọn ghế, truyền danh sách ghế đã chọn (nếu có)
			ChonGhe_GUI chonGheDialog = new ChonGhe_GUI(parentFrame, maPhong, this, gheDaChon);
			chonGheDialog.setVisible(true);

		} else if (e.getSource() == btnXacNhan) {
			if (gheDaChon.isEmpty() || cboTenPhim.getSelectedIndex() == -1 || cboSuatChieu.getSelectedIndex() <= 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ Phim, Suất chiếu và Ghế!", "Lỗi đặt vé",
						JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this,
						"Xác nhận đặt vé thành công cho các ghế: " + String.join(", ", gheDaChon), "Thành công",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	// Lớp Wrapper để chạy giao diện (mô phỏng thanh menu bên trái)
	public static void main(String[] args) {
		JFrame frame = new JFrame("Quản lý Rạp Phim - BeanCinema");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1100, 750);
		frame.setLocationRelativeTo(null);

		JPanel contentPanel = new JPanel(new BorderLayout());

		// Menu bên trái (Mô phỏng thanh menu của Hình 1)
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(new Color(175, 23, 30));
		menuPanel.setPreferredSize(new Dimension(180, 0));
		menuPanel.setLayout(new GridLayout(7, 1, 0, 0));

		addButtonToMenu(menuPanel, "BeanCinema", new Color(175, 23, 30), Color.WHITE, new Font("Arial", Font.BOLD, 20));
		addButtonToMenu(menuPanel, "Phim", new Color(175, 23, 30), Color.WHITE, new Font("Arial", Font.BOLD, 14));
		addButtonToMenu(menuPanel, "Suất Chiếu", new Color(175, 23, 30), Color.WHITE, new Font("Arial", Font.BOLD, 14));
		addButtonToMenu(menuPanel, "Vé", new Color(200, 50, 50), Color.WHITE, new Font("Arial", Font.BOLD, 14));
		addButtonToMenu(menuPanel, "Danh Sách Vé", new Color(175, 23, 30), Color.WHITE,
				new Font("Arial", Font.BOLD, 14));
		addButtonToMenu(menuPanel, "Nhân Viên", new Color(175, 23, 30), Color.WHITE, new Font("Arial", Font.BOLD, 14));

		contentPanel.add(menuPanel, BorderLayout.WEST);

		Ve_GUI veGui = new Ve_GUI();
		contentPanel.add(veGui, BorderLayout.CENTER);

		frame.setContentPane(contentPanel);
		frame.setVisible(true);
	}

	private static void addButtonToMenu(JPanel menuPanel, String text, Color bg, Color fg, Font font) {
		JButton btn = new JButton(text);
		btn.setBackground(bg);
		btn.setForeground(fg);
		btn.setFont(font);
		btn.setPreferredSize(new Dimension(180, 50));
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		btn.setFocusPainted(false);
		menuPanel.add(btn);
	}
}