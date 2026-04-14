package com.interview.debug.controller;

import com.interview.debug.model.Scenario128Report;
import com.interview.debug.service.Scenario128Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Scenario 128: Caffeine Caching Controller
 */
@RestController
@RequestMapping("/api/scenario128")
@RequiredArgsConstructor
public class Scenario128Controller {

    private final Scenario128Service scenario128Service;

    @GetMapping("/ttl/{id}")
    public Scenario128Report getTtlReport(@PathVariable String id) {
        return scenario128Service.getExpiringReport(id);
    }

    @GetMapping("/size/{id}")
    public Scenario128Report getSizeReport(@PathVariable String id) {
        return scenario128Service.getSizeLimitedReport(id);
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return scenario128Service.getCacheStats();
    }
}
