package com.interview.debug.repository;

import com.interview.debug.model.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    
    // 1. Page<T> is already provided by findAll(Pageable) -> Executes COUNT
    
    // 2. Slice<T> avoids the COUNT query but still uses LIMIT/OFFSET
    Slice<Customer> findBy(Pageable pageable);

    // 3. Window<T> (Cursor/Keyset) avoids OFFSET and COUNT. Uses WHERE id > ?
    Window<Customer> findFirst10ByOrderByIdAsc(ScrollPosition position);
}
