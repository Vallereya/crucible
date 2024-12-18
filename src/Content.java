import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class Content extends JPanel {

    private String storedUsername = "";
    private String storedPassword = "";

    public Content() {
        loadCredentialsFromFile("../server/modules/api/auth/auth.md");
        // Set the default background color.
        // Centers content using GridBagLayout.
        setLayout(new GridBagLayout());
        setBackground(Color.LIGHT_GRAY);

        // The UI components.
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");

        // Layout constraints for positioning the components.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Adding the components to panel.
        add(titleLabel, gbc);

        gbc.gridy++;
        add(userLabel, gbc);

        gbc.gridx++;
        add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(passLabel, gbc);

        gbc.gridx++;
        add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        // Add the button functionality.
        loginButton.addActionListener((ActionEvent e) -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            if (username.equals(storedUsername) && password.equals(storedPassword)) {
                JOptionPane.showMessageDialog(null, "Login Successful!");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // TEMP (For Testing Purposes Only).
    // Will reads username and password from a API folder (auth.md).
    // Expected file is format:
    // "username, password"
    private void loadCredentialsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the first line.
            String line = reader.readLine();
            if (line != null) {
                // Split by comma and optional whitespace.
                String[] parts = line.split(",\\s*");
                if (parts.length == 2) {
                    storedUsername = parts[0].trim();
                    storedPassword = parts[1].trim();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading credentials file: " + e.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
