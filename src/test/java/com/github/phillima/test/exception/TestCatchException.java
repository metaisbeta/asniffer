package com.github.phillima.test.exception;

import com.github.phillima.asniffer.AmFactory;
import com.github.phillima.asniffer.model.AMReport;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class TestCatchException {

    private static AMReport report;

    @Test
    public void testHandleParseErrorExceptionAndLog() {
        String filePath = Paths.get(System.getProperty("user.dir") + "/annotationtest/parse-file-test").toString();
        report = AmFactory.createAm(filePath, "parse-file-test").calculate();

        assertEquals("parse-file-test", report.getProjectName());
        assertEquals(0, report.getPackages().size());
    }
}
