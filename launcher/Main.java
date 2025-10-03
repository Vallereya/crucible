import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.*;

public class Main {
    private static Process serverProcess;
    private static Process clientProcess;
     // This will wait for 5 seconds for server startup.
    private static final int SERVER_START_WAIT_TIME = 5000;
    
    public static void main(String[] args) {
        JFrame window = new JFrame("Crucible Launcher");
        window.setSize(800, 600);
        
        ImageIcon icon = new ImageIcon("assets/icons/icon.png");
        Image image = icon.getImage();
        window.setIconImage(image);
        
        window.add(new Content());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stopProcesses();
        }));

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    
    public static void launchGame() {
        try {
            // Get the current working directory and parent directory.
            String currentDir = System.getProperty("user.dir");
            File parentDir = new File(currentDir).getParentFile();
            
            // Launch server first.
            launchServer(parentDir);
            
            // Wait for server to start.
            System.out.println("Waiting for server to initialize...");
            Thread.sleep(SERVER_START_WAIT_TIME);
            
            // Then launch the client.
            launchClient(parentDir);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error launching game: " + e.getMessage() + "\nCheck console for details.",
                "Launch Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void launchServer(File parentDir) throws IOException {
        System.out.println("Attempting to launch server...");
        
        File serverDir = new File(parentDir, "server");
        File serverScript = new File(serverDir, "server.cr");
        
        if (!serverScript.exists()) {
            throw new IOException("server.cr not found at: " + serverScript.getAbsolutePath());
        }
        
        ProcessBuilder serverBuilder = new ProcessBuilder(
            "crystal",
            "run",
            "server.cr"
        );
        
        serverBuilder.directory(serverDir);
        serverBuilder.redirectErrorStream(true);
        
        serverProcess = serverBuilder.start();
        
        // Read server output in a separate thread.
        new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(serverProcess.getInputStream())
                );
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Server output: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        
        // Check if server process died immediately.
        try {
            // Wait a bit to check if process stays alive.
            Thread.sleep(1000);
            if (!isProcessAlive(serverProcess)) {
                throw new IOException("Server process failed to start properly");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private static void launchClient(File parentDir) throws IOException {
        System.out.println("Attempting to launch client...");
        
        File clientDir = new File(parentDir, "client");
        File clientFile = new File(clientDir, "Client.class");
        
        if (!clientFile.exists()) {
            throw new IOException("Client.class not found at: " + clientFile.getAbsolutePath());
        }
        
        ProcessBuilder clientBuilder = new ProcessBuilder(
            "java",
            "Client"
        );
        
        clientBuilder.directory(clientDir);
        clientBuilder.redirectErrorStream(true);
        
        clientProcess = clientBuilder.start();
        
        // Read client output in separate thread.
        new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientProcess.getInputStream())
                );
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Client output: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private static boolean isProcessAlive(Process process) {
        try {
            process.exitValue();
            return false;
        } catch (IllegalThreadStateException e) {
            return true;
        }
    }
    
    private static void stopProcesses() {
        System.out.println("Stopping processes...");
        
        if (clientProcess != null) {
            clientProcess.destroy();
            System.out.println("Client process terminated");
        }
        
        if (serverProcess != null) {
            serverProcess.destroy();
            System.out.println("Server process terminated");
        }
    }
}