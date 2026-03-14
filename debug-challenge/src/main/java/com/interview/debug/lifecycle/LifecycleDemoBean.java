package com.interview.debug.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * SCENARIO 39: BEAN LIFECYCLE HOOKS
 * Demonstrates @PostConstruct (Runs after dependencies are injected)
 * and @PreDestroy (Runs right before the context is closed).
 * 
 * SCENARIO 41: ADVANCED @VALUE (Defaults & SpEL)
 * Demonstrates default values and basic calculations inside @Value.
 */
@Component
public class LifecycleDemoBean {

    private static final Logger logger = LoggerFactory.getLogger(LifecycleDemoBean.class);

    // Scenario 41: Default value if property is missing
    @Value("${app.config.api-key:DEFAULT_DEVELOPMENT_KEY}")
    private String apiKey;

    // Scenario 41: SpEL (Spring Expression Language) calculation
    @Value("#{10 * 2}") 
    private int sessionTimeout;

    public LifecycleDemoBean() {
        logger.info("[Lifecycle] 🏗️ Constructor: Dependencies NOT injected yet. API Key: {}", apiKey);
    }

    @PostConstruct
    public void init() {
        logger.info("[Lifecycle] ✅ @PostConstruct: Dependencies ARE injected. API Key: {}", apiKey);
        logger.info("[Lifecycle] ⏱️ Session Timeout calculated via SpEL: {} minutes", sessionTimeout);
    }

    @PreDestroy
    public void cleanup() {
        logger.info("[Lifecycle] 🛑 @PreDestroy: Application is shutting down. Cleaning up resources...");
    }
}
