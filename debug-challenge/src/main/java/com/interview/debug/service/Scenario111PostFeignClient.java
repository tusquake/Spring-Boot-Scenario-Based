package com.interview.debug.service;

import com.interview.debug.model.Scenario111Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Scenario 111: OpenFeign (Spring Cloud Client)
 * This is the classic declarative approach in the Spring Cloud ecosystem.
 */
@FeignClient(name = "postFeignClient", url = "https://jsonplaceholder.typicode.com")
public interface Scenario111PostFeignClient {

    @GetMapping("/posts/{id}")
    Scenario111Post getPostById(@PathVariable("id") Integer id);
}
