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
        // return Map.of(
        // "isVirtual", Thread.currentThread().isVirtual(),
        // "threadName", Thread.currentThread().toString(),
        // "propertyEnabled", virtualThreadsEnabled,
        // "message",
        // "If isVirtual is false, check if you are running on Java 21+ and have
        // spring.threads.virtual.enabled=true");
    }

    @GetMapping("/blocking")
    public String blockingTask() throws InterruptedException {
        // Simulating a blocking operation
        Thread.sleep(500);
        return "Processed by: " + Thread.currentThread();
    }
}
