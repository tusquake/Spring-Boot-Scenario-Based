package com.interview.debug.controller;

import com.interview.debug.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario49")
public class Scenario49Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(Scenario49Controller.class);
    private final AsyncService asyncService;

    public Scenario49Controller(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @GetMapping("/run")
    public Map<String, Object> triggerAsync(@RequestParam(defaultValue = "DefaultJob") String name) {
        logger.info("[Controller] Received request to start async task: {}", name);
        
        // This call is non-blocking! 
        // It returns immediately while the service runs in a separate thread.
        asyncService.runLongTask(name, 10);
        
        logger.info("[Controller] Response sent to client immediately.");
        
        return Map.of(
            "status", "Job Accepted",
            "message", "The task '" + name + "' is running in the background. Check console logs for completion!",
            "note", "Notice how the API responded in milliseconds even though the task takes 10 seconds!"
        );
    }
}
