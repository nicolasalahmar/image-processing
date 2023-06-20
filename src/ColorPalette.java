import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    public static Set<Color> getBlockColorsFromImage(BufferedImage image, int blockSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        Set<Color> blockColors = new HashSet<>();

        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;
                int count = 0;

                for (int blockY = y; blockY < y + blockSize && blockY < height; blockY++) {
                    for (int blockX = x; blockX < x + blockSize && blockX < width; blockX++) {
                        int rgb = image.getRGB(blockX, blockY);

                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        sumRed += red;
                        sumGreen += green;
                        sumBlue += blue;
                        count++;
                    }
                }

                int averageRed = sumRed / count;
                int averageGreen = sumGreen / count;
                int averageBlue = sumBlue / count;

                Color blockColor = new Color(averageRed, averageGreen, averageBlue);
                blockColors.add(blockColor);
            }
        }

        return blockColors;
    }

    public static Set<Color> getSimilarColors(Color baseColor, int similarityThreshold) {
        Set<Color> similarColors = new HashSet<>();

        int red = baseColor.getRed();
        int green = baseColor.getGreen();
        int blue = baseColor.getBlue();

        for (int r = red - similarityThreshold; r <= red + similarityThreshold; r++) {
            for (int g = green - similarityThreshold; g <= green + similarityThreshold; g++) {
                for (int b = blue - similarityThreshold; b <= blue + similarityThreshold; b++) {
                    if (isValidRGBValue(r) && isValidRGBValue(g) && isValidRGBValue(b)) {
                        Color color = new Color(r, g, b);
                        similarColors.add(color);
                    }
                }
            }
        }

        return similarColors;
    }

    private static boolean isValidRGBValue(int value) {
        return value >= 0 && value <= 255;
    }
}