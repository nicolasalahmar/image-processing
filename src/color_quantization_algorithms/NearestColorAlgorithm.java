package color_quantization_algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class NearestColorAlgorithm {
    private final List<Color> colorPalette;

    public NearestColorAlgorithm(List<Color> colorPalette) {
        this.colorPalette = colorPalette;
    }

    public BufferedImage quantize(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                Color pixelColor = new Color(rgb);

                // Find the closest color in the palette
                Color closestColor = getClosestColor(pixelColor);

                // Set the new color for the pixel
                outputImage.setRGB(x, y, closestColor.getRGB());
            }
        }

        return outputImage;
    }

    private Color getClosestColor(Color color) {
        Color closestColor = null;
        double closestDistance = Double.MAX_VALUE;

        for (Color paletteColor : colorPalette) {
            // Calculate the distance between the input color and the current color in the palette
            double distance = getDistance(color, paletteColor);
            // If the distance is smaller than the current closest distance
            // update the closest color and distance
            if (distance < closestDistance) {
                closestColor = paletteColor;
                closestDistance = distance;
            }
        }

        return closestColor;
    }

    private double getDistance(Color color1, Color color2) {
        int r1 = color1.getRed();
        int g1 = color1.getGreen();
        int b1 = color1.getBlue();

        int r2 = color2.getRed();
        int g2 = color2.getGreen();
        int b2 = color2.getBlue();
        // Calculate the Euclidean distance between the two colors

        double distance = Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2));
        return distance;
    }
}