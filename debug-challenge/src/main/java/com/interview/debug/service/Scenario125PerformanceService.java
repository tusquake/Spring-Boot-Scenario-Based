package com.interview.debug.service;

import com.interview.debug.model.Scenario125Author;
import com.interview.debug.model.Scenario125Book;
import com.interview.debug.repository.Scenario125AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class Scenario125PerformanceService {

    private final Scenario125AuthorRepository authorRepository;

    @Transactional
    public void seedData() {
        if (authorRepository.count() > 0) {
            log.info("Data already seeded for Scenario 125.");
            return;
        }

        log.info("Seeding authors and books for performance scenario...");
        IntStream.range(1, 11).forEach(i -> {
            Scenario125Author author = Scenario125Author.builder()
                    .name("Author " + i)
                    .build();
            
            IntStream.range(1, 6).forEach(j -> {
                author.addBook(Scenario125Book.builder()
                        .title("Book " + j + " by Author " + i)
                        .build());
            });
            
            authorRepository.save(author);
        });
        log.info("Seeding complete.");
    }

    /**
     * Demonstrates the N+1 Problem.
     * 1 query to fetch all authors.
     * N queries to fetch books for each author (triggered by accessing the lazy-loaded list).
     */
    @Transactional(readOnly = true)
    public int getNPlusOnePerformance() {
        log.info("🚀 Starting N+1 demo (Lazy Loading with loop access)...");
        List<Scenario125Author> authors = authorRepository.findAll();
        
        // This loop triggers a separate SQL query for each author's books
        int totalBooksAccessed = 0;
        for (Scenario125Author author : authors) {
            totalBooksAccessed += author.getBooks().size();
        }
        
        log.info("N+1 demo complete. Total books: {}", totalBooksAccessed);
        return totalBooksAccessed;
    }

    /**
     * Demonstrates the optimized approach using JOIN FETCH.
     * Result: Exactly 1 SQL query.
     */
    @Transactional(readOnly = true)
    public int getOptimizedPerformance() {
        log.info("🚀 Starting Optimized demo (JOIN FETCH)...");
        List<Scenario125Author> authors = authorRepository.findAllOptimized();
        
        // This loop does NOT trigger extra queries because books are already fetched
        int totalBooksAccessed = 0;
        for (Scenario125Author author : authors) {
            totalBooksAccessed += author.getBooks().size();
        }
        
        log.info("Optimized demo complete. Total books: {}", totalBooksAccessed);
        return totalBooksAccessed;
    }
}
