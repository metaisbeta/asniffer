package com.github.phillima.asniffer.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AnnotationsGlossary {
    public static String file = "./src/main/java/com/github/phillima/asniffer/utils/Glossary.json";

    private static Map<String, Set<String>> ANNOTATION_NAME_TO_SCHEMA;

    private static void loadMap(){
        try (BufferedReader fileReader = Files.newBufferedReader(Paths.get(file));){            
            Gson gson = new Gson();
            ANNOTATION_NAME_TO_SCHEMA = gson.fromJson(fileReader, new TypeToken<HashMap<String, HashSet<String>>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Optional<Set<String>> get(String key){
        if(ANNOTATION_NAME_TO_SCHEMA == null){
            loadMap();
        }
        return Optional.ofNullable(ANNOTATION_NAME_TO_SCHEMA.get(key));
        
    }

    public static String get_(String key){
        try (BufferedReader fileReader = Files.newBufferedReader(Paths.get(file));
            JsonReader reader = new JsonReader(fileReader)) {
                        
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

}
