package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * A "fat" entity with many fields.
 * In a real app, loading such an entity for a list view is inefficient.
 */
@Entity
@Table(name = "scenario95_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario95User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
    
    @Lob // Large Object for bio
    private String bio;
    
    private String profilePictureUrl;
}
