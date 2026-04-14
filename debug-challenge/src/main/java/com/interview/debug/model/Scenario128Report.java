package com.interview.debug.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Scenario 128: Caffeine Caching
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scenario128Report implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String content;
    private LocalDateTime generatedAt;
    private String cacheSource;
}
