package annotationtest;
import org.springframework.context.myimport.MyImport;
import com.salesmanager.shop.validation.FieldMatch;
import org.springframework.web.bind.annotation.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/services")
@FieldMatch.List({
        @FieldMatch(first="password",second="checkPassword",message="password.notequal")
})
public class SchemaTest{
    
    @org.springframework.context.annotation.Import
    public void method() {}
   
    @MyImport
    public void methodC() {}

    @Override
    public void methodB(){}

    @com.salesmanager.shop.validation.FieldMatch.List({
            @FieldMatch(first="password",second="checkPassword",message="password.notequal")
    })
    public void methodD() {}

		
}