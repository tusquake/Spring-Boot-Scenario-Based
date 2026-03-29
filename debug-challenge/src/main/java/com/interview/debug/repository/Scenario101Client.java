package com.interview.debug.repository;

import com.interview.debug.model.Scenario101Model;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Scenario 101: Declarative HTTP Clients (@HttpExchange).
 * Interface-based client for JSONPlaceholder API.
 */
@HttpExchange("/posts")
public interface Scenario101Client {

    @GetExchange
    List<Scenario101Model> getAllPosts();

    @GetExchange("/{id}")
    Scenario101Model getPostById(@PathVariable("id") Long id);

    @PostExchange
    Scenario101Model createPost(@RequestBody Scenario101Model post);
}
