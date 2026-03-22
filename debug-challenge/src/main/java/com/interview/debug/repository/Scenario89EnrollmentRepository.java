package com.interview.debug.repository;

import com.interview.debug.model.Scenario89Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Scenario89EnrollmentRepository extends JpaRepository<Scenario89Enrollment, Long> {
}
