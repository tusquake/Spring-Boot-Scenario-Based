package com.interview.debug.scenario136;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activities", indexes = {
    // @Index(name = "idx_tracking_id", columnList = "tracking_id") // Uncomment this to FIX the performance issue
})
@Getter
@Setter
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_id", nullable = false)
    private String trackingId;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
