package com.interview.debug.service;

import com.interview.debug.model.Scenario93Audit;
import com.interview.debug.repository.Scenario93AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Main service for Scenario 93: @Transactional Deep Dive.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class Scenario93Service {

    private final Scenario93AuditRepository auditRepository;
    private final Scenario93InternalService internalService;

    /**
     * Case 1: REQUIRES_NEW
     * If the outer transaction rolls back, the inner (REQUIRES_NEW) should still persist.
     */
    @Transactional
    public void testRequiresNew(boolean failOuter) {
        log.info("Starting Outer Transaction (REQUIRED)");
        auditRepository.save(Scenario93Audit.builder()
                .action("OUTER_ACTION")
                .status("STARTED")
                .detail("This should rollback if failOuter is true")
                .build());

        // Inner transaction (Independent)
        internalService.requiresNewAction("Internal REQUIRES_NEW work");

        if (failOuter) {
            log.info("Simulating failure in Outer Transaction");
            throw new RuntimeException("Outer transaction failed!");
        }
    }

    /**
     * Case 2: Self-Invocation Pitfall
     * Calling a local @Transactional method from within the same class.
     * The annotation will be IGNORED because it doesn't go through the Spring Proxy.
     */
    public void testSelfInvocationEntrance() {
        log.info("Entering non-transactional method, calling local @Transactional method...");
        this.localTransactionalMethod(); // Self-invocation
    }

    @Transactional
    public void localTransactionalMethod() {
        log.info("Inside local @Transactional method. Saving audit...");
        auditRepository.save(Scenario93Audit.builder()
                .action("SELF_INVOCATION_ACTION")
                .status("COMPLETED")
                .detail("This was saved via self-invocation (Proxy Pitfall)")
                .build());
        
        log.info("Throwing exception. If Proxy worked, this should rollback. (Spoiler: It won't rollback)");
        throw new RuntimeException("Self-invocation rollback failure demo");
    }

    /**
     * Case 3: Checked vs Unchecked Exceptions
     * Checked exceptions (e.g., IOException) do NOT trigger rollback by default.
     */
    @Transactional
    public void testCheckedExceptionRollback() throws IOException {
        log.info("Inside @Transactional. Saving audit before checked exception...");
        auditRepository.save(Scenario93Audit.builder()
                .action("CHECKED_EXCEPTION_DEMO")
                .status("STARTED")
                .detail("Checked exceptions don't rollback by default")
                .build());
        
        throw new IOException("Simulated checked exception (No rollback happens)");
    }

    /**
     * Case 4: Unchecked Exception Rollback
     * RuntimeExceptions DO trigger rollback by default.
     */
    @Transactional
    public void testUncheckedExceptionRollback() {
        log.info("Inside @Transactional. Saving audit before runtime exception...");
        auditRepository.save(Scenario93Audit.builder()
                .action("UNCHECKED_EXCEPTION_DEMO")
                .status("STARTED")
                .detail("RuntimeExceptions DO rollback by default")
                .build());
        
        throw new RuntimeException("Simulated runtime exception (Rollback happens!)");
    }

    /**
     * Case 5: Isolation Level (REPEATABLE_READ)
     */
    @Transactional
    public List<Scenario93Audit> testRepeatableRead() {
        return internalService.readAuditsTwiceWithRepeatableRead();
    }
    
    /**
     * Fetch all audits to verify transaction results.
     */
    @Transactional(readOnly = true)
    public List<Scenario93Audit> getAllAudits() {
        return auditRepository.findAll();
    }

    /**
     * Helper to clean up audits for testing.
     */
    @Transactional
    public void clearAudits() {
        auditRepository.deleteAll();
    }
}
