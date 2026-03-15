package com.interview.debug.controller;

import com.interview.debug.model.BankAccount;
import com.interview.debug.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenario54")
public class Scenario54Controller {

    private final BankAccountService service;

    public Scenario54Controller(BankAccountService service) {
        this.service = service;
    }

    @GetMapping("/account/{id}")
    public BankAccount getAccount(@PathVariable Long id) {
        // This will be secured by @PostAuthorize in the service
        return service.getAccountDetails(id);
    }

    @PostMapping("/withdraw/optimistic")
    public Double withdrawOptimistic(@RequestParam Long id, @RequestParam Double amount) {
        // Secured by @PreAuthorize("@bankSecurity.isOwner(...)")
        return service.withdrawOptimistic(id, amount, 0);
    }

    @PostMapping("/withdraw/pessimistic")
    public Double withdrawPessimistic(@RequestParam Long id, @RequestParam Double amount) {
        // Secured by @PreAuthorize("hasPermission(...)")
        return service.withdrawPessimistic(id, amount, 0);
    }

    @GetMapping("/admin/all")
    public Iterable<BankAccount> getAll() {
        // Secured by @PreAuthorize("hasRole('ADMIN')")
        return service.getAllAccounts();
    }
}
