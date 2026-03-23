package com.interview.debug.repository;

import com.interview.debug.model.Scenario93Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for transaction audit records.
 */
@Repository
public interface Scenario93AuditRepository extends JpaRepository<Scenario93Audit, Long> {
}
