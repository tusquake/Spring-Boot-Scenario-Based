package com.interview.debug.controller;

import com.interview.external.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario1")
public class Scenario1Controller {

    private final OrderService orderService;

    public Scenario1Controller(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/test")
    public String testComponentScan() {
        return "Scenario 1 Success: " + orderService.fetchOrders();
    }
}
