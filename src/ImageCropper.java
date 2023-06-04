//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.geom.Rectangle2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class ImageCropper extends JFrame {
//    private BufferedImage originalImage;
//    private BufferedImage croppedImage;
//    private Rectangle2D cropBounds;
//    private boolean isCropSelected;
//
//    private ImagePanel imagePanel;
//    private JButton cropButton;
//    private JButton resizeButton;
//
//    public ImageCropper() {
//        setTitle("Image Cropper");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//
//        // Create components
//        imagePanel = new ImagePanel();
//        cropButton = new JButton("Crop");
//        resizeButton = new JButton("Resize");
//        cropButton.setEnabled(false);
//        resizeButton.setEnabled(false);
//
//        // Add components to the frame
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(cropButton);
//        buttonPanel.add(resizeButton);
//        add(imagePanel, BorderLayout.CENTER);
//        add(buttonPanel, BorderLayout.SOUTH);
//
//        // Load image
//        loadImage();
//
//        // Add mouse listener to the image panel
//        imagePanel.addMouseListener(new CustomMouseListener());
//
//        // Add action listener to the crop button
//        cropButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                cropImage();
//            }
//        });
//
//        // Add action listener to the resize button
//        resizeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                resizeImage();
//            }
//        });
//
//        // Set the frame size based on the image size
//        pack();
//        setLocationRelativeTo(null);
//        setVisible(true);
//    }
//
//    private void loadImage() {
//        try {
//            // Replace "path/to/image.jpg" with the path to your image file
//            originalImage = ImageIO.read(new File("/Users/ihssanabou/Desktop/anya.png"));
//            imagePanel.setPreferredSize(new Dimension(originalImage.getWidth(), originalImage.getHeight()));
//            resizeButton.setEnabled(true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void cropImage() {
//        int x = (int) cropBounds.getX();
//        int y = (int) cropBounds.getY();
//        int width = (int) cropBounds.getWidth();
//        int height = (int) cropBounds.getHeight();
//        croppedImage = originalImage.getSubimage(x, y, width, height);
//
//        // Display cropped image in a new frame
//        JFrame croppedFrame = new JFrame("Cropped Image");
//        JLabel croppedLabel = new JLabel(new ImageIcon(croppedImage));
//        croppedFrame.getContentPane().add(croppedLabel);
//        croppedFrame.pack();
//        croppedFrame.setLocationRelativeTo(null);
//        croppedFrame.setVisible(true);
//    }
//
//    private void resizeImage() {
//        // Prompt for new width and height
//        String widthString = JOptionPane.showInputDialog("Enter the new width:");
//        String heightString = JOptionPane.showInputDialog("Enter the new height:");
//
//        // Parse width and height values
//        int newWidth = Integer.parseInt(widthString);
//        int newHeight = Integer.parseInt(heightString);
//
//        // Resize the image
//        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
//
//        // Display resized image in a new frame
//        JFrame resizedFrame = new JFrame("Resized Image");
//        JLabel resizedLabel = new JLabel(new ImageIcon(resizedImage));
//        resizedFrame.getContentPane().add(resizedLabel);
//        resizedFrame.pack();
//        resizedFrame.setLocationRelativeTo(null);
//        resizedFrame.setVisible(true);
//    }
//
//    private class ImagePanel extends JPanel {
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2d = (Graphics2D) g;
//
//            // Draw the original image
//            g2d.drawImage(originalImage, 0, 0, null);
//
//            // Draw the crop box overlay
//            if (isCropSelected) {
//                g2d.setColor(Color.PINK);
//                g2d.setStroke(new BasicStroke(2));
//                g2d.draw(cropBounds);
//                g2d.setColor(new Color(255, 255, 255, 128));
//                g2d.fill(cropBounds);
//            }
//        }
//    }
//
//    private class CustomMouseListener extends MouseAdapter {
//        private Point startPoint;
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//            startPoint = e.getPoint();
//            isCropSelected = false;
//            imagePanel.repaint();
//        }
//
//        @Override
//        public void mouseReleased(MouseEvent e) {
//            int x = Math.min(startPoint.x, e.getX());
//            int y = Math.min(startPoint.y, e.getY());
//            int width = Math.abs(startPoint.x - e.getX());
//            int height = Math.abs(startPoint.y - e.getY());
//            cropBounds = new Rectangle2D.Double(x, y, width, height);
//            cropButton.setEnabled(true);
//            isCropSelected = true;
//            imagePanel.repaint();
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(ImageCropper::new);
//    }
//}
