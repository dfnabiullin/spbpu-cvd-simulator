package simulate;

import convert.ColorSpaceConverter;
import enums.Deficiency;

public abstract class Simulator {
    public Simulator() {
    }

    public int[][][] simulateCvd(int[][][] imageSRGBUint8, Deficiency deficiency) {
        float[][][] imLinearRGB = ColorSpaceConverter.asFloat32(imageSRGBUint8);
        imLinearRGB = ColorSpaceConverter.linearRGBFromSRGB(imLinearRGB);
        float[][][] imCvdLinearRGB = simulateCvdLinearRGB(imLinearRGB, deficiency);
        float[][][] imCvdFloat;
        imCvdFloat = ColorSpaceConverter.sRGBFromLinearRGB(imCvdLinearRGB);
        return ColorSpaceConverter.asUint8(imCvdFloat);
    }

    protected abstract float[][][] simulateCvdLinearRGB(float[][][] imageLinearRGBFloat32, Deficiency deficiency);
}