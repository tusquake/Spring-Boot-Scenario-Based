package com.interview.debug.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "scenario89_enrollment")
@Getter
@Setter
public class Scenario89Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference("course-enrollment")
    private Scenario89Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonBackReference("student-enrollment")
    private Scenario89Student student;

    private LocalDateTime enrollmentDate;
    private String grade; // e.g., "A", "B", "In-Progress"
}
