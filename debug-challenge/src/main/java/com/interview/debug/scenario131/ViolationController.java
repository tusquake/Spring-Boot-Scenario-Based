package com.interview.debug.scenario131;

import com.interview.debug.repository.BankAccountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Scenario 131: Architecture Violation Demo.
 * This controller violates the rule: "Controllers must not depend on Repositories directly."
 * It should go through a Service layer.
 */
@RestController
@RequestMapping("/api/scenario131/violation")
public class ViolationController {

    private final BankAccountRepository bankAccountRepository;

    public ViolationController(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @GetMapping("/accounts")
    public List<?> getAllAccounts() {
        return bankAccountRepository.findAll();
    }
}
