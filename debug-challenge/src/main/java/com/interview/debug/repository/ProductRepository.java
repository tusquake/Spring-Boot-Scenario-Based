package com.interview.debug.repository;

import com.interview.debug.model.SoftDeleteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<SoftDeleteProduct, Long> {

    // Native query bypasses Hibernate's @SQLRestriction
    @org.springframework.data.jpa.repository.Query(value = "SELECT * FROM products", nativeQuery = true)
    List<com.interview.debug.model.SoftDeleteProduct> findAllIncludingDeleted();
}
