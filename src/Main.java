import convert.LMSModelSRGBSmithPokorny75;
import enums.Deficiency;
import simulate.Simulator;
import simulate.SimulatorVienot1999;
import util.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
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
            cmdArgs.inputPath = Paths.get(args[0]);
            cmdArgs.outputPath = Paths.get(args[1]);
        } catch (InvalidPathException e) {
            System.err.println("Error: Invalid directory path provided. " + e.getMessage());
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
                    System.err.println("Error: Deficiency flag requires an argument.");
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
        System.err.println("Usage: java Main <input_directory> <output_directory> [-d deficiency_type]");
        System.err.println("Deficiency_type: protan, deutan, or tritan (default: protan)");
        System.exit(1);
    }

    public static void main(String[] rawArgs) {
        CmdArgs args = parseCommandLine(rawArgs);
        if (!Files.isDirectory(args.inputPath)) {
            System.err.println("Error: Input path is not a directory: " + args.inputPath);
            System.exit(1);
        }
        try {
            Files.createDirectories(args.outputPath);
        } catch (IOException e) {
            System.err.println("Error creating output directory: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Starting batch processing...");
        Simulator simulator = new SimulatorVienot1999(new LMSModelSRGBSmithPokorny75());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(args.inputPath, "*.{jpg,jpeg,png,bmp}")) {
            for (Path inputImagePath : stream) {
                processImage(inputImagePath, args.outputPath, simulator, args.deficiency);
            }
        } catch (IOException e) {
            System.err.println("Error reading from input directory: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Batch processing complete.");
    }

    private static void processImage(Path inputImagePath, Path outputDir, Simulator simulator, Deficiency deficiency) {
        String fileName = inputImagePath.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        Path outputImagePath = outputDir.resolve(baseName + "_simulated." + extension);
        System.out.println("Processing " + inputImagePath + "  ->  " + outputImagePath);
        try {
            BufferedImage inputImage = ImageIO.read(inputImagePath.toFile());
            if (inputImage == null) {
                System.err.println("Warning: Could not read or unsupported format for " + inputImagePath);
                return;
            }
            int[][][] imageSRGBUint8 = ImageUtils.bufferedImageToSRGBArray(inputImage);
            int[][][] outputImageSRGBUint8 = simulator.simulateCvd(imageSRGBUint8, deficiency);
            BufferedImage outputBufferedImage = ImageUtils.sRGBArrayToBufferedImage(outputImageSRGBUint8);
            ImageIO.write(outputBufferedImage, extension, outputImagePath.toFile());
        } catch (IOException e) {
            System.err.println("ERROR processing file '" + inputImagePath + "': " + e.getMessage());
        }
    }

    private static class CmdArgs {
        Path inputPath;
        Path outputPath;
        Deficiency deficiency = Deficiency.PROTAN;
    }
}