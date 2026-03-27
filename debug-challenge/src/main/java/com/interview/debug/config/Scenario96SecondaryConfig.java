package com.interview.debug.config;

import io.github.tusquake.envvalidator.annotation.ValidateEnv;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Secondary configuration for Scenario 96.
 * Demonstrates how env-validator collects errors from multiple beans.
 */
@Configuration
@Component
@Getter
@ValidateEnv("SCENARIO96_DB_PASS") // Secondary required variable
public class Scenario96SecondaryConfig {

    @Value("${SCENARIO96_EMAIL:}")
    @ValidateEnv(value = "SCENARIO96_EMAIL", pattern = "^[A-Za-z0-9+_.-]+@(.+)$") // Regex validation
    private String adminEmail;
}
