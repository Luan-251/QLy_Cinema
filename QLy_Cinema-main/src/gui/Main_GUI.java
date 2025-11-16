package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.*;

public class Main_GUI extends JFrame {
    private JButton btnPhim, btnNhanVien, btnSuatChieu;
    private JLabel lblNorth;
    private JPanel pnlWest, pnlCenter;

    public Main_GUI() {
        setFont(new Font("Arial", Font.BOLD, 13));

        // ===== NORTH =====
        JPanel pnlNorth = new JPanel();
        lblNorth = new JLabel("TRANG CHỦ");
        lblNorth.setFont(new Font("Arial", Font.BOLD, 20));
        lblNorth.setForeground(Color.RED);
        pnlNorth.add(lblNorth);
        pnlNorth.setBackground(new Color(0x000000));
        add(pnlNorth, BorderLayout.NORTH);

        // ===== WEST =====
        pnlWest = new JPanel();
        pnlWest.setLayout(new BoxLayout(pnlWest, BoxLayout.Y_AXIS));
        pnlWest.setBackground(new Color(0x000000));
        pnlWest.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/logo_Cinema.png"));
            int maxWidth = 180;
            int newWidth = originalIcon.getIconWidth() <= maxWidth ? originalIcon.getIconWidth() : maxWidth;
            int newHeight = (int) ((double) originalIcon.getIconHeight() * newWidth / originalIcon.getIconWidth());
            ImageIcon scaledIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
            JLabel lblLogo = new JLabel(scaledIcon);
            lblLogo.setAlignmentX(CENTER_ALIGNMENT);
            pnlWest.add(lblLogo);
            pnlWest.add(Box.createVerticalStrut(20));
        } catch (Exception e) {
            JLabel lblError = new JLabel("Không thể tải logo");
            lblError.setAlignmentX(CENTER_ALIGNMENT);
            pnlWest.add(lblError);
            pnlWest.add(Box.createVerticalStrut(20));
        }

        // Nút Phim
        Box b1 = Box.createHorizontalBox();
        btnPhim = createButton(b1, "Quản lý phim");
        btnPhim.addActionListener(e -> showPhim());

        // Nút Nhân viên
        Box b2 = Box.createHorizontalBox();
        btnNhanVien = createButton(b2, "Quản lý nhân viên");
        btnNhanVien.addActionListener(e -> showNhanVien());

        // Nút Suất chiếu
        Box b3 = Box.createHorizontalBox();
        btnSuatChieu = createButton(b3, "Quản lý suất chiếu");
        btnSuatChieu.addActionListener(e -> showSuatChieu());

        add(pnlWest, BorderLayout.WEST);

        // ===== CENTER =====
        pnlCenter = new JPanel();
        pnlCenter.setBackground(new Color(0xF5F5DC));
        pnlCenter.setLayout(new BorderLayout());
        add(pnlCenter, BorderLayout.CENTER);

        // Hiển thị Phim mặc định
        showPhim();

        // Frame
        setTitle("CINEMA");
        setResizable(true);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ===== HIỂN THỊ PANEL =====
    private void showPhim() {
        lblNorth.setText("QUẢN LÝ PHIM");
        resetButtonColor();
        btnPhim.setBackground(new Color(34, 139, 34));
        Phim_GUI pnlPhim = new Phim_GUI();
        updateCenterPanel(pnlPhim.getContentPane());
    }

    private void showNhanVien() {
        lblNorth.setText("QUẢN LÝ NHÂN VIÊN");
        resetButtonColor();
        btnNhanVien.setBackground(new Color(34, 139, 34));
        NhanVien_GUI pnlNV = new NhanVien_GUI();
        updateCenterPanel(pnlNV.getContentPane());
    }

    private void showSuatChieu() {
        lblNorth.setText("QUẢN LÝ SUẤT CHIẾU");
        resetButtonColor();
        btnSuatChieu.setBackground(new Color(34, 139, 34));
        SuatChieu_GUI pnlSC = new SuatChieu_GUI();
        updateCenterPanel(pnlSC);
    }

    // ===== RESET MÀU NÚT =====
    private void resetButtonColor() {
        Color normalColor = new Color(0xF5F5DC);
        btnPhim.setBackground(normalColor);
        btnNhanVien.setBackground(normalColor);
        btnSuatChieu.setBackground(normalColor);
    }

    // ===== CẬP NHẬT PANEL TRUNG TÂM =====
    private void updateCenterPanel(Component panel) {
        pnlCenter.removeAll();
        pnlCenter.add(panel, BorderLayout.CENTER);
        pnlCenter.revalidate();
        pnlCenter.repaint();
    }

    // ===== TẠO NÚT =====
    private JButton createButton(Box box, String nameBtn) {
        Color defaultColor = new Color(0xF5F5DC);
        Color hoverColor = new Color(0xF5F5F5);
        Color textColor = Color.BLACK;
        Color hoverTextColor = Color.RED;

        JButton btn = new JButton(nameBtn) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setForeground(textColor);
        btn.setBackground(defaultColor);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(hoverTextColor);
                btn.setBackground(hoverColor);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(textColor);
                btn.setBackground(defaultColor);
            }
        });

        try {
            String iconPath = "/img/" + nameBtn.toLowerCase().replace(" ", "_") + ".png";
            URL imgUrl = getClass().getResource(iconPath);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                icon = new ImageIcon(icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
                btn.setIcon(icon);
                btn.setHorizontalAlignment(SwingConstants.LEFT);
                btn.setIconTextGap(15);
            }
        } catch (Exception e) {
            btn.setHorizontalAlignment(SwingConstants.CENTER);
        }

        box.add(btn);
        pnlWest.add(box);
        pnlWest.add(Box.createVerticalStrut(10));
        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Main_GUI();
    }
}
