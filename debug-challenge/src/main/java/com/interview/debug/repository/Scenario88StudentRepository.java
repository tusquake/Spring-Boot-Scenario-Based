package com.interview.debug.repository;

import com.interview.debug.model.Scenario88Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Scenario88StudentRepository extends JpaRepository<Scenario88Student, Long> {
}
