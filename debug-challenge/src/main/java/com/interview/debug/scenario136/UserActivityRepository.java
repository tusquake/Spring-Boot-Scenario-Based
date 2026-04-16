package com.interview.debug.scenario136;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    // This lookup will be slow without an index on tracking_id
    Optional<UserActivity> findByTrackingId(String trackingId);
}
