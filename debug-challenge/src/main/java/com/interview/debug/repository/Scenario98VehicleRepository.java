package com.interview.debug.repository;

import com.interview.debug.model.Scenario98Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Scenario 98: Vehicle Repository.
 * Using the base class 'Scenario98Vehicle' allows us to fetch all 
 * subclasses (Cars and Bikes) in a single polymorphic query.
 */
@Repository
public interface Scenario98VehicleRepository extends JpaRepository<Scenario98Vehicle, Long> {
}
