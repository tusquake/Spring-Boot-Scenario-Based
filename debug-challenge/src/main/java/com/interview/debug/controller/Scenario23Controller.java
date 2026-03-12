package com.interview.debug.controller;

import com.interview.debug.service.DownstreamService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario23")
public class Scenario23Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario23Controller.class);
    private final DownstreamService downstreamService;

    public Scenario23Controller(DownstreamService downstreamService) {
        this.downstreamService = downstreamService;
    }

    @GetMapping("/recoverable-call")
    @CircuitBreaker(name = "externalService", fallbackMethod = "fallback")
    public String recoverableCall(@RequestParam(defaultValue = "false") boolean fail) {
        return downstreamService.callExternalApi(fail);
    }

    // Fallback method must have the same signature + an optional exception parameter
    public String fallback(boolean fail, Exception e) {
        logger.warn("Circuit Breaker triggered fallback. Reason: {}", e.getMessage());
        return "Fallback: Service is temporarily unavailable, but we are returning cached safe metadata.";
    }
}
