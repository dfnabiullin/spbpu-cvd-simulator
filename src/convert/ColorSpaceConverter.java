package convert;

public class ColorSpaceConverter {
    public static int[][][] asUint8(float[][][] im) {
        int height = im.length;
        int width = im[0].length;
        int[][][] result = new int[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 3; k++) {
                    result[i][j][k] = (int) (Math.max(0.0f, Math.min(1.0f, im[i][j][k])) * 255.0f);
                }
            }
        }
        return result;
    }

    public static float[][][] asFloat32(int[][][] im) {
        int height = im.length;
        int width = im[0].length;
        float[][][] result = new float[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < 3; k++) {
                    result[i][j][k] = im[i][j][k] / 255.0f;
                }
            }
        }
        return result;
    }

    public static float[][][] linearRGBFromSRGB(float[][][] im) {
        int height = im.length;
        int width = im[0].length;
        float[][][] out = new float[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int c = 0; c < 3; c++) {
                    float val = im[i][j][c];
                    if (val < 0.04045f) {
                        out[i][j][c] = val / 12.92f;
                    } else {
                        out[i][j][c] = (float) Math.pow((val + 0.055f) / 1.055f, 2.4f);
                    }
                }
            }
        }
        return out;
    }

    public static float[][][] sRGBFromLinearRGB(float[][][] im) {
        int height = im.length;
        int width = im[0].length;
        float[][][] out = new float[height][width][3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int c = 0; c < 3; c++) {
                    float val = Math.max(0.0f, Math.min(1.0f, im[i][j][c]));
                    if (val < 0.0031308f) {
                        out[i][j][c] = val * 12.92f;
                    } else {
                        out[i][j][c] = (float) (1.055f * Math.pow(val, 1.0f / 2.4f) - 0.055f);
                    }
                }
            }
        }
        return out;
    }

    public static float[][][] applyColorMatrix(float[][][] im, double[][] matrix) {
        int height = im.length;
        int width = im[0].length;
        float[][][] result = new float[height][width][3];
        double[] pixelIn = new double[3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixelIn[0] = im[i][j][0];
                pixelIn[1] = im[i][j][1];
                pixelIn[2] = im[i][j][2];
                double r = matrix[0][0] * pixelIn[0] + matrix[0][1] * pixelIn[1] + matrix[0][2] * pixelIn[2];
                double g = matrix[1][0] * pixelIn[0] + matrix[1][1] * pixelIn[1] + matrix[1][2] * pixelIn[2];
                double b = matrix[2][0] * pixelIn[0] + matrix[2][1] * pixelIn[1] + matrix[2][2] * pixelIn[2];
                result[i][j][0] = (float) r;
                result[i][j][1] = (float) g;
                result[i][j][2] = (float) b;
            }
        }
        return result;
    }
}