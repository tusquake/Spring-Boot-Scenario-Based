package com.interview.debug.aspect;

import com.interview.debug.config.DataSourceType;
import com.interview.debug.util.RoutingContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * Aspect to switch DataSourceType based on @Transactional(readOnly = true).
 * This runs BEFORE the standard Spring Transaction aspect (due to @Order).
 */
@Aspect
@Component
@Order(0) // Higher precedence than TransactionInterceptor
@Slf4j
public class ReadOnlyAspect {

    @Around("@annotation(org.springframework.transaction.annotation.Transactional) || @within(org.springframework.transaction.annotation.Transactional)")
    public Object setRoutingContext(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 1. Check method level
        Transactional transactional = AnnotationUtils.findAnnotation(method, Transactional.class);
        if (transactional == null) {
            // 2. Check class level
            transactional = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), Transactional.class);
        }

        if (transactional != null && transactional.readOnly()) {
            log.debug("Detected @Transactional(readOnly=true). Setting context to READ_ONLY.");
            RoutingContextHolder.set(DataSourceType.READ_ONLY);
        } else {
            log.debug("Detected read-write @Transactional. Setting context to WRITE.");
            RoutingContextHolder.set(DataSourceType.WRITE);
        }

        try {
            return joinPoint.proceed();
        } finally {
            RoutingContextHolder.clear();
        }
    }
}
