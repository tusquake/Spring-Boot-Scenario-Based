package com.interview.debug.scenario132;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountServiceTest {

    private final DiscountService discountService = new DiscountService();

    @Test
    public void testHighDiscount() {
        // Line coverage: price > 1000 branch
        assertEquals(110.0, discountService.calculateDiscount(1100), 0.01);
    }

    @Test
    public void testMediumDiscount() {
        // Line coverage: price > 500 branch
        assertEquals(30.0, discountService.calculateDiscount(600), 0.01);
    }

    @Test
    public void testNoDiscount() {
        // Line coverage: else branch
        assertEquals(0.0, discountService.calculateDiscount(200), 0.01);
    }
}
