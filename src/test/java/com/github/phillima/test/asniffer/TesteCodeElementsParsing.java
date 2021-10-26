package com.github.phillima.test.asniffer;

import com.github.phillima.asniffer.AmFactory;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.CodeElementType;
import com.github.phillima.asniffer.model.PackageModel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.BeforeClass;
import org.junit.Test;

public class TesteCodeElementsParsing {

    private static AMReport report;

    @BeforeClass
    public static void setUp() {
        String testFilePath = System.getProperty("user.dir") + "/annotationtest";
        report = AmFactory.createAm(testFilePath, "project").calculate();
    }


    @Test
    public void testReadInnerClass() {

        //There should be 5 packages
        assertEquals(5, report.getPackages().size());

        //Get the annotationtest package
        PackageModel testPackage_ = report.getPackageByName("annotationtest");
        //get the InnerClassTest

        ClassModel clazz = testPackage_.getClassModel("annotationtest.InnerClassTest");

        assertEquals(6, clazz.getElementsReport().size());
        assertNotNull(clazz.getElementReport("InnerClass1", CodeElementType.CLASS));
        assertNotNull(clazz.getElementReport("InnerClass2", CodeElementType.CLASS));
        assertNotNull(clazz.getElementReport("InnerClassTest", CodeElementType.CLASS));
        assertNotNull(clazz.getElementReport("Enum1", CodeElementType.ENUM));
        assertNotNull(clazz.getElementReport("Enum2", CodeElementType.ENUM));
        assertNotNull(clazz.getElementReport("InnerClassTest", CodeElementType.CONSTRUCTOR));

    }


    @Test
    public void testReadEnumType() {

        //There should be 5 packages
        assertEquals(5, report.getPackages().size());

        //Get the annotationtest package
        PackageModel testPackage_ = report.getPackageByName("annotationtest");
        //get the EnumTest

        ClassModel clazz = testPackage_.getClassModel("annotationtest.EnumTest");
        assertEquals(CodeElementType.ENUM, clazz.getType());
        assertEquals(1, clazz.getElementsReport().size());

    }

}