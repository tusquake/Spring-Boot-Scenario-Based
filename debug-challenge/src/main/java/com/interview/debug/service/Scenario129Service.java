package com.interview.debug.service;

import com.interview.debug.model.Scenario129Account;
import com.interview.debug.repository.Scenario129AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Scenario 129: Transaction Isolation Levels Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class Scenario129Service {

    private final Scenario129AccountRepository repository;
    private final PlatformTransactionManager transactionManager;

    public void reset() {
        repository.deleteAll();
        repository.save(new Scenario129Account(1L, "Alice", 1000.0));
        repository.save(new Scenario129Account(2L, "Bob", 500.0));
    }

    /**
     * DEMO 1: DIRTY READ
     * Isolation.READ_UNCOMMITTED
     */
    public Map<String, Object> executeDirtyReadDemo(boolean fixed) {
        reset();
        List<String> logs = new ArrayList<>();
        
        // T1: Update Alice's balance to 9999 but will rollback
        CompletableFuture<Void> t1 = CompletableFuture.runAsync(() -> {
            TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.execute(status -> {
                Scenario129Account alice = repository.findById(1L).get();
                alice.setBalance(9999.0);
                repository.save(alice);
                logs.add("T1: Updated Alice balance to 9999 (Uncommitted)");
                sleep(2000); // Hold the transaction
                status.setRollbackOnly();
                logs.add("T1: Rolling back...");
                return null;
            });
        });

        sleep(500); // Let T1 start

        // T2: Read Alice's balance
        double balanceRead = -1;
        TransactionTemplate tt2 = new TransactionTemplate(transactionManager);
        tt2.setIsolationLevel(fixed ? TransactionDefinition.ISOLATION_READ_COMMITTED : TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        
        balanceRead = tt2.execute(status -> {
            double b = repository.findById(1L).get().getBalance();
            logs.add("T2: Read Alice balance as: " + b);
            return b;
        });

        t1.join();
        double finalBalance = repository.findById(1L).get().getBalance();

        return Map.of(
            "isolationLevel", fixed ? "READ_COMMITTED" : "READ_UNCOMMITTED",
            "phenomenon", "Dirty Read",
            "balanceReadByT2", balanceRead,
            "finalBalanceInDb", finalBalance,
            "logs", logs
        );
    }

    /**
     * DEMO 2: NON-REPEATABLE READ
     * Isolation.REPEATABLE_READ
     */
    public Map<String, Object> executeNonRepeatableReadDemo(boolean fixed) {
        reset();
        List<String> logs = new ArrayList<>();

        TransactionTemplate tt1 = new TransactionTemplate(transactionManager);
        tt1.setIsolationLevel(fixed ? TransactionDefinition.ISOLATION_REPEATABLE_READ : TransactionDefinition.ISOLATION_READ_COMMITTED);

        Map<String, Object> result = tt1.execute(status -> {
            double read1 = repository.findById(1L).get().getBalance();
            logs.add("T1: First Read: " + read1);

            // Trigger T2 to change data
            CompletableFuture.runAsync(() -> {
                TransactionTemplate tt2 = new TransactionTemplate(transactionManager);
                tt2.execute(s -> {
                    Scenario129Account alice = repository.findById(1L).get();
                    alice.setBalance(2000.0);
                    repository.save(alice);
                    return null;
                });
            }).join();
            logs.add("T2: Updated and Committed balance to 2000");

            double read2 = repository.findById(1L).get().getBalance();
            logs.add("T1: Second Read: " + read2);
            
            return Map.of("read1", read1, "read2", read2);
        });

        return Map.of(
            "isolationLevel", fixed ? "REPEATABLE_READ" : "READ_COMMITTED",
            "phenomenon", "Non-Repeatable Read",
            "results", result,
            "logs", logs,
            "isRepeatable", result.get("read1").equals(result.get("read2"))
        );
    }

    /**
     * DEMO 3: PHANTOM READ
     * Isolation.SERIALIZABLE
     */
    public Map<String, Object> executePhantomReadDemo(boolean fixed) {
        reset();
        List<String> logs = new ArrayList<>();

        TransactionTemplate tt1 = new TransactionTemplate(transactionManager);
        tt1.setIsolationLevel(fixed ? TransactionDefinition.ISOLATION_SERIALIZABLE : TransactionDefinition.ISOLATION_REPEATABLE_READ);

        Map<String, Object> result = tt1.execute(status -> {
            long count1 = repository.count();
            logs.add("T1: First Count: " + count1);

            // Trigger T2 to Insert data
            CompletableFuture.runAsync(() -> {
                TransactionTemplate tt2 = new TransactionTemplate(transactionManager);
                tt2.execute(s -> {
                    repository.save(new Scenario129Account(3L, "Charlie", 700.0));
                    return null;
                });
            }).join();
            logs.add("T2: Inserted and Committed Charlie");

            long count2 = repository.count();
            logs.add("T1: Second Count: " + count2);
            
            return Map.of("count1", count1, "count2", count2);
        });

        return Map.of(
            "isolationLevel", fixed ? "SERIALIZABLE" : "REPEATABLE_READ",
            "phenomenon", "Phantom Read",
            "results", result,
            "logs", logs,
            "isPhantomAvoided", result.get("count1").equals(result.get("count2"))
        );
    }

    private void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
