package com.github.phillima.test.utils;

import com.github.phillima.asniffer.utils.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class TestFileUtils {

    private FileUtils fileUtils;

    @Test
    public void TestPath(){
        if(SystemUtils.IS_OS_WINDOWS) {
            Path path = Paths.get("C:\\Users\\Usuario\\Documents\\Projetos\\spring-retry");
            String target = "spring-retry";
            assertEquals(FileUtils.getProjectName(path), target);
        } else {
            Path path = Paths.get("/usr2/username/bin/usr/local/spring-retry");
            String target = "spring-retry";
            assertEquals(FileUtils.getProjectName(path),target);
        }
    }
}
