import color_quantization_algorithms.KMeansQuantizer;
import color_quantization_algorithms.MediaCutNew;
import color_quantization_algorithms.NearestColorAlgorithm;
import color_quantization_algorithms.UniformQuantization;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ImageGUI extends JFrame {
    private static String formatName;
    final ImagePanel imageLabel;
    int originalImageSize, kMeanImageSize, uniformImageSize, medianCutImageSize, nearestColorImageSize;
    JFileChooser fileChooser = new JFileChooser(image_route.image_route);
    BufferedImage currentImage;
    private BufferedImage originalImage;
    private Rectangle2D cropBounds;
    private boolean isCropSelected;
    private JButton searchByDateButton, restoreOriginal, cropButton, resizeButton, uniformButton, nearestColorButton, colorPaletteButton, colorHistogramButton, compareButton, findSimilarImagesButton, kmeansButton, saveIndexedImageButton, saveImageButton, medianCutButton;


    JLabel kMeanLabel = new JLabel("clusters number");
    JLabel uniformLabel = new JLabel("colors number");
    JLabel nearestLabel = new JLabel("nearest color");
    JLabel medianCutLabel = new JLabel("boxes number");

    SpinnerModel medianCutSpinnerModel, kMeansSpinnerModel, uniformSpinnerModel, nearestColorSpinnerModel;
    JSpinner medianCutSpinner, kMeansSpinner, uniformSpinner, nearestSpinner;
    JPanel controlPanel;
    JPanel colorPalettePanel = new JPanel(new GridLayout(0, 5));
    JFrame colorPaletteFrame = new JFrame("Color Palette");
    JFrame colorHistogramFrame = new JFrame("Color Histogram");
    long kMeanImageTime = 0, uniformImageTime = 0, medianCutImageTime = 0, nearestColorTime = 0;

    class ImagePanel extends JPanel {
//        public static void getFormatName(String name) {
//            formatName = name.split("\\.")[1];
//        }

        public static void getFormatName(String name) {
            formatName = name.split("\\.")[1];
        }

        public void setImage(BufferedImage setImage) {

            currentImage = setImage;
            repaint(); // Redraw the panel with the new image
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
//            if (currentImage != null) {
//                int x = (getWidth() - currentImage.getWidth(null)) / 2;
//                int y = (getHeight() - currentImage.getHeight(null)) / 2;
//                g.drawImage(currentImage, x, y, null);
//            }
            // Draw the original image
            g2d.drawImage(currentImage, 0, 0, null);

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

    public ImageGUI() {

        colorPaletteFrame.setSize(300, 200);

        // Set up the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Image GUI");

        // Create the components
        imageLabel = new ImagePanel();

//        imageLabel.setHorizontalAlignment(JLabel.CENTER);
//        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Create the control panel
        controlPanel = new JPanel();
        controlPanel.setBackground(Color.lightGray);
        controlPanel.setPreferredSize(new Dimension(getWidth(), 100));


        // initialize buttons
        init_UniformButton();
        init_UniformSpinner();
        init_KmeansButton();
        init_KmeansSpinner();
        init_NearestColor();
        init_NearestSpinner();
        init_MedianButton();
        median_cut_Spinner();
        ImageCropper();


        init_loadImageButton();
        init_findSimilarImagesButton();
        init_saveImageButton();
        init_saveIndexedImageButton();
        init_CompareButton();
        init_ColorPaletteButton();
        init_ColorHistogramButton();
        init_restoreOriginalImageButton();
        init_searchByColor();
        init_searchBySize();
        init_searchByDate();


        // Add the components to the frame
        add(imageLabel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        add(colorPalettePanel, BorderLayout.NORTH);

    }

    public void init_UniformButton() {
        uniformButton = new JButton("Uniform");
        uniformButton.setEnabled(false);
        uniformButton.addActionListener(e -> uniform());
        controlPanel.add(uniformButton);
    }

    public void init_NearestColor() {
        nearestColorButton = new JButton("Nearest Color");
        nearestColorButton.setEnabled(false);
        nearestColorButton.addActionListener(e -> nearestColor());
        controlPanel.add(nearestColorButton);
    }

    public void init_ColorPaletteButton() {
        colorPaletteButton = new JButton("show color palette");
        colorPaletteButton.setEnabled(false);
        colorPaletteButton.addActionListener(e -> showColorPalette());
        controlPanel.add(colorPaletteButton);
    }

    public void init_ColorHistogramButton() {
        colorHistogramButton = new JButton("show color histogram");
        colorHistogramButton.setEnabled(false);
        colorHistogramButton.addActionListener(e -> showColorHistogram());
        controlPanel.add(colorHistogramButton);
    }

    public void init_CompareButton() {
        compareButton = new JButton("compare algorithms");
        compareButton.setEnabled(false);
        compareButton.addActionListener(e -> compareAlgorithms());
        controlPanel.add(compareButton);
    }

    public void init_loadImageButton() {
        JButton loadImageButton = new JButton("load image");
        loadImageButton.addActionListener(e -> loadImage());
        controlPanel.add(loadImageButton);
    }

    public void init_findSimilarImagesButton() {
        findSimilarImagesButton = new JButton("find similar images");
        //findSimilarImagesButton.setEnabled(false);
        findSimilarImagesButton.addActionListener(e -> {
            try {
                findSimilarImages();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        controlPanel.add(findSimilarImagesButton);
    }

    public void init_saveImageButton() {
        saveImageButton = new JButton("save image");
        saveImageButton.setEnabled(false);
        saveImageButton.addActionListener(e -> saveImage());
        controlPanel.add(saveImageButton);
    }

    public void init_saveIndexedImageButton() {
        saveIndexedImageButton = new JButton("save as indexed image");
        saveIndexedImageButton.setEnabled(false);
        saveIndexedImageButton.addActionListener(e -> saveIndexedImage());
        controlPanel.add(saveIndexedImageButton);
    }

    public void init_KmeansButton() {
        kmeansButton = new JButton("K_Means");
        kmeansButton.setEnabled(false);
        kmeansButton.addActionListener(e -> kMean());
        controlPanel.add(kmeansButton);
    }

    public void init_MedianButton() {
        medianCutButton = new JButton("Median Cut");
        medianCutButton.setEnabled(false);
        medianCutButton.addActionListener(e -> median_cut_new());
        controlPanel.add(medianCutButton);
    }

    public void init_restoreOriginalImageButton() {
        restoreOriginal = new JButton("restore original");
        restoreOriginal.setEnabled(false);
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

    public void median_cut_Spinner() {
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

    public void init_searchBySize() {
        JButton searchBySize = new JButton("search By Size");
        searchBySize.addActionListener(e -> searchBySize());
        controlPanel.add(searchBySize);
    }

    public void init_searchByDate() {
        searchByDateButton = new JButton("Search By Date");
        searchByDateButton.addActionListener(e -> {
            try {
                searchByDate();
            } catch (ImageProcessingException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        controlPanel.add(searchByDateButton);
    }

    public void loadImage() {
        isCroped=false;
        int result = fileChooser.showOpenDialog(this);
        if (fileChooser.getSelectedFile() != null) {
            ImagePanel.getFormatName(fileChooser.getSelectedFile().getName());

        }

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                originalImage = ImageIO.read(fileChooser.getSelectedFile());
                imageLabel.setImage(originalImage);
                imageLabel.setPreferredSize(new Dimension(originalImage.getWidth(), originalImage.getHeight()));


                 resizeButton.setEnabled(true);
                uniformButton.setEnabled(true);
                nearestColorButton.setEnabled(true);
                colorPaletteButton.setEnabled(true);
                colorHistogramButton.setEnabled(true);
                compareButton.setEnabled(true);
                findSimilarImagesButton.setEnabled(true);
                kmeansButton.setEnabled(true);
                saveIndexedImageButton.setEnabled(true);
                saveImageButton.setEnabled(true);
                medianCutButton.setEnabled(true);
                restoreOriginal.setEnabled(true);

                originalImageSize = getImageSize(originalImage);
                currentImage = originalImage;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Color> image_to_palette(BufferedImage image, int colorPaletteSize) throws IOException {
        //originalImage = ImageIO.read(file);
        //originalImageSize = getImageSize(image);
        //currentImage = originalImage;
        ColorPalette colorPalette = new ColorPalette();
        List getColorPalette = colorPalette.createColorPalette(image, colorPaletteSize);
        //System.out.println(getColorPalette);
        return getColorPalette;
    }

    public void findSimilarImages() throws IOException {
     //   loadImage();

        //File file2 = fileChooser.getSelectedFile();

       // BufferedImage image = ImageIO.read(file2);
        BufferedImage quantizedImage = UniformQuantization.quantize(currentImage, (int) uniformSpinnerModel.getValue());
        indexed_image indexed = new indexed_image(quantizedImage);
        double[] lab_paletteVector1 = PicturesSimilarity.toLabVector(image_to_palette(indexed.constructed_image, 20));
        File resultsFolder = new File(image_route.image_route + "\\similar_images_search_results");
        String folderPath = (image_route.image_route + "\\similar_images_search_results");
        resultsFolder.mkdirs();
        JFileChooser fileChooser = new JFileChooser(image_route.image_route);
        fileChooser.setDialogTitle("Choose one or multiple folders to search in");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File[] chosenFolders = fileChooser.getSelectedFiles();

            for (File folder : chosenFolders) {
                System.out.println("Processing folder: " + folder.getName());
                ArrayList<File> images = (ArrayList<File>) loopOverFolderContentsAndCompareSimilarity(folder, lab_paletteVector1, resultsFolder);
                DisplayPicsList.setImages(images);
                DisplayPicsList.display();
                for (File file : images) {
                    Path destFolder = Paths.get(folderPath);
                    try {
                        if (!Files.exists(destFolder.resolve(file.getName()))) {
                            Files.copy(file.toPath(), destFolder.resolve(file.getName()));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    private List<File> loopOverFolderContentsAndCompareSimilarity(File folder, double[] lab_paletteVector1, File resultsFolder) throws IOException {
        File[] listOfFiles = folder.listFiles();
        List<File> resultImages = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                BufferedImage image2 = ImageIO.read(file);
                if(image2 != null){
                    BufferedImage quantizedImage = UniformQuantization.quantize(image2, 10);

                    double[] lab_paletteVector2 = PicturesSimilarity.toLabVector(image_to_palette(quantizedImage, 20));
                    System.out.println(file.getName() + " " + (PicturesSimilarity.cosineSimilarity(lab_paletteVector1, lab_paletteVector2)));
                    double similarityFactor = 0.79;
                    if(isCroped){
                        similarityFactor = 0.9;
                    }
                    if (PicturesSimilarity.cosineSimilarity(lab_paletteVector1, lab_paletteVector2) > similarityFactor) {
                        resultImages.add(file);
                    }

                }
            } else if (file.isDirectory()) {
                // Recursively loop over the contents of the subfolder
                System.out.println("Entering subfolder: " + file.getName());
                loopOverFolderContentsAndCompareSimilarity(file, lab_paletteVector1, resultsFolder);
            }
        }
        return resultImages;
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
        isCroped=false;
        fileChooser.setSelectedFile(new File("original." + formatName));
        currentImage = originalImage;
        imageLabel.setImage(originalImage);
    }

    private void uniform() {
        fileChooser.setSelectedFile(new File("uniform." + formatName));
        long startTime = System.nanoTime();
        BufferedImage quantizedImage = UniformQuantization.quantize(originalImage, (int) uniformSpinnerModel.getValue());
        long endTime = System.nanoTime();
        uniformImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setImage(quantizedImage);
        uniformImageSize = getImageSize(quantizedImage);
    }

    private void nearestColor() {
        ColorPalette colorPalette = new ColorPalette();
        List<Color> palette = colorPalette.createColorPalette(originalImage, (int) nearestColorSpinnerModel.getValue());
        fileChooser.setSelectedFile(new File("nearest_color." + formatName));
        long startTime = System.nanoTime();
        NearestColorAlgorithm algorithm = new NearestColorAlgorithm(palette);
        BufferedImage nearestColorImage = algorithm.quantize(originalImage);
        long endTime = System.nanoTime();
        nearestColorTime = endTime - startTime;
        currentImage = nearestColorImage;
        imageLabel.setImage(nearestColorImage);
        nearestColorImageSize = getImageSize(nearestColorImage);

    }

    private void kMean() {
        fileChooser.setSelectedFile(new File("kMean." + formatName));
        long startTime = System.nanoTime();
        BufferedImage quantizedImage = KMeansQuantizer.quantize(originalImage, (int) kMeansSpinnerModel.getValue());
        long endTime = System.nanoTime();
        kMeanImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setImage(quantizedImage);
        kMeanImageSize = getImageSize(quantizedImage);
    }

    private void median_cut_new() {

        fileChooser.setSelectedFile(new File("median_cut." + formatName));
        long startTime = System.nanoTime();

        BufferedImage quantizedImage = MediaCutNew.quantizeImage(originalImage, (int) medianCutSpinnerModel.getValue());

        long endTime = System.nanoTime();
        medianCutImageTime = endTime - startTime;
        currentImage = quantizedImage;
        imageLabel.setImage(quantizedImage);
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

        double minimumSize = Math.min(Math.min(Math.min(kMeanImageSize, medianCutImageSize), uniformImageSize), nearestColorImageSize);
        double minimumTime = Math.min(Math.min(Math.min(kMeanImageTime, medianCutImageTime), uniformImageTime), nearestColorTime);

        if (minimumSize == kMeanImageSize) {
            bestAlgorithmInSize = "K Means";
        } else if (minimumSize == uniformImageSize) {
            bestAlgorithmInSize = "Uniform";
        } else if (minimumSize == medianCutImageSize) {
            bestAlgorithmInSize = "Median Cut";
        } else {
            bestAlgorithmInSize = "Nearest Color";
        }
        if (minimumTime == kMeanImageTime) {
            bestAlgorithmInTime = "K Means";
        } else if (minimumTime == uniformImageTime) {
            bestAlgorithmInTime = "Uniform";
        } else if (minimumTime == medianCutImageTime) {
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

    private void searchByDate() throws ImageProcessingException, IOException {
        DatePicker datePicker = new DatePicker(this);
        Calendar oldestDate = Calendar.getInstance();
        oldestDate.set(Calendar.YEAR, 1); // set the year to 1
        oldestDate.set(Calendar.MONTH, Calendar.JANUARY); // set the month to January (month 0)
        oldestDate.set(Calendar.DAY_OF_MONTH, 1); // set the day of month to 1
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1); // add 1 day to the current date

        datePicker.setVisible(true);
        Date startDate = oldestDate.getTime();
        Date endDate=tomorrow.getTime();
        if(datePicker.getStartDateChooser().getDate() !=null){

            startDate=datePicker.getStartDateChooser().getDate();
        }
        if(datePicker.getEndDateChooser().getDate() !=null){

            endDate=datePicker.getEndDateChooser().getDate();

        }
        File resultsFolder = new File(image_route.image_route + "\\date_search_results");
        String folderPath = (image_route.image_route + "\\date_search_results");
        resultsFolder.mkdirs();
        JFileChooser fileChooser = new JFileChooser(image_route.image_route);
        fileChooser.setDialogTitle("Choose one or multiple folders to search in");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File[] chosenFolders = fileChooser.getSelectedFiles();


            for (File folder : chosenFolders) {
                System.out.println("Processing folder: " + folder.getName());
                ArrayList<File> images = (ArrayList<File>) loopOverFolderContentsAndCheckDates(folder, startDate, endDate, resultsFolder);
                DisplayPicsList.setImages(images);
                DisplayPicsList.display();
                for (File file : images) {
                    Path destFolder = Paths.get(folderPath);
                    try {
                        if (!Files.exists(destFolder.resolve(file.getName()))) {
                            Files.copy(file.toPath(), destFolder.resolve(file.getName()));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    private static List<File> loopOverFolderContentsAndCheckDates(File folder, Date firstDate, Date secondDate, File resultsFolder) throws ImageProcessingException, IOException {
        File[] listOfFiles = folder.listFiles();
        List<File> resultImages = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Date date;
                Metadata metadata = ImageMetadataReader.readMetadata(file);
                ExifSubIFDDirectory directory
                        = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

                if (directory != null) {
                    if (directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL) != null) {
                        date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                    } else {
                        long timestamp = file.lastModified();
                        Date fileDateTime = new Date(timestamp);
                        date = fileDateTime;
                    }

                } else {
                    long timestamp = file.lastModified();
                    Date fileDateTime = new Date(timestamp);
                    date = fileDateTime;
                }


                long timestamp = file.lastModified();
                Date fileDateTime = new Date(timestamp);
                if (date.compareTo(firstDate) >= 0 && date.compareTo(secondDate) <= 0) {
                   System.out.println(date);
                    resultImages.add(file);


                }
            } else if (file.isDirectory()) {
                // Recursively loop over the contents of the subfolder
                System.out.println("Entering subfolder: " + file.getName());
                loopOverFolderContentsAndCheckDates(file, firstDate, secondDate, resultsFolder);
            }
        }
        return resultImages;
    }

    private void searchBySize() {
        // Prompt for max and min size

        String minSizeString = JOptionPane.showInputDialog("Enter the minimum size in KB:");
        String maxSizeString = JOptionPane.showInputDialog("Enter the maximum size in KB:");
        Double minSize = 0.0;
        Double maxSize = 100000.0;
        if (!minSizeString.equals("")) {
            try {
                minSize = Double.parseDouble(minSizeString);
            }
           catch (NumberFormatException e){
               System.out.println("The string does not represent an integer: " + minSizeString);

           }
        }
        if (!maxSizeString.equals("")) {
            try {
                maxSize = Double.parseDouble(maxSizeString);
            }catch (NumberFormatException e){
                System.out.println("The string does not represent an integer: " + maxSizeString);

            }

        }


        //making the results folder
        File resultsFolder = new File(image_route.image_route + "\\size_search_results");
        String folderPath = (image_route.image_route + "\\size_search_results");
        resultsFolder.mkdirs();
        JFileChooser fileChooser = new JFileChooser(image_route.image_route);
        fileChooser.setDialogTitle("Choose one or multiple folders to search in");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File[] chosenFolders = fileChooser.getSelectedFiles();

            for (File folder : chosenFolders) {
                System.out.println("Processing folder: " + folder.getName());
                ArrayList<File> images = (ArrayList<File>) loopOverFolderContentsAndCompareSize(folder, minSize, maxSize, resultsFolder);
                DisplayPicsList.setImages(images);
                DisplayPicsList.display();
                for (File file : images) {
                    Path destFolder = Paths.get(folderPath);
                    try {
                        if (!Files.exists(destFolder.resolve(file.getName()))) {
                            Files.copy(file.toPath(), destFolder.resolve(file.getName()));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    private static List<File> loopOverFolderContentsAndCompareSize(File folder, double minSize, double maxSize, File resultsFolder) {
        File[] listOfFiles = folder.listFiles();
        List<File> resultImages = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try{
                    if (ImageIO.read(file) != null) {
                        if (file.length() / 1024 <= maxSize && file.length() / 1024 >= minSize) {
                            resultImages.add(file);
                        }
                    }
                }catch(Exception e){
                    System.out.println("this file is not an image!");
                }
            } else if (file.isDirectory()) {
                // Recursively loop over the contents of the subfolder
                System.out.println("Entering subfolder: " + file.getName());
                resultImages.addAll(loopOverFolderContentsAndCompareSize(file, minSize, maxSize, resultsFolder));
            }
        }
        return resultImages;
    }

    public void ImageCropper() {
        // Create components
        cropButton = new JButton("Crop");
        resizeButton = new JButton("Resize");
        cropButton.setEnabled(false);
        resizeButton.setEnabled(false);
        // Add components to the frame
        controlPanel.add(cropButton);
        controlPanel.add(resizeButton);
        add(controlPanel, BorderLayout.SOUTH);
        // Add mouse listener to the image panel
        imageLabel.addMouseListener(new CustomMouseListener());
        // Add action listener to the crop button
        cropButton.addActionListener(e -> cropImage());
        // Add action listener to the resize button
        resizeButton.addActionListener(e -> resizeImage());
        // Set the frame size based on the image size
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
boolean isCroped=false;
    private void cropImage() {
         isCroped = true;
        int x = (int) cropBounds.getX();
        int y = (int) cropBounds.getY();
        int width = (int) cropBounds.getWidth();
        int height = (int) cropBounds.getHeight();
        try {
            currentImage = currentImage.getSubimage(x, y, width, height);
        } catch (Exception e) {
            System.out.println("Crop failed outside of Raster");
        }
        //Update the original image in the image panel
        imageLabel.setImage(currentImage);
        imageLabel.setPreferredSize(new Dimension(currentImage.getWidth(), currentImage.getHeight()));

        // Reset the crop selection
        isCropSelected = false;
        cropButton.setEnabled(false);
        cropBounds = null;
        // imageLabel.repaint();
    }

    private void resizeImage() {
        // Prompt for new width and height
        String widthString = JOptionPane.showInputDialog("Enter the new width:");
        String heightString = JOptionPane.showInputDialog("Enter the new height:");
        // Parse width and height values
        int newWidth = Integer.parseInt(widthString);
        int newHeight = Integer.parseInt(heightString);
        // Resize the image
        currentImage = resizeImageBuffer(originalImage, newWidth, newHeight);
        // Update the original image in the ImagePanel
        imageLabel.setImage(currentImage);
        imageLabel.setPreferredSize(new Dimension(newWidth, newHeight));
        imageLabel.revalidate();
        // Reset the crop selection
        isCropSelected = false;
        cropButton.setEnabled(false);
        cropBounds = null;
        imageLabel.repaint();
    }

    public static BufferedImage resizeImageBuffer(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resizedImage;
    }

    private class CustomMouseListener extends MouseAdapter {
        private Point startPoint;

        @Override
        public void mousePressed(MouseEvent e) {
            startPoint = e.getPoint();
            isCropSelected = false;
            imageLabel.repaint();
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
            imageLabel.repaint();
        }
    }


}