package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dao.PhimDAO;
import dao.SuatChieuDAO;
import entity.Phim;
import entity.SuatChieu;

public class Ve_GUI extends JFrame implements ActionListener {

	private JComboBox<Phim> cboTenPhim;
	private JComboBox<String> cboPhong;
	private JComboBox<SuatChieu> cboSuatChieu;
	private JButton btnChonGhe, btnXacNhan;

	private JLabel lblGiaTriTenPhim, lblGiaTriTenPhong, lblGiaTriThoiLuong, lblGiaTriTheLoai, lblGiaTriTGBatDau,
			lblGiaTriGheDaChon;

	private ArrayList<String> gheDaChon = new ArrayList<>();

	private PhimDAO phimDAO;
	private SuatChieuDAO suatChieuDAO;

	public Ve_GUI() {
		setTitle("Đặt vé xem phim");
		setSize(900, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		phimDAO = new PhimDAO();
		suatChieuDAO = new SuatChieuDAO();

		JPanel pMain = new JPanel(new BorderLayout(10, 10));
		pMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// ===== PHẦN TRÊN: CHỌN PHIM - PHÒNG - SUẤT CHIẾU =====
		JPanel pChon = new JPanel(new GridLayout(3, 2, 10, 10));

		JLabel lblPhim = new JLabel("Phim:");
		JLabel lblPhong = new JLabel("Phòng:");
		JLabel lblSuat = new JLabel("Suất chiếu:");

		cboTenPhim = new JComboBox<>();
		cboPhong = new JComboBox<>();
		cboSuatChieu = new JComboBox<>();

		pChon.add(lblPhim);
		pChon.add(cboTenPhim);
//		pChon.add(lblPhong);
//		pChon.add(cboPhong);
		pChon.add(lblSuat);
		pChon.add(cboSuatChieu);

		btnChonGhe = new JButton("Chọn ghế");
		JPanel pNorth = new JPanel(new BorderLayout());
		pNorth.add(pChon, BorderLayout.CENTER);
		pNorth.add(btnChonGhe, BorderLayout.EAST);

		// ===== PHẦN GIỮA: THÔNG TIN VÉ =====
		JPanel pThongTin = new JPanel(new GridLayout(6, 2, 10, 10));
		pThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin vé"));

		lblGiaTriTenPhim = new JLabel("");
		lblGiaTriTenPhong = new JLabel("");
		lblGiaTriThoiLuong = new JLabel("");
		lblGiaTriTheLoai = new JLabel("");
		lblGiaTriTGBatDau = new JLabel("");
		lblGiaTriGheDaChon = new JLabel("Chưa chọn ghế");
		lblGiaTriGheDaChon.setForeground(Color.RED);

		pThongTin.add(new JLabel("Tên phim:"));
		pThongTin.add(lblGiaTriTenPhim);
		pThongTin.add(new JLabel("Tên phòng:"));
		pThongTin.add(lblGiaTriTenPhong);
		pThongTin.add(new JLabel("Thời lượng:"));
		pThongTin.add(lblGiaTriThoiLuong);
		pThongTin.add(new JLabel("Thể loại:"));
		pThongTin.add(lblGiaTriTheLoai);
		pThongTin.add(new JLabel("Thời gian bắt đầu:"));
		pThongTin.add(lblGiaTriTGBatDau);
		pThongTin.add(new JLabel("Ghế đã chọn:"));
		pThongTin.add(lblGiaTriGheDaChon);

		// ===== PHẦN DƯỚI: NÚT XÁC NHẬN =====
		btnXacNhan = new JButton("Xác nhận đặt vé");
		JPanel pSouth = new JPanel();
		pSouth.add(btnXacNhan);

		// ===== THÊM VÀO GIAO DIỆN =====
		pMain.add(pNorth, BorderLayout.NORTH);
		pMain.add(pThongTin, BorderLayout.CENTER);
		pMain.add(pSouth, BorderLayout.SOUTH);
		add(pMain, BorderLayout.CENTER);

		// ===== SỰ KIỆN =====
		cboTenPhim.addActionListener(this);
		cboSuatChieu.addActionListener(this);
		btnChonGhe.addActionListener(this);
		btnXacNhan.addActionListener(this);

		// ===== TẢI DỮ LIỆU BAN ĐẦU =====
		loadPhim();
	}

	// ==================== HÀM XỬ LÝ ====================

	private void loadPhim() {
		ArrayList<Phim> dsPhim = phimDAO.getAllPhim();

		cboTenPhim.removeAllItems();
		for (Phim p : dsPhim) {
			cboTenPhim.addItem(p);
		}
	}

	private void loadSuatChieu(Phim phim) {
		cboSuatChieu.removeAllItems();
	//	ArrayList<SuatChieu> ds = suatChieuDAO.getSuatChieuByPhim(phim);
	//	for (SuatChieu s : ds) {
		//	cboSuatChieu.addItem(s);
		}
//	}

	void capNhatGheDaChon(ArrayList<String> dsGhe) {
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

	// ==================== ACTION ====================

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == cboTenPhim) {
			Phim selectedPhim = (Phim) cboTenPhim.getSelectedItem();
			if (selectedPhim != null) {
				loadSuatChieu(selectedPhim);

				// ✅ Lấy thông tin phim từ DB
				lblGiaTriTenPhim.setText(selectedPhim.getTenPhim());
				lblGiaTriThoiLuong.setText(selectedPhim.getThoiLuong() + " phút");
				lblGiaTriTheLoai.setText(selectedPhim.getTheLoai());
			}

		} else if (src == cboSuatChieu) {
			SuatChieu selectedSuat = (SuatChieu) cboSuatChieu.getSelectedItem();
			if (selectedSuat != null) {
				// ✅ Lấy thông tin suất chiếu từ DB
		//		lblGiaTriTenPhong.setText(selectedSuat.getPhong().getMaPhong());
				lblGiaTriTGBatDau.setText(selectedSuat.getThoiGianBatDau().toString());
			}

		} else if (src == btnChonGhe) {
			if (cboTenPhim.getSelectedItem() == null || cboSuatChieu.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn phim và suất chiếu!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			SuatChieu suatChieu = (SuatChieu) cboSuatChieu.getSelectedItem();
		//	String maPhong = suatChieu.getPhong().getMaPhong();

			// ✅ Mở giao diện chọn ghế
		//	ChonGhe_GUI chonGhe = new ChonGhe_GUI(this, maPhong, this, gheDaChon);
		//	chonGhe.setVisible(true);

		} else if (src == btnXacNhan) {
			if (gheDaChon.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Chưa chọn ghế!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "🎉 Đặt vé thành công!\nGhế: " + String.join(", ", gheDaChon),
						"Thành công", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	// ==================== MAIN ====================
	public static void main(String[] args) {
		new Ve_GUI().setVisible(true);
	}
}
