import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import color_quantization_algorithms.*;

public class ImageGUI extends JFrame {
    private JLabel imageLabel;
    private JButton uniformButton, KmeansButton, loadImageButton, restoreOriginal,saveImageButton;
    JFileChooser fileChooser = new JFileChooser();

    BufferedImage currentImage;
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
        init_saveImageButton();
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
    public void init_saveImageButton() {
        saveImageButton = new JButton("save image");
        saveImageButton.addActionListener(e -> saveImage());
        controlPanel.add(saveImageButton);
    }
    public void init_KmeansButton() {
        KmeansButton = new JButton("K_Means");
        KmeansButton.addActionListener(e -> kMean());
        controlPanel.add(KmeansButton);
    }
    public void init_restoreOriginalImageButton() {
        restoreOriginal = new JButton("restore original");
        restoreOriginal.addActionListener(e -> restoreOriginal());
        controlPanel.add(restoreOriginal);
    }
    public void init_KmeansSpinner(){
        kMeansSpinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
        kMeansSpinner = new JSpinner(kMeansSpinnerModel);
        controlPanel.add(kMeanLabel);
        controlPanel.add(kMeansSpinner);
    }

    public void init_UniformSpinner(){

        uniformSpinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
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
    public void saveImage() {

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                ImageIO.write(currentImage,"png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void restoreOriginal()  {
        fileChooser.setSelectedFile(new File("original.png"));
        currentImage=image;
        imageLabel.setIcon(new ImageIcon(image));

    }
    private void uniform()  {
        fileChooser.setSelectedFile(new File("uniform.png"));
        BufferedImage quantizedImage = UniformQuantization.quantize(image,(int) uniformSpinnerModel.getValue());
        currentImage=quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));

    }
    private void kMean() {
        fileChooser.setSelectedFile(new File("kMean.png"));
        BufferedImage quantizedImage = KMeansQuantizer.quantize(image, (int) kMeansSpinnerModel.getValue());
        currentImage=quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
    }
}