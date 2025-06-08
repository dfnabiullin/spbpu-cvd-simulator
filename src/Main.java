import convert.LMSModelSRGBSmithPokorny75;
import enums.Deficiency;
import simulate.Simulator;
import simulate.SimulatorVienot1999;
import util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<String, Deficiency> deficiencyFromString = new HashMap<>();

    static {
        deficiencyFromString.put("protan", Deficiency.PROTAN);
        deficiencyFromString.put("deutan", Deficiency.DEUTAN);
        deficiencyFromString.put("tritan", Deficiency.TRITAN);
    }

    private static CmdArgs parseCommandLine(String[] args) {
        if (args.length < 2) {
            printUsageAndExit();
        }
        CmdArgs cmdArgs = new CmdArgs();
        try {
            cmdArgs.inputImagePath = Paths.get(args[0]);
            cmdArgs.outputImagePath = Paths.get(args[1]);
        } catch (InvalidPathException e) {
            System.err.println("Error: Invalid file path provided. " + e.getMessage());
            printUsageAndExit();
        }
        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("-d")) {
                if (i + 1 < args.length) {
                    String defStr = args[i + 1].toLowerCase();
                    if (deficiencyFromString.containsKey(defStr)) {
                        cmdArgs.deficiency = deficiencyFromString.get(defStr);
                    } else {
                        System.err.println("Error: Unknown deficiency type '" + args[i + 1] + "'.");
                        printUsageAndExit();
                    }
                    i++;
                } else {
                    System.err.println("Error: enums.Deficiency flag requires an argument.");
                    printUsageAndExit();
                }
            } else {
                System.err.println("Error: Unknown option '" + args[i] + "'.");
                printUsageAndExit();
            }
        }
        return cmdArgs;
    }

    private static void printUsageAndExit() {
        System.err.println("Usage: java Main <input_image> <output_image> [-d deficiency_type]");
        System.err.println("Deficiency_type: protan, deutan, or tritan (default: protan)");
        System.exit(1);
    }

    public static void main(String[] rawArgs) {
        CmdArgs args = parseCommandLine(rawArgs);
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(args.inputImagePath.toFile());
            if (inputImage == null) {
                throw new IOException("Could not read image or unsupported format.");
            }
        } catch (IOException e) {
            System.err.println("Error reading input image '" + args.inputImagePath + "': " + e.getMessage());
            System.exit(1);
        }
        int[][][] imageSRGBUint8 = ImageUtils.bufferedImageToSRGBArray(inputImage);
        Simulator simulator = new SimulatorVienot1999(new LMSModelSRGBSmithPokorny75());
        int[][][] outputImageSRGBUint8 = simulator.simulateCvd(imageSRGBUint8, args.deficiency);
        BufferedImage outputBufferedImage = ImageUtils.sRGBArrayToBufferedImage(outputImageSRGBUint8);
        try {
            String outputFormat = ImageUtils.getFileExtension(args.outputImagePath.toFile());
            ImageIO.write(outputBufferedImage, outputFormat, args.outputImagePath.toFile());
            System.out.println("Successfully wrote output image to " + args.outputImagePath);
        } catch (IOException e) {
            System.err.println("Error writing output image '" + args.outputImagePath + "': " + e.getMessage());
            System.exit(1);
        }
    }

    private static class CmdArgs {
        Path inputImagePath;
        Path outputImagePath;
        Deficiency deficiency = Deficiency.PROTAN;
    }
}