package com.interview.debug.repository;

import com.interview.debug.model.Scenario89Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Scenario89StudentRepository extends JpaRepository<Scenario89Student, Long> {
}
