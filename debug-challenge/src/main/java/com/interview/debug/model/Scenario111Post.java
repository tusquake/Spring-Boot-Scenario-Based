package com.interview.debug.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Scenario 111: External Client DTO
 * Matches the structure of the JSONPlaceholder public API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario111Post {
    private Integer id;
    private Integer userId;
    private String title;
    private String body;
}
