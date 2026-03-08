package com.coevolution.collector.pipeline;

import com.coevolution.collector.git.GitRepositoryManager;

import java.io.File;
import java.nio.file.Paths;

public class CollectorMain {

    public static void main(String[] args) throws Exception {

        File projectRoot = findProjectRoot();

        String repoPath  = args.length > 0 ? args[0]
                : System.getProperty("repo.path", projectRoot.getAbsolutePath());

        String outputDir = args.length > 1 ? args[1]
                : System.getProperty("output.dir",
                        new File(projectRoot, "dataset").getAbsolutePath());

        int maxPairs = args.length > 2 ? parseIntSafe(args[2], 200)
                : Integer.parseInt(System.getProperty("max.pairs", "200"));

        System.out.println("=================================================");
        System.out.println("[CollectorMain] Demarrage du Collector");
        System.out.println("[CollectorMain] Repo     : " + repoPath);
        System.out.println("[CollectorMain] Output   : " + outputDir);
        System.out.println("[CollectorMain] MaxPairs : " + maxPairs);
        System.out.println("=================================================");

        File repoDir = new File(repoPath);
        if (!repoDir.exists() || !repoDir.isDirectory()) {
            System.err.println("[CollectorMain] ERREUR : chemin introuvable : " + repoPath);
            System.exit(1);
        }

        File gitDir = new File(repoDir, ".git");
        if (!gitDir.exists()) {
            System.err.println("[CollectorMain] ERREUR : pas de depot Git dans : " + repoPath);
            System.err.println("[CollectorMain] Dossier .git attendu dans     : " + gitDir.getAbsolutePath());
            System.exit(1);
        }

        GitRepositoryManager gitManager = new GitRepositoryManager();
        try {
            gitManager.openLocal(repoPath);
            System.out.println("[CollectorMain] Depot Git ouvert avec succes.");

            File outDir = new File(outputDir);
            outDir.mkdirs();

            CollectionPipeline pipeline = new CollectionPipeline(gitManager, outDir);
            pipeline.setMaxPairs(maxPairs);
            pipeline.run();

            System.out.println("\n[CollectorMain] Resume :");
            System.out.println("  Paires collectees : " + pipeline.getPairsCollected());
            System.out.println("  Paires identiques : " + pipeline.getPairsIdentical());
            System.out.println("  Paires invalides  : " + pipeline.getPairsInvalid());
            System.out.println("  Paires skippees   : " + pipeline.getPairsSkipped());
            System.out.println("[CollectorMain] Dataset disponible dans : "
                    + outDir.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("[CollectorMain] ERREUR fatale : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            gitManager.close();
        }
    }

    /**
     * Remonte depuis user.dir jusqu'à trouver un dossier contenant .git
     * Fonctionne quel que soit le niveau d'imbrication du module Maven
     */
    private static File findProjectRoot() {
        File dir = new File(System.getProperty("user.dir")).getAbsoluteFile();

        // Remonte jusqu'à trouver un .git
        File current = dir;
        while (current != null) {
            if (new File(current, ".git").exists()) {
                System.out.println("[CollectorMain] Racine projet detectee : "
                        + current.getAbsolutePath());
                return current;
            }
            current = current.getParentFile();
        }

        // Aucun .git trouvé → utilise user.dir tel quel
        System.out.println("[CollectorMain] Aucun .git trouve, utilisation de : "
                + dir.getAbsolutePath());
        return dir;
    }

    private static int parseIntSafe(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("[CollectorMain] maxPairs invalide, defaut: " + defaultValue);
            return defaultValue;
        }
    }
}
