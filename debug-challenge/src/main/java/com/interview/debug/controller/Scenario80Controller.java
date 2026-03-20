package com.interview.debug.controller;

import com.interview.debug.model.Scenario80Book;
import com.interview.debug.repository.Scenario80BookRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario80")
public class Scenario80Controller {

    private final Scenario80BookRepository bookRepository;

    public Scenario80Controller(Scenario80BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void seedData() {
        if (bookRepository.count() == 0) {
            bookRepository.saveAll(List.of(
                Scenario80Book.builder().title("Spring in Action").author("Craig Walls").price(45.0).publishedDate(LocalDate.of(2022, 1, 1)).build(),
                Scenario80Book.builder().title("Clean Code").author("Robert Martin").price(50.0).publishedDate(LocalDate.of(2008, 8, 1)).build(),
                Scenario80Book.builder().title("Effective Java").author("Joshua Bloch").price(55.0).publishedDate(LocalDate.of(2018, 5, 1)).build(),
                Scenario80Book.builder().title("Java Concurrency").author("Brian Goetz").price(60.0).publishedDate(LocalDate.of(2006, 5, 1)).build()
            ));
        }
    }

    @GetMapping("/search/author")
    public List<Scenario80Book> getByAuthor(@RequestParam String name) {
        return bookRepository.findByAuthorIgnoreCase(name);
    }

    @GetMapping("/search/expensive")
    public List<Scenario80Book> getExpensive(@RequestParam Double min) {
        return bookRepository.findExpensiveBooks(min);
    }

    @GetMapping("/search/native")
    public List<Scenario80Book> getByKeywordNative(@RequestParam String keyword) {
        return bookRepository.findBooksByAuthorNative(keyword);
    }

    @PostMapping("/update-prices")
    public Map<String, Object> updatePrices(@RequestParam Double factor) {
        int count = bookRepository.updateAllPrices(factor);
        return Map.of("updated_count", count, "all_books", bookRepository.findAll());
    }
}
