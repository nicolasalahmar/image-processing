package color_quantization_algorithms;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class UniformQuantization {

    public static BufferedImage quantize(BufferedImage image, double numColors) {
        // Calculate the color quantization level
        int levels = (int) Math.ceil(Math.log(numColors) / Math.log(2));

        // Calculate the color quantization interval
        int interval = 256 / (int) Math.pow(2, levels);

        // Create the output image
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Apply the quantization
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // Get the color of the pixel
                Color color = new Color(image.getRGB(x, y));

                // Calculate the quantized color
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