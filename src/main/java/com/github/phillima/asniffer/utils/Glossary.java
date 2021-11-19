package com.github.phillima.asniffer.utils;

import java.util.Map;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import com.google.gson.stream.JsonReader;

public class Glossary {
    public static String file = "Glossary.json";

    public static String get(String key){
        try {
            BufferedReader fileReader = Files.newBufferedReader(Paths.get(file));
            
            // Streaming json reader for low memory usage
            JsonReader reader = new JsonReader(fileReader);
            
            reader.beginObject();
            while (reader.hasNext()) {
                if(reader.nextName() == key){
                    return reader.nextString();
                }
            }
            reader.endObject();

            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Map<String, String> ANNOTATION_NAME_TO_SCHEMA = Map.of(
        //SPRING
        "Autowired", "org.springframework.web.bind.annotation",
        "AutoConfigurationPackage", "org.springframework.boot.autoconfigure",
        "RestController", "org.springframework.beans.factory.annotation",
        //JPA
        "",""
        //Junit
        );
}
