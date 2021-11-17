package annotationtest;

import java.util.*;
import br.com.metaisbeta.schema.annotations.Override;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@AnnotationSamePackage
@RestController
@AutoConfigurationPackage
public class TestSchemaWildCard {

    @br.com.metaisbeta.schema.annotations.RestController
    private int member1;

    @br.com.metaisbeta.annotations.RestController
    private int member2;

    @Autowired
    private int member3;

    @Override
    public int hashCode() {
        return Objects.hash(member1, member2, member3);
    }
}