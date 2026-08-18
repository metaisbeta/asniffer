package com.github.phillima.asniffer.output;

import com.github.phillima.asniffer.git.models.AMReportGitModel;

public interface IHistoryReport {

	void start(String reportPath, String projectName);

	void append(AMReportGitModel analysis);

	void finish();

}
