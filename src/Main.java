import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // Create a JFrame instance for a window.
        JFrame window = new JFrame("Crucible");

        // Set the default size of the window (width, height).
        window.setSize(800, 600);

        // Set the window icon.
        // (Using this icon using for Testing Purposes Only)
        ImageIcon icon = new ImageIcon("assets/icons/icon.png");
        Image image = icon.getImage();
        window.setIconImage(image);

        // For adding content into the window.
        window.add(new Content());

        // Specify what happens when the window is closed.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set window location to the center of the screen.
        window.setLocationRelativeTo(null);

        // Make the window itself visible.
        window.setVisible(true);
    }
}