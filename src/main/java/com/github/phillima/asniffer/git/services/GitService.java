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
import java.time.ZoneOffset;
import java.util.*;

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
                commits.add(toModel(commit, null));
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

    private GitCommitModel toModel(RevCommit commit, Integer year) {
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
                parentHashes,
                year
        );
    }

    public String getCurrentRef(String projectPath) {
        try (Repository repository = openRepository(projectPath)) {
            String ref = repository.getBranch();

            if (ref == null) {
                throw new IllegalStateException("Não foi possível localizar a HEAD do repositório: " + projectPath);
            }

            return ref;
        } catch (IOException ex) {
            throw new RuntimeException("Error opening repository: " + projectPath, ex);
        }
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

    public List<GitCommitModel> getYearlyCommits(String projectPath) {
        try (Repository repository = openRepository(projectPath);
             RevWalk revWalk = new RevWalk(repository))
        {
            ObjectId headId = repository.resolve(Constants.HEAD);

            if (headId == null) {
                throw new IllegalStateException("Não foi possível localizar a HEAD do repositório: " + projectPath);
            }

            TreeMap<Integer, RevCommit> lastCommitOfYear = new TreeMap<>();

            RevCommit commit = revWalk.parseCommit(headId);
            while (commit != null) {
                int year = Instant.ofEpochSecond(commit.getCommitTime()).atZone(ZoneOffset.UTC).getYear();
                lastCommitOfYear.merge(year, commit,
                        (current, candidate) -> candidate.getCommitTime() > current.getCommitTime() ? candidate : current);

                commit = commit.getParentCount() > 0 ? revWalk.parseCommit(commit.getParent(0)) : null;
            }

            int firstYear = lastCommitOfYear.firstKey();
            int lastYear = lastCommitOfYear.lastKey();

            List<GitCommitModel> commits = new ArrayList<>();

            for (int year = lastYear; year >= firstYear; year--) {
                RevCommit lastCommit = lastCommitOfYear.floorEntry(year).getValue();
                commits.add(toModel(lastCommit, year));
            }

            return commits;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
