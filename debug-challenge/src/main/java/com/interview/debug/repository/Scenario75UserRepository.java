package com.interview.debug.repository;

import com.interview.debug.model.Scenario75User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface Scenario75UserRepository extends JpaRepository<Scenario75User, Long> {
    Optional<Scenario75User> findByEmail(String email);
}
