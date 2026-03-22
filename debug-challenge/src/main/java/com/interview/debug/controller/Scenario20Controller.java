package com.interview.debug.controller;

import com.interview.debug.config.Scenario20Config;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario20")
@RequiredArgsConstructor
public class Scenario20Controller {

    private final Scenario20Config config;

    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        Map<String, Object> result = new HashMap<>();
        
        // Data from @Value
        Map<String, String> appInfo = new HashMap<>();
        appInfo.put("name", config.getAppName());
        appInfo.put("version", config.getAppVersion());
        appInfo.put("description", config.getAppDescription());
        result.put("via_value_annotation", appInfo);

        // Data from @ConfigurationProperties
        Map<String, Object> serverInfo = new HashMap<>();
        serverInfo.put("host", config.getHost());
        serverInfo.put("port", config.getPort());
        serverInfo.put("timeout", config.getTimeout());
        result.put("via_configuration_properties", serverInfo);

        result.put("analysis", "Use @Value for ad-hoc properties; use @ConfigurationProperties for structured, type-safe groups.");
        
        return result;
    }
}
