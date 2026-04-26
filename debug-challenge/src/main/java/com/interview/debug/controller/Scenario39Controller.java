package com.interview.debug.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario39")
public class Scenario39Controller {

    @Value("${spring.threads.virtual.enabled:false}")
    private boolean virtualThreadsEnabled;

    @GetMapping("/info")
    public Map<String, Object> getThreadInfo() {
        return Map.of(
            "isVirtual", false,
            "threadName", Thread.currentThread().toString(),
            "propertyEnabled", virtualThreadsEnabled,
            "message", "Java 21+ required for Virtual Threads."
        );
    }

    @GetMapping("/blocking")
    public String blockingTask() throws InterruptedException {
        // Simulating a blocking operation
        Thread.sleep(500);
        return "Processed by: " + Thread.currentThread();
    }
}
