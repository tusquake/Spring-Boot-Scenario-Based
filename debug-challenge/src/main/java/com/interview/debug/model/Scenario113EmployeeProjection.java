package com.interview.debug.model;

import org.springframework.data.rest.core.config.Projection;

/**
 * Scenario 113: Employee Projection
 * Use this to hide sensitive data like 'salary'.
 * 
 * Usage: GET /api/data-rest/employees?projection=summary
 */
@Projection(name = "summary", types = { Scenario113Employee.class })
public interface Scenario113EmployeeProjection {

    String getName();

    String getPosition();
}
