package com.interview.debug.scenario132;

import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    /**
     * Calculates discount based on price.
     * Rules:
     * - price > 1000: 10% discount
     * - price > 500: 5% discount
     * - otherwise: 0% discount
     */
    public double calculateDiscount(double price) {
        if (price > 1000) {
            return price * 0.10;
        } else if (price > 500) {
            return price * 0.05;
        }
        return 0;
    }
}
