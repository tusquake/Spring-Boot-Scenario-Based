package com.interview.debug.repository;

import com.interview.debug.model.Scenario99OrderItem;
import com.interview.debug.model.Scenario99OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Composite ID Item.
 * Notice the ID type is Scenario99OrderItemId.
 */
@Repository
public interface Scenario99OrderItemRepository extends JpaRepository<Scenario99OrderItem, Scenario99OrderItemId> {
}
