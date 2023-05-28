import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorInputGUI {
    private final JFrame frame;
    private final JPanel inputPanel,buttonPanel;
    private final JLabel numColorsLabel;
    private final JTextField numColorsField;
    private final List<JTextField> componentFields;
    private final JButton submitButton,nextButton;
    List<double[]> colors = new ArrayList<>();
    int numColors;
    int counter = 0;

    public List<double[]> getColors() {
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
            double[] rgbs = new double[3];
            for (int j = 0; j < 3; j++) {
                rgbs[j] = Double.parseDouble(componentFields.get(j).getText().trim());
            }
            colors.add(rgbs);
            for (int i = 0; i < numColors; i++) {
                System.out.println(Arrays.toString(colors.get(i)));
            }
            frame.dispose();
        }
    }

    private class NextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            inputPanel.remove(numColorsLabel);
            inputPanel.remove(numColorsField);
            try {
                counter++;
                numColors = Integer.parseInt(numColorsField.getText().trim());
                double[] rgbs = new double[3];
                buttonPanel.revalidate();
                buttonPanel.repaint();
                for (int j = 0; j < 3; j++) {
                    rgbs[j] = Double.parseDouble(componentFields.get(j).getText().trim());
                }
                if (numColors==1) {
                    buttonPanel.revalidate();
                    buttonPanel.repaint();
                    frame.remove(inputPanel);
                    frame.revalidate();
                    frame.repaint();
                    buttonPanel.add(submitButton);
                    buttonPanel.remove(nextButton);
                }
                if (counter == (numColors-1)) {
                    buttonPanel.revalidate();
                    buttonPanel.repaint();
                    buttonPanel.add(submitButton);
                    buttonPanel.remove(nextButton);
                }
                colors.add(rgbs);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter integers and doubles only.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}