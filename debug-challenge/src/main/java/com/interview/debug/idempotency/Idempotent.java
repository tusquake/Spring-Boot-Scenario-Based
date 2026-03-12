package com.interview.debug.idempotency;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    /**
     * Optional header name for the idempotency key.
     * Default is "Idempotency-Key".
     */
    String headerName() default "Idempotency-Key";
}
