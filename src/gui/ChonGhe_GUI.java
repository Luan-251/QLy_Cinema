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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dao.VeDAO;

public class ChonGhe_GUI extends JDialog implements ActionListener {

	private final String maPhong;
	private final String maSuatChieu;
	private final Ve_GUI parentGui;
	private final ArrayList<String> gheDaChonHienTai; // Lưu trạng thái ghế đã chọn trước đó

	private final ArrayList<String> gheChonMoi = new ArrayList<>(); // Lưu ghế mới được chọn
	private ArrayList<String> gheDaDatTrongDB = new ArrayList<>(); // Ghế đã đặt từ database

	// Màu sắc
	private final Color COLOR_GHE_TRONG = Color.LIGHT_GRAY;
	private final Color COLOR_GHE_CHON = new Color(76, 175, 80); // Xanh lá cây đậm
	private final Color COLOR_GHE_DAT = new Color(175, 23, 30); // Đỏ đậm (giống menu)
	private final Color COLOR_MAN_HINH = new Color(30, 30, 30);

	private VeDAO veDAO;

	public ChonGhe_GUI(JFrame parent, String maPhong, String maSuatChieu, Ve_GUI parentGui,
			ArrayList<String> gheDaChon) {
		super(parent, "Chọn ghế - " + maPhong, true);
		this.maPhong = maPhong;
		this.maSuatChieu = maSuatChieu;
		this.parentGui = parentGui;
		this.gheDaChonHienTai = new ArrayList<>(gheDaChon); // Copy danh sách ghế hiện tại
		this.veDAO = new VeDAO();

		// Lấy danh sách ghế đã đặt từ database
		gheDaDatTrongDB = veDAO.getGheDaDatBySuatChieu(maSuatChieu, maPhong);

		setSize(800, 600);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout(10, 10));

		// 1. PHẦN BẮC (Màn Hình)
		JPanel pNorth = new JPanel(new BorderLayout());
		pNorth.setPreferredSize(new Dimension(0, 50));
		pNorth.setBackground(COLOR_MAN_HINH);
		JLabel lblScreen = new JLabel("MÀN HÌNH", SwingConstants.CENTER);
		lblScreen.setFont(new Font("Arial", Font.BOLD, 20));
		lblScreen.setForeground(Color.WHITE);
		pNorth.add(lblScreen, BorderLayout.CENTER);
		add(pNorth, BorderLayout.NORTH);

		// 2. PHẦN TRUNG TÂM (Lưới Ghế)
		JPanel panelGhe = new JPanel(new GridLayout(5, 10, 10, 10));
		panelGhe.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

		// Tạo các nút ghế
		for (char hang = 'A'; hang <= 'E'; hang++) {
			for (int i = 1; i <= 10; i++) {
				String tenGhe = hang + String.format("%02d", i);
				JButton ghe = new JButton(tenGhe);
				ghe.setPreferredSize(new Dimension(60, 40));
				ghe.addActionListener(this);

				// Ghế đã được đặt trong database (không thể chọn)
				if (gheDaDatTrongDB.contains(tenGhe)) {
					ghe.setBackground(COLOR_GHE_DAT);
					ghe.setEnabled(false);
				}
				// Ghế đã được chọn từ Ve_GUI (có thể hủy)
				else if (gheDaChonHienTai.contains(tenGhe)) {
					ghe.setBackground(COLOR_GHE_CHON);
					gheChonMoi.add(tenGhe); // Khởi tạo danh sách ghế đang được chọn
				}
				// Ghế trống
				else {
					ghe.setBackground(COLOR_GHE_TRONG);
				}

				panelGhe.add(ghe);
			}
		}
		add(panelGhe, BorderLayout.CENTER);

		// 3. PHẦN NAM (Chú thích và nút chức năng)
		JPanel pSouth = new JPanel(new BorderLayout());
		pSouth.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		// --- Chú thích màu ---
		JPanel pLegend = new JPanel();
		pLegend.add(createLegendButton("Ghế trống", COLOR_GHE_TRONG));
		pLegend.add(createLegendButton("Ghế đã chọn", COLOR_GHE_CHON));
		pLegend.add(createLegendButton("Ghế đã đặt", COLOR_GHE_DAT));

		pSouth.add(pLegend, BorderLayout.NORTH);

		// --- Nút XÁC NHẬN / HỦY ---
		JPanel pButtons = new JPanel();
		JButton btnHuy = new JButton("HỦY");
		btnHuy.setBackground(Color.GRAY);
		btnHuy.setForeground(Color.WHITE);
		btnHuy.addActionListener(e -> dispose());

		JButton btnXacNhan = new JButton("XÁC NHẬN");
		btnXacNhan.setBackground(new Color(30, 144, 255)); // Xanh dương
		btnXacNhan.setForeground(Color.WHITE);
		btnXacNhan.addActionListener(e -> xacNhanChonGhe());

		pButtons.add(btnHuy);
		pButtons.add(btnXacNhan);

		pSouth.add(pButtons, BorderLayout.SOUTH);

		add(pSouth, BorderLayout.SOUTH);
	}

	private JButton createLegendButton(String text, Color color) {
		JButton btn = new JButton(text);
		btn.setBackground(color);
		btn.setPreferredSize(new Dimension(150, 25));
		btn.setEnabled(false);
		return btn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton ghe = (JButton) e.getSource();
		String tenGhe = ghe.getText();

		if (ghe.getBackground().equals(COLOR_GHE_TRONG)) {
			// Chọn ghế
			ghe.setBackground(COLOR_GHE_CHON);
			gheChonMoi.add(tenGhe);
		} else if (ghe.getBackground().equals(COLOR_GHE_CHON)) {
			// Hủy chọn ghế
			ghe.setBackground(COLOR_GHE_TRONG);
			gheChonMoi.remove(tenGhe);
		}

		// Sắp xếp lại danh sách ghế đã chọn
		Collections.sort(gheChonMoi);
	}

	private void xacNhanChonGhe() {
		// Gửi danh sách ghế đã chọn về cho Ve_GUI
		parentGui.capNhatGheDaChon(gheChonMoi);
		dispose();
	}
}