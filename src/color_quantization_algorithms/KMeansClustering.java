package color_quantization_algorithms;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansClustering {

    public static BufferedImage applyKMeans(BufferedImage inputImage, int k) {

        // initialize cluster centers randomly
        //The code initializes the cluster centers
        // randomly by generating k random points
        // in the input image, and using the color
        // values of these points as the initial centers.
        List<Color> centers = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < k; i++) {
            int x = random.nextInt(inputImage.getWidth());
            int y = random.nextInt(inputImage.getHeight());
            centers.add(new Color(inputImage.getRGB(x, y)));
        }
        //iteratively performs the K-means clustering algorithm
        // until convergence. At each iteration, it assigns each
        // pixel in the input image to the closest cluster center
        // and then updates the cluster centers based on the mean
        // of the pixels in each cluster.

        // Perform K-Means clustering iterations
        boolean converged = false;
        while (!converged) {
            // Assign samples to clusters
            List<List<Color>> clusters = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                clusters.add(new ArrayList<>());
            }
            for (int x = 0; x < inputImage.getWidth(); x++) {
                for (int y = 0; y < inputImage.getHeight(); y++) {
                    Color pixelColor = new Color(inputImage.getRGB(x, y));
                    int closestCenterIndex = findClosestCenterIndex(pixelColor, centers);
                    clusters.get(closestCenterIndex).add(pixelColor);
                }
            }
            // Update cluster centers
            List<Color> newCenters = new ArrayList<>();
            for (List<Color> cluster : clusters) {
                if (cluster.isEmpty()) {
                    // Reinitialize empty clusters with new random center
                    int x = random.nextInt(inputImage.getWidth());
                    int y = random.nextInt(inputImage.getHeight());
                    newCenters.add(new Color(inputImage.getRGB(x, y)));
                } else {
                    Color center = computeClusterCenter(cluster);
                    newCenters.add(center);
                }
            }
            // Check for convergence
            converged = true;
            for (int i = 0; i < k; i++) {
                if (!centers.get(i).equals(newCenters.get(i))) {
                    converged = false;
                    break;
                }
            }
            // Update cluster centers for next iteration
            centers = newCenters;
        }
        //after the algorithm converges we create an output image
        // with the same dimensions as the input image. Each pixel in the
        // output image is assigned the color value of the cluster center
        // that it was assigned to during the K-means clustering.

        // Create output image with each pixel replaced by its cluster center
        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
        for (int x = 0; x < inputImage.getWidth(); x++) {
            for (int y = 0; y < inputImage.getHeight(); y++) {
                Color pixelColor = new Color(inputImage.getRGB(x, y));
                int closestCenterIndex = findClosestCenterIndex(pixelColor, centers);
                outputImage.setRGB(x, y, centers.get(closestCenterIndex).getRGB());
            }
        }

        return outputImage;
    }
      //this method takes a pixel color and a list of cluster centers
      // and returns the index of the cluster center that is closest to
      // the pixel color
      // it computes the Euclidean distance between the pixel color and each
      // cluster center and returns the index of the closest center.
    private static int findClosestCenterIndex(Color pixelColor, List<Color> centers) {
        int closestCenterIndex = 0;
        double closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < centers.size(); i++) {
            Color center = centers.get(i);
            double distance = Math.sqrt(Math.pow(center.getRed() - pixelColor.getRed(), 2)
                    + Math.pow(center.getGreen() - pixelColor.getGreen(), 2)
                    + Math.pow(center.getBlue() - pixelColor.getBlue(), 2));
            if (distance < closestDistance) {
                closestDistance = distance;
                closestCenterIndex = i;
            }
        }
        return closestCenterIndex;

    }
    //this method takes a list of pixel colors and returns the mean
    // color of the pixels in the list. It computes the mean red, green, and blue
    // values of the pixels in the list, and returns a new Color object with these
    // values as its components.
    private static Color computeClusterCenter(List<Color> cluster) {
        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;
        for (Color pixelColor : cluster) {
            totalRed += pixelColor.getRed();
            totalGreen += pixelColor.getGreen();
            totalBlue += pixelColor.getBlue();
        }
        int numPixels = cluster.size();
        int meanRed = totalRed / numPixels;
        int meanGreen = totalGreen / numPixels;
        int meanBlue = totalBlue / numPixels;
        return new Color(meanRed, meanGreen, meanBlue);
    }
}
