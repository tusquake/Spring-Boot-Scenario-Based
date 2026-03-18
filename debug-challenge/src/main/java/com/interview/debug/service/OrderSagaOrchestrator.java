package com.interview.debug.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderSagaOrchestrator {
    private static final Logger logger = LoggerFactory.getLogger(OrderSagaOrchestrator.class);

    private final PaymentService paymentService;
    private final InventoryService inventoryService;

    public OrderSagaOrchestrator(PaymentService paymentService, InventoryService inventoryService) {
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
    }

    public String executeOrder(String orderId, String failAt) {
        logger.info("--- Starting Saga for Order: {} ---", orderId);
        List<Runnable> compensations = new ArrayList<>();

        try {
            // Step 1: Payment
            if ("payment".equalsIgnoreCase(failAt)) throw new RuntimeException("Payment failed!");
            paymentService.reserve(orderId);
            compensations.add(() -> paymentService.refund(orderId));
            logger.info("Step 1: Payment Reserved.");

            // Step 2: Inventory
            if ("inventory".equalsIgnoreCase(failAt)) throw new RuntimeException("Inventory unavailable!");
            inventoryService.deduct(orderId);
            compensations.add(() -> inventoryService.addBack(orderId));
            logger.info("Step 2: Inventory Deducted.");

            logger.info("--- Saga Completed Successfully ---");
            return "SUCCESS: Order " + orderId + " placed.";

        } catch (Exception e) {
            logger.error("Saga Failed at {}: {}. Starting Compensating Transactions...", failAt, e.getMessage());
            // Rollback in reverse order
            for (int i = compensations.size() - 1; i >= 0; i--) {
                compensations.get(i).run();
            }
            return "FAILED: " + e.getMessage() + " (Compensated)";
        }
    }
}

@Service
class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    void reserve(String id) { logger.info("[Payment] Reserving funds for order {}", id); }
    void refund(String id) { logger.warn("[Compensating] Refunding funds for order {}", id); }
}

@Service
class InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
    void deduct(String id) { logger.info("[Inventory] Deducting stock for order {}", id); }
    void addBack(String id) { logger.warn("[Compensating] Adding stock back for order {}", id); }
}
