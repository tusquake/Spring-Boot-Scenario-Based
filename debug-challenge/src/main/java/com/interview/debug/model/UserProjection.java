package com.interview.debug.model;

import org.springframework.beans.factory.annotation.Value;

/**
 * Spring Data JPA Interface Projection.
 * Hibernate will generate SQL that selects ONLY these two columns.
 */
public interface UserProjection {
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();

    String getEmail();
}
