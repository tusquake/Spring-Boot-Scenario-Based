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
        // Simulate a task with variable execution time (500ms to 2000ms)
        int sleepTime = 500 + random.nextInt(1500);
        Thread.sleep(sleepTime);
        
        return Map.of(
            "status", "completed",
            "simulatedTimeMs", sleepTime,
            "message", "This method execution was tracked via AOP!"
        );
    }
}
