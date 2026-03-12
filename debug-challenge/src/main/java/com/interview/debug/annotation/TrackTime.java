package com.interview.debug.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to track the execution time of methods.
 * Intercepted by PerformanceTrackerAspect.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackTime {
    String value() default ""; // Optional name for the metric
}
