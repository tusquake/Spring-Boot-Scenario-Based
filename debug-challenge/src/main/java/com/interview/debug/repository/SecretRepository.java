package com.interview.debug.repository;

import com.interview.debug.model.UserSecret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SecretRepository extends JpaRepository<UserSecret, Long> {

    // Native query to see the RAW data in DB (encrypted)
    @Query(value = "SELECT sensitive_data FROM user_secrets WHERE username = ?1", nativeQuery = true)
    String findRawSensitiveDataByUsername(String username);
}
