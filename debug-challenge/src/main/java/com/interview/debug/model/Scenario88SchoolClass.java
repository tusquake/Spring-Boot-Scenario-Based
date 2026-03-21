package com.interview.debug.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "scenario88_class")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "students")
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Scenario88SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;
    private String teacherName;

    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Inverse Side
    @JsonManagedReference
    @JsonIgnore
    @Builder.Default
    private Set<Scenario88Student> students = new HashSet<>(); // Use Set for performance

    // Helper method to keep both sides in sync
    public void addStudent(Scenario88Student student) {
        students.add(student);
        student.setSchoolClass(this);
    }
}
