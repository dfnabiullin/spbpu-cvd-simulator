package simulate;

import convert.ColorSpaceConverter;
import convert.LMSModel;
import enums.Deficiency;
import util.MatrixUtils;

public class SimulatorVienot1999 extends DichromacySimulator {
    private final LMSModel colorModel;

    public SimulatorVienot1999(LMSModel colorModel) {
        super();
        this.colorModel = colorModel;
    }

    @Override
    protected float[][][] simulateDichromacyLinearRGB(float[][][] imageLinearRGBFloat32, Deficiency deficiency) {
        double[] n;
        double[][] lmsProjectionMatrix;
        if (deficiency == Deficiency.PROTAN || deficiency == Deficiency.DEUTAN) {
            double[] lmsBlue = MatrixUtils.multiplyVector(colorModel.lmsFromLinearRGB, new double[]{0.0, 0.0, 1.0});
            double[] lmsYellow = MatrixUtils.multiplyVector(colorModel.lmsFromLinearRGB, new double[]{1.0, 1.0, 0.0});
            n = MatrixUtils.crossProduct(lmsYellow, lmsBlue);
            lmsProjectionMatrix = planeProjectionMatrix(n, deficiency);
        } else {
            double[] vRed = MatrixUtils.multiplyVector(colorModel.lmsFromLinearRGB, new double[]{1.0, 0.0, 0.0});
            double[] vCyan = MatrixUtils.multiplyVector(colorModel.lmsFromLinearRGB, new double[]{0.0, 1.0, 1.0});
            n = MatrixUtils.crossProduct(vCyan, vRed);
            lmsProjectionMatrix = planeProjectionMatrix(n, Deficiency.TRITAN);
        }
        double[][] tempMatrix = MatrixUtils.multiply(lmsProjectionMatrix, colorModel.lmsFromLinearRGB);
        double[][] cvdLinearRgbMatrix = MatrixUtils.multiply(colorModel.linearRGBFromLMS, tempMatrix);
        return ColorSpaceConverter.applyColorMatrix(imageLinearRGBFloat32, cvdLinearRgbMatrix);
    }

}