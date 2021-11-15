package org.springframework.boot.context.properties;

import org.springframework.boot.context.properties.bind.DefaultValue;

class ConfigurationPropertiesTests {

	static class ConstructorParameterWithFormatProperties {

		ConstructorParameterWithFormatProperties(@DefaultValue("2d") String duration, @DefaultValue("3y") String period) {

		}

	}

}
