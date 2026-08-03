package com.github.phillima.test.filter;

import com.github.phillima.asniffer.filter.AnnotationFilter;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The Class TestAnnotationFilter
 *
 * @author Pedro Junho Silveira
 * @since 03/08/2026
 */

public class TestAnnotationFilter {

    @Test
    public void shouldAcceptAnnotationWhenFilterIsDisabled() {
        AnnotationFilter filter = AnnotationFilter.disabled();

        assertTrue(filter.matches("Autowired"));
        assertTrue(filter.matches("Override"));
    }

    @Test
    public void shouldAcceptConfiguredAnnotation() {
        AnnotationFilter filter = AnnotationFilter.of(
                Set.of("Autowired")
        );

        assertTrue(filter.matches("Autowired"));
    }

    @Test
    public void shouldRejectAnnotationNotConfigured() {
        AnnotationFilter filter = AnnotationFilter.of(
                Set.of("Autowired")
        );

        assertFalse(filter.matches("Override"));
    }

}
