package com.interview.debug.repository;

import com.interview.debug.model.Scenario97User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Scenario 97 User.
 */
@Repository
public interface Scenario97UserRepository extends JpaRepository<Scenario97User, Long> {
}
