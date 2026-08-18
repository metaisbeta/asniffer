package com.github.phillima.asniffer.git.models;

import com.github.phillima.asniffer.model.AMReport;

import java.time.Instant;

/**
 * The Class AMReportGitModel
 *
 * @author Pedro Junho Silveira
 * @since 07/08/2026
 */

public record AMReportGitModel(

        String projectName,

        String commitHash,

        String authorName,

        Instant commitDate,

        String commitMessage,

        AMReport report

) {
}
