package com.interview.debug.service;

import com.interview.debug.exception.BalanceCannotbeNegativeException;
import com.interview.debug.model.BankAccount;
import com.interview.debug.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankAccountService {
    private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);
    private final BankAccountRepository repository;

    public BankAccountService(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void seedInitialData() {
        if (repository.count() == 0) {
            repository.save(new BankAccount("Tushar", 1000.0));
            logger.info("[Locking] Seeded initial account with 1000.0");
        }
    }

    /**
     * Optimistic Locking: Relies on @Version.
     * Security: Only the owner can withdraw.
     */
    @Transactional
    @PreAuthorize("@bankSecurity.isOwner(authentication, #id)")
    public Double withdrawOptimistic(Long id, Double amount, int delayMs) {
        logger.info("[Optimistic] Start withdrawal of {} from account {}", amount, id);

        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (amount > account.getBalance()) {
            throw new BalanceCannotbeNegativeException("Balance cannot be negative.");
        }

        logger.info("[Optimistic] Read account. Current Balance: {}, Version: {}",
                account.getBalance(), account.getVersion());

        simulateDelay(delayMs);

        account.setBalance(account.getBalance() - amount);
        repository.save(account);

        logger.info("[Optimistic] Completed withdrawal. New Balance: {}", account.getBalance());
        return account.getBalance();
    }

    /**
     * Pessimistic Locking: SELECT ... FOR UPDATE.
     * Security: Demonstrating hasPermission with CustomPermissionEvaluator.
     */
    @Transactional
    @PreAuthorize("hasPermission(#id, 'BankAccount', 'WRITE')")
    public Double withdrawPessimistic(Long id, Double amount, int delayMs) {
        logger.info("[Pessimistic] Start withdrawal of {} from account {}", amount, id);

        // This line will BLOCK if another pessimistic lock is held on this ID
        BankAccount account = repository.findByIdPessimistic(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (amount > account.getBalance()) {
            throw new BalanceCannotbeNegativeException("Balance cannot be negative.");
        }

        logger.info("[Pessimistic] Acquired Lock. Current Balance: {}", account.getBalance());

        simulateDelay(delayMs);

        account.setBalance(account.getBalance() - amount);
        repository.save(account);

        logger.info("[Pessimistic] Completed withdrawal. New Balance: {}", account.getBalance());
        return account.getBalance();
    }

    @Transactional(readOnly = true)
    @PostAuthorize("@bankSecurity.isOwner(authentication, returnObject)")
    public BankAccount getAccountDetails(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public Iterable<BankAccount> getAllAccounts() {
        return repository.findAll();
    }

    private void simulateDelay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
