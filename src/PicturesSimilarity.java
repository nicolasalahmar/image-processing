import java.awt.*;
import java.util.List;

public class PicturesSimilarity {


    public static double[] toLabVector(List<Color> colorPalette) {

        //This method takes in a list of colors and returns a L*A*B* vector that represents the color palette.


        double[] paletteVector = new double[colorPalette.size() * 3];
        int i = 0;
        for (Color color : colorPalette) {
            double r = color.getRed() / 255.0;
            double g = color.getGreen() / 255.0;
            double b = color.getBlue() / 255.0;
            // Convert RGB to XYZ
            double x = 0.4124 * r + 0.3576 * g + 0.1805 * b;
            double y = 0.2126 * r + 0.7152 * g + 0.0722 * b;
            double z = 0.0193 * r + 0.1192 * g + 0.9505 * b;

            // Convert XYZ to LAB
            double fx = x > 0.008856 ? Math.pow(x, 1.0 / 3) : (7.787 * x) + (16.0 / 116);
            double fy = y > 0.008856 ? Math.pow(y, 1.0 / 3) : (7.787 * y) + (16.0 / 116);
            double fz = z > 0.008856 ? Math.pow(z, 1.0 / 3) : (7.787 * z) + (16.0 / 116);

            double L = (116 * fy) - 16;
            double A = 500 * (fx - fy);
            double B = 200 * (fy - fz);

            paletteVector[i++] = L;
            paletteVector[i++] = A;
            paletteVector[i++] = B;
        }
        return paletteVector;
    }

    public static double[] toRgbVector(List<Color> colorPalette) {

        //This method takes in a list of colors and returns an RBG vector that represents the color palette.

        double[] paletteVector = new double[colorPalette.size() * 3];
        int i = 0;
        for (Color color : colorPalette) {

            // Each color is represented as a normalized RGB value between 0 and 1.
            paletteVector[i++] = color.getRed() / 255.0;
            paletteVector[i++] = color.getGreen() / 255.0;
            paletteVector[i++] = color.getBlue() / 255.0;
        }
        return paletteVector;
    }


    public static double cosineSimilarity(double[] v1, double[] v2) {

        //This method takes in two palette vectors and returns their cosine similarity score.

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        int length = Math.min(v1.length, v2.length);
        for (int i = 0; i < length; i++) {
            dotProduct += v1[i] * v2[i];
            norm1 += Math.pow(v1[i], 2);
            norm2 += Math.pow(v2[i], 2);
        }
        // dividing the dot product of the two vectors by the product of their Euclidean normals.

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public static double euclideanDistance(double[] v1, double[] v2) {

        //  This method takes in two palette vectors and returns their euclidean distance score.
        double sum = 0;
        double normalized;
        for (int i = 0; i < v1.length; i++) {
            sum += Math.pow(v2[i] - v1[i], 2);
        }
        double euclidean_distance = Math.sqrt(sum);

        normalized = (1 / (1 + euclidean_distance)) * 100;
        return normalized;
    }


}