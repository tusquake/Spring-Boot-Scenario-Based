package com.interview.debug.repository;

import com.interview.debug.model.Scenario127Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Scenario 127: Caching Strategies
 */
@Repository
public interface Scenario127ProductRepository extends JpaRepository<Scenario127Product, Long> {
}
