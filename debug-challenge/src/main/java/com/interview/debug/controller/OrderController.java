package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interview.external.service.OrderService;

import jakarta.annotation.PostConstruct;

import com.interview.debug.service.UserService;

@RestController
public class OrderController {

    @PostConstruct
    public void init() {
        System.out.println("OrderController initialized");
    }

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orders")
    public String getOrders() {
        userService.processUser();
        return orderService.fetchOrders();
    }
}
