package annotationtest;
import org.springframework.context.myimport.MyImport;

public class SchemaTest{
    
    @org.springframework.context.annotation.Import
    public void method() {}
   
    @MyImport
    public void methodC() {}

    @Override
    public void methodB(){}

		
}