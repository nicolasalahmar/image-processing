package color_quantization_algorithms;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MedianCutColorQuantization {

    public static void medianCutQuantize(BufferedImage img, int[][] imgArr) {
        int size = imgArr.length;
        int rSum = 0;
        int gSum = 0;
        int bSum = 0;

        for (int i = 0; i < size; i++) {
            int[] data = imgArr[i];
            rSum += data[0];
            gSum += data[1];
            bSum += data[2];
        }

        int rAverage = rSum / size;
        int gAverage = gSum / size;
        int bAverage = bSum / size;

        for (int[] data : imgArr) {
            int rIndex = data[3];
            int cIndex = data[4];
            img.setRGB(cIndex, rIndex, new Color(rAverage, gAverage, bAverage).getRGB());
        }
    }

    public static BufferedImage splitIntoBuckets(BufferedImage img, int[][] imgArr, int depth) {
        if (depth <= 0) {
            return img;
        }

        BufferedImage imgCopy = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics2D g2d = imgCopy.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        splitImage(imgCopy, imgArr, depth);

        return imgCopy;
    }

    private static void splitImage(BufferedImage img, int[][] imgArr, int depth) {
        int size = imgArr.length;

        if (size == 0) {
            return;
        }

        if (depth == 0) {
            medianCutQuantize(img, imgArr);
            return;
        }

        int rRange = getMaxValue(imgArr, 0) - getMinValue(imgArr, 0);
        int gRange = getMaxValue(imgArr, 1) - getMinValue(imgArr, 1);
        int bRange = getMaxValue(imgArr, 2) - getMinValue(imgArr, 2);

        int spaceWithHighestRange = 0;

        if (gRange >= rRange && gRange >= bRange) {
            spaceWithHighestRange = 1;
        } else if (bRange >= rRange && bRange >= gRange) {
            spaceWithHighestRange = 2;
        } else if (rRange >= bRange && rRange >= gRange) {
            spaceWithHighestRange = 0;
        }

        imgArr = sortByColorSpace(imgArr, spaceWithHighestRange);
        int medianIndex = (size + 1) / 2;

        splitImage(img, subArray(imgArr, 0, medianIndex), depth - 1);
        splitImage(img, subArray(imgArr, medianIndex, size), depth - 1);
    }

    private static int getMaxValue(int[][] arr, int index) {
        int max = Integer.MIN_VALUE;
        for (int[] data : arr) {
            if (data[index] > max) {
                max = data[index];
            }
        }
        return max;
    }

    private static int getMinValue(int[][] arr, int index) {
        int min = Integer.MAX_VALUE;
        for (int[] data : arr) {
            if (data[index] < min) {
                min = data[index];
            }
        }
        return min;
    }

    private static int[][] sortByColorSpace(int[][] arr, int spaceIndex) {
        int[][] sortedArr = new int[arr.length][5];
        System.arraycopy(arr, 0, sortedArr, 0, arr.length);

        for (int i = 0; i < sortedArr.length - 1; i++) {
            for (int j = 0; j < sortedArr.length - 1 - i; j++) {
                if (sortedArr[j][spaceIndex] > sortedArr[j + 1][spaceIndex]) {
                    int[] temp = sortedArr[j];
                    sortedArr[j] = sortedArr[j + 1];
                    sortedArr[j + 1] = temp;
                }
            }
        }

        return sortedArr;
    }

    private static int[][] subArray(int[][] arr, int startIndex, int endIndex) {
        int length = endIndex - startIndex;
        int[][] subArr = new int[length][5];
        System.arraycopy(arr, startIndex, subArr, 0, length);
        return subArr;
    }

}
