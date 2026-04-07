package com.interview.debug.service;

import com.interview.debug.model.Scenario111Post;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * Scenario 111: Declarative HTTP Interface (Introduced in Spring 6)
 * This allows defining client contracts using annotations similar to Controllers.
 */
@HttpExchange("/posts")
public interface Scenario111PostClient {

    @GetExchange("/{id}")
    Scenario111Post getPostById(Integer id);
}
