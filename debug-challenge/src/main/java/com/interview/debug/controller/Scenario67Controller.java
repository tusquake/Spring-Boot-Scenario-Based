package com.interview.debug.controller;

import com.interview.debug.service.OrderSagaOrchestrator;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/scenario67")
public class Scenario67Controller {

    private final OrderSagaOrchestrator orchestrator;

    public Scenario67Controller(OrderSagaOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    /**
     * Demonstrate Saga Pattern (Orchestration).
     * @param failAt optional parameter to simulate failure at 'payment' or 'inventory'
     */
    @GetMapping("/process")
    public String processOrder(@RequestParam(required = false) String failAt) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        return orchestrator.executeOrder(orderId, failAt);
    }
}
