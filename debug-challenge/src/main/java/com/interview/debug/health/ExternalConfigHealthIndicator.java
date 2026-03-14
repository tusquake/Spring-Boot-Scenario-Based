package com.interview.debug.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * SCENARIO 38: CUSTOM HEALTH INDICATOR
 * This tells Actuator how to check if your application's external dependencies
 * (like a specific directory, a custom DB, or an external API) are healthy.
 */
@Component
public class ExternalConfigHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Simulation: We check if a specific configuration directory exists
        File configDir = new File("src/main/resources/db/migration");
        
        if (configDir.exists() && configDir.isDirectory()) {
            return Health.up()
                    .withDetail("configDir", "Accessible")
                    .withDetail("path", configDir.getAbsolutePath())
                    .build();
        }
        
        return Health.down()
                .withDetail("error", "Critical migration directory missing!")
                .build();
    }
}
