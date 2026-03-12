package com.interview.debug.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario17")
public class Scenario17Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario17Controller.class);

    @GetMapping("/generate-logs")
    public String generateLogs() {
        logger.info("This is an INFO log - used for tracking normal business flow.");
        logger.debug("This is a DEBUG log - only visible because we configured it in logback.");
        logger.warn("This is a WARN log - used for unexpected but non-fatal issues.");
        
        try {
            throw new RuntimeException("Simulated error for logging check!");
        } catch (Exception e) {
            logger.error("This is an ERROR log - used for capturing exceptions and failures: {}", e.getMessage());
        }

        return "Logs generated! Check the 'logs' folder to see the rotation (app.log).";
    }
}
