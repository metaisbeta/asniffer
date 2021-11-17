package com.github.phillima.asniffer.utils;

import java.util.Map;

public class Glossary {
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
