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
            sample.stop(Timer.builder("method.execution.time")
                    .description("Time taken to execute methods")
                    .tag("method", methodName)
                    .register(meterRegistry));
            
            logger.info("Performance monitoring recorded for: {}", methodName);
        }
    }
}
