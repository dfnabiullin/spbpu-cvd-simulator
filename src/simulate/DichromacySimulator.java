package simulate;

import enums.Deficiency;

public abstract class DichromacySimulator extends Simulator {
    protected static double[][] planeProjectionMatrix(double[] planeNormal, Deficiency deficiency) {
        double[][] projMatrix = new double[3][3];
        switch (deficiency) {
            case PROTAN:
                if (planeNormal[0] == 0) throw new ArithmeticException("Division by zero in PROTAN projection.");
                projMatrix[0][0] = 0.0;
                projMatrix[0][1] = -planeNormal[1] / planeNormal[0];
                projMatrix[0][2] = -planeNormal[2] / planeNormal[0];
                projMatrix[1][0] = 0.0;
                projMatrix[1][1] = 1.0;
                projMatrix[1][2] = 0.0;
                projMatrix[2][0] = 0.0;
                projMatrix[2][1] = 0.0;
                projMatrix[2][2] = 1.0;
                break;
            case DEUTAN:
                if (planeNormal[1] == 0) throw new ArithmeticException("Division by zero in DEUTAN projection.");
                projMatrix[0][0] = 1.0;
                projMatrix[0][1] = 0.0;
                projMatrix[0][2] = 0.0;
                projMatrix[1][0] = -planeNormal[0] / planeNormal[1];
                projMatrix[1][1] = 0.0;
                projMatrix[1][2] = -planeNormal[2] / planeNormal[1];
                projMatrix[2][0] = 0.0;
                projMatrix[2][1] = 0.0;
                projMatrix[2][2] = 1.0;
                break;
            case TRITAN:
                if (planeNormal[2] == 0) throw new ArithmeticException("Division by zero in TRITAN projection.");
                projMatrix[0][0] = 1.0;
                projMatrix[0][1] = 0.0;
                projMatrix[0][2] = 0.0;
                projMatrix[1][0] = 0.0;
                projMatrix[1][1] = 1.0;
                projMatrix[1][2] = 0.0;
                projMatrix[2][0] = -planeNormal[0] / planeNormal[2];
                projMatrix[2][1] = -planeNormal[1] / planeNormal[2];
                projMatrix[2][2] = 0.0;
                break;
            default:
                throw new IllegalArgumentException("Unknown deficiency: " + deficiency);
        }
        return projMatrix;
    }

    @Override
    protected float[][][] simulateCvdLinearRGB(float[][][] imageLinearRGBFloat32, Deficiency deficiency) {
        return simulateDichromacyLinearRGB(imageLinearRGBFloat32, deficiency);
    }

    protected abstract float[][][] simulateDichromacyLinearRGB(float[][][] imageLinearRGBFloat32, Deficiency deficiency);
}