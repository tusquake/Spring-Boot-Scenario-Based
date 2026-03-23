package com.interview.debug.controller;

import com.interview.debug.service.Scenario94Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario94")
@RequiredArgsConstructor
public class Scenario94Controller {

    private final Scenario94Service scenario94Service;

    @PostMapping("/test/identity")
    public Map<String, Object> testIdentity(@RequestParam(defaultValue = "100") int count) {
        long duration = scenario94Service.testBatchPerformanceIdentity(count);
        return Map.of(
            "strategy", "IDENTITY",
            "count", count,
            "durationMs", duration,
            "note", "IDENTITY strategy disables JDBC batching because Hibernate needs IDs immediately from DB."
        );
    }

    @PostMapping("/test/sequence")
    public Map<String, Object> testSequence(@RequestParam(defaultValue = "100") int count) {
        long duration = scenario94Service.testBatchPerformanceSequence(count);
        return Map.of(
            "strategy", "SEQUENCE",
            "count", count,
            "durationMs", duration,
            "note", "SEQUENCE allows the Persistence Context to pre-fetch IDs, which enables JDBC batch inserts."
        );
    }

    @PostMapping("/test/uuid")
    public Map<String, Object> testUUID(@RequestParam(defaultValue = "100") int count) {
        long duration = scenario94Service.testBatchPerformanceUUID(count);
        return Map.of(
            "strategy", "UUID",
            "count", count,
            "durationMs", duration,
            "note", "UUID strategy generates IDs in memory (App Layer). Fast and distributed-friendly."
        );
    }

    @PostMapping("/test/auto")
    public Map<String, Object> testAuto(@RequestParam(defaultValue = "100") int count) {
        long duration = scenario94Service.testBatchPerformanceAuto(count);
        return Map.of(
            "strategy", "AUTO",
            "count", count,
            "durationMs", duration,
            "note", "AUTO lets Hibernate decide. With H2, it usually picks SEQUENCE."
        );
    }
}
