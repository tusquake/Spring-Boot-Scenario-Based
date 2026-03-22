package com.interview.debug.repository;

import com.interview.debug.model.Scenario91Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for multi-tenant projects.
 */
@Repository
public interface Scenario91ProjectRepository extends JpaRepository<Scenario91Project, Long> {
}
