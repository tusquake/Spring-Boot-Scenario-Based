package com.interview.debug.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    @Async("backgroundTaskExecutor")
    public CompletableFuture<String> processAsyncTask() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Service: Async task running on thread: " + Thread.currentThread().getName());
        System.out.println("Service: Checking context... User is: " + username);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return CompletableFuture.completedFuture("Success! User was propagated: " + username);
    }

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
