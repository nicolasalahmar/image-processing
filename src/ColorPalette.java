import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;



public class ColorPalette {


    public List<Color> createColorPalette(BufferedImage image, int numColors) {
        List<Color> colors = new ArrayList<>();

        // Get the RGB values of all pixels in the image
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixelData = new int[width * height];
        image.getRGB(0, 0, width, height, pixelData, 0, width);

        // Count the frequency of each color
        int[] colorFreq = new int[256 * 256 * 256];
        for (int i = 0; i < pixelData.length; i++) {
            int rgb = pixelData[i];
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;
            int index = r * 256 * 256 + g * 256 + b;
            colorFreq[index]++;
        }

        // Find the most common colors
        //  finds the most common colors
        //  if it is the most common color
        //  adds the color to the colors list
        for (int i = 0; i < numColors; i++) {
            int maxFreq = 0;
            int maxIndex = 0;
            for (int j = 0; j < colorFreq.length; j++) {
                if (colorFreq[j] > maxFreq) {
                    maxFreq = colorFreq[j];
                    maxIndex = j;
                }
            }
            //0xFF is used to mask the lower 8 bits of an integer
            // extracting  component of an RGB value
            int r = (maxIndex >> 16) & 0xFF;
            int g = (maxIndex >> 8) & 0xFF;
            int b = maxIndex & 0xFF;
            colors.add(new Color(r, g, b));
            colorFreq[maxIndex] = 0;
        }

        return colors;
    }
}