import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import color_quantization_algorithms.*;

public class ImageGUI extends JFrame {
    private JLabel imageLabel;
    private JButton grayscaleButton, KmeansButton, loadImageButton, restoreOriginal;
    private BufferedImage image;
    SpinnerModel spinnerModel;
    JSpinner spinner;
    JPanel controlPanel;

    public ImageGUI() {
        // Set up the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setTitle("Image GUI");

        // Create the components
        imageLabel = new JLabel();

        // Create the control panel
        controlPanel = new JPanel();
        // initialize buttons
        init_grayScaleButton();
        init_KmeansButton();
        init_KmeansSpinner();
        init_loadImageButton();
        init_restoreOriginalImageButton();

        // Add the components to the frame
        add(imageLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
    public void init_grayScaleButton() {
        grayscaleButton = new JButton("Uniform");
        grayscaleButton.addActionListener(e -> setGrayscale());
        controlPanel.add(grayscaleButton);
    }
    public void init_loadImageButton() {
        loadImageButton = new JButton("load image");
        loadImageButton.addActionListener(e -> loadImage());
        controlPanel.add(loadImageButton);
    }
    public void init_KmeansButton() {
        KmeansButton = new JButton("K_Means");
        KmeansButton.addActionListener(e -> kMean());
        controlPanel.add(KmeansButton);
    }
    public void init_restoreOriginalImageButton() {
        restoreOriginal = new JButton("restore original");
        restoreOriginal.addActionListener(e -> imageLabel.setIcon(new ImageIcon(image)));
        controlPanel.add(restoreOriginal);
    }
    public void init_KmeansSpinner(){
        spinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
        spinner = new JSpinner(spinnerModel);
        controlPanel.add(spinner);
    }
    public void loadImage() {
        // Load the image
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\Nicol\\OneDrive\\Desktop\\College\\Multimedia\\Practical\\Image Processing\\image-processing\\images");
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
    private void setGrayscale()  {

        BufferedImage quantizedImage = UniformQuantization.quantize(image, 24);
        imageLabel.setIcon(new ImageIcon(quantizedImage));

    }
    private void kMean() {
        BufferedImage bufferedImage = KMeansQuantizer.quantize(image, (int) spinnerModel.getValue());
        imageLabel.setIcon(new ImageIcon(bufferedImage));
    }
}