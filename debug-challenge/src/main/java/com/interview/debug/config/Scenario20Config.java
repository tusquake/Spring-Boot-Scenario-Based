package com.interview.debug.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Scenario 20: Custom Property Source
 * Demonstrates BOTH @Value and @ConfigurationProperties.
 */
@Configuration
@PropertySource("classpath:scenario20.properties")
@ConfigurationProperties(prefix = "scenario20.server")
@Getter
@Setter // Required for @ConfigurationProperties bind
public class Scenario20Config {

    // Style 1: @Value for individual, ad-hoc properties
    @Value("${scenario20.app.name}")
    private String appName;

    @Value("${scenario20.app.version}")
    private String appVersion;

    @Value("${scenario20.app.description}")
    private String appDescription;

    // Style 2: @ConfigurationProperties for structured groups
    // Prefix is 'scenario20.server', so these map to host, port, timeout
    private String host;
    private int port;
    private long timeout;
}
