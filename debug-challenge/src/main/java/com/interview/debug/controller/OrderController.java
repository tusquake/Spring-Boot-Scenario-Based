package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.interview.external.service.OrderService;

import jakarta.annotation.PostConstruct;

import com.interview.debug.service.UserService;
import com.interview.debug.service.OrderProcessingService;

@RestController
public class OrderController {

    @PostConstruct
    public void init() {
        System.out.println("OrderController initialized");
    }

    private final OrderService orderService;
    private final UserService userService;
    private final OrderProcessingService orderProcessingService;

    public OrderController(OrderService orderService, UserService userService,
            OrderProcessingService orderProcessingService) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderProcessingService = orderProcessingService;
    }

    @GetMapping("/orders")
    public String getOrders() {
        userService.processUser();
        return orderService.fetchOrders();
    }

    @GetMapping("/checkout/{id}")
    public String checkout(@PathVariable String id) {
        orderProcessingService.checkout(id);
        return "Checkout process initiated for: " + id;
    }
}
