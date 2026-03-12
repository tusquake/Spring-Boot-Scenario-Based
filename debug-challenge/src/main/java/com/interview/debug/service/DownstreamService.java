package com.interview.debug.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DownstreamService {
    private static final Logger logger = LoggerFactory.getLogger(DownstreamService.class);
    private final Random random = new Random();

    public String callExternalApi(boolean fail) {
        logger.info("Calling external API...");

        if (fail || random.nextInt(10) < 5) { // 50% failure rate simulation
            logger.error("External API call failed!");
            throw new RuntimeException("Downstream service is unavailable");
        }

        return "External data retrieved successfully";
    }
}
