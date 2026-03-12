package com.interview.debug.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario24")
public class Scenario24Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario24Controller.class);

    @GetMapping("/trace-me")
    public String traceMe() {
        logger.info("Received request to trace-me endpoint");
        
        doSomeProcessing();
        
        logger.info("Finished processing request");
        return "Check your logs! You should see a consistent Trace ID across multiple log lines.";
    }

    private void doSomeProcessing() {
        logger.debug("Starting some internal processing...");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.debug("Internal processing complete.");
    }
}
