package com.interview.debug.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Scenario 101: Declarative HTTP Clients (@HttpExchange).
 * POJO for external API response (Post).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario101Model implements Serializable {
    
    private Long id;
    private Long userId;
    private String title;
    private String body;
}
