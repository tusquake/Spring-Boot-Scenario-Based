package com.interview.debug.service;

import com.interview.debug.model.BankAccount;
import com.interview.debug.repository.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * If another transaction updates the record while we are sleeping, 
     * this save() will throw ObjectOptimisticLockingFailureException.
     */
    @Transactional
    public void withdrawOptimistic(Long id, Double amount, int delayMs) {
        logger.info("[Optimistic] Start withdrawal of {} from account {}", amount, id);
        
        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        logger.info("[Optimistic] Read account. Current Balance: {}, Version: {}", 
                account.getBalance(), account.getVersion());

        simulateDelay(delayMs);

        account.setBalance(account.getBalance() - amount);
        repository.save(account);
        
        logger.info("[Optimistic] Completed withdrawal. New Balance: {}", account.getBalance());
    }

    /**
     * Pessimistic Locking: SELECT ... FOR UPDATE.
     * This blocks other threads at the database level from reading/writing 
     * this specific row until this transaction finishes.
     */
    @Transactional
    public void withdrawPessimistic(Long id, Double amount, int delayMs) {
        logger.info("[Pessimistic] Start withdrawal of {} from account {}", amount, id);
        
        // This line will BLOCK if another pessimistic lock is held on this ID
        BankAccount account = repository.findByIdPessimistic(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        logger.info("[Pessimistic] Acquired Lock. Current Balance: {}", account.getBalance());

        simulateDelay(delayMs);

        account.setBalance(account.getBalance() - amount);
        repository.save(account);
        
        logger.info("[Pessimistic] Completed withdrawal. New Balance: {}", account.getBalance());
    }

    private void simulateDelay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
