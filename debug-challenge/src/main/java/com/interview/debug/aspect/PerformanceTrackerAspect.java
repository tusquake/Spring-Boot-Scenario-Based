package com.interview.debug.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceTrackerAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceTrackerAspect.class);
    private final MeterRegistry meterRegistry;

    public PerformanceTrackerAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Around("@annotation(com.interview.debug.annotation.TrackTime)")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            return joinPoint.proceed();
        } finally {
            sample.stop(Timer.builder("method.execution.time").tag("method", methodName).register(meterRegistry));
            logger.info("[@Around] Performance monitoring recorded for: {}", methodName);
        }
    }

    @org.aspectj.lang.annotation.Before("@annotation(com.interview.debug.annotation.TrackTime)")
    public void beforeAdvice(org.aspectj.lang.JoinPoint joinPoint) {
        logger.info("[@Before] Security/Auth check before method: {}", joinPoint.getSignature().toShortString());
    }

    @org.aspectj.lang.annotation.AfterReturning(pointcut = "@annotation(com.interview.debug.annotation.TrackTime)", returning = "result")
    public void afterReturningAdvice(org.aspectj.lang.JoinPoint joinPoint, Object result) {
        logger.info("[@AfterReturning] Success: {} returned: {}", joinPoint.getSignature().toShortString(), result);
    }

    @org.aspectj.lang.annotation.AfterThrowing(pointcut = "@annotation(com.interview.debug.annotation.TrackTime)", throwing = "ex")
    public void afterThrowingAdvice(org.aspectj.lang.JoinPoint joinPoint, Exception ex) {
        logger.error("[@AfterThrowing] Error in method: {}. Message: {}", joinPoint.getSignature().toShortString(), ex.getMessage());
    }

    @org.aspectj.lang.annotation.After("@annotation(com.interview.debug.annotation.TrackTime)")
    public void afterFinallyAdvice(org.aspectj.lang.JoinPoint joinPoint) {
        logger.info("[@After] Finally block (Resources released) for: {}", joinPoint.getSignature().toShortString());
    }
}
