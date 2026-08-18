package com.github.phillima.asniffer;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

import com.github.phillima.asniffer.git.models.AMReportGitModel;
import com.github.phillima.asniffer.git.models.GitCommitModel;
import com.github.phillima.asniffer.git.services.GitService;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.output.IHistoryReport;
import com.github.phillima.asniffer.output.json.HistoryJsonReport;
import com.github.phillima.asniffer.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.phillima.asniffer.output.IReport;
import com.github.phillima.asniffer.parameters.ParamMapper;
import com.github.phillima.asniffer.parameters.Parameters;
import com.github.phillima.asniffer.utils.ReportTypeUtils;


public class Main {

	private static final Logger logger =
			LogManager.getLogger(Main.class);

	//Called as an executable jar                                                 	                                          
	public static void main(String[] args) throws FileNotFoundException {
		LocalTime start = LocalTime.now();

		ifInvalidArgsPrintHowToUseAndExit(args);

		//Read the parameters
		Parameters param = new ParamMapper().map(args, Parameters.class);

		run(
				param.getProjectPath(),
				param.getReportPath(),
				param.isAMultiProject(),
				ReportTypeUtils.getReportInstance(param.getReportType()),
				param.getFilterPath(),
				param.isHistoryMode()
		);

		LocalTime finish = LocalTime.now();
		LocalTime diff = finish.minusNanos(start.toNanoOfDay());

		System.out.println("Execution time: " + diff);
	}

	public static void run(String projectPath, String reportPath, boolean multiProject,
						   IReport reportType, String filterPath,
						   boolean history
	) {

		if (history) runHistory(projectPath, reportPath, multiProject, reportType, filterPath);
		else runCurrent(projectPath, reportPath, multiProject, reportType, filterPath);

	}

	//run asniffer in current state of the project
    private static void runCurrent(
            final String projectPath,
            final String reportPath,
            final boolean multiProject,
            final IReport reportType,
            final String filterPath
    ) {
		boolean writeOutput = true;
		ASniffer runner = new ASniffer(projectPath, reportPath, reportType, filterPath);

		if (!multiProject) {
			logger.info("Initializing extraction for current single project.");
			runner.collectSingle(writeOutput);
		} else {
			logger.info("Initializing extraction for current multiple projects.");
			runner.collectMultiple(writeOutput);
		}
    }

	//run asniffer in each commit in the project
	private static void runHistory(
            final String projectPath,
            final String reportPath,
            final boolean multiProject,
            final IReport reportType,
            final String filterPath
    ) {
		if (!multiProject) {
			runHistorySingle(projectPath, reportPath, reportType, filterPath);
		} else {
			runHistoryMultiple(projectPath, reportPath, reportType, filterPath);
		}
    }

	//walks the commit history of ONE git repository and writes its <project>-history.json
	private static void runHistorySingle(
            final String projectPath,
            final String reportPath,
            final IReport reportType,
            final String filterPath
    ) {
		boolean writeOutput = false;

		GitService historyService = new GitService();

		List<GitCommitModel> commits = historyService.getCommits(projectPath);
		GitCommitModel currentCommit = commits.getFirst();

		ASniffer runner = new ASniffer(projectPath, reportPath, reportType, filterPath);

		IHistoryReport historyReport = new HistoryJsonReport();
		String projectName = FileUtils.getProjectName(Paths.get(projectPath));
		historyReport.start(reportPath, projectName);

		try {
			for (GitCommitModel commit : commits) {
				logger.info("Initializing extraction for commit: {}", commit.hash());

				try {
					historyService.checkout(projectPath, commit.hash());
					AMReport report = runner.collectSingle(writeOutput);
					historyReport.append(toReportGitModel(report, commit));
				} catch (Exception ex) {
					logger.error("Failed to analyze commit {} ({}): {}", commit.shortHash(), commit.message(), ex.getMessage(), ex);
				}
			}
		} finally {
			historyReport.finish();
		}

		historyService.checkout(projectPath, currentCommit.hash());
    }

	//runs runHistorySingle once for every subproject found under rootPath
	private static void runHistoryMultiple(
            final String rootPath,
            final String reportPath,
            final IReport reportType,
            final String filterPath
    ) {
		for (Path subprojectPath : FileUtils.getProjectsPath(rootPath)) {
			logger.info("Initializing history extraction for project: {}", subprojectPath);
			try {
				runHistorySingle(subprojectPath.toString(), reportPath, reportType, filterPath);
			} catch (Exception ex) {
				logger.error("Failed to analyze history for project {}: {}", subprojectPath, ex.getMessage(), ex);
			}
		}
    }

	private static AMReportGitModel toReportGitModel(AMReport report, GitCommitModel commit) {
		return new AMReportGitModel(
				report.getProjectName(),
				commit.hash(),
				commit.authorName(),
				commit.date(),
				commit.message(),
				report
		);
	}

	public static void run(String projectPath, String reportPath) throws FileNotFoundException {
		run(projectPath,reportPath,false,ReportTypeUtils.getReportInstance(Parameters.DEFAULT_PROJECT_REPORT), null, false);
	}

	public static void run(String projectPath) throws FileNotFoundException {
		run(projectPath,projectPath);
	}


	private static void ifInvalidArgsPrintHowToUseAndExit(String[] args) {
		if(args ==null || args.length < 2) {
			System.out.println("To use ASniffer please run the "
					+ "command as follows:");
			System.out.println("java -jar asniffer.jar ");
			System.out.println("-p <path to project> (A complete path to where your project(s) is located)");
			System.out.println("-r <path to report> (Path where you would like to store your report. "
					+ "If no path is provided, ASniffer will place the report in your project folder.");
			System.out.println("-m <single/multi> (you have to specify single or multi. Single is the default value. Multi specifies that the directory contains multiple projects");
			System.out.println("-t <report type> (the report type can be variations of json. If no value is specified, a default json file will be generated. README file contains more details about the different json reports.");
			System.out.println("-f <path to annotation filter> (Optional file containing the annotations to be analyzed)");
			System.out.println("-h <current/history> (Optional execution mode. "
					+ "Use 'history' to analyze all commits in the repository history, "
					+ "or 'current' to analyze only the current project state. "
					+ "If no value is provided, 'current' is used by default.)");
			System.exit(1);
		}
	}


}
