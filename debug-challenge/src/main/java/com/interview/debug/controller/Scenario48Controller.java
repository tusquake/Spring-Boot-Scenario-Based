package com.interview.debug.controller;

import com.interview.debug.service.MdcTracingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario48")
public class Scenario48Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(Scenario48Controller.class);
    private final MdcTracingService mdcTracingService;

    public Scenario48Controller(MdcTracingService mdcTracingService) {
        this.mdcTracingService = mdcTracingService;
    }

    @GetMapping("/trace")
    public Map<String, Object> trace(@RequestParam(required = false) String input) {
        logger.info("[Controller Layer] Received request to /api/scenario48/trace");
        
        String result = mdcTracingService.processData(input);
        
        logger.info("[Controller Layer] Sending response back to client.");
        
        return Map.of(
            "status", "success",
            "message", "Check your console logs to see the traceId across layers!",
            "result", result
        );
    }
}
