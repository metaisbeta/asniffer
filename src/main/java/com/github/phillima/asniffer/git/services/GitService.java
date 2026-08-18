package com.github.phillima.asniffer.git.services;

import com.github.phillima.asniffer.git.models.GitCommitModel;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Class GitService
 *
 * @author Pedro Junho Silveira
 * @since 05/08/2026
 */

public class GitService {

    public List<GitCommitModel> getCommits(String projectPath){
        try (Repository repository = openRepository(projectPath);
             RevWalk revWalk = new RevWalk(repository))
        {
            ObjectId headId = repository.resolve(Constants.HEAD);

            if (headId == null) {
                throw new IllegalStateException("Não foi possível localizar a HEAD do repositório: " + projectPath);
            }

            RevCommit headCommit = revWalk.parseCommit(headId);

            revWalk.sort(RevSort.COMMIT_TIME_DESC);
            revWalk.markStart(headCommit);

            List<GitCommitModel> commits = new ArrayList<>();

            for (RevCommit commit : revWalk) {
                commits.add(toModel(commit));
            }

            return commits;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Repository openRepository(String projectPath) throws IOException {

        File projectDirectory = new File(projectPath);

        Repository repository = new FileRepositoryBuilder()
                .findGitDir(projectDirectory)
                .readEnvironment()
                .build();

        if (repository.getDirectory() == null) {
            repository.close();

            throw new IllegalArgumentException("O caminho informado não contém um repositório Git: " + projectPath);
        }

        return repository;
    }

    private GitCommitModel toModel(RevCommit commit) {
        List<String> parentHashes = Arrays.stream(commit.getParents())
                .map(RevCommit::getName)
                .toList();

        return new GitCommitModel(
                commit.getName(),
                commit.abbreviate(7).name(),
                commit.getAuthorIdent().getName(),
                commit.getAuthorIdent().getEmailAddress(),
                Instant.ofEpochSecond(commit.getCommitTime()),
                commit.getShortMessage(),
                parentHashes
        );
    }

    public void checkout(
            final String projectPath,
            final String hash
    ) {
        try (Repository repository = openRepository(projectPath);
                Git git = new Git(repository)
        ) {
            git.checkout().setName(hash).call();
        } catch (IOException ex) {
            throw new RuntimeException("Error opening repository: " + projectPath, ex);
        } catch (GitAPIException ex) {
            throw new RuntimeException("Error checking out commit: " + hash, ex);
        }
    }
}
