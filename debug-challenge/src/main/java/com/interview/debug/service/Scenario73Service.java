package com.interview.debug.service;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class Scenario73Service {
    private static final Logger log = LoggerFactory.getLogger(Scenario73Service.class);

    /**
     * Simulated Service B
     * Timeout: 1s (configured in properties)
     */
    @TimeLimiter(name = "serviceB", fallbackMethod = "serviceBFallback")
    public CompletableFuture<String> callServiceB(int delayMs) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Service B processing with delay {}ms...", delayMs);
                Thread.sleep(delayMs);
                return "Service B Success";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "Service B Interrupted";
            }
        });
    }

    public CompletableFuture<String> serviceBFallback(Exception e) {
        log.warn("Service B Timeout/Error: {}", e.getMessage());
        return CompletableFuture.completedFuture("Service B Fallback (Timeout)");
    }
}
