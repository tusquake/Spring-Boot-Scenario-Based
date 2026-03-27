package com.interview.debug.controller;

import com.interview.debug.config.Scenario96Config;
import com.interview.debug.config.Scenario96SecondaryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Scenario 96 Controller to demonstrate startup validation.
 * If this controller is reachable, it means the startup validation was successful.
 */
@RestController
@RequestMapping("/api/scenario96")
@RequiredArgsConstructor
public class Scenario96Controller {

    private final Scenario96Config scenario96Config;
    private final Scenario96SecondaryConfig secondaryConfig;

    @GetMapping("/config")
    public Map<String, String> getConfig() {
        return Map.of(
            "externalUrl", scenario96Config.getExternalUrl(),
            "timeout", scenario96Config.getTimeout(),
            "adminEmail", secondaryConfig.getAdminEmail(),
            "status", "Validation Passed"
        );
    }
}
