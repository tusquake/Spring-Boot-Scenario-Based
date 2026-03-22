package com.interview.debug.controller;

import com.interview.debug.service.Scenario90Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario90")
@RequiredArgsConstructor
@Slf4j
public class Scenario90Controller {

    private final Scenario90Service bulkheadService;

    @GetMapping("/test")
    public String testBulkhead(@RequestParam(defaultValue = "anon") String id) {
        log.info("📥 Controller received request for ID: {} (Semaphore Mode)", id);
        return bulkheadService.performSlowOperation(id);
    }

    @GetMapping("/test-threadpool")
    public CompletableFuture<String> testThreadPoolBulkhead(@RequestParam(defaultValue = "anon") String id) {
        log.info("📥 Controller received request for ID: {} (Thread-Pool Mode)", id);
        return bulkheadService.performThreadIsolatedOperation(id);
    }
}
