package com.interview.debug.repository;

import com.interview.debug.model.Scenario97Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Scenario 97 Warehouse.
 */
@Repository
public interface Scenario97WarehouseRepository extends JpaRepository<Scenario97Warehouse, Long> {
}
