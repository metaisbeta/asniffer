package br.inpe.cap.processor;

import java.util.Map;

public abstract class MultiMetricProcessor {
	
	public abstract int executeSimpleMetric();
	
	public abstract Map<Object,Integer> executeMultiMetric();

}
