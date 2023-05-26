import color_quantization_algorithms.KMeansQuantizer;
import color_quantization_algorithms.MedianCutColorQuantization;
import color_quantization_algorithms.UniformQuantization;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ImageGUI extends JFrame {
    private JLabel imageLabel;
    private JButton uniformButton, KmeansButton, MedianCutButton, loadImageButton, restoreOriginal, saveImageButton, saveIndexedImageButton, compareButton, compareButton2,
            colorPaletteButton, colorHistogramButton;
    int originalImageSize, kMeanImageSize, uniformImageSize, medianCutImageSize;
    JFileChooser fileChooser = new JFileChooser(image_route.image_route);

    BufferedImage currentImage;
    JLabel kMeanLabel = new JLabel("clusters number");
    JLabel uniformLabel = new JLabel("colors number");
    JLabel medianLabel = new JLabel("median number");

    JLabel colorPaletteLabel = new JLabel("colors palette number");
    private BufferedImage image;
    SpinnerModel kMeansSpinnerModel, uniformSpinnerModel, MedianCutSpinnerModel;
    JSpinner kMeansSpinner, uniformSpinner, medianSpinner;
    JPanel controlPanel;
    JPanel colorPalettePanel = new JPanel(new GridLayout(0, 5));
    JFrame colorPaletteFrame = new JFrame("Color Palette");
    JFrame colorHistogramFrame = new JFrame("Color Histogram");
    long kMeanImageTime = 0, uniformImageTime = 0, medianCutImageTime = 0;


    public ImageGUI() {

        colorPaletteFrame.setSize(300, 200);

        // Set up the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Image GUI");

        // Create the components
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Create the control panel
        controlPanel = new JPanel();
        controlPanel.setBackground(Color.lightGray);
        controlPanel.setPreferredSize(new Dimension(getWidth(), 65));


        // initialize buttons
        init_UniformButton();
        init_UniformSpinner();
        init_KmeansButton();
        init_KmeansSpinner();
        init_MedianButton();init_MedianSpinner();

        init_loadImageButton();
        init_findSimilarImagesButton();
        init_saveImageButton();
        init_saveIndexedImageButton();
        init_CompareButton();
        init_CompareButton2();
        init_ColorPaletteButton();
        init_ColorHistogramButton();
        init_restoreOriginalImageButton();

        // Add the components to the frame
        add(imageLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(colorPalettePanel, BorderLayout.NORTH);
    }

    public void init_UniformButton() {
        uniformButton = new JButton("Uniform");
        uniformButton.addActionListener(e -> uniform());
        controlPanel.add(uniformButton);
    }

    public void init_ColorPaletteButton() {
        colorPaletteButton = new JButton("show color palette");
        colorPaletteButton.addActionListener(e -> showColorPalette());
        controlPanel.add(colorPaletteButton);
    }

    public void init_ColorHistogramButton() {
        colorHistogramButton = new JButton("show color histogram");
        colorHistogramButton.addActionListener(e -> showColorHistogram());
        controlPanel.add(colorHistogramButton);
    }

    public void init_CompareButton() {
        compareButton = new JButton("compare algorithms");
        compareButton.addActionListener(e -> compareAlgorithms());
        //controlPanel.add(compareButton);
    }

    public void init_CompareButton2() {
        compareButton2 = new JButton("compare algorithms 2");
        compareButton2.addActionListener(e -> compareAlgorithms2());
        controlPanel.add(compareButton2);
    }

    public void init_loadImageButton() {
        loadImageButton = new JButton("load image");
        loadImageButton.addActionListener(e -> loadImage());
        controlPanel.add(loadImageButton);
    }

    public void init_findSimilarImagesButton() {
        JButton findSimilarImagesButton;
        findSimilarImagesButton = new JButton("find similar images");
        findSimilarImagesButton.addActionListener(e -> findSimilarImages());
        controlPanel.add(findSimilarImagesButton);
    }

    public void init_saveImageButton() {
        saveImageButton = new JButton("save image");
        saveImageButton.addActionListener(e -> saveImage());
        controlPanel.add(saveImageButton);
    }

    public void init_saveIndexedImageButton() {
        saveIndexedImageButton = new JButton("save as indexed image");
        saveIndexedImageButton.addActionListener(e -> saveIndexedImage());
        controlPanel.add(saveIndexedImageButton);
    }

    public void indexed(indexed_image i) {
        imageLabel.setIcon(new ImageIcon(i.constructed_image));
    }

    public void init_KmeansButton() {
        KmeansButton = new JButton("K_Means");
        KmeansButton.addActionListener(e -> kMean());
        controlPanel.add(KmeansButton);
    }

    public void init_MedianButton() {
        MedianCutButton = new JButton("Median Cut");
        MedianCutButton.addActionListener(e -> median_cut());
        controlPanel.add(MedianCutButton);
    }


    public void init_restoreOriginalImageButton() {
        restoreOriginal = new JButton("restore original");
        restoreOriginal.addActionListener(e -> restoreOriginal());
        controlPanel.add(restoreOriginal);
    }

    public void init_KmeansSpinner() {
        kMeansSpinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
        kMeansSpinner = new JSpinner(kMeansSpinnerModel);
        controlPanel.add(kMeanLabel);
        controlPanel.add(kMeansSpinner);
    }

    public void init_UniformSpinner() {

        uniformSpinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
        uniformSpinner = new JSpinner(uniformSpinnerModel);
        controlPanel.add(uniformLabel);
        controlPanel.add(uniformSpinner);
    }

    public void init_MedianSpinner() {

        MedianCutSpinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
        medianSpinner = new JSpinner(MedianCutSpinnerModel);
        controlPanel.add(medianLabel);
        controlPanel.add(medianSpinner);
    }

    public void loadImage() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                image = ImageIO.read(fileChooser.getSelectedFile());
                imageLabel.setIcon(new ImageIcon(image));
                originalImageSize = getImageSize(image);
                currentImage = image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public double[] image_to_vector(File file) throws IOException {
        image = ImageIO.read(file);
        originalImageSize = getImageSize(image);
        currentImage = image;
        ColorPalette colorPalette = new ColorPalette();
        List<Color> palette = colorPalette.createColorPalette(currentImage, 10);
        return PaletteVector.toLabVector(palette);
    }

    public void findSimilarImages() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                double[] lab_paletteVector1 = image_to_vector(fileChooser.getSelectedFile());
                imageLabel.setIcon(new ImageIcon(image));
                File folder = new File(image_route.indexed_image_route);
                for (File file : Objects.requireNonNull(folder.listFiles())) {
                    if (file.isFile()) {
                        double[] lab_paletteVector2 = image_to_vector(file);
                       // System.out.printf(file.getName() + ":  %.2f\n", PaletteVector.computeCosineSimilarity(lab_paletteVector1, lab_paletteVector2));
                       // System.out.printf(file.getName() + ":  %.2f\n", PaletteVector.euclideanDistance(lab_paletteVector1, lab_paletteVector2));

                        if (PaletteVector.computeCosineSimilarity(lab_paletteVector1, lab_paletteVector2) >0.75) {
                               System.out.println(file.getName());
                               System.out.println("--------------------------");
                            try {
                                ImageIO.write(currentImage, "png", new File(image_route.output1 + file.getName()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void searchByColor() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            //TODO rita will create a method that searches for a collection that contains a specific color

        }
    }

    public void saveImage() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                ImageIO.write(currentImage, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveIndexedImage() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                indexed_image indexed = new indexed_image(currentImage);
                ImageIO.write(indexed.constructed_image, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void restoreOriginal() {

        fileChooser.setSelectedFile(new File("original.png"));
        currentImage = image;
        imageLabel.setIcon(new ImageIcon(image));

    }

    private void uniform() {
        fileChooser.setSelectedFile(new File("uniform.png"));
        long startTime = System.nanoTime();
        BufferedImage quantizedImage = UniformQuantization.quantize(image, (int) uniformSpinnerModel.getValue());
        long endTime = System.nanoTime();
        uniformImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
        uniformImageSize = getImageSize(quantizedImage);

    }

    private void kMean() {
        fileChooser.setSelectedFile(new File("kMean.png"));
        long startTime = System.nanoTime();
        BufferedImage quantizedImage = KMeansQuantizer.quantize(image, (int) kMeansSpinnerModel.getValue());
        long endTime = System.nanoTime();
        kMeanImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
        kMeanImageSize = getImageSize(quantizedImage);
    }


    private void median_cut() {

        fileChooser.setSelectedFile(new File("median_cut.png"));
        long startTime = System.nanoTime();

        int height = image.getHeight();
        int width = image.getWidth();
        int[][] flattenedImgArray = new int[height * width][5];

        int index = 0;
        for (int rIndex = 0; rIndex < height; rIndex++) {
            for (int cIndex = 0; cIndex < width; cIndex++) {
                int rgb = image.getRGB(cIndex, rIndex);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                flattenedImgArray[index] = new int[]{r, g, b, rIndex, cIndex};
                index++;
            }
        }

        BufferedImage quantizedImage = MedianCutColorQuantization.splitIntoBuckets(image, flattenedImgArray,(int) medianSpinner.getValue());

        long endTime = System.nanoTime();
        medianCutImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
        medianCutImageSize = getImageSize(quantizedImage);
    }

    private Integer getImageSize(BufferedImage quantizedImage) {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        try {
            ImageIO.write(quantizedImage, "png", tmp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            tmp.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tmp.size() / 1024;

    }

    private void compareAlgorithms() {
        kMean();
        uniform();
        restoreOriginal();
        int sizeDifference = Math.abs(kMeanImageSize - uniformImageSize);
        double timeDifference = Math.abs(kMeanImageTime - uniformImageTime);
        timeDifference = (timeDifference / 1000000000);

        String theBetterAlgorithmInSize = "K Means", theWorseAlgorithmInSize = "Uniform";
        if (kMeanImageSize > uniformImageSize) {
            theBetterAlgorithmInSize = "Uniform";
            theWorseAlgorithmInSize = "K Means";
        }

        String theBetterAlgorithmInTime = "K Means", theWorseAlgorithmInTime = "Uniform";
        if (kMeanImageTime > uniformImageTime) {
            theBetterAlgorithmInTime = "Uniform";
            theWorseAlgorithmInTime = "K Means";
        }

        double kmeantime = ((double) kMeanImageTime / 1000000000);
        double uniformtime = ((double) uniformImageTime / 1000000000);
        JOptionPane.showMessageDialog
                (null, "the size of the original image is" +
                        " " + originalImageSize + " kb" + "\n" + "the size of quantized image that use K_Means algorithm is"
                        + " " + kMeanImageSize + " kb" + "\n" + "the size of quantized image that use Uniform algorithm is"
                        + " " + uniformImageSize + " kb" + "\n"
                        + " " + "the time taken to execute K_Means algorithm is"
                        + " " + (double) Math.round(kmeantime * 100) / 100 + " s" + "\n" + "the time taken to execute Uniform algorithm is"
                        + " " + (double) Math.round(uniformtime * 100) / 100 + " s" + "\n\n"
                        + theBetterAlgorithmInSize + " is  better than " + theWorseAlgorithmInSize + " in size by " + sizeDifference + " kb\n"
                        + theBetterAlgorithmInTime + " is  better than " + theWorseAlgorithmInTime + " in time by " + (double) Math.round(timeDifference * 100) / 100 + " s");

    }

    private void compareAlgorithms2() {
        kMean();
        uniform();
        median_cut();
        restoreOriginal();

        int sizeDifferenceKMeanUniform = Math.abs(kMeanImageSize - uniformImageSize);
        int sizeDifferenceKMeanMedianCut = Math.abs(kMeanImageSize - medianCutImageSize);
        int sizeDifferenceUniformMedianCut = Math.abs(uniformImageSize - medianCutImageSize);

        double timeDifferenceKMeanUniform = Math.abs(kMeanImageTime - uniformImageTime) / 1000000000.0;
        double timeDifferenceKMeanMedianCut = Math.abs(kMeanImageTime - medianCutImageTime) / 1000000000.0;
        double timeDifferenceUniformMedianCut = Math.abs(uniformImageTime - medianCutImageTime) / 1000000000.0;

        String bestAlgorithmInSize = "";
        String bestAlgorithmInTime = "";

        if (kMeanImageSize <= uniformImageSize && kMeanImageSize <= medianCutImageSize) {
            bestAlgorithmInSize = "K Means";
        } else if (uniformImageSize <= kMeanImageSize && uniformImageSize <= medianCutImageSize) {
            bestAlgorithmInSize = "Uniform";
        } else if (medianCutImageSize <= kMeanImageSize && medianCutImageSize <= uniformImageSize) {
            bestAlgorithmInSize = "Median Cut";
        }

        if (kMeanImageTime <= uniformImageTime && kMeanImageTime <= medianCutImageTime) {
            bestAlgorithmInTime = "K Means";
        } else if (uniformImageTime <= kMeanImageTime && uniformImageTime <= medianCutImageTime) {
            bestAlgorithmInTime = "Uniform";
        } else if (medianCutImageTime <= kMeanImageTime && medianCutImageTime <= uniformImageTime) {
            bestAlgorithmInTime = "Median Cut";
        }

        double kMeanTimeInSeconds = (double) kMeanImageTime / 1000000000.0;
        double uniformTimeInSeconds = (double) uniformImageTime / 1000000000.0;
        double medianCutTimeInSeconds = (double) medianCutImageTime / 1000000000.0;

        String message = "Comparison Results:\n\n";
        message += "Original Image Size: " + originalImageSize + " kb\n\n";
        message += "K Means Algorithm:\n";
        message += "- Quantized Image Size: " + kMeanImageSize + " kb\n";
        message += "- Execution Time: " + kMeanTimeInSeconds + " s\n\n";
        message += "Uniform Algorithm:\n";
        message += "- Quantized Image Size: " + uniformImageSize + " kb\n";
        message += "- Execution Time: " + uniformTimeInSeconds + " s\n\n";
        message += "Median Cut Algorithm:\n";
        message += "- Quantized Image Size: " + medianCutImageSize + " kb\n";
        message += "- Execution Time: " + medianCutTimeInSeconds + " s\n\n";
        message += "Size Comparison:\n";
        message += "- The best algorithm in terms of size is: " + bestAlgorithmInSize + "\n\n";
        message += "Execution Time Comparison:\n";
        message += "- The best algorithm in terms of execution time is: " + bestAlgorithmInTime + "\n";

        JOptionPane.showMessageDialog(null, message);
    }


    private void showColorPalette() {
        // Clear the color palette panel.
        colorPalettePanel.removeAll();

        // Clear the content pane of the color palette frame.
        colorPaletteFrame.getContentPane().removeAll();
        ColorPalette colorPalette = new ColorPalette();

        List<Color> palette = colorPalette.createColorPalette(currentImage, 10);

        for (Color color : palette) {

            JLabel label = new JLabel();

            //the label will fill its entire background with its background color.
            // If the label were not opaque, it would be transparent
            // and you would be able to see through it to the background behind it
            label.setOpaque(true);

            // Set the label's background to the current color.
            label.setBackground(color);

            // Add the label to the color palette panel.
            colorPalettePanel.add(label);
        }

        // Add the color palette panel to the frame.
        colorPaletteFrame.getContentPane().add(colorPalettePanel);

        colorPaletteFrame.setVisible(true);
    }

    private void showColorHistogram() {
        // Clear the color palette panel.
        colorPalettePanel.removeAll();

        // Clear the content pane of the color palette frame.
        colorPaletteFrame.getContentPane().removeAll();

        // Clear the content pane of the color histogram frame.
        colorHistogramFrame.getContentPane().removeAll();

        ColorPalette colorPalette = new ColorPalette();

        List<Color> palette = colorPalette.createColorPalette(currentImage, 10);

        // Create a new color histogram panel.
        ColorHistogram histogramPanel = new ColorHistogram(palette);

        histogramPanel.createColorHistogram(currentImage);

        colorHistogramFrame.setTitle("Color Histogram");

        // Add the color histogram panel to the color histogram frame.
        colorHistogramFrame.add(histogramPanel);

        colorHistogramFrame.pack();

        // Set the location of the color histogram frame to the center of the screen.
        colorHistogramFrame.setLocationRelativeTo(null);

        colorHistogramFrame.setVisible(true);
    }

}