package color_quantization_algorithms;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class UniformQuantization {

    public static BufferedImage quantize(BufferedImage image, int numColors) {
        // Calculate the color quantization level
        //Math.ceil() round up the logarithm of the number of colors to the nearest integer
        //This determines the number of bits that will be used to represent each color channel
        int levels = (int) Math.ceil(Math.log(numColors) / Math.log(2));

        // Calculate the color quantization interval
        //This determines the size of the intervals used to divide the color space
        //A smaller quantization interval will result in a larger number of discrete colors
        int interval = 256 / (int) Math.pow(2, levels);

        // Create the output image
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Apply the quantization
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // Get the color of the pixel
                Color color = new Color(image.getRGB(x, y));

                // Calculate the quantized color
                //calculates the quantized color for the pixel. This is done by dividing
                // each component of the color by the color quantization interval
                // and then rounding down to the nearest integer
                int red = (color.getRed() / interval) * interval;
                int green = (color.getGreen() / interval) * interval;
                int blue = (color.getBlue() / interval) * interval;

                // Set the quantized color to the output image
                Color quantizedColor = new Color(red, green, blue);
                output.setRGB(x, y, quantizedColor.getRGB());
            }
        }

        return output;
    }

}