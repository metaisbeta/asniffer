package annotationtest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@AnnotationTest
@br.inatel.cdg.annotation.AnnotationFullyName
public class InnerAnnotationTypeTest {

	private int member1;

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	private static @interface Foo{

	}

	private static class SampleInnerClass{
		@AnnotationTest
		@Foo
		private final int innerField;

		private SampleInnerClass(int innerField) {
			this.innerField = innerField;
		}
	}

}