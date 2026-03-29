package com.interview.debug.repository;

import com.interview.debug.model.Scenario100InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Scenario 100: JSON Columns in JPA.
 */
@Repository
public interface Scenario100InventoryRepository extends JpaRepository<Scenario100InventoryItem, Long> {
}
