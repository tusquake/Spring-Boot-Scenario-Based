package com.interview.debug.repository;

import com.interview.debug.model.Scenario97Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Scenario 97 Company.
 */
@Repository
public interface Scenario97CompanyRepository extends JpaRepository<Scenario97Company, Long> {
}
