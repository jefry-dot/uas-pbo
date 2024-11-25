package aplikasi_personal_finance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginForm() {
        frame = new JFrame("Login Form");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 1));
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Personal Finance Tracker - Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        frame.add(inputPanel);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginActionListener());
        frame.add(loginButton);

        frame.setVisible(true);
    }

    // Inner class to handle login
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                frame.dispose();
                // Lanjutkan ke aplikasi utama
                new ExpenseAndIncomeTrackerApp();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to authenticate user
  // LoginForm.java - Otentikasi user
private boolean authenticateUser(String username, String password) {
    String query = "SELECT user_id, password FROM users WHERE username = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement ps = connection.prepareStatement(query)) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String dbPassword = rs.getString("password");
            int userId = rs.getInt("user_id");

            if (dbPassword.equals(password)) { // Ganti dengan hashing jika perlu
                // Simpan user_id ke sesi
                SessionManager.setCurrentUserId(userId);
                return true;
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false;
}


    public static void main(String[] args) {
        new LoginForm();
    }
}

