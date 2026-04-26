package com.interview.debug.repository;

import com.interview.debug.model.Scenario21Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface Scenario21EmployeeRepository extends JpaRepository<Scenario21Employee, Long>, JpaSpecificationExecutor<Scenario21Employee> {
}
