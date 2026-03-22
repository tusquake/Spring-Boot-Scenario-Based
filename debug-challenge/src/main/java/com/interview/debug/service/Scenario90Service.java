package com.interview.debug.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

/**
 * Scenario 90: Bulkhead Pattern
 * Demonstrates how to limit concurrent calls to a slow service to prevent
 * thread exhaustion.
 */
@Service
@Slf4j
public class Scenario90Service {

    /**
     * This method simulates a very slow external call (5 seconds).
     * The @Bulkhead annotation will limit how many threads can enter this method
     * simultaneously.
     */
    @Bulkhead(name = "slowService", fallbackMethod = "bulkheadFallback")
    public String performSlowOperation(String requestId) {
        log.info("🚀 [{}] Entering slow operation at {}", requestId, LocalTime.now());

        try {
            Thread.sleep(5000); // Simulate 5s delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("✅ [{}] Finished slow operation at {}", requestId, LocalTime.now());
        return "Success for " + requestId;
    }

    /**
     * Fallback method called when the bulkhead is full.
     */
    public String bulkheadFallback(String requestId, io.github.resilience4j.bulkhead.BulkheadFullException ex) {
        log.warn("🛑 [{}] Semaphore Bulkhead is FULL! Rejecting request at {}", requestId, LocalTime.now());
        return "FALLBACK: Service is currently overloaded. (Semaphore Bulkhead Full)";
    }

    /**
     * Demonstrates Thread Pool Bulkhead.
     * This method runs on a SEPARATE thread pool, isolated from the controller's
     * worker threads.
     * NOTE: ThreadPool bulkhead MUST return CompletableFuture.
     */
    @Bulkhead(name = "threadPoolService", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "threadPoolFallback")
    public CompletableFuture<String> performThreadIsolatedOperation(String requestId) {
        log.info("🧵 [{}] Entering thread-pool operation at {}", requestId, LocalTime.now());
        log.info("🧵 Running in thread: {}", Thread.currentThread().getName());

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("✅ [{}] Finished thread-pool operation at {}", requestId, LocalTime.now());
        return CompletableFuture.completedFuture("Thread-Pool Success for " + requestId);
    }

    /**
     * Fallback method for the thread pool bulkhead.
     */
    public CompletableFuture<String> threadPoolFallback(String requestId,
            io.github.resilience4j.bulkhead.BulkheadFullException ex) {
        log.warn("🛑 [{}] Thread-Pool Bulkhead is FULL! Rejecting request at {}", requestId, LocalTime.now());
        return CompletableFuture.completedFuture("FALLBACK: Thread-Pool is exhausted. (Isolation Success!)");
    }
}
