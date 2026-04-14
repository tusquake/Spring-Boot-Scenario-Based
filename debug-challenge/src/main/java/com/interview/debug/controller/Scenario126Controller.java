package com.interview.debug.controller;

import com.interview.debug.model.Scenario126Product;
import com.interview.debug.service.Scenario126Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Scenario 126: Cache Stampede Controller
 */
@RestController
@RequestMapping("/api/scenario126")
@RequiredArgsConstructor
@Slf4j
public class Scenario126Controller {

    private final Scenario126Service scenario126Service;

    /**
     * Get product using vulnerable cache logic.
     */
    @GetMapping("/vulnerable/{id}")
    public Scenario126Product getVulnerable(@PathVariable String id) {
        return scenario126Service.getProductVulnerable(id);
    }

    /**
     * Get product using secured cache logic (sync=true).
     */
    @GetMapping("/secured/{id}")
    public Scenario126Product getSecured(@PathVariable String id) {
        return scenario126Service.getProductSecured(id);
    }

    /**
     * Triggers 5 CONCURRENT requests to the vulnerable endpoint.
     * Use this to see the "Stampede" in the logs.
     */
    @PostMapping("/trigger/vulnerable/{id}")
    public Map<String, Object> triggerVulnerable(@PathVariable String id) {
        scenario126Service.resetAll();
        log.info("🚀 Triggering 5 concurrent VULNERABLE requests for ID: {}", id);
        
        List<CompletableFuture<Scenario126Product>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> scenario126Service.getProductVulnerable(id)));
        }
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        int hits = scenario126Service.getDatabaseHits();
        log.info("📊 Finished vulnerable stampede. Total DB Hits: {}", hits);
        
        return Map.of(
            "message", "Stampede completed (Vulnerable)",
            "databaseHits", hits,
            "expectedHits", 5,
            "description", "All 5 concurrent requests hit the database because none were synchronized."
        );
    }

    /**
     * Triggers 5 CONCURRENT requests to the secured endpoint.
     * Use this to see how sync=true prevents the stampede.
     */
    @PostMapping("/trigger/secured/{id}")
    public Map<String, Object> triggerSecured(@PathVariable String id) {
        scenario126Service.resetAll();
        log.info("🛡️ Triggering 5 concurrent SECURED requests for ID: {}", id);
        
        List<CompletableFuture<Scenario126Product>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> scenario126Service.getProductSecured(id)));
        }
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        int hits = scenario126Service.getDatabaseHits();
        log.info("📊 Finished secured execution. Total DB Hits: {}", hits);
        
        return Map.of(
            "message", "Execution completed (Secured)",
            "databaseHits", hits,
            "expectedHits", 1,
            "description", "Only the first request hit the database. Others waited and used the cached result."
        );
    }

    @GetMapping("/stats")
    public Map<String, Integer> getStats() {
        return Map.of("databaseHits", scenario126Service.getDatabaseHits());
    }

    @PostMapping("/reset")
    public String reset() {
        scenario126Service.resetAll();
        return "Cache cleared and hits reset.";
    }
}
