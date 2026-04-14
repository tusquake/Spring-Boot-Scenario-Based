package com.interview.debug.repository;

import com.interview.debug.model.Scenario125Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Scenario125AuthorRepository extends JpaRepository<Scenario125Author, Long> {

    /**
     * Optimized query that fetches authors and their books in a single SQL JOIN.
     * This solves the N+1 problem.
     */
    @Query("SELECT DISTINCT a FROM Scenario125Author a LEFT JOIN FETCH a.books")
    List<Scenario125Author> findAllOptimized();
}
