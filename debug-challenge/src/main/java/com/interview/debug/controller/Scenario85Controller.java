package com.interview.debug.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario85")
public class Scenario85Controller {

    private static final Logger logger = LoggerFactory.getLogger(Scenario85Controller.class);

    @GetMapping("/test")
    public Map<String, String> testFilters() {
        logger.info("[Scenario 85] Controller: Inside the secure room!");
        
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "FILTER_SEQUENCE_VERIFIED");
        result.put("tip", "Check the console logs to see the nested execution of both filters.");
        return result;
    }
}
