package color_quantization_algorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class KMeansQuantizer {

    public static BufferedImage quantize(BufferedImage image, int k) {
        // Initialize random centroids
        ArrayList<Color> centroids = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            int x = rand.nextInt(image.getWidth());
            int y = rand.nextInt(image.getHeight());
            Color c = new Color(image.getRGB(x, y));
            centroids.add(c);
        }

        // Assign pixels to nearest centroid
        int[] labels = new int[image.getWidth() * image.getHeight()];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                double minDist = Double.MAX_VALUE;
                int label = 0;
                for (int i = 0; i < centroids.size(); i++) {
                    double dist = distance(pixelColor, centroids.get(i));
                    if (dist < minDist) {
                        minDist = dist;
                        label = i;
                    }
                }
                labels[y * image.getWidth() + x] = label;
            }
        }

        // Update centroids
        for (int i = 0; i < centroids.size(); i++) {
            int count = 0;
            int rSum = 0, gSum = 0, bSum = 0;
            for (int j = 0; j < labels.length; j++) {
                if (labels[j] == i) {
                    int x = j % image.getWidth();
                    int y = j / image.getWidth();
                    Color c = new Color(image.getRGB(x, y));
                    rSum += c.getRed();
                    gSum += c.getGreen();
                    bSum += c.getBlue();
                    count++;
                }
            }
            if (count > 0) {
                int avgR = rSum / count;
                int avgG = gSum / count;
                int avgB = bSum / count;
                centroids.set(i, new Color(avgR, avgG, avgB));
            }
        }

        // Create output image
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int label = labels[y * image.getWidth() + x];
                Color c = centroids.get(label);
                output.setRGB(x, y, c.getRGB());
            }
        }
        return output;
    }

    private static double distance(Color c1, Color c2) {
        double rDiff = c1.getRed() - c2.getRed();
        double gDiff = c1.getGreen() - c2.getGreen();
        double bDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }

}
