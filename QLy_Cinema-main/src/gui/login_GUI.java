package gui;

import entity.NhanVien;
import collection.Login_Collection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class login_GUI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    // Thêm service
    private final Login_Collection Login = new Login_Collection();

    public login_GUI() {
        setTitle("Đăng nhập hệ thống");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== PANEL TRÁI =====
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 0, 0));
        leftPanel.setPreferredSize(new Dimension(300, 400));
        leftPanel.setLayout(new BorderLayout());
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/logo_Cinema.png"));
            Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(img), SwingConstants.CENTER);
            leftPanel.add(lblLogo, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel lblFallback = new JLabel("Không tải được logo", SwingConstants.CENTER);
            lblFallback.setForeground(Color.WHITE);
            lblFallback.setFont(new Font("Arial", Font.BOLD, 24));
            leftPanel.add(lblFallback, BorderLayout.CENTER);
        }

        // ===== PANEL PHẢI =====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);

        JLabel lblTitle = new JLabel("Đăng nhập");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setBounds(180, 20, 200, 40);
        rightPanel.add(lblTitle);

        JLabel lblUsername = new JLabel("Email / Mã NV:");
        lblUsername.setBounds(50, 100, 120, 30);
        rightPanel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(180, 100, 200, 30);
        rightPanel.add(txtUsername);

        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setBounds(50, 160, 120, 30);
        rightPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(180, 160, 200, 30);
        rightPanel.add(txtPassword);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(180, 220, 200, 35);
        btnLogin.addActionListener(this::xuLyDangNhap);
        rightPanel.add(btnLogin);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // ===== XỬ LÝ ĐĂNG NHẬP (sửa lại để dùng DB) =====
    private void xuLyDangNhap(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Email/Mã NV và Mật khẩu!");
            return;
        }

        try {
            NhanVien nv = Login.login(username, password);
            if (nv == null) {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
                return;
            }

            // => Đăng nhập thành công
            JOptionPane.showMessageDialog(this, "Xin chào " + nv.getTenNhanVien() + " (" + nv.getMaNhanVien() + ")");
            // Gợi ý: truyền NhanVien vào Main_GUI
            new Main_GUI(); // Bạn sửa Main_GUI nhận NhanVien trong constructor
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Có lỗi khi đăng nhập. Vui lòng thử lại!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(login_GUI::new);
    }
}