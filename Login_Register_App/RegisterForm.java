import java.sql.*;
import javax.swing.*;

public class RegisterForm extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterForm() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);  // null layout

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 120, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(160, 30, 180, 25);
        panel.add(usernameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 70, 120, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(160, 70, 180, 25);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 110, 120, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(160, 110, 180, 25);
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(30, 150, 120, 25);
        panel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBounds(160, 150, 180, 25);
        panel.add(confirmPasswordField);

        JButton registerButton = new JButton("Sign Up");
        registerButton.setBounds(50, 210, 120, 30);
        panel.add(registerButton);

        JButton backButton = new JButton("Back to Sign In");
        backButton.setBounds(210, 210, 150, 30);
        panel.add(backButton);

        add(panel);

        registerButton.addActionListener(e -> registerUser());
        backButton.addActionListener(e -> backToLogin());

        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Check if username exists
            String checkUsernameSql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkUsernameStmt = conn.prepareStatement(checkUsernameSql);
            checkUsernameStmt.setString(1, username);
            ResultSet usernameRs = checkUsernameStmt.executeQuery();
            if (usernameRs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if email exists
            String checkEmailSql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql);
            checkEmailStmt.setString(1, email);
            ResultSet emailRs = checkEmailStmt.executeQuery();
            if (emailRs.next()) {
                JOptionPane.showMessageDialog(this, "Email already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert new user
            String insertSql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, email);
            insertStmt.setString(3, password);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToLogin() {
        this.dispose();
        Sign_In_Sign_Out.createLoginWindow();
    }
}
