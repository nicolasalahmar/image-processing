import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCropper extends JFrame {
    private BufferedImage originalImage;
    private BufferedImage croppedImage;
    private Rectangle2D cropBounds;
    private boolean isCropSelected;

    private ImagePanel imagePanel;
    private JButton cropButton;

    public ImageCropper() {
        setTitle("Image Cropper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create components
        imagePanel = new ImagePanel();
        cropButton = new JButton("Crop");
        cropButton.setEnabled(false);

        // Add components to the frame
        add(imagePanel, BorderLayout.CENTER);
        add(cropButton, BorderLayout.SOUTH);

        // Load image
        loadImage();

        // Add mouse listener to the image panel
        imagePanel.addMouseListener(new MouseAdapter() {
            private Point startPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                isCropSelected = false;
                imagePanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int x = Math.min(startPoint.x, e.getX());
                int y = Math.min(startPoint.y, e.getY());
                int width = Math.abs(startPoint.x - e.getX());
                int height = Math.abs(startPoint.y - e.getY());
                cropBounds = new Rectangle2D.Double(x, y, width, height);
                cropButton.setEnabled(true);
                isCropSelected = true;
                imagePanel.repaint();
            }
        });

        // Add action listener to the crop button
        cropButton.addActionListener(e -> cropImage());

        // Set the frame size based on the image size
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadImage() {
        try {
            // Replace "path/to/image.jpg" with the path to your image file
            originalImage = ImageIO.read(new File("/Users/ihssanabou/Desktop/anya.png"));
            imagePanel.setPreferredSize(new Dimension(originalImage.getWidth(), originalImage.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cropImage() {
        int x = (int) cropBounds.getX();
        int y = (int) cropBounds.getY();
        int width = (int) cropBounds.getWidth();
        int height = (int) cropBounds.getHeight();
        croppedImage = originalImage.getSubimage(x, y, width, height);

        // Display cropped image in a new frame
        JFrame croppedFrame = new JFrame("Cropped Image");
        JLabel croppedLabel = new JLabel(new ImageIcon(croppedImage));
        croppedFrame.getContentPane().add(croppedLabel);
        croppedFrame.pack();
        croppedFrame.setLocationRelativeTo(null);
        croppedFrame.setVisible(true);
    }

    private class ImagePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Draw the original image
            g2d.drawImage(originalImage, 0, 0, null);

            // Draw the crop box overlay
            if (isCropSelected) {
                g2d.setColor(Color.PINK);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(cropBounds);
                g2d.setColor(new Color(255, 255, 255, 128));
                g2d.fill(cropBounds);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageCropper::new);
    }
}
