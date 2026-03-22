package com.interview.debug.controller;

import com.interview.debug.model.Scenario2PrototypeBean;
import com.interview.debug.model.Scenario2SingletonBean;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario2")
@RequiredArgsConstructor
public class Scenario2Controller {

    private final ApplicationContext applicationContext;

    @GetMapping("/test")
    public Map<String, Object> testScopes() {
        Map<String, Object> result = new HashMap<>();

        // Fetch Singleton twice from context
        Scenario2SingletonBean s1 = applicationContext.getBean(Scenario2SingletonBean.class);
        Scenario2SingletonBean s2 = applicationContext.getBean(Scenario2SingletonBean.class);

        // Fetch Prototype twice from context
        Scenario2PrototypeBean p1 = applicationContext.getBean(Scenario2PrototypeBean.class);
        Scenario2PrototypeBean p2 = applicationContext.getBean(Scenario2PrototypeBean.class);

        result.put("singleton_ids", Map.of("instance_1", s1.getBeanId(), "instance_2", s2.getBeanId()));
        result.put("prototype_ids", Map.of("instance_1", p1.getBeanId(), "instance_2", p2.getBeanId()));
        result.put("analysis", "Singleton IDs should be IDENTICAL. Prototype IDs should be DIFFERENT.");

        return result;
    }
}
