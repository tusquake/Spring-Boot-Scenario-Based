package com.interview.debug.repository;

import com.interview.debug.model.Scenario80Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Scenario80BookRepository extends JpaRepository<Scenario80Book, Long> {

    // 1. Derived Query Method (Case-Insensitive)
    List<Scenario80Book> findByAuthorIgnoreCase(String author);

    // 2. JPQL Query (Database Agnostic)
    @Query("SELECT b FROM Scenario80Book b WHERE b.price > :minPrice ORDER BY b.price DESC")
    List<Scenario80Book> findExpensiveBooks(@Param("minPrice") Double minPrice);

    // 3. Native Query (Uses H2 specific or standard SQL)
    @Query(value = "SELECT * FROM scenario80book WHERE author LIKE %:authorKeyword%", nativeQuery = true)
    List<Scenario80Book> findBooksByAuthorNative(@Param("authorKeyword") String authorKeyword);

    // 4. Modifying Query (Bulk Update)
    @Modifying
    @Transactional
    @Query("UPDATE Scenario80Book b SET b.price = b.price * :factor")
    int updateAllPrices(@Param("factor") Double factor);
}
