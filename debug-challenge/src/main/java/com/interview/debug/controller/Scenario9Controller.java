package com.interview.debug.controller;

import com.interview.debug.model.Customer;
import com.interview.debug.model.Order;
import com.interview.debug.repository.CustomerRepository;
import com.interview.debug.repository.OrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scenario9")
public class Scenario9Controller {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public Scenario9Controller(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/init")
    public String initData() {
        Customer c1 = new Customer();
        c1.setName("Alice");
        c1.setEmail("alice@example.com");
        customerRepository.save(c1);

        Customer c2 = new Customer();
        c2.setName("Bob");
        c2.setEmail("bob@example.com");
        customerRepository.save(c2);

        for (int i = 1; i <= 5; i++) {
            Order o = new Order();
            o.setProductName("Laptop " + i);
            o.setQuantity(1);
            o.setPrice(1000.0);
            o.setOrderDate(new Date());
            o.setCustomer(i % 2 == 0 ? c1 : c2);
            orderRepository.save(o);
        }

        return "Data initialized: 2 Customers and 5 Orders created.";
    }

    @GetMapping("/test/n-plus-one")
    public List<String> testNPlusOne() {
        // This will trigger N+1 selects if we iterate and access Customer
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(o -> o.getProductName() + " ordered by " + o.getCustomer().getName())
                .collect(Collectors.toList());
    }

    @GetMapping("/test/join-fetch")
    public List<String> testJoinFetch() {
        // This uses a single query with JOIN FETCH
        List<Order> orders = orderRepository.findAllWithCustomers();
        return orders.stream()
                .map(o -> o.getProductName() + " ordered by " + o.getCustomer().getName())
                .collect(Collectors.toList());
    }
}
