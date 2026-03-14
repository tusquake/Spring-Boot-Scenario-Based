package com.interview.debug.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * SCENARIO 36: TYPE-SAFE CONFIGURATION
 * Using @ConfigurationProperties is preferred over scattered @Value tags
 * because it supports hierarchy, validation, and is easier to maintain.
 */
@Configuration
@ConfigurationProperties(prefix = "app.daily-dev")
public class AppConfigProps {

    private String welcomeMessage;
    private int maxRetryAttempts;
    private List<String> supportEmails;
    private Map<String, String> featureFlags;

    // Getters and Setters are REQUIRED for @ConfigurationProperties to work
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public int getMaxRetryAttempts() {
        return maxRetryAttempts;
    }

    public void setMaxRetryAttempts(int maxRetryAttempts) {
        this.maxRetryAttempts = maxRetryAttempts;
    }

    public List<String> getSupportEmails() {
        return supportEmails;
    }

    public void setSupportEmails(List<String> supportEmails) {
        this.supportEmails = supportEmails;
    }

    public Map<String, String> getFeatureFlags() {
        return featureFlags;
    }

    public void setFeatureFlags(Map<String, String> featureFlags) {
        this.featureFlags = featureFlags;
    }
}
