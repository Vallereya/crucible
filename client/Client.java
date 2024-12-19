import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 43594;  // Matching the port in server.cr
    private Socket socket;
    private GameWindow gameWindow;
    
    public Client() {
        gameWindow = new GameWindow();
        connectToServer();
    }
    
    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server successfully!");
            
            // Start a thread to listen for server messages
            new Thread(this::listenForServerMessages).start();
            
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            JOptionPane.showMessageDialog(gameWindow, 
                "Failed to connect to server. Please ensure the server is running.",
                "Connection Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void listenForServerMessages() {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
                
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Server says: " + message);
            }
        } catch (IOException e) {
            System.err.println("Lost connection to server: " + e.getMessage());
        }
    }
    
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        // Start client on EDT
        SwingUtilities.invokeLater(() -> new Client());
    }
}

// GameWindow.java
class GameWindow extends JFrame {
    private int playerX = 100;
    private int playerY = 100;
    private static final int MOVE_SPEED = 5;
    
    public GameWindow() {
        setTitle("Crucible - Test Client");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Add game panel
        add(new GamePanel());
        
        // Add key listener for movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: playerY -= MOVE_SPEED; break;
                    case KeyEvent.VK_S: playerY += MOVE_SPEED; break;
                    case KeyEvent.VK_A: playerX -= MOVE_SPEED; break;
                    case KeyEvent.VK_D: playerX += MOVE_SPEED; break;
                }
                repaint();
            }
        });
        
        setFocusable(true);
        setVisible(true);
    }
    
    // Inner class for the game panel
    private class GamePanel extends JPanel {
        public GamePanel() {
            setBackground(Color.BLACK);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Draw game world
            g.setColor(Color.DARK_GRAY);
            for (int i = 0; i < getWidth(); i += 50) {
                for (int j = 0; j < getHeight(); j += 50) {
                    g.drawRect(i, j, 50, 50);
                }
            }
            
            // Draw player
            g.setColor(Color.GREEN);
            g.fillOval(playerX, playerY, 30, 30);
            
            // Draw instructions
            g.setColor(Color.WHITE);
            g.drawString("Use WASD to move", 10, 20);
            g.drawString("Player Position: " + playerX + ", " + playerY, 10, 40);
        }
    }
}