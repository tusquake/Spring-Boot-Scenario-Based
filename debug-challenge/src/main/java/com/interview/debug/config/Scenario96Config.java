package com.interview.debug.config;

import io.github.tusquake.envvalidator.annotation.ValidateEnv;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Scenario 96: Startup Environment Validation.
 * This class demonstrates both class-level and field-level validation
 * using the custom 'env-validator' library.
 */
@Configuration
@Component
@Getter
@ValidateEnv(value = "SCENARIO96_API_KEY") // Class-level: checks if this key exists in the environment
public class Scenario96Config {

    /**
     * Field-level validation:
     * - required = true
     * - pattern = Regex to ensure it's a valid HTTPS URL
     */
    @Value("${SCENARIO96_EXTERNAL_URL:}")
    @ValidateEnv(value = "SCENARIO96_EXTERNAL_URL", pattern = "^https://.*")
    private String externalUrl;

    /**
     * Default value demonstration:
     * Even if SCENARIO96_TIMEOUT is missing, the library can be configured to use a
     * default.
     */
    @Value("${SCENARIO96_TIMEOUT:}")
    @ValidateEnv(value = "SCENARIO96_TIMEOUT", defaultValue = "10000")
    private String timeout;
}
