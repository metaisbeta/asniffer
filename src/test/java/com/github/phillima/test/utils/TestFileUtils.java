package com.github.phillima.test.utils;

import com.github.phillima.asniffer.utils.FileUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class TestFileUtils {

    private FileUtils fileUtils;

    @Test
    public void TestPathWindows(){
        Path path = Paths.get("C:\\Users\\Usuario\\Documents\\Projetos\\spring-retry");
        String target = "spring-retry";
        assertEquals(FileUtils.getProjectName(path),target);
    }

    @Test
    public void TestPathUnix(){
        Path path = Paths.get("/usr2/username/bin/usr/local/spring-retry");
        String target = "spring-retry";
        assertEquals(FileUtils.getProjectName(path),target);
    }
}
