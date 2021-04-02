package com.github.phillima.asniffer.parameters;

import com.github.phillima.asniffer.annotations.IsParameterPresent;
import com.github.phillima.asniffer.annotations.Mandatory;
import com.github.phillima.asniffer.annotations.TextValue;

public class Parameters {
	
	public static final String DEFAULT_PROJECT_REPORT = "json";
	public static final String DEFAULT_SINGLE_MULTI = "single";
	
	@IsParameterPresent(name = "p")
	private boolean projPathPresent;

	@Mandatory
	@TextValue(name = "p")
	private String projectPath;

	@IsParameterPresent(name = "r")
	private boolean reportPathPresent;
	
	@TextValue(name = "r")
	private String reportPath;
	
	@IsParameterPresent(name = "t")
	private boolean reportTypePresent;
	
	@TextValue(name = "t")
	private String reportType;//default value is json
	
	@IsParameterPresent(name = "m")
	private boolean multiProjectPresent;
	
	@TextValue(name = "m")
	private String multiProject;//default value is single
	
	//Getters and Setters
	public String getMultiProject() {
		if(isMultiProjectPresent())
			if(!this.multiProject.isEmpty())
				return multiProject;
		return DEFAULT_SINGLE_MULTI;
	}

	public void setMultiProject(String multiProject) {
		this.multiProject = multiProject;
	}

	public boolean isReportTypePresent() {
		return reportTypePresent;
	}

	public void setReportTypePresent(boolean reportTypePresent) {
		this.reportTypePresent = reportTypePresent;
	}

	public boolean isMultiProjectPresent() {
		return multiProjectPresent;
	}

	public void setMultiProjectPresent(boolean multiProjectPresent) {
		this.multiProjectPresent = multiProjectPresent;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getReportPath() {
		if(isReportPathPresent())
			if(!this.reportPath.isEmpty())
				return reportPath;
		return getProjectPath();
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getReportType() {
		if(isReportTypePresent())
			if(!this.reportType.isEmpty())
				return reportType;
		return DEFAULT_PROJECT_REPORT;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public boolean isProjPathPresent() {
		return projPathPresent;
	}

	public void setProjPathPresent(boolean projPathPresent) {
		this.projPathPresent = projPathPresent;
	}
	public boolean isReportPathPresent() {
		return reportPathPresent;
	}
	public void setReportPathPresent(boolean reportPathPresent) {
		this.reportPathPresent = reportPathPresent;
	}

	//Behavior
	public boolean isAMultiProject() {
		if(getMultiProject().equalsIgnoreCase("multi"))
			return true;
		return false;
	}

}
