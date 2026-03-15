package com.interview.debug.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MdcTracingService {
    private static final Logger logger = LoggerFactory.getLogger(MdcTracingService.class);

    public String processData(String input) {
        logger.info("[Service Layer] Processing request with input: {}", input);
        
        // Simulating some business logic
        if (input == null || input.isBlank()) {
            logger.warn("[Service Layer] Input is blank, using default value.");
            input = "Standard Data";
        }
        
        logger.info("[Service Layer] Successfully processed data.");
        return "Processed: " + input;
    }
}
