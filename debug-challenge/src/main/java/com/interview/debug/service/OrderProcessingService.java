package com.interview.debug.service;

// import org.apache.catalina.servlets.DefaultServlet.SortManager.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProcessingService {

    // public void checkout(String orderId) {
    // System.out.println("Starting checkout for order: " + orderId);
    // try {
    // // Internal call to a transactional method
    // updateInventoryAndSaveOrder(orderId);
    // } catch (Exception e) {
    // System.err.println("Checkout failed: " + e.getMessage());
    // }
    // }

    @Transactional
    public void checkout(String orderId) {
        if (orderId.equals(null) || orderId.equals("")) {
            throw new RuntimeException("Order ID cannot be null or empty!");
        }
        System.out.println("Updating inventory for: " + orderId);

        // Simulating a database save
        System.out.println("Saving order to DB...");

        if (orderId.equals("FAIL")) {
            System.out.println("Simulating a failure that should trigger rollback!");
            throw new RuntimeException("Database connection lost!");
        }

        System.out.println("Order saved successfully.");
    }
}
