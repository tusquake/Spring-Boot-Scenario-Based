package com.interview.debug.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Scenario 110: Jackson Mapping & Annotations
 * Demonstrates common Jackson decorators to control JSON
 * serialization/deserialization.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Anti-Pattern #32: Clean responses by hiding nulls
@JsonPropertyOrder({ "id", "full_name", "email_address", "created_at" }) // Ensures predictable JSON structure
public class Scenario110JacksonModel {

    private Long id;

    @JsonProperty("full_name") // Anti-Pattern #86: Mapping camelCase to snake_case
    private String fullName;

    @JsonProperty("email_address")
    private String email;

    @JsonIgnore // Anti-Pattern #90: Preventing sensitive data leakage in JSON responses
    private String internalSecret;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // Standardizing date formats
    private LocalDateTime createdAt;

    private String nullableField; // This will stay hidden if null due to @JsonInclude
}
