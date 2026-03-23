package com.interview.debug.repository;

import com.interview.debug.model.Scenario95User;
import com.interview.debug.model.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Scenario95UserRepository extends JpaRepository<Scenario95User, Long> {

    /**
     * Spring Data JPA recognizes the return type is an interface.
     * It will automatically perform a selective SELECT.
     */
    List<UserProjection> findAllProjectedBy();
    
    /**
     * You can also use explicit @Query with projections.
     */
    @Query("SELECT u.firstName as firstName, u.lastName as lastName, u.email as email FROM Scenario95User u")
    List<UserProjection> findSimplifiedUsers();
}
