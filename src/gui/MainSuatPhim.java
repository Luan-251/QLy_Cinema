package gui;

import javax.swing.*;

public class MainSuatPhim {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Suất Chiếu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new SuatPhimPanel());
            frame.setVisible(true);
        });
    }
}
