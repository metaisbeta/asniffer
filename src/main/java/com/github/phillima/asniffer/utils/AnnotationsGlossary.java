package com.github.phillima.asniffer.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AnnotationsGlossary {
    public static String file = "/Glossary.json";

    private static Map<String, Set<String>> ANNOTATION_NAME_TO_SCHEMA;

    private static void loadMap(){
        var stream = new InputStreamReader(AnnotationsGlossary.class.getResourceAsStream(file));
        try (BufferedReader fileReader = new BufferedReader(stream)){            
            Gson gson = new Gson();
            var type = new TypeToken<HashMap<String, HashSet<String>>>(){}.getType();
            ANNOTATION_NAME_TO_SCHEMA = gson.fromJson(fileReader, type);
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

}
