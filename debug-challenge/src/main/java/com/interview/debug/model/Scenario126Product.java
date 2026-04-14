package com.interview.debug.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Scenario 126: Cache Stampede
 * A simple model to demonstrate caching.
 * Must implement Serializable for Redis caching if profile=prod is used.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scenario126Product implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private BigDecimal price;
    private String description;
}
