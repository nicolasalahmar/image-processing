import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import color_quantization_algorithms.*;

public class ImageGUI extends JFrame {
    private JLabel imageLabel;
    private JButton uniformButton, KmeansButton, loadImageButton, restoreOriginal;
    JLabel kMeanLabel = new JLabel("clusters number");
    JLabel uniformLabel = new JLabel("colors number");
    private BufferedImage image;
    SpinnerModel kMeansSpinnerModel, uniformSpinnerModel;
    JSpinner kMeansSpinner;
    JSpinner uniformSpinner;
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
        init_UniformButton();
        init_KmeansButton();
        init_KmeansSpinner();
        init_UniformSpinner();
        init_loadImageButton();
        init_restoreOriginalImageButton();

        // Add the components to the frame
        add(imageLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
    public void init_UniformButton() {
        uniformButton = new JButton("Uniform");
        uniformButton.addActionListener(e -> uniform());
        controlPanel.add(uniformButton);
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
        kMeansSpinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
        kMeansSpinner = new JSpinner(kMeansSpinnerModel);
        controlPanel.add(kMeanLabel);
        controlPanel.add(kMeansSpinner);
    }
    public void init_UniformSpinner(){
        int value=2;
        double stepSize=value*2;
        uniformSpinnerModel = new SpinnerNumberModel(0, 1, 1024, 1);
        uniformSpinner = new JSpinner(uniformSpinnerModel);
        controlPanel.add(uniformLabel);
        controlPanel.add(uniformSpinner);
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
    private void uniform()  {

        BufferedImage quantizedImage = UniformQuantization.quantize(image,(double) uniformSpinnerModel.getValue());
        imageLabel.setIcon(new ImageIcon(quantizedImage));

    }
    private void kMean() {
        BufferedImage bufferedImage = KMeansQuantizer.quantize(image, (int) kMeansSpinnerModel.getValue());
        imageLabel.setIcon(new ImageIcon(bufferedImage));
    }
}