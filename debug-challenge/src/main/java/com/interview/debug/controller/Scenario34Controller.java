package com.interview.debug.controller;

import com.interview.debug.service.Scenario34AsyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/scenario34")
@RequiredArgsConstructor
@Slf4j
public class Scenario34Controller {

    private final Scenario34AsyncService asyncService;

    @GetMapping("/test")
    public CompletableFuture<Map<String, Object>> runTasks() {
        log.info("Received request for deep parallel tasks (Total sequential time: 20s)");
        long start = System.currentTimeMillis();

        // 1. Kick off all 4 tasks in parallel
        CompletableFuture<String> taskA = asyncService.processTaskA();
        CompletableFuture<String> taskB = asyncService.processTaskB();
        CompletableFuture<String> taskC = asyncService.processTaskC();
        CompletableFuture<String> taskD = asyncService.processTaskD();

        // 2. Wait for all of them to finish using allOf()
        return CompletableFuture.allOf(taskA, taskB, taskC, taskD)
                .thenApply(v -> {
                    long duration = System.currentTimeMillis() - start;
                    log.info("All 4 tasks completed in {} ms", duration);

                    return Map.of(
                            "results", Map.of(
                                    "taskA", taskA.join(),
                                    "taskB", taskB.join(),
                                    "taskC", taskC.join(),
                                    "taskD", taskD.join()),
                            "total_time_ms", duration,
                            "logic", "Total sequential work was 2s + 3s + 5s + 10s = 20s.",
                            "efficiency", "In parallel, it only takes the duration of the longest task (~10s).");
                });
    }
}
