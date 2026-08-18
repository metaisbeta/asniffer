package com.github.phillima.asniffer.git.models;

import java.time.Instant;
import java.util.List;

/**
 * The Class GitCommitModel
 *
 * @author Pedro Junho Silveira
 * @since 05/08/2026
 */

public record GitCommitModel(
        String hash,

        String shortHash,

        String authorName,

        String authorEmail,

        Instant date,

        String message,

        List<String> parentHashes
) {
}
