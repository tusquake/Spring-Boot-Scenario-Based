package com.interview.debug.controller;

import com.interview.debug.validation.ValidCouponCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenario43")
public class Scenario43Controller {

    public static class CouponRequest {
        @ValidCouponCode
        private String code;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    @PostMapping("/apply")
    public String applyCoupon(@Valid @RequestBody CouponRequest request) {
        return "Coupon '" + request.getCode() + "' applied successfully!";
    }
}
