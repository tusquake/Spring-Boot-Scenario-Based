package com.interview.debug.controller;

import com.interview.debug.model.Scenario112Event;
import com.interview.debug.service.Scenario112AsyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

/**
 * Scenario 112: Asynchronous Communication Controller
 * Compares three ways to handle requests asynchronously.
 */
@RestController
@RequestMapping("/api/scenario112")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Scenario 112: Async Communication", description = "Comparison of CompletableFuture, DeferredResult, and Events.")
public class Scenario112AsyncController {

    private final Scenario112AsyncService asyncService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 1. Using CompletableFuture
     * Spring MVC detects the CompletableFuture return type and automatically 
     * releases the servlet thread, waiting for the future to complete.
     */
    @GetMapping("/completable-future")
    @Operation(summary = "Async via CompletableFuture", description = "Standard async processing with a direct return value.")
    public CompletableFuture<String> getWithFuture(@RequestParam(defaultValue = "test-input") String input) {
        log.info("[Scenario 112] Thread: {} - Request received for Future", Thread.currentThread().getName());
        return asyncService.processWithResult(input);
    }

    /**
     * 2. Using DeferredResult
     * Gives you manual control over when and how the result is returned. 
     * Excellent for long-polling or external async triggers.
     */
    @GetMapping("/deferred-result")
    @Operation(summary = "Async via DeferredResult", description = "Manual control over result resolution.")
    public DeferredResult<String> getWithDeferred() {
        log.info("[Scenario 112] Thread: {} - Request received for DeferredResult", Thread.currentThread().getName());
        DeferredResult<String> deferredResult = new DeferredResult<>(5000L); // 5s timeout

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000); // Simulate some async work
                deferredResult.setResult("Deferred result successfully resolved!");
            } catch (Exception e) {
                deferredResult.setErrorResult("Failed to resolve result");
            }
        });

        return deferredResult;
    }

    /**
     * 3. Using Internal Events (Fire-and-Forget)
     * Triggers an event and returns immediately without waiting for the handler.
     */
    @GetMapping("/trigger-event")
    @Operation(summary = "Fire-and-Forget via Events", description = "Asynchronous event publishing.")
    public String triggerEvent(@RequestParam String message) {
        log.info("[Scenario 112] Thread: {} - Publishing Event...", Thread.currentThread().getName());
        eventPublisher.publishEvent(new Scenario112Event(message, "Controller"));
        return "Event published! Check logs to see it handled on a different thread.";
    }
}
