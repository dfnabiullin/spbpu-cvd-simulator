package convert;

public class LMSModelSRGBSmithPokorny75 extends LMSModel {
    public static final double[][] XYZ_JUDDVOS_FROM_LINEAR_RGB_BT709 = {{0.409568, 0.355041, 0.179167}, {0.213389, 0.706743, 0.0798680}, {0.0186297, 0.114620, 0.912367}};
    public static final double[][] LMS_FROM_XYZ_JUDDVOS_SMITH_POKORNY_1975 = {{0.15514, 0.54312, -0.03286}, {-0.15514, 0.45684, 0.03286}, {0.0, 0.0, 0.01608}};

    public LMSModelSRGBSmithPokorny75() {
        super(XYZ_JUDDVOS_FROM_LINEAR_RGB_BT709, LMS_FROM_XYZ_JUDDVOS_SMITH_POKORNY_1975);
    }
}