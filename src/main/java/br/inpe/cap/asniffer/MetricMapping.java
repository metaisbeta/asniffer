package br.inpe.cap.asniffer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import br.inpe.cap.asniffer.annotations.Clazz;
import br.inpe.cap.asniffer.annotations.MultMetric;
import br.inpe.cap.asniffer.annotations.Package_;
import br.inpe.cap.asniffer.annotations.SimpMetric;
import br.inpe.cap.asniffer.output.ClassPojo;
import br.inpe.cap.asniffer.output.MetricsValues;
import br.inpe.cap.asniffer.output.MultiMetric;
import br.inpe.cap.asniffer.output.PackagePojo;
import br.inpe.cap.asniffer.output.SimpleMetric;

public class MetricMapping {

	private static List<SimpleMetric> simpleMetric;
	private static List<MultiMetric> multiMetric;
	private static List<ClassPojo> classPojo = new ArrayList<>();
	private static List<PackagePojo> packagePojo = new ArrayList<>();
	private static int packageIndex = 0;
	private static String currentPackageName = "", oldPackageName = "";
	
	public static List<PackagePojo> getPackagePojo() {
		return packagePojo;
	}

	public static void map(ASniffer asniffer) {
		//Initialize lists
		simpleMetric = new ArrayList<>();
		multiMetric = new ArrayList<>();
		Class<?> clazz = asniffer.getClass();
		String className = "";
		for(Method m : clazz.getMethods()) {
			try {
				boolean isGetter = m.getName().startsWith("get");
				boolean noParam = (m.getParameterTypes().length == 0);
				boolean notGetClass = !m.getName().equals("getClass");
				boolean notGetInstance = !m.getName().equals("getInstance");
				if(isGetter && noParam && notGetClass && notGetInstance) {
					Object getterValue = m.invoke(asniffer);
					String propName = m.getName().substring(3,4).toLowerCase() + m.getName().substring(4).toLowerCase();

					if(m.isAnnotationPresent(Clazz.class)) {
						className = (String)getterValue;
					}
					
					if(m.isAnnotationPresent(Package_.class)) {
						currentPackageName = (String)getterValue;
					}
					
					if(m.isAnnotationPresent(SimpMetric.class)) {
						handleSimpleMetric(propName, getterValue);
					}
					
					if(m.isAnnotationPresent(MultMetric.class)) {
						handleMultiMetric(propName, getterValue);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		//Mapping for this class is over
		handleClassPackage(className);
		
	}

	private static void handleClassPackage(String className) {
		if(oldPackageName.equals("")) {
			oldPackageName = currentPackageName;
			classPojo.add(new ClassPojo(className, simpleMetric, multiMetric));
			packagePojo.add(new PackagePojo(oldPackageName, classPojo));
			return;
		} 
		if(oldPackageName.equals(currentPackageName)) {
			classPojo.add(new ClassPojo(className, simpleMetric, multiMetric));
			packagePojo.set(packageIndex, new PackagePojo(oldPackageName, classPojo));
		}else {
			oldPackageName = currentPackageName;
			classPojo = new ArrayList<>();
			classPojo.add(new ClassPojo(className, simpleMetric, multiMetric));
			packagePojo.add(new PackagePojo(oldPackageName, classPojo));
			packageIndex++;
		}
	}

	private static void handleSimpleMetric(String propName, Object metricValue) {
		simpleMetric.add(new SimpleMetric(propName, (Integer) metricValue));
	}
	
	private static void handleMultiMetric(String propName, Object metricValue) {
		List<MetricsValues> metricValues = new ArrayList<>();
		Map<Object, Integer> values = (Map<Object, Integer>) metricValue;
		values.forEach((k, v) -> {
			if(k instanceof AbstractTypeDeclaration) {
				String name = ((AbstractTypeDeclaration) k).getName().toString();
				metricValues.add(new MetricsValues(name,v));
			}else if(k instanceof MethodDeclaration) {
				String name = ((MethodDeclaration) k).getName().toString();
				metricValues.add(new MetricsValues(name,v));
			} else if(k instanceof FieldDeclaration) {
				Object obj = ((FieldDeclaration) k).fragments().get(0);
				String name = "";
				if(obj instanceof VariableDeclarationFragment)
					name = ((VariableDeclarationFragment)obj).getName().toString();
				metricValues.add(new MetricsValues(name,v));
			}
			else if(k instanceof Annotation) 
				metricValues.add(new MetricsValues(((Annotation)k).getTypeName().toString(),v));
		});
		multiMetric.add(new MultiMetric(propName,metricValues));
	}
}
