package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.*;

public class Main_GUI extends JFrame {
    private JButton btnPhim;  // Chỉ giữ nút phim
    private JButton btnNhanVien;
    private JLabel lblNorth;
    private JPanel pnlWest;
    private JPanel pnlCenter;

    public Main_GUI() {
        setFont(new Font("Arial", Font.BOLD, 13));

        JPanel pnlNorth = new JPanel();
        pnlNorth.add(lblNorth = new JLabel("TRANG CHỦ"));
        lblNorth.setFont(new Font("Arial", Font.BOLD, 20));
        lblNorth.setForeground(Color.RED);
        pnlNorth.setBackground(new Color(0x000000)); // mau den
        add(pnlNorth, BorderLayout.NORTH);

        pnlWest = new JPanel();
        pnlWest.setLayout(new BoxLayout(pnlWest, BoxLayout.Y_AXIS));
        pnlWest.setBackground(new Color(0x000000)); // xanh rieu
        pnlWest.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        Box b1;

        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/img/logo_Cinema.png"));
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
            int maxWidth = 180;
            int newWidth, newHeight;
            if (originalWidth <= maxWidth) {
                newWidth = originalWidth;
                newHeight = originalHeight;
            } else {
                newWidth = maxWidth;
                newHeight = (int) ((double) originalHeight * maxWidth / originalWidth);
            }

            Image scaledImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel lblLogo = new JLabel(scaledIcon);
            lblLogo.setAlignmentX(CENTER_ALIGNMENT);
            pnlWest.add(lblLogo);
            pnlWest.add(Box.createVerticalStrut(20));
        } catch (Exception e) {
            System.err.println("Không thể tải hình ảnh logo: " + e.getMessage());
            JLabel lblError = new JLabel("Không thể tải logo");
            lblError.setAlignmentX(CENTER_ALIGNMENT);
            pnlWest.add(lblError);
            pnlWest.add(Box.createVerticalStrut(20));
        }

        // Chỉ tạo nút phim
        b1 = Box.createHorizontalBox();
        btnPhim = createButton(b1, "Quản lý phim");
        add(pnlWest, BorderLayout.WEST);
        btnPhim.addActionListener(e -> showPhim());

        Box b2 = Box.createHorizontalBox();
        btnNhanVien = createButton(b2, "Quản lý nhân viên");
        btnNhanVien.addActionListener(e -> showNhanVien());

        // Panel trung tâm
        pnlCenter = new JPanel();
        pnlCenter.setBackground(new Color(0xF5F5DC)); // Beige nhạt
        pnlCenter.setLayout(new BorderLayout());
        add(pnlCenter, BorderLayout.CENTER);

        // Hiển thị phim mặc định
        showPhim();

        setTitle("CINEMA");
        setResizable(true);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showPhim() {
        lblNorth.setText("QUẢN LÝ PHIM");
        resetButtonColor();
        Phim_GUI pnlPhim = new Phim_GUI(); // Giả định Phim_GUI là JFrame
        // Lấy nội dung bên trong của JFrame và add vào panel
        updateCenterPanel(pnlPhim.getContentPane());
        // Đổi màu nút đang được chọn (nếu cần)
        btnPhim.setBackground(new Color(34, 139, 34));
    }
    
    private void showNhanVien() {
        lblNorth.setText("QUẢN LÝ NHÂN VIÊN");
        resetButtonColor();
        NhanVien_GUI pnlNV = new NhanVien_GUI(); // giả sử NhanVien_GUI extends JFrame
        updateCenterPanel(pnlNV.getContentPane());
        btnNhanVien.setBackground(new Color(34, 139, 34)); // đổi màu khi được chọn
    }

    private void resetButtonColor() {
        Color normalColor = new Color(0xF5F5DC); // màu default
        btnPhim.setBackground(normalColor);
        btnNhanVien.setBackground(normalColor);
    }

    
    private void updateCenterPanel(Component panel) {
        pnlCenter.removeAll();                      // Xóa giao diện cũ
        pnlCenter.add(panel, BorderLayout.CENTER);  // Thêm giao diện mới
        pnlCenter.revalidate();                     // Làm mới Layout
        pnlCenter.repaint();                        // Vẽ lại
    }

    private JButton createButton(Box box, String nameBtn) {
        Color defaultColor = new Color(0xF5F5DC); // Beige nhạt
        Color hoverColor = new Color(0xF5F5F5); // den 
        Color textColor = new Color(0xF5F5F5); // den
        Color hoverTextColor = Color.red;

        JButton btn = new JButton(nameBtn) {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (getModel().isRollover() || getModel().isPressed()) {
                        g2.setColor(hoverColor);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    }

                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };

        btn.setPreferredSize(new Dimension(200, 45));
        btn.setMinimumSize(new Dimension(200, 45));
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
