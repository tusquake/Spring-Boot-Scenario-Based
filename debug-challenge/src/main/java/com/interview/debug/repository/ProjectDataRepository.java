package com.interview.debug.repository;

import com.interview.debug.model.ProjectData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDataRepository extends JpaRepository<ProjectData, Long> {
}
