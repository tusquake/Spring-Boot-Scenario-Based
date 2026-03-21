package com.interview.debug.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scenario88_student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "schoolClass")
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Scenario88Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String rollNumber;

    @ManyToOne(fetch = FetchType.LAZY) // Many / Owning Side
    @JoinColumn(name = "class_id")
    @JsonBackReference
    private Scenario88SchoolClass schoolClass;
}
