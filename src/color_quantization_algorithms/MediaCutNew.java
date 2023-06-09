package color_quantization_algorithms;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MediaCutNew {
    public static BufferedImage quantizeImage(BufferedImage image, int numColors) {
        List<Color> colorPalette = quantizeImageColor(image, numColors);

        BufferedImage outputImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                Color pixelColor = new Color(image.getRGB(x, y));

                int closestColorIndex = findClosestColorIndex(pixelColor, colorPalette);

                Color closestColor = colorPalette.get(closestColorIndex);

                outputImage.setRGB(x, y, closestColor.getRGB());
            }
        }

        return outputImage;
    }

    private static List<Color> quantizeImageColor(BufferedImage image, int numColors) {
        List<Color> colorPalette = new ArrayList<>();

        // Create a list of pixels from the image
        List<Color> pixelList = new ArrayList<>();
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                pixelList.add(pixelColor);
            }
        }

        // Apply Modified Median Cut algorithm
        recursiveMedianCut(pixelList, colorPalette, numColors);

        return colorPalette;
    }

    private static void recursiveMedianCut(List<Color> pixels, List<Color> colorPalette, int numColors) {
        if (colorPalette.size() >= numColors) {
            return;
        }

        // Calculate the range of colors in the current box
        int redMin = 255, redMax = 0;
        int greenMin = 255, greenMax = 0;
        int blueMin = 255, blueMax = 0;
        for (Color pixel : pixels) {
            int red = pixel.getRed();
            int green = pixel.getGreen();
            int blue = pixel.getBlue();
            redMin = Math.min(redMin, red);
            redMax = Math.max(redMax, red);
            greenMin = Math.min(greenMin, green);
            greenMax = Math.max(greenMax, green);
            blueMin = Math.min(blueMin, blue);
            blueMax = Math.max(blueMax, blue);
        }

        // Calculate the color range with the maximum width
        int redRange = redMax - redMin;
        int greenRange = greenMax - greenMin;
        int blueRange = blueMax - blueMin;
        int maxRange = Math.max(Math.max(redRange, greenRange), blueRange);

        // Check if there are no pixels or the range is 0
        if (pixels.isEmpty() || maxRange == 0) {
            return;
        }

        // Sort the pixels based on the color range
        if (maxRange == redRange) {
            pixels.sort((p1, p2) -> Integer.compare(p1.getRed(), p2.getRed()));
        } else if (maxRange == greenRange) {
            pixels.sort((p1, p2) -> Integer.compare(p1.getGreen(), p2.getGreen()));
        } else {
            pixels.sort((p1, p2) -> Integer.compare(p1.getBlue(), p2.getBlue()));
        }

        // Split the box into two sub-boxes
        int midIndex = pixels.size() / 2;
        List<Color> leftPixels = pixels.subList(0, midIndex);
        List<Color> rightPixels = pixels.subList(midIndex, pixels.size());

        // Calculate the average color of each sub-box
        Color leftAvgColor = calculateAverageColor(leftPixels);
        Color rightAvgColor = calculateAverageColor(rightPixels);

        // Add the average colors to the color palette
        colorPalette.add(leftAvgColor);
        colorPalette.add(rightAvgColor);

        // Recursively apply the algorithm on the sub-boxes
        recursiveMedianCut(leftPixels, colorPalette, numColors);
        recursiveMedianCut(rightPixels, colorPalette, numColors);
    }
    private static Color calculateAverageColor(List<Color> pixels) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        for (Color pixel : pixels) {
            redSum += pixel.getRed();
            greenSum += pixel.getGreen();
            blueSum += pixel.getBlue();
        }
        int avgRed = redSum / pixels.size();
        int avgGreen = greenSum / pixels.size();
        int avgBlue = blueSum / pixels.size();
        return new Color(avgRed, avgGreen, avgBlue);
    }

    private static int findClosestColorIndex(Color targetColor, List<Color> colorPalette) {
        int closestColorIndex = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < colorPalette.size(); i++) {
            Color paletteColor = colorPalette.get(i);
            double distance = calculateColorDistance(targetColor, paletteColor);

            if (distance < minDistance) {
                minDistance = distance;
                closestColorIndex = i;
            }
        }

        return closestColorIndex;
    }

    private static double calculateColorDistance(Color color1, Color color2) {
        int redDiff = color1.getRed() - color2.getRed();
        int greenDiff = color1.getGreen() - color2.getGreen();
        int blueDiff = color1.getBlue() - color2.getBlue();

        return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
    }
}
