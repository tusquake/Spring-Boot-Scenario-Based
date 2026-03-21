package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scenario87_person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Scenario87Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Owning Side
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @com.fasterxml.jackson.annotation.JsonManagedReference // The "Forward" part of the relationship
    private Scenario87Address address;

    // Helper method to keep both sides in sync (Defensive Coding)
    public void setAddress(Scenario87Address address) {
        this.address = address;
        if (address != null) {
            address.setPerson(this);
        }
    }
}
