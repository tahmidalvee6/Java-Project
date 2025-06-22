import java.awt.*;
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

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(usernameField, gbc);

        
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(emailField, gbc);

        
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(passwordField, gbc);

        
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(confirmPasswordLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(confirmPasswordField, gbc);

       
        JButton registerButton = new JButton("Sign Up");
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(registerButton, gbc);

        JButton backButton = new JButton("Back to Sign In");
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(backButton, gbc);

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
            
            String checkUsernameSql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkUsernameStmt = conn.prepareStatement(checkUsernameSql);
            checkUsernameStmt.setString(1, username);
            ResultSet usernameRs = checkUsernameStmt.executeQuery();
            if (usernameRs.next()) {
                JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            String checkEmailSql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql);
            checkEmailStmt.setString(1, email);
            ResultSet emailRs = checkEmailStmt.executeQuery();
            if (emailRs.next()) {
                JOptionPane.showMessageDialog(this, "Email already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
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
