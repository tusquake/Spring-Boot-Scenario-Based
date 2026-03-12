package com.interview.debug.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomBannerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomBannerService.class);
    private final String message;

    public CustomBannerService(String message) {
        this.message = message;
    }

    public void printBanner() {
        logger.info("**************************************************");
        logger.info("STREATER CUSTOM STARTER: {}", message);
        logger.info("**************************************************");
    }
}
