package com.interview.debug.controller;

import com.interview.debug.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario51")
public class Scenario51Controller {

    private final BankAccountService service;

    public Scenario51Controller(BankAccountService service) {
        this.service = service;
    }

    @PostMapping("/seed")
    public String seed() {
        service.seedInitialData();
        return "Initial account seeded!";
    }

    @PostMapping("/withdraw/optimistic")
    public Map<String, Object> withdrawOptimistic(
            @RequestParam Long id, 
            @RequestParam Double amount,
            @RequestParam(defaultValue = "5000") int delay) {
        
        service.withdrawOptimistic(id, amount, delay);
        return Map.of("message", "Withdrawal successful (Optimistic)");
    }

    @PostMapping("/withdraw/pessimistic")
    public Map<String, Object> withdrawPessimistic(
            @RequestParam Long id, 
            @RequestParam Double amount,
            @RequestParam(defaultValue = "5000") int delay) {
        
        service.withdrawPessimistic(id, amount, delay);
        return Map.of("message", "Withdrawal successful (Pessimistic)");
    }
}
