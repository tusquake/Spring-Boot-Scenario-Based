package com.interview.debug.repository;

import com.interview.debug.model.Scenario94UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface Scenario94UUIDRepository extends JpaRepository<Scenario94UUID, UUID> {
}
