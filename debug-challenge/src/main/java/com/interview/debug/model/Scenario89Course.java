package com.interview.debug.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "scenario89_course")
@Getter
@Setter
@ToString(exclude = "enrollments")
public class Scenario89Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;
    private String courseCode;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("course-enrollment")
    private Set<Scenario89Enrollment> enrollments = new HashSet<>();
}
