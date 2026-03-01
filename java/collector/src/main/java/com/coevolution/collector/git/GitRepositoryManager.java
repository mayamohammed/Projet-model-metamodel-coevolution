package com.coevolution.collector.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * GitRepositoryManager - ouvre et ferme un depot Git local.
 */
public class GitRepositoryManager {

    private Repository repository;
    private Git git;

    public GitRepositoryManager() {}

    /**
     * Ouvre un depot Git local depuis son chemin absolu.
     *
     * @param repoPath chemin absolu vers la racine du depot Git
     * @throws IOException si le depot ne peut pas etre ouvert
     */
    public void openLocal(String repoPath) throws IOException {
        File repoDir = new File(repoPath);

        if (!repoDir.exists() || !repoDir.isDirectory()) {
            throw new IOException("[GitRepositoryManager] Chemin invalide : " + repoPath);
        }

        // Chercher le .git dans repoPath directement
        File gitDir = new File(repoDir, ".git");

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        builder.setMustExist(true);

        if (gitDir.exists() && gitDir.isDirectory()) {
            builder.setGitDir(gitDir);
        } else {
            // Chercher dans les parents
            builder.findGitDir(repoDir);
        }

        this.repository = builder.build();
        this.git = new Git(this.repository);

        System.out.println("[GitRepositoryManager] Depot ouvert : "
                + this.repository.getDirectory().getAbsolutePath());
    }

    /**
     * Retourne le Repository JGit ouvert.
     */
    public Repository getRepository() {
        if (repository == null) {
            throw new IllegalStateException(
                    "[GitRepositoryManager] Aucun depot ouvert. Appelez openLocal() d abord.");
        }
        return repository;
    }

    /**
     * Retourne l objet Git JGit.
     */
    public Git getGit() {
        return git;
    }

    /**
     * Ferme le depot et libere les ressources.
     */
    public void close() {
        if (git != null) {
            git.close();
        }
        if (repository != null) {
            repository.close();
        }
        System.out.println("[GitRepositoryManager] Ressources fermees.");
    }
}