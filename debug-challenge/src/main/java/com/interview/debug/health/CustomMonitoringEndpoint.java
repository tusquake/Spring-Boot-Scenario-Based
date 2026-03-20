package com.interview.debug.health;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom Actuator Endpoint: /actuator/system-status
 * Shows how to expose business-specific metrics or control signals.
 */
@Component
@Endpoint(id = "system-status")
public class CustomMonitoringEndpoint {

    private final io.micrometer.core.instrument.MeterRegistry meterRegistry;
    private String currentAppStatus = "ALL_SYSTEMS_GO";

    public CustomMonitoringEndpoint(io.micrometer.core.instrument.MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @ReadOperation
    public Map<String, Object> getSystemStatus() {
        // Increment a custom counter every time this is called
        meterRegistry.counter("app.monitoring.access.count").increment();
        
        Map<String, Object> details = new HashMap<>();
        details.put("status", currentAppStatus);
        details.put("memory_free_mb", Runtime.getRuntime().freeMemory() / (1024 * 1024));
        details.put("active_scenarios", 77);
        details.put("server_time", java.time.LocalDateTime.now().toString());
        return details;
    }

    @WriteOperation
    public void updateStatus(String newStatus) {
        this.currentAppStatus = newStatus;
    }

    @DeleteOperation
    public void resetStatus() {
        this.currentAppStatus = "ALL_SYSTEMS_GO";
    }
}
