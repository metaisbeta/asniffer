package com.github.phillima.test.filter;

import com.github.phillima.asniffer.AmFactory;
import com.github.phillima.asniffer.filter.AnnotationFilter;
import com.github.phillima.asniffer.model.AMReport;
import com.github.phillima.asniffer.model.ClassModel;
import com.github.phillima.asniffer.model.PackageModel;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * The Class TestFilteredMetrics
 *
 * @author Pedro Junho Silveira
 * @since 03/08/2026
 */

public class TestFilteredMetrics {

    private static AMReport report;

    @BeforeClass
    public static void setUp() {
        String testFilePath =
                System.getProperty("user.dir")
                        + "/annotationtest/annotation-declaration-test";

        AnnotationFilter filter =
                AnnotationFilter.of(Set.of("Documented"));

        report = AmFactory
                .createAm(testFilePath, "project", filter)
                .calculate();
    }

    @Test
    public void shouldCountOnlyFilteredAnnotation() {
        PackageModel packageModel = report.getPackageByName(
                "com.salesmanager.shop.validation"
        );

        ClassModel clazz = packageModel.getClassModel(
                "com.salesmanager.shop.validation.Enum"
        );

        assertEquals(1, clazz.getClassMetric("AC"));
        assertEquals(1, clazz.getClassMetric("UAC"));
        assertEquals(1, clazz.getClassMetric("ASC"));
    }
}
