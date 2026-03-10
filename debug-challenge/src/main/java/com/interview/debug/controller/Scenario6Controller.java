package com.interview.debug.controller;

import com.interview.debug.service.BankService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/scenario6")
public class Scenario6Controller {

    private final BankService bankService;
    private Long accountId;

    public Scenario6Controller(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/init")
    public String init() {
        this.accountId = bankService.createAccount("Demo User", 1000.0);
        return "Account created with ID: " + accountId;
    }

    @GetMapping("/test/race-condition")
    public String testRaceCondition() {
        if (accountId == null) return "Call /init first";

        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Thread 1
        executor.submit(() -> {
            try { bankService.depositOptimistic(accountId, 100.0); } catch (Exception e) {
                System.err.println("Thread 1 Error (Optimistic): " + e.getMessage());
            }
        });

        // Thread 2
        executor.submit(() -> {
            try { bankService.depositOptimistic(accountId, 100.0); } catch (Exception e) {
                System.err.println("Thread 2 Error (Optimistic): " + e.getMessage());
            }
        });

        executor.shutdown();
        return "Two concurrent updates triggered! Check console for OptimisticLockException.";
    }

    @GetMapping("/test/pessimistic")
    public String testPessimistic() {
        if (accountId == null) return "Call /init first";
        bankService.depositPessimistic(accountId, 100.0);
        return "Balance updated safely using Pessimistic Lock!";
    }

    @GetMapping("/balance")
    public String getBalance() {
        if (accountId == null) return "Call /init first";
        Double balance = bankService.getBalance(accountId);
        return "Current Balance: " + balance;
    }
}
