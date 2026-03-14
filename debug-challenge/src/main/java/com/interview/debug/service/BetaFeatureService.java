package com.interview.debug.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * SCENARIO 40: FEATURE TOGGLES (@ConditionalOnProperty)
 * This bean will ONLY be created if the property 'app.feature.beta-enabled' is set to 'true'.
 * This is a standard way to manage feature flags in Spring Boot.
 */
@Service
@ConditionalOnProperty(name = "app.feature.beta-enabled", havingValue = "true", matchIfMissing = false)
public class BetaFeatureService {

    public String executeBetaLogic() {
        return "Beta Logic Executed Successfully!";
    }
}
