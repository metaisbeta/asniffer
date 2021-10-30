package com.github.phillima.test.asniffer;

import com.github.phillima.asniffer.AmFactory;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.CodeElementModel;
import com.github.phillima.asniffer.model.CodeElementType;
import com.github.phillima.asniffer.model.PackageModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAnnotationDeclaration {

    private static AMReport report;

    @BeforeClass
    public static void setUp() {
        String testFilePath = System.getProperty("user.dir") + "/annotationtest/annotation-declaration-test";
        report = AmFactory.createAm(testFilePath, "project").calculate();
    }

    @Test
    public void testPackages(){

        report.getPackages().forEach(t -> System.out.println(t.getPackageName()));

        Assert.assertEquals(3, report.getPackages().size());

        PackageModel package1 = report.getPackageByName("com.salesmanager.shop.validation");
        PackageModel package2 = report.getPackageByName("com.salesmanager.shop.model.customer");
        PackageModel package3 = report.getPackageByName("com.salesmanager.shop.model.order.transaction");

        Assert.assertNotNull(package1);
        Assert.assertNotNull(package2);
        Assert.assertNotNull(package3);

    }

    @Test
    public void testAnnotationDeclaration1(){

        PackageModel package_ = report.getPackageByName("com.salesmanager.shop.validation");

        ClassModel clazz = package_.getClassModel("com.salesmanager.shop.validation.Enum");

        int ac = clazz.getClassMetric("AC");
        int asc = clazz.getClassMetric("ASC");
        String schema1 = clazz.getAnnotationSchema("Documented-12");
        String schema2 = clazz.getAnnotationSchema("Constraint-13");

        Assert.assertEquals("java.lang.annotation",schema1);
        Assert.assertEquals("javax.validation",schema2);
        Assert.assertEquals(2, asc);
        Assert.assertEquals(4, ac);
        Assert.assertEquals(CodeElementType.ANNOTATION_DECLARATION,clazz.getType());
    }



    @Test
    public void testAnnotationDeclaration2(){

        PackageModel package_ = report.getPackageByName("com.salesmanager.shop.validation");

        ClassModel clazz = package_.getClassModel("com.salesmanager.shop.validation.FieldMatch");

        int ac = clazz.getClassMetric("AC");
        int asc = clazz.getClassMetric("ASC");
        String schema1 = clazz.getAnnotationSchema("Retention-38");
        String schema2 = clazz.getAnnotationSchema("Constraint-35");

        //Fetch @interface List
        CodeElementModel innerAnnotDeclaration =  clazz.getElementReport("List",CodeElementType.ANNOTATION_DECLARATION);

        //Assert FieldMatch annotation declaration
        Assert.assertNotNull(innerAnnotDeclaration);
        Assert.assertEquals("java.lang.annotation",schema1);
        Assert.assertEquals("javax.validation",schema2);
        Assert.assertEquals(2, asc);
        Assert.assertEquals(7, ac);
        Assert.assertEquals(CodeElementType.ANNOTATION_DECLARATION,clazz.getType());

    }

}