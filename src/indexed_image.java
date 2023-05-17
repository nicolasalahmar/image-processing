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

        colorModel = new IndexColorModel(8, 256, cmap, 0, true, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

        BufferedImage indexed_image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, colorModel);

        Graphics2D g = indexed_image.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return indexed_image;
    }
}
