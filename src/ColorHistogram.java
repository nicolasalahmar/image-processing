import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorHistogram extends JPanel {

    private int[] colorFreq;
    private List<Color> colorPalette;

    public ColorHistogram(List<Color> colorPalette) {
        this.colorPalette = colorPalette;
        this.colorFreq = new int[colorPalette.size()];
        setPreferredSize(new Dimension(400, 300));
    }

    public void createColorHistogram(BufferedImage image) {
        // Initialize an array to store the frequency of each color in the palette
        this.colorFreq = new int[colorPalette.size()];

        // Loop through each pixel in the image and check if its color is in the palette
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = new Color(image.getRGB(x, y));

                // Check if the pixel color is in the palette
                int colorIndex = colorPalette.indexOf(pixelColor);
                if (colorIndex != -1) {
                    colorFreq[colorIndex]++;
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the color histogram bars using the frequency data and color palette
        int barWidth = getWidth() / colorPalette.size();
        int maxHeight = getHeight() - 10;
        int maxFreq = getMaxFrequency();

        for (int i = 0; i < colorPalette.size(); i++) {
            Color color = colorPalette.get(i);
            int freq = colorFreq[i];
            int barHeight = (int) ((double) freq / maxFreq * maxHeight);
            int x = i * barWidth;
            int y = getHeight() - barHeight;

            g.setColor(color);
            g.fillRect(x + 5, y, barWidth - 10, barHeight);

            g.setColor(Color.BLACK);
            g.drawRect(x + 5, y, barWidth - 10, barHeight);

            g.drawString("" + freq, x + 5, y - 2);


        }

    }

    private int getMaxFrequency() {
        int maxFreq = 0;
        for (int freq : colorFreq) {
            if (freq > maxFreq) {
                maxFreq = freq;
            }
        }
        return maxFreq;
    }
}