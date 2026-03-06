package com.interview.debug.repository;

import com.interview.debug.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // THE FIX: "JOIN FETCH" tells Hibernate to fetch the related Customer
    // in the same SQL SELECT statement, solving the N+1 problem.
    @Query("SELECT o FROM Order o JOIN FETCH o.customer")
    List<Order> findAllWithCustomers();

    /*
     * ALTERNATIVE: Using EntityGraph (More modern/flexible)
     * 
     * @EntityGraph(attributePaths = {"customer"})
     * List<Order> findAll();
     */
}
