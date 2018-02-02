package br.inpe.cap.asniffer;

import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import br.inpe.cap.asniffer.metric.MetricCollector;


public class MetricsExecutor extends FileASTRequestor{

	private AMReport report;
	private Callable<List<MetricCollector>> metrics;
	
	
	public MetricsExecutor(Callable<List<MetricCollector>> metrics, String projectName) {
		this.metrics = metrics;
		this.report = new AMReport(projectName);
	}

	@Override
	public void acceptAST(String sourceFilePath, 
			CompilationUnit cu) {
		
		MetricResult result = null;
		
		try {
			ClassInfo info = new ClassInfo();
			cu.accept(info);
			if(info.getClassName()==null) return;
		
			result = new MetricResult(sourceFilePath, info.getClassName(), info.getType());
			
			//int loc = new LOCCalculator().calculate(new FileInputStream(sourceFilePath));
			//result.setLoc(loc);
			
			for(MetricCollector visitor : metrics.call()) {
				visitor.execute(cu, result, report);
				visitor.setResult(result);
			}
			report.add(result);
		} catch(Exception e) {
			//if(result!=null) result.error();
		}
	}
	
	public AMReport getReport() {
		return report;
	}
	
}
