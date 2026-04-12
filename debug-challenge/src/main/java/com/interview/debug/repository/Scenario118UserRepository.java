package com.interview.debug.repository;

import com.interview.debug.model.Scenario118User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface Scenario118UserRepository extends JpaRepository<Scenario118User, Long> {
    Optional<Scenario118User> findByEmail(String email);
}
