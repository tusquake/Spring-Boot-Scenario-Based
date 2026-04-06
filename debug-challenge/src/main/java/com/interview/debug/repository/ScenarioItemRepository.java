package com.interview.debug.repository;

import com.interview.debug.model.ScenarioItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioItemRepository extends JpaRepository<ScenarioItem, Long> {

    // 1. @Query (JPQL/HQL)
    @Query("SELECT i FROM ScenarioItem i WHERE i.name LIKE %:keyword%")
    List<ScenarioItem> searchByNameQuery(@Param("keyword") String keyword);

    // 2. @Query (Native)
    @Query(value = "SELECT * FROM scenario_item WHERE active = true ORDER BY price DESC LIMIT 1", nativeQuery = true)
    ScenarioItem findMostExpensiveActiveItemNative();

    // 3. @NamedQuery
    List<ScenarioItem> findActiveItems();

    // 4. @NamedNativeQuery
    List<ScenarioItem> findExpensiveItemsNative(@Param("minPrice") Double minPrice);
}
