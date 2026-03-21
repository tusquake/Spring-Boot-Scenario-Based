package com.interview.debug.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario84")
public class Scenario84Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario84Controller.class);

    @GetMapping("/test")
    public Map<String, String> testInterceptor() {
        logger.info("[Scenario 84] Controller: Executing Business Logic...");
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "INTERCEPTOR_FLOW_COMPLETE");
        response.put("instruction", "Check the application console logs to see the execution order!");
        return response;
    }
}
