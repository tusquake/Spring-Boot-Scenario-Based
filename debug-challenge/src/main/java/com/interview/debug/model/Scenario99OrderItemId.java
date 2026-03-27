package com.interview.debug.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * Scenario 99: JPA Composite Key.
 * A composite key MUST:
 * 1. Implement Serializable.
 * 2. Implement equals() and hashCode() (handled by @Data).
 * 3. Have a default constructor (handled by @NoArgsConstructor).
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // No 'private' keywords needed!
public class Scenario99OrderItemId implements Serializable {

    Long orderId;
    Long productId;
}
