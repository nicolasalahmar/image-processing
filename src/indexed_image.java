import color_quantization_algorithms.KMeansQuantizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.File;

import static color_quantization_algorithms.KMeansQuantizer.init_centroids;


public class indexed_image {
    IndexColorModel colorModel;
    BufferedImage input_image;
    BufferedImage constructed_image;
    public indexed_image(BufferedImage input_image){
        this.input_image = input_image;
        this.constructed_image = fillImageColors(input_image);
    }
    public BufferedImage fillImageColors(BufferedImage image) {

        KMeansQuantizer.labels_centroids temp = init_centroids(image, 256);

        int[] cmap = temp.centroids.stream().mapToInt(Color::getRGB).toArray();

        byte[] red = new byte[cmap.length];
        byte[] green = new byte[cmap.length];
        byte[] blue = new byte[cmap.length];

        for (int i = 0; i < cmap.length; i++) {
            red[i] = (byte)((cmap[i] >> 16) & 0xff);
            green[i] = (byte)((cmap[i] >> 8) & 0xff);
            blue[i] = (byte)(cmap[i] & 0xff);
        }

        colorModel = new IndexColorModel(8, 256, red, green, blue);

        BufferedImage indexed_image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, colorModel);

        Graphics2D g = indexed_image.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return indexed_image;
    }
}
