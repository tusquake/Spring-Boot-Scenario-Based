package com.interview.debug.service;

import com.interview.debug.model.Scenario112Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Scenario 112: Async Service
 * Demonstrates the implementation of asynchronous method execution.
 * 
 * CRITICAL INTERVIEW TIP:
 * @Async only works if the method is called from another component (Proxying).
 * Calling an @Async method from within the same class (Self-Invocation) will NOT be asynchronous.
 */
@Service
@Slf4j
public class Scenario112AsyncService {

    /**
     * 1. Fire-and-Forget @Async
     * Moves execution to the "scenario112Executor" thread pool.
     */
    @Async("scenario112Executor")
    public void executeFireAndForget(String taskName) {
        log.info("[Scenario 112] Thread: {} - Starting task: {}", Thread.currentThread().getName(), taskName);
        try {
            Thread.sleep(2000); // Simulate heavy work
        } catch (InterruptedException e) {
            log.error("Task interrupted", e);
        }
        log.info("[Scenario 112] Thread: {} - Completed task: {}", Thread.currentThread().getName(), taskName);
    }

    /**
     * 2. @Async with CompletableFuture
     * Returns a wrapper around the eventual result.
     */
    @Async("scenario112Executor")
    public CompletableFuture<String> processWithResult(String input) {
        log.info("[Scenario 112] Thread: {} - Processing input: {}", Thread.currentThread().getName(), input);
        try {
            Thread.sleep(3000); // Simulate heavy computation
        } catch (InterruptedException e) {
            log.error("Process interrupted", e);
        }
        return CompletableFuture.completedFuture("Result for: " + input);
    }

    /**
     * 3. @Async Event Listener
     * Listens for Scenario112Event and handles it on a separate thread.
     */
    @Async("scenario112Executor")
    @EventListener
    public void onScenarioEvent(Scenario112Event event) {
        log.info("[Scenario 112] Thread: {} - Received Event: {} from Source: {}", 
                 Thread.currentThread().getName(), event.message(), event.source());
    }
}
