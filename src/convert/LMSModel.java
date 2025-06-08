package convert;

import util.MatrixUtils;

public abstract class LMSModel {
    public final double[][] xyzFromLinearRGB;
    public final double[][] lmsFromXYZ;
    public final double[][] lmsFromLinearRGB;
    public final double[][] linearRGBFromLMS;
    public final double[][] linearRGBFromXYZ;

    public LMSModel(double[][] xyzFromLinearRGB, double[][] lmsFromXYZ) {
        this.xyzFromLinearRGB = xyzFromLinearRGB;
        this.lmsFromXYZ = lmsFromXYZ;
        this.lmsFromLinearRGB = MatrixUtils.multiply(this.lmsFromXYZ, this.xyzFromLinearRGB);
        this.linearRGBFromLMS = MatrixUtils.inverse3x3(this.lmsFromLinearRGB);
        this.linearRGBFromXYZ = MatrixUtils.inverse3x3(this.xyzFromLinearRGB);
    }
}