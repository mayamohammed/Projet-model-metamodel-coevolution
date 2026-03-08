
package com.coevolution.augmentation.pipeline;

import com.coevolution.augmentation.generator.PairGenerator;
import java.io.File;

public class AugmentationMain {

    public static void main(String[] args) throws Exception {
        String inputPath  = args.length > 0 ? args[0]
            : "C:\\Users\\maya mohammed\\eclipse-workspace\\model-metamodel-coevolution3\\dataset\\pairs";
        String outputPath = args.length > 1 ? args[1]
            : "C:\\Users\\maya mohammed\\eclipse-workspace\\model-metamodel-coevolution3\\dataset\\augmented";
        int maxMutations  = args.length > 2 ? Integer.parseInt(args[2]) : 10;

        System.out.println("=================================================");
        System.out.println("  DATA AUGMENTATION — SEMAINE 3a");
        System.out.println("=================================================");
        System.out.println("  Source    : " + inputPath);
        System.out.println("  Output    : " + outputPath);
        System.out.println("  Mutations : " + maxMutations + " par fichier");
        System.out.println("=================================================");

        File inputDir  = new File(inputPath);
        File outputDir = new File(outputPath);

        if (!inputDir.exists()) {
            System.err.println("ERREUR : dossier source introuvable -> " + inputPath);
            System.exit(1);
        }

        PairGenerator generator = new PairGenerator(inputDir, outputDir, maxMutations);
        generator.generate();

        int original  = inputDir.listFiles(File::isDirectory).length;
        int augmented = generator.getTotalGenerated();

        System.out.println("\n=================================================");
        System.out.println("  RESUME FINAL");
        System.out.println("=================================================");
        System.out.println("  Paires originales  : " + original);
        System.out.println("  Paires augmentees  : " + augmented);
        System.out.println("  TOTAL DATASET      : " + (original + augmented));
        System.out.println("=================================================");
    }
}