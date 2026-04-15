package com.interview.debug.repository;

import com.interview.debug.model.Scenario129Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Scenario 129: Transaction Isolation Levels
 */
@Repository
public interface Scenario129AccountRepository extends JpaRepository<Scenario129Account, Long> {
}
