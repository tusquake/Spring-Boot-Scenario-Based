package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Scenario 99: Entity with a Composite Key.
 * Notice how we don't use @Id. We use @EmbeddedId instead.
 */
@Entity
@Table(name = "scenario99_order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Clean fields!
public class Scenario99OrderItem {

    @EmbeddedId
    Scenario99OrderItemId id;

    int quantity;
    double price;
    String note;
}
