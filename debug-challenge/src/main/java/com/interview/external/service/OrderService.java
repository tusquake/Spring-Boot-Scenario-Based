package com.interview.external.service;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    public String fetchOrders() {
        return "Order 1, Order 2, Order 3";
    }
}
