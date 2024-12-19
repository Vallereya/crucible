import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;

public class Content extends JPanel {
    private String storedUsername = "";
    private String storedPassword = "";
    private JButton playButton;
    private JPanel loginPanel;
    private JPanel gamePanel;
    private CardLayout cardLayout;

    public Content() {
        loadCredentialsFromFile("../server/modules/api/auth/auth.md");
        
        // Use CardLayout to switch between login and game panels
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        
        // Create and add login panel
        createLoginPanel();
        
        // Create and add game panel
        createGamePanel();
        
        // Show login panel first
        cardLayout.show(this, "login");
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.LIGHT_GRAY);
        
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        loginPanel.add(titleLabel, gbc);

        gbc.gridy++;
        loginPanel.add(userLabel, gbc);

        gbc.gridx++;
        loginPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(passLabel, gbc);

        gbc.gridx++;
        loginPanel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener((ActionEvent e) -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            if (username.equals(storedUsername) && password.equals(storedPassword)) {
                cardLayout.show(this, "game");
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Invalid Username or Password.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        add(loginPanel, "login");
    }
    
    private void createGamePanel() {
        gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setBackground(Color.LIGHT_GRAY);
        
        JLabel welcomeLabel = new JLabel("Welcome to Crucible!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        playButton = new JButton("Play!");
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.setPreferredSize(new Dimension(200, 50));
        
        playButton.addActionListener((ActionEvent e) -> {
            playButton.setEnabled(false);
            playButton.setText("Launching...");
            
            // Launch game in a separate thread
            new Thread(() -> {
                Main.launchGame();
            }).start();
        });
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gamePanel.add(welcomeLabel, gbc);
        
        gbc.gridy = 1;
        gamePanel.add(playButton, gbc);
        
        add(gamePanel, "game");
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
