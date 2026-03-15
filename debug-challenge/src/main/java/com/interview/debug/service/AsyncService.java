package com.interview.debug.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Async("backgroundTaskExecutor") // Matches the bean name in AsyncConfig
    public void runLongTask(String taskName, int seconds) {
        logger.info("[Async Service] Starting long-running task: {} (Duration: {}s)", taskName, seconds);
        
        try {
            // Simulate heavy work like report generation or batch processing
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            logger.error("[Async Service] Task {} was interrupted!", taskName);
            Thread.currentThread().interrupt();
        }
        
        logger.info("[Async Service] COMPLETED long-running task: {}", taskName);
    }
}
