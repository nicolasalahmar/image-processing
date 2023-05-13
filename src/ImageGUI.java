import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import color_quantization_algorithms.*;

public class ImageGUI extends JFrame {
    private JLabel imageLabel;
    private JButton grayscaleButton;
    private JButton negativeButton;
    private BufferedImage image;

    public ImageGUI() {
        // Set up the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setTitle("Image GUI");

        // Create the components
        imageLabel = new JLabel();
        grayscaleButton = new JButton("Grayscale");
        negativeButton = new JButton("K_Mean");

        // Add event listeners to the buttons
        grayscaleButton.addActionListener(e -> setGrayscale());
        negativeButton.addActionListener(e -> kMean());

        // Create the control panel
        JPanel controlPanel = new JPanel();
        controlPanel.add(grayscaleButton);
        controlPanel.add(negativeButton);

        // Add the components to the frame
        add(imageLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public void loadImage() {
        // Load the image
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                image = ImageIO.read(fileChooser.getSelectedFile());
                imageLabel.setIcon(new ImageIcon(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setGrayscale() {
        // Convert the image to grayscale
        BufferedImage grayscaleImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayscaleImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        imageLabel.setIcon(new ImageIcon(grayscaleImage));
    }

    private void kMean() {
        BufferedImage bufferedImage = KMeansQuantizer.quantize(image,24);
        imageLabel.setIcon(new ImageIcon(bufferedImage));

    }}