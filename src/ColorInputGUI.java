import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColorInputGUI {
    private final JFrame frame;
    private final JPanel inputPanel, buttonPanel;
    private final JLabel numColorsLabel;
    private final JTextField numColorsField;
    private final List<JTextField> componentFields;
    private final JButton submitButton, nextButton;
    static List<Color> colors = new ArrayList<>();
    int numColors;
    int counter = 0;

    public static List<Color> getColors() {
        return colors;
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
        Color c = new Color(27, 59, 51);
        Color c2 = new Color(28, 60, 28);
        Color c3 = new Color(45, 77, 66);

        Color c11 = new Color(86, 59, 114);
        Color c22 = new Color(205, 106, 135);
        Color c33 = new Color(229, 92, 146);
        Color c44 = new Color(100, 37, 61);


        Color c111 = new Color(102, 34, 0);
        Color c222 = new Color(174, 68, 35);
        Color c333 = new Color(191, 99, 0);
        Color c444 = new Color(242, 82, 8);

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
            colors.add(new Color(rgbs[0], rgbs[1], rgbs[2]));
            frame.dispose();
            try {
                searchByColor();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void searchByColor() throws IOException {
        ImageGUI im = new ImageGUI();
        List<Color> targetColors = getColors();
        List<Color> palette;
        ArrayList<Double> similarityScores = new ArrayList<>();
        File folder = new File(image_route.indexed_image_route);
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isFile() && !file.getName().equals(".gitkeep")) {
                palette = im.image_to_palette(file,30);
                for (Color color : palette) {
                    double maxSimilarity = 0;
                    List<Color> c1 = new ArrayList<>();
                    c1.add(color);
                    double[] v1= PicturesSimilarity.toLabVector(c1);
                    for (Color targetColor : targetColors) {
                        List<Color> c2 = new ArrayList<>();
                        c2.add(targetColor);
                        double[] v2= PicturesSimilarity.toLabVector(c2);
                        double similarity = PicturesSimilarity.cosineSimilarity(v1, v2);

                        if (similarity > maxSimilarity) {
                            maxSimilarity = similarity;
                        }
                    }
                    similarityScores.add(maxSimilarity);
                }
                double similaritySum = 0;
                for (double score : similarityScores) {
                    similaritySum += score;
                }
                System.out.println(file.getName());
                double similarityAverage = similaritySum / similarityScores.size();
                System.out.println(similarityAverage+"\n"+"--------------------------");
                if(similarityAverage>0.88){
                try {
                    ImageIO.write(im.currentImage, im.getFormatName(), new File(image_route.byColor, file.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }}
            }
        }
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
                colors.add(new Color(rgbs[0], rgbs[1], rgbs[2]));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter integers and doubles only.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}