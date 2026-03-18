package com.interview.debug.controller;

import com.interview.debug.annotation.TrackTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/scenario27")
public class Scenario27Controller {

    private final Random random = new Random();

    @TrackTime("slowTask")
    @GetMapping("/slow-task")
    public Map<String, Object> slowTask() throws InterruptedException {
        int sleepTime = 500 + random.nextInt(1500);
        Thread.sleep(sleepTime);
        return Map.of("status", "completed", "simulatedTimeMs", sleepTime);
    }

    @TrackTime("successTask")
    @GetMapping("/success-task")
    public String successTask() {
        return "Task processed successfully!";
    }

    @TrackTime("failTask")
    @GetMapping("/fail-task")
    public String failTask() {
        throw new RuntimeException("Simulated error in business logic!");
    }
}
