package util;

import java.awt.image.BufferedImage;
import java.io.File;

public class ImageUtils {
    public static int[][][] bufferedImageToSRGBArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][][] result = new int[height][width][3];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                result[y][x][0] = (rgb >> 16) & 0xFF;
                result[y][x][1] = (rgb >> 8) & 0xFF;
                result[y][x][2] = rgb & 0xFF;
            }
        }
        return result;
    }

    public static BufferedImage sRGBArrayToBufferedImage(int[][][] sRGBArray) {
        int height = sRGBArray.length;
        int width = sRGBArray[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = sRGBArray[y][x][0];
                int g = sRGBArray[y][x][1];
                int b = sRGBArray[y][x][2];
                int rgb = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgb);
            }
        }
        return image;
    }

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "png";
        }
        return name.substring(lastIndexOf + 1).toLowerCase();
    }
}