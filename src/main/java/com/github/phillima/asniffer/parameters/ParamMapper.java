package com.github.phillima.asniffer.parameters;

import java.util.HashMap;
import java.util.Map;

import com.github.phillima.asniffer.parameters.container.ParamMapperContainer;
import com.github.phillima.asniffer.parameters.container.TextValueContainer;
import org.apache.commons.beanutils.BeanUtils;

import com.github.phillima.asniffer.parameters.container.IsParameterPresentContainer;
import net.sf.esfinge.metadata.AnnotationReader;

public class ParamMapper {
	
	public <E> E map(String[] args, Class<E> paramClass) {
		ParamMapperContainer c = readMetadata(paramClass);
		@SuppressWarnings("unchecked")
		E paramInstance = (E) readParameters(args,c);
		return paramInstance;
	}
	
	private ParamMapperContainer readMetadata(Class<?> paramClass) {
		
		AnnotationReader reader = new AnnotationReader();
		ParamMapperContainer container = null;
		try {
			container = reader.readingAnnotationsTo(paramClass, ParamMapperContainer.class);
			container.setClassToParameter(paramClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return container;
		
	}
	
	private Object readParameters(String[] args, ParamMapperContainer c) {
		
		if(!args[0].startsWith("-")) {
			throw new ParameterReadingException("Cannot start with a value");
		}
		
		Map<String, String> parameters = new HashMap<>();
		
		if (args.length != 0) {
			for (int i = 0; i < args.length; ) {
				String key = args[i++].substring(1);
				String value = "";
				while (i < args.length && !args[i].startsWith("-")) {
					value += args[i++] + " ";
				}
				parameters.put(key, value.trim());
			}

		}
		try {
			Object o = c.getClassToParameter().newInstance();
			
			//IsParameterPresent
			for (IsParameterPresentContainer ipContainer : c.getFieldsWithIsPresent()) {
				BeanUtils.setProperty(o, ipContainer.getName(), false);
			
				for(String argument: args) {
					if(argument.equals("-"+ipContainer.getValue())) {
						BeanUtils.setProperty(o, ipContainer.getName(), true);
					}
				}
			}
			
			//TextValue
			for(TextValueContainer tvContainer: c.getFieldsWithTextValue()) {
				if(parameters.containsKey(tvContainer.getValue())) {
					if(tvContainer.isMandatory() && parameters.get(tvContainer.getValue()).isEmpty()) 
						throw new ParameterReadingException("Parameter: -" + tvContainer.getValue() + " is empty. "
								+ "Please provided a value for field: " + tvContainer.getName());
					try {
						BeanUtils.setProperty(o, tvContainer.getName(), parameters.get(tvContainer.getValue()));
					}catch (Exception e) {
						e.printStackTrace();
					}
				} else if(tvContainer.isMandatory())
						throw new ParameterReadingException("Parameter: -" + tvContainer.getValue() + " is mandatory. "
								+ "Please provided a value for field: " + tvContainer.getName());
			}	
			return o;	
		} catch (Exception e) {
			throw new ParameterReadingException("Cannot instantiate parameter class",e);
		}
	}

}
