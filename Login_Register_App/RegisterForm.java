import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class RegisterForm extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton, backButton;

    public RegisterForm() {
        setTitle("Sign Up");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 30, 120, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(160, 30, 180, 25);
        add(usernameField);

        // Email Label and Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 70, 120, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 70, 180, 25);
        add(emailField);

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 110, 120, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 110, 180, 25);
        add(passwordField);

        // Confirm Password Label and Field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(30, 150, 130, 25);
        add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(160, 150, 180, 25);
        add(confirmPasswordField);

        // Register Button
        registerButton = new JButton("Sign Up");
        registerButton.setBounds(50, 210, 120, 30);
        registerButton.addActionListener(this);
        add(registerButton);

        // Back Button
        backButton = new JButton("Back to Sign In");
        backButton.setBounds(210, 210, 150, 30);
        backButton.addActionListener(this);
        add(backButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            registerUser();
        } else if (e.getSource() == backButton) {
            this.dispose();
            new Sign_In_Sign_Out();  // এখানে আপনার লগইন ফ্রেমের constructor কল হবে
        }
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
            this.dispose();
            new Sign_In_Sign_Out();  // আবার লগইন উইন্ডো খুলবে
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new RegisterForm();
    }
}












