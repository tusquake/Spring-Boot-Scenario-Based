package com.interview.debug.controller;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/scenario73")
public class Scenario73Controller {

    private static final Logger log = LoggerFactory.getLogger(Scenario73Controller.class);
    private final com.interview.debug.service.Scenario73Service serviceB;

    public Scenario73Controller(com.interview.debug.service.Scenario73Service serviceB) {
        this.serviceB = serviceB;
    }

    /**
     * Entry Point: Service A
     * Timeout: 2s (configured in properties)
     */
    @GetMapping("/service-a")
    @TimeLimiter(name = "serviceA", fallbackMethod = "serviceAFallback")
    public CompletableFuture<String> callServiceA(@RequestParam(defaultValue = "500") int delayB) {
        log.info("Service A received request. Calling Service B...");
        return serviceB.callServiceB(delayB).thenApply(res -> "Service A -> " + res);
    }

    // Fallbacks
    public CompletableFuture<String> serviceAFallback(Exception e) {
        log.warn("Service A Timeout/Error: {}", e.getMessage());
        return CompletableFuture.completedFuture("Service A Fallback (Timeout)");
    }
}
