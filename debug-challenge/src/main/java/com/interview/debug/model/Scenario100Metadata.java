package com.interview.debug.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Scenario 100: JSON Columns in JPA.
 * POJO for flexible metadata storage in a JSON column.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario100Metadata implements Serializable {
    
    private List<String> tags;
    private Map<String, String> attributes;
    private Double weight;
    private String origin;
}
