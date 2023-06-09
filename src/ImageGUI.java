import color_quantization_algorithms.*;

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
    private final String formatName = "png";

    public String getFormatName() {
        return formatName;
    }

    private final JLabel imageLabel;
    private JButton MedianCutButton;
    private JButton restoreOriginal;
    int originalImageSize, kMeanImageSize, uniformImageSize, medianCutImageSize, nearestColorImageSize;
    JFileChooser fileChooser = new JFileChooser(image_route.image_route);

    BufferedImage currentImage;
    JLabel kMeanLabel = new JLabel("clusters number");
    JLabel uniformLabel = new JLabel("colors number");
    JLabel nearestLabel = new JLabel("nearest color");
    JLabel medianCutLabel = new JLabel("boxes number");

    JLabel colorPaletteLabel = new JLabel("colors palette number");
    private BufferedImage image;
    SpinnerModel medianCutSpinnerModel,kMeansSpinnerModel, uniformSpinnerModel, nearestColorSpinnerModel;
    JSpinner medianCutSpinner,kMeansSpinner, uniformSpinner, nearestSpinner;
    JPanel controlPanel;
    JPanel colorPalettePanel = new JPanel(new GridLayout(0, 5));
    JFrame colorPaletteFrame = new JFrame("Color Palette");
    JFrame colorHistogramFrame = new JFrame("Color Histogram");
    long kMeanImageTime = 0, uniformImageTime = 0, medianCutImageTime = 0, nearestColorTime = 0;


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
        init_NearestColor();
        init_NearestSpinner();
        init_MedianButton();
        Init_median_cut_Spinner();
        
        init_loadImageButton();
        init_findSimilarImagesButton();
        init_saveImageButton();
        init_saveIndexedImageButton();
        init_CompareButton();
        init_ColorPaletteButton();
        init_ColorHistogramButton();
        init_restoreOriginalImageButton();
        init_searchByColor();


        // Add the components to the frame
        add(imageLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(colorPalettePanel, BorderLayout.NORTH);
    }

    public void init_UniformButton() {
        JButton uniformButton = new JButton("Uniform");
        uniformButton.addActionListener(e -> uniform());
        controlPanel.add(uniformButton);
    }

    public void init_NearestColor() {
        JButton nearestColorButton = new JButton("Nearest Color");
        nearestColorButton.addActionListener(e -> nearestColor());
        controlPanel.add(nearestColorButton);
    }

    public void init_ColorPaletteButton() {
        JButton colorPaletteButton = new JButton("show color palette");
        colorPaletteButton.addActionListener(e -> showColorPalette());
        controlPanel.add(colorPaletteButton);
    }

    public void init_ColorHistogramButton() {
        JButton colorHistogramButton = new JButton("show color histogram");
        colorHistogramButton.addActionListener(e -> showColorHistogram());
        controlPanel.add(colorHistogramButton);
    }

    public void init_CompareButton() {
        JButton compareButton = new JButton("compare algorithms");
        compareButton.addActionListener(e -> compareAlgorithms());
        controlPanel.add(compareButton);
    }

    public void init_loadImageButton() {
        JButton loadImageButton = new JButton("load image");
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
        JButton saveImageButton = new JButton("save image");
        saveImageButton.addActionListener(e -> saveImage());
        controlPanel.add(saveImageButton);
    }

    public void init_saveIndexedImageButton() {
        JButton saveIndexedImageButton = new JButton("save as indexed image");
        saveIndexedImageButton.addActionListener(e -> saveIndexedImage());
        controlPanel.add(saveIndexedImageButton);
    }

    public void indexed(indexed_image i) {
        imageLabel.setIcon(new ImageIcon(i.constructed_image));
    }

    public void init_KmeansButton() {
        JButton kmeansButton = new JButton("K_Means");
        kmeansButton.addActionListener(e -> kMean());
        controlPanel.add(kmeansButton);
    }

    public void init_MedianButton() {
        JButton medianCutButton = new JButton("Median Cut");
        medianCutButton.addActionListener(e -> median_cut_new());
        controlPanel.add(medianCutButton);
    }


    public void init_restoreOriginalImageButton() {
        JButton restoreOriginal = new JButton("restore original");
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

    public void init_NearestSpinner() {
        nearestColorSpinnerModel = new SpinnerNumberModel(10, 1, 1024, 1);
        nearestSpinner = new JSpinner(nearestColorSpinnerModel);
        controlPanel.add(nearestLabel);
        controlPanel.add(nearestSpinner);
    }

    public void Init_median_cut_Spinner() {
        medianCutSpinnerModel = new SpinnerNumberModel(10, 2, 1024, 2);
        medianCutSpinner = new JSpinner(medianCutSpinnerModel);
        controlPanel.add(medianCutLabel);
        controlPanel.add(medianCutSpinner);
    }

    public void init_searchByColor() {
        JButton searchByColor = new JButton("search By Color");
        searchByColor.addActionListener(e -> new ColorInputGUI());
        controlPanel.add(searchByColor);


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

    public List<Color> image_to_palette(File file,int colorPaletteSize) throws IOException {
        image = ImageIO.read(file);
        originalImageSize = getImageSize(image);
        currentImage = image;
        ColorPalette colorPalette = new ColorPalette();
        return colorPalette.createColorPalette(currentImage, colorPaletteSize);
    }


    public void findSimilarImages() {
        loadImage();
        double[] lab_paletteVector1;
        try {
            lab_paletteVector1 = PicturesSimilarity.toLabVector(image_to_palette(fileChooser.getSelectedFile(),10));
            imageLabel.setIcon(new ImageIcon(image));
            File folder = new File(image_route.indexed_image_route);
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.isFile() && !file.getName().equals(".gitkeep")) {
                    double[] lab_paletteVector2 = PicturesSimilarity.toLabVector(image_to_palette(file,10));
                    // System.out.printf(file.getName() + ":  %.2f\n", PicturesSimilarity.computeCosineSimilarity(lab_paletteVector1, lab_paletteVector2));
                    // System.out.printf(file.getName() + ":  %.2f\n", PicturesSimilarity.euclideanDistance(lab_paletteVector1, lab_paletteVector2));
                    if (PicturesSimilarity.cosineSimilarity(lab_paletteVector1, lab_paletteVector2) > 0.75) {
                        System.out.println(file.getName() + "\n" + "--------------------------");
                        try {
                            ImageIO.write(currentImage, formatName, new File(image_route.output1, file.getName()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveImage() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                ImageIO.write(currentImage, formatName, file);
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
                ImageIO.write(indexed.constructed_image, formatName, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void restoreOriginal() {

        fileChooser.setSelectedFile(new File("original." + formatName));
        currentImage = image;
        imageLabel.setIcon(new ImageIcon(image));

    }

    private void uniform() {
        fileChooser.setSelectedFile(new File("uniform." + formatName));
        long startTime = System.nanoTime();
        BufferedImage quantizedImage = UniformQuantization.quantize(image, (int) uniformSpinnerModel.getValue());
        long endTime = System.nanoTime();
        uniformImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
        uniformImageSize = getImageSize(quantizedImage);

    }

    private void nearestColor() {
        ColorPalette colorPalette = new ColorPalette();
        List<Color> palette = colorPalette.createColorPalette(image, (int) nearestColorSpinnerModel.getValue());
        fileChooser.setSelectedFile(new File("nearest_color." + formatName));
        long startTime = System.nanoTime();
        NearestColorAlgorithm algorithm = new NearestColorAlgorithm(palette);
        BufferedImage nearestColorImage = algorithm.quantize(image);
        long endTime = System.nanoTime();
        nearestColorTime = endTime - startTime;
        currentImage = nearestColorImage;
        imageLabel.setIcon(new ImageIcon(nearestColorImage));
        nearestColorImageSize = getImageSize(nearestColorImage);

    }

    private void kMean() {
        fileChooser.setSelectedFile(new File("kMean." + formatName));
        long startTime = System.nanoTime();
        BufferedImage quantizedImage = KMeansQuantizer.quantize(image, (int) kMeansSpinnerModel.getValue());
        long endTime = System.nanoTime();
        kMeanImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
        kMeanImageSize = getImageSize(quantizedImage);
    }

    private void median_cut_new() {

        fileChooser.setSelectedFile(new File("median_cut."+formatName));
        long startTime = System.nanoTime();

        BufferedImage quantizedImage = MediaCutNew.quantizeImage(image,(int) medianCutSpinnerModel.getValue());

        long endTime = System.nanoTime();
        medianCutImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
        medianCutImageSize = getImageSize(quantizedImage);

    }


    private void median_cut() {

        fileChooser.setSelectedFile(new File("median_cut." + formatName));
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

        BufferedImage quantizedImage = MedianCutColorQuantization.splitIntoBuckets(image, flattenedImgArray, 2);

        long endTime = System.nanoTime();
        medianCutImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setIcon(new ImageIcon(quantizedImage));
        medianCutImageSize = getImageSize(quantizedImage);
    }

    private Integer getImageSize(BufferedImage quantizedImage) {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        try {
            ImageIO.write(quantizedImage, formatName, tmp);
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
        nearestColor();
        median_cut_new();
        restoreOriginal();

        String bestAlgorithmInSize = "";
        String bestAlgorithmInTime = "";

        double minimumSize = Math.min(Math.min(Math.min(kMeanImageSize,medianCutImageSize),uniformImageSize),nearestColorImageSize);
        double minimumTime = Math.min(Math.min(Math.min(kMeanImageTime,medianCutImageTime),uniformImageTime),nearestColorTime);


        if (minimumSize == kMeanImageSize) {
            bestAlgorithmInSize = "K Means";
        }
        else if (minimumSize == uniformImageSize) {
            bestAlgorithmInSize = "Uniform";
        }
        else if (minimumSize == medianCutImageSize) {
            bestAlgorithmInSize = "Median Cut";
        }
        else {
            bestAlgorithmInSize = "Nearest Color";
        }


        if (minimumTime == kMeanImageTime) {
            bestAlgorithmInTime = "K Means";
        }
        else if (minimumTime == uniformImageTime) {
            bestAlgorithmInTime = "Uniform";
        }
        else if (minimumTime == medianCutImageTime) {
            bestAlgorithmInTime = "Median Cut";
        } else {
            bestAlgorithmInTime = "Nearest Color";
        }

        double kMeanTimeInSeconds = (double) kMeanImageTime / 1000000000.0;
        double uniformTimeInSeconds = (double) uniformImageTime / 1000000000.0;
        double nearestColorTimeInSeconds = (double) nearestColorTime / 1000000000.0;
        double medianCutTimeInSeconds = (double) medianCutImageTime / 1000000000.0;

        String message = "Comparison Results:\n\n";
        message += "Original Image Size: " + originalImageSize + " kb\n\n";
        message += "K Means Algorithm:\n";
        message += "- Quantized Image Size: " + kMeanImageSize + " kb\n";
        message += "- Execution Time: " + kMeanTimeInSeconds + " s\n\n";
        message += "Median Cut Algorithm:\n";
        message += "- Quantized Image Size: " + medianCutImageSize + " kb\n";
        message += "- Execution Time: " + medianCutTimeInSeconds + " s\n\n";
        message += "Uniform Algorithm:\n";
        message += "- Quantized Image Size: " + uniformImageSize + " kb\n";
        message += "- Execution Time: " + uniformTimeInSeconds + " s\n\n";
        message += "Nearest Color Algorithm:\n";
        message += "- Quantized Image Size: " + nearestColorImageSize + " kb\n";
        message += "- Execution Time: " + nearestColorTimeInSeconds + " s\n\n";
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
            // If the label were not opaque, it would be transparent,
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
        //Choose most 10 colors to appear in the color histogram
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