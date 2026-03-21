package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scenario87_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "person") // CRITICAL: Prevents StackOverflowError in bidirectional loop
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Scenario87Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String zipCode;

    @OneToOne(mappedBy = "address") // Inverse Side
    @com.fasterxml.jackson.annotation.JsonBackReference // Prevents Jackson from looping back to person
    private Scenario87Person person;
}
