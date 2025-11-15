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

import dao.GheDAO;
import dao.PhimDAO;
import dao.SuatChieuDAO;
import dao.VeDAO;
import entity.Ghe;
import entity.Phim;
import entity.SuatChieu;
import entity.Ve;

public class Ve_GUI extends JFrame implements ActionListener {

	private JComboBox<Phim> cboTenPhim;
	private JComboBox<SuatChieu> cboSuatChieu;
	private JButton btnChonGhe, btnXacNhan;

	private JLabel lblGiaTriTenPhim, lblGiaTriThoiLuong, lblGiaTriTheLoai;
	private JLabel lblGiaTriTGBatDau, lblGiaTriMaPhong, lblGiaTriGheDaChon;
	private JLabel lblGiaVe, lblGiaTriTongTien;

	private ArrayList<String> gheDaChon = new ArrayList<>();

	private PhimDAO phimDAO;
	private SuatChieuDAO suatChieuDAO;
	private VeDAO veDAO;
	private GheDAO gheDAO;

	public Ve_GUI() {
		setTitle("Đặt vé xem phim");
		setSize(900, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		phimDAO = new PhimDAO();
		suatChieuDAO = new SuatChieuDAO();
		veDAO = new VeDAO();
		gheDAO = new GheDAO();

		JPanel pMain = new JPanel(new BorderLayout(10, 10));
		pMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// ===== CHỌN PHIM – SUẤT CHIẾU =====
		JPanel pChon = new JPanel(new GridLayout(2, 2, 10, 10));

		JLabel lblPhim = new JLabel("Phim:");
		JLabel lblSuat = new JLabel("Suất chiếu:");

		cboTenPhim = new JComboBox<>();
		cboSuatChieu = new JComboBox<>();

		pChon.add(lblPhim);
		pChon.add(cboTenPhim);
		pChon.add(lblSuat);
		pChon.add(cboSuatChieu);

		btnChonGhe = new JButton("Chọn ghế");
		JPanel pNorth = new JPanel(new BorderLayout());
		pNorth.add(pChon, BorderLayout.CENTER);
		pNorth.add(btnChonGhe, BorderLayout.EAST);

		// ===== THÔNG TIN VÉ =====
		JPanel pThongTin = new JPanel(new GridLayout(8, 2, 10, 10));
		pThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin vé"));

		lblGiaTriTenPhim = new JLabel("");
		lblGiaTriThoiLuong = new JLabel("");
		lblGiaTriTheLoai = new JLabel("");
		lblGiaTriTGBatDau = new JLabel("");
		lblGiaTriMaPhong = new JLabel("");
		lblGiaTriGheDaChon = new JLabel("Chưa chọn ghế");
		lblGiaTriGheDaChon.setForeground(Color.RED);
		lblGiaVe = new JLabel("0");
		lblGiaTriTongTien = new JLabel("0");

		pThongTin.add(new JLabel("Tên phim:"));
		pThongTin.add(lblGiaTriTenPhim);
		pThongTin.add(new JLabel("Thời lượng:"));
		pThongTin.add(lblGiaTriThoiLuong);
		pThongTin.add(new JLabel("Thể loại:"));
		pThongTin.add(lblGiaTriTheLoai);
		pThongTin.add(new JLabel("Thời gian bắt đầu:"));
		pThongTin.add(lblGiaTriTGBatDau);
		pThongTin.add(new JLabel("Mã phòng:"));
		pThongTin.add(lblGiaTriMaPhong);
		pThongTin.add(new JLabel("Ghế đã chọn:"));
		pThongTin.add(lblGiaTriGheDaChon);
		pThongTin.add(new JLabel("Giá vé:"));
		pThongTin.add(lblGiaVe);
		pThongTin.add(new JLabel("Tổng tiền:"));
		pThongTin.add(lblGiaTriTongTien);

		// ===== NÚT XÁC NHẬN =====
		btnXacNhan = new JButton("Xác nhận đặt vé");
		JPanel pSouth = new JPanel();
		pSouth.add(btnXacNhan);

		// ===== ĐƯA VÀO GIAO DIỆN =====
		pMain.add(pNorth, BorderLayout.NORTH);
		pMain.add(pThongTin, BorderLayout.CENTER);
		pMain.add(pSouth, BorderLayout.SOUTH);
		add(pMain, BorderLayout.CENTER);

		// ===== SỰ KIỆN =====
		cboTenPhim.addActionListener(this);
		cboSuatChieu.addActionListener(this);
		btnChonGhe.addActionListener(this);
		btnXacNhan.addActionListener(this);

		// ===== TẢI DỮ LIỆU =====
		loadPhim();
	}

	// ==================== LOAD DATA ====================
	private void loadPhim() {
		ArrayList<Phim> dsPhim = phimDAO.getAllPhim();
		cboTenPhim.removeAllItems();
		for (Phim p : dsPhim) {
			cboTenPhim.addItem(p);
		}
	}

	private void loadSuatChieu(Phim phim) {
		cboSuatChieu.removeAllItems();
		ArrayList<SuatChieu> ds = suatChieuDAO.getSuatChieuByPhim(phim);
		for (SuatChieu s : ds) {
			cboSuatChieu.addItem(s);
		}
	}

	// ==================== GHẾ ====================
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

		// Cập nhật tổng tiền
		if (cboSuatChieu.getSelectedItem() != null) {
			SuatChieu suat = (SuatChieu) cboSuatChieu.getSelectedItem();
			double tongTien = suat.getPhim().getGiaPhim() * dsGhe.size();
			lblGiaTriTongTien.setText(String.format("%.0f", tongTien));
		}
	}

	// ==================== XÁC NHẬN ĐẶT VÉ ====================
	private void xacNhanDatVe() {
		if (gheDaChon.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Chưa chọn ghế!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (cboSuatChieu.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(this, "Chưa chọn suất chiếu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return;
		}

		SuatChieu suatChieu = (SuatChieu) cboSuatChieu.getSelectedItem();
		String maPhong = suatChieu.getPhong().getMaPhong();
		double giaVe = suatChieu.getPhim().getGiaPhim();

		int soVeThanhCong = 0;
		int soVeThatBai = 0;
		ArrayList<String> danhSachMaVe = new ArrayList<>();

		// Tạo vé cho từng ghế
		for (String tenGhe : gheDaChon) {
			// Lấy thông tin ghế từ database
			Ghe ghe = gheDAO.getGheByMaGheAndMaPhong(tenGhe, maPhong);

			if (ghe == null) {
				System.out.println("⚠️ Không tìm thấy ghế: " + tenGhe + " trong phòng: " + maPhong);
				soVeThatBai++;
				continue;
			}

			// Tạo mã vé mới
			String maVe = veDAO.taoMaVeMoi();
			danhSachMaVe.add(maVe);

			// Tạo đối tượng vé
			Ve ve = new Ve(maVe, giaVe, suatChieu, ghe);

			// Thêm vé vào database
			boolean ketQua = veDAO.themVe(ve);
			if (ketQua) {
				soVeThanhCong++;
				System.out.println("✅ Đã tạo vé: " + maVe + " cho ghế " + tenGhe);
			} else {
				soVeThatBai++;
				System.out.println("❌ Thất bại khi tạo vé cho ghế " + tenGhe);
			}
		}

		// Hiển thị kết quả
		if (soVeThanhCong > 0) {
			String thongBao = String.format(
					"🎉 Đặt vé thành công!\n\n" + "Số vé đã đặt: %d\n" + "Ghế: %s\n" + "Tổng tiền: %s VNĐ\n\n"
							+ "Mã vé: %s",
					soVeThanhCong, String.join(", ", gheDaChon), lblGiaTriTongTien.getText(),
					String.join(", ", danhSachMaVe));

			if (soVeThatBai > 0) {
				thongBao += "\n\n⚠️ Có " + soVeThatBai + " vé không thể tạo.";
			}

			JOptionPane.showMessageDialog(this, thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);

			// Reset sau khi đặt vé thành công
			capNhatGheDaChon(new ArrayList<>());
		} else {
			JOptionPane.showMessageDialog(this, "❌ Không thể tạo vé!\nVui lòng kiểm tra lại dữ liệu.", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// ==================== SỰ KIỆN ====================
	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == cboTenPhim) {
			Phim selectedPhim = (Phim) cboTenPhim.getSelectedItem();
			if (selectedPhim != null) {
				loadSuatChieu(selectedPhim);

				lblGiaTriTenPhim.setText(selectedPhim.getTenPhim());
				lblGiaTriThoiLuong.setText(selectedPhim.getThoiLuong() + " phút");
				lblGiaTriTheLoai.setText(selectedPhim.getTheLoai());
				lblGiaVe.setText(String.format("%.0f", selectedPhim.getGiaPhim()));

				// Reset thông tin khi đổi phim
				lblGiaTriTGBatDau.setText("");
				lblGiaTriMaPhong.setText("");
				capNhatGheDaChon(new ArrayList<>());
			}

		} else if (src == cboSuatChieu) {
			SuatChieu selectedSuat = (SuatChieu) cboSuatChieu.getSelectedItem();
			if (selectedSuat != null) {
				lblGiaTriTGBatDau.setText(selectedSuat.getThoiGianBatDau().toString());
				lblGiaTriMaPhong.setText(selectedSuat.getPhong().getMaPhong());

				// Cập nhật giá vé và tổng tiền nếu đã chọn ghế
				lblGiaVe.setText(String.format("%.0f", selectedSuat.getPhim().getGiaPhim()));
				double tongTien = selectedSuat.getPhim().getGiaPhim() * gheDaChon.size();
				lblGiaTriTongTien.setText(String.format("%.0f", tongTien));
			}

		} else if (src == btnChonGhe) {
			if (cboTenPhim.getSelectedItem() == null || cboSuatChieu.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn phim và suất chiếu!", "Lỗi",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			SuatChieu suat = (SuatChieu) cboSuatChieu.getSelectedItem();
			String maPhong = suat.getPhong().getMaPhong();
			String maSuatChieu = suat.getMaSuatChieu();

			// Constructor: (parent, maPhong, maSuatChieu, parentGui, gheDaChon)
			ChonGhe_GUI chonGhe = new ChonGhe_GUI(this, maPhong, maSuatChieu, this, gheDaChon);
			chonGhe.setVisible(true);

		} else if (src == btnXacNhan) {
			xacNhanDatVe();
		}
	}

	// ==================== MAIN ====================
	public static void main(String[] args) {
		new Ve_GUI().setVisible(true);
	}
}