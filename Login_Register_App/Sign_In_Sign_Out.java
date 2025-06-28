import java.sql.*;
import javax.swing.*;

public class Sign_In_Sign_Out {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createLoginWindow());
    }

    public static void createLoginWindow() {
        JFrame loginFrame = new JFrame("Sign In");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 250);
        loginFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);  

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 30, 100, 25);
        panel.add(usernameLabel);

        JTextField usernameField = new JTextField(20);
        usernameField.setBounds(150, 30, 180, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 70, 100, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(150, 70, 180, 25);
        panel.add(passwordField);

        JButton loginButton = new JButton("Sign In");
        loginButton.setBounds(50, 120, 120, 30);
        panel.add(loginButton);

        JButton registerButton = new JButton("Sign Up");
        registerButton.setBounds(210, 120, 120, 30);
        panel.add(registerButton);

        loginFrame.add(panel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(loginFrame, "Welcome, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(loginFrame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            loginFrame.dispose();
            new RegisterForm();
        });

        loginFrame.setVisible(true);
    }
}












