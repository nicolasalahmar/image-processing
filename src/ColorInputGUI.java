import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ColorInputGUI {
    private final JFrame frame;
    private final JPanel inputPanel, buttonPanel;
    private final JLabel numColorsLabel;
    private final JTextField numColorsField;
    private final List<JTextField> componentFields;
    public final JButton submitButton;
    private final JButton nextButton;
     List<Color> colorsToSearchFor = new ArrayList<>();
    int numColors;
    int counter = 0;

    public  List<Color> getColorsToSearchFor() {
        return colorsToSearchFor;
    }

    public ColorInputGUI() {
        frame = new JFrame("Color Input");
        inputPanel = new JPanel(new GridLayout(0, 2));
        buttonPanel = new JPanel(new FlowLayout());
        numColorsLabel = new JLabel("     Number of colors:");
        numColorsField = new JTextField();
        componentFields = new ArrayList<>();
        submitButton = new JButton("Submit");
        nextButton = new JButton("Next");
        inputPanel.add(numColorsLabel);
        inputPanel.add(numColorsField);

        char[] rgb = {'R', 'G', 'B'};
        for (int i = 1; i <= 3; i++) {
            inputPanel.add(new JLabel("     " + rgb[i - 1]));
            JTextField field = new JTextField();
            componentFields.add(field);
            inputPanel.add(field);
        }

        buttonPanel.add(nextButton);
        submitButton.addActionListener(new SubmitListener());
        nextButton.addActionListener(new NextListener());
        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);  // center on screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private class SubmitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int[] rgbs = new int[3];

            for (int j = 0; j < 3; j++) {
                rgbs[j] = Integer.parseInt(componentFields.get(j).getText().trim());
            }

            // get input color
            Color inputTempColor = new Color(rgbs[0], rgbs[1], rgbs[2]);
            colorsToSearchFor.add(inputTempColor);

            //get similar colors
            List<Color> tempListSearch = new ArrayList<>();

            for (Color searchColor : colorsToSearchFor) {
                tempListSearch.addAll(ColorPalette.getSimilarColors(searchColor,15));
            }

            colorsToSearchFor.addAll(tempListSearch);
//            colorsToSearchFor.addAll(
//                    ColorPalette.getSimilarColors(inputTempColor,15)
//            );

            frame.dispose();
            try {
                searchByColor();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    private static List<File> loopOverFolderContents(File folder,List<Color>colors) {
        File[] listOfFiles = folder.listFiles();
        System.out.println("List of files"+listOfFiles);
        List<File> resultImages =  new ArrayList<>();


        for (File file : listOfFiles) {

            try {
                BufferedImage image =  ImageIO.read(file);
                if(image != null){

                    Set<Color> palette = ColorPalette.getBlockColorsFromImage(image,10);

                    for (Color color1 : colors) {
                        if(palette.contains(color1)){
                            resultImages.add(file);
                            break;
                        }
                    }
                }else{
                    System.out.println(file.getName() + "Is not a valid file");
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }


        }
        return resultImages;
    }
    public void searchByColor() throws IOException {

        List<Color> targetColors =new ArrayList<>();
        targetColors = getColorsToSearchFor();

        File resultsFolder = new File(image_route.image_route+"\\color_search_results");
        String folderPath =(image_route.image_route+"\\color_search_results");
        resultsFolder.mkdirs();
        JFileChooser fileChooser = new JFileChooser(image_route.image_route);
        fileChooser.setDialogTitle("Choose one or multiple folders to search in");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File[] chosenFolders = fileChooser.getSelectedFiles();

            for (File folder : chosenFolders) {
                ArrayList<File> images= (ArrayList<File>) loopOverFolderContents(folder,targetColors);
                DisplayPicsList.setImages(images);
                DisplayPicsList.display();
                for(File file: images){
                    Path destFolder = Paths.get(folderPath);
                    try {

                        if(!Files.exists(destFolder.resolve(file.getName()))){
                            Files.copy(file.toPath(), destFolder.resolve(file.getName()));
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }
    public static double colorDistance(Color color1, Color color2) {
        int r1 = color1.getRed();
        int g1 = color1.getGreen();
        int b1 = color1.getBlue();

        int r2 = color2.getRed();
        int g2 = color2.getGreen();
        int b2 = color2.getBlue();

        int rDiff = r1 - r2;
        int gDiff = g1 - g2;
        int bDiff = b1 - b2;

        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }
    private class NextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            inputPanel.remove(numColorsLabel);
            inputPanel.remove(numColorsField);
            try {
                counter++;
                numColors = Integer.parseInt(numColorsField.getText().trim());
                int[] rgbs = new int[3];
                buttonPanel.revalidate();
                buttonPanel.repaint();
                for (int j = 0; j < 3; j++) {
                    rgbs[j] = Integer.parseInt(componentFields.get(j).getText().trim());
                }
                if (numColors == 1) {
                    buttonPanel.revalidate();
                    buttonPanel.repaint();
                    frame.remove(inputPanel);
                    frame.revalidate();
                    frame.repaint();
                    buttonPanel.add(submitButton);
                    buttonPanel.remove(nextButton);
                }
                if (counter == (numColors - 1)) {
                    buttonPanel.revalidate();
                    buttonPanel.repaint();
                    buttonPanel.add(submitButton);
                    buttonPanel.remove(nextButton);
                }
                colorsToSearchFor.add(new Color(rgbs[0], rgbs[1], rgbs[2]));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter integers and doubles only.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}