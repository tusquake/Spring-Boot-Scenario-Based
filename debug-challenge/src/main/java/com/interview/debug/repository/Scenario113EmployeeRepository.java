package com.interview.debug.repository;

import com.interview.debug.model.Scenario113Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Scenario 113: Employee Repository
 * This interface is all you need! Spring Data REST will automatically 
 * handle CRUD, Pagination, and Sorting.
 */
@Repository
@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
public interface Scenario113EmployeeRepository extends JpaRepository<Scenario113Employee, Long> {
}
