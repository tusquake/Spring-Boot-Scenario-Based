package com.interview.debug.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Scenario 34: Async Return Types
 * Demonstrates how to return results from @Async methods using
 * CompletableFuture.
 */
@Service
@Slf4j
public class Scenario34AsyncService {

    @Async
    public CompletableFuture<String> processTaskA() {
        log.info("Starting Task A in thread: {}", Thread.currentThread().getName());
        try {
            Thread.sleep(2000); // Simulate 2-second task
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Finished Task A");
        return CompletableFuture.completedFuture("Result from Task A");
    }

    @Async
    public CompletableFuture<String> processTaskB() {
        log.info("Starting Task B (3s) in thread: {}", Thread.currentThread().getName());
        try {
            Thread.sleep(3000); // Simulate 3-second task
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Finished Task B");
        return CompletableFuture.completedFuture("Result from Task B");
    }

    @Async
    public CompletableFuture<String> processTaskC() {
        log.info("Starting Task C (5s) in thread: {}", Thread.currentThread().getName());
        try {
            Thread.sleep(5000); // Simulate 5-second task
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Finished Task C");
        return CompletableFuture.completedFuture("Result from Task C");
    }

    @Async
    public CompletableFuture<String> processTaskD() {
        log.info("Starting Task D (10s) in thread: {}", Thread.currentThread().getName());
        try {
            Thread.sleep(10000); // Simulate 10-second task
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Finished Task D");
        return CompletableFuture.completedFuture("Result from Task D");
    }
}
