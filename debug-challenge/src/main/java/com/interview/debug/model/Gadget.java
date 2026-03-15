package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "gadgets")
@Data
public class Gadget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private Double price;
}
