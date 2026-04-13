package com.interview.debug.repository;

import com.interview.debug.model.Scenario121Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Scenario121ProductRepository extends JpaRepository<Scenario121Product, Long> {
    List<Scenario121Product> findByCategory(String category);
}
