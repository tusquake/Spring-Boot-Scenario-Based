package com.interview.debug.service;

import com.interview.debug.model.Scenario93Audit;
import com.interview.debug.repository.Scenario93AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Isolation;

import java.util.List;

/**
 * Internal service to demonstrate transaction propagation and isolation behaviors.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class Scenario93InternalService {

    private final Scenario93AuditRepository auditRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewAction(String detail) {
        log.info("Starting REQUIRES_NEW transaction for Action: {}", detail);
        auditRepository.save(Scenario93Audit.builder()
                .action("INTERNAL_ACTION")
                .status("COMPLETED")
                .detail(detail)
                .build());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void mandatoryAction(String detail) {
        log.info("Starting MANDATORY transaction for Action: {}", detail);
        auditRepository.save(Scenario93Audit.builder()
                .action("MANDATORY_ACTION")
                .status("COMPLETED")
                .detail(detail)
                .build());
    }

    @Transactional(propagation = Propagation.NEVER)
    public void neverAction(String detail) {
        log.info("Executing NEVER transaction for Action: {}", detail);
        auditRepository.save(Scenario93Audit.builder()
                .action("NEVER_ACTION")
                .status("COMPLETED")
                .detail(detail)
                .build());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Scenario93Audit> readAuditsTwiceWithRepeatableRead() {
        log.info("First read in REPEATABLE_READ transaction");
        List<Scenario93Audit> firstRead = auditRepository.findAll();
        log.info("Audits found in first read: {}", firstRead.size());

        try {
            // Sleep to let another transaction modify the data
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("Second read in REPEATABLE_READ transaction");
        List<Scenario93Audit> secondRead = auditRepository.findAll();
        log.info("Audits found in second read: {}", secondRead.size());
        
        return secondRead;
    }
}
