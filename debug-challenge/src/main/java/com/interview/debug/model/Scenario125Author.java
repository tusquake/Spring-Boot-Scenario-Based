package com.interview.debug.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scenario125_authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scenario125Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // LAZY is the default for @OneToMany, but we specify it for clarity
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Scenario125Book> books = new ArrayList<>();

    public void addBook(Scenario125Book book) {
        books.add(book);
        book.setAuthor(this);
    }
}
