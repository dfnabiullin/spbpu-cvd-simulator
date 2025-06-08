package util;

public class MatrixUtils {
    public static double[][] multiply(double[][] a, double[][] b) {
        int aRows = a.length;
        int aCols = a[0].length;
        int bCols = b[0].length;
        if (aCols != b.length) {
            throw new IllegalArgumentException("Matrix A columns must match Matrix B rows.");
        }
        double[][] result = new double[aRows][bCols];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                for (int k = 0; k < aCols; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    public static double[] multiplyVector(double[][] a, double[] v) {
        if (a[0].length != v.length) {
            throw new IllegalArgumentException("Matrix A columns must match vector v length.");
        }
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                result[i] += a[i][j] * v[j];
            }
        }
        return result;
    }

    public static double[][] inverse3x3(double[][] mat) {
        if (mat.length != 3 || mat[0].length != 3) {
            throw new IllegalArgumentException("Matrix must be 3x3.");
        }
        double[][] inv = new double[3][3];
        double det = mat[0][0] * (mat[1][1] * mat[2][2] - mat[2][1] * mat[1][2]) - mat[0][1] * (mat[1][0] * mat[2][2] - mat[1][2] * mat[2][0]) + mat[0][2] * (mat[1][0] * mat[2][1] - mat[1][1] * mat[2][0]);
        if (det == 0) {
            throw new ArithmeticException("Matrix is singular, cannot invert.");
        }
        double invDet = 1.0 / det;
        inv[0][0] = (mat[1][1] * mat[2][2] - mat[2][1] * mat[1][2]) * invDet;
        inv[0][1] = (mat[0][2] * mat[2][1] - mat[0][1] * mat[2][2]) * invDet;
        inv[0][2] = (mat[0][1] * mat[1][2] - mat[0][2] * mat[1][1]) * invDet;
        inv[1][0] = (mat[1][2] * mat[2][0] - mat[1][0] * mat[2][2]) * invDet;
        inv[1][1] = (mat[0][0] * mat[2][2] - mat[0][2] * mat[2][0]) * invDet;
        inv[1][2] = (mat[1][0] * mat[0][2] - mat[0][0] * mat[1][2]) * invDet;
        inv[2][0] = (mat[1][0] * mat[2][1] - mat[2][0] * mat[1][1]) * invDet;
        inv[2][1] = (mat[2][0] * mat[0][1] - mat[0][0] * mat[2][1]) * invDet;
        inv[2][2] = (mat[0][0] * mat[1][1] - mat[1][0] * mat[0][1]) * invDet;
        return inv;
    }

    public static double[] crossProduct(double[] a, double[] b) {
        if (a.length != 3 || b.length != 3) {
            throw new IllegalArgumentException("Vectors must have 3 elements for cross product.");
        }
        return new double[]{a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0]};
    }
}