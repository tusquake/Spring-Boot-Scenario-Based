package com.interview.debug.controller;

import com.interview.debug.service.Scenario125PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/scenario125")
@RequiredArgsConstructor
public class Scenario125PerformanceController {

    private final Scenario125PerformanceService performanceService;

    @GetMapping("/initialize")
    public String initialize() {
        performanceService.seedData();
        return "Database seeded with 10 authors and 50 books total.";
    }

    @GetMapping("/n-plus-one")
    public Map<String, Object> runNPlusOne() {
        long startTime = System.currentTimeMillis();
        int bookCount = performanceService.getNPlusOnePerformance();
        long duration = System.currentTimeMillis() - startTime;

        Map<String, Object> response = new HashMap<>();
        response.put("type", "N+1 Problem (Lazy Loading Loop)");
        response.put("totalBooksProcessed", bookCount);
        response.put("expectedQueries", "1 (authors) + 10 (individual book fetches) = 11 total");
        response.put("executionTimeMs", duration);
        response.put("status", "Check console logs for multiple SQL signals.");
        return response;
    }

    @GetMapping("/optimized")
    public Map<String, Object> runOptimized() {
        long startTime = System.currentTimeMillis();
        int bookCount = performanceService.getOptimizedPerformance();
        long duration = System.currentTimeMillis() - startTime;

        Map<String, Object> response = new HashMap<>();
        response.put("type", "Optimized (JOIN FETCH)");
        response.put("totalBooksProcessed", bookCount);
        response.put("expectedQueries", "1 total (atomic join)");
        response.put("executionTimeMs", duration);
        response.put("status", "Check console logs for a single SQL signal.");
        return response;
    }
}
