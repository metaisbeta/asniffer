package com.github.phillima.asniffer.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class AnnotationFilter
 *
 * @author Pedro Junho Silveira
 * @since 28/07/2026
 */

public class AnnotationFilter {

    private static final Logger logger =
            LogManager.getLogger(AnnotationFilter.class);

    private final Set<String> annotations;

    public AnnotationFilter(final Set<String> annotations) {
        this.annotations = annotations;
    }

    public static AnnotationFilter disabled() {
        return new AnnotationFilter(Collections.emptySet());
    }

    public static AnnotationFilter fromFile(Path filterPath) {
        Set<String> annotations = new HashSet<>();

        try {
            for (String line : Files.readAllLines(filterPath)) {
                String annotationName = line.trim();

                if (annotationName.isBlank() || annotationName.startsWith("#")) {
                    continue;
                }

                annotations.add(annotationName);
            }
        } catch (IOException ex){
            logger.error("");
        }

        return new AnnotationFilter(annotations);
    }

    public boolean isEnabled() {
        return !annotations.isEmpty();
    }

    public boolean matches(String annotation) {
        if (!isEnabled()) {
            return true;
        }

        return annotations.contains(annotation)
                || annotations.contains(getSimpleName(annotation));
    }

    private String getSimpleName(String annotation) {
        int lastDot = annotation.lastIndexOf('.');

        if (lastDot < 0) {
            return annotation;
        }

        return annotation.substring(lastDot + 1);
    }

    public static AnnotationFilter of(Set<String> annotations){
        return new AnnotationFilter(
                annotations
        );
    }
}
