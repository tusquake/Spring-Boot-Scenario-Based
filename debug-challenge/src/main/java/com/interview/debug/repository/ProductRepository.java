package com.interview.debug.repository;

import com.interview.debug.model.SoftDeleteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<SoftDeleteProduct, Long> {
}
