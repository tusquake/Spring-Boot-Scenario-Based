package com.interview.debug.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class BalanceCannotbeNegativeException extends RuntimeException {
    public BalanceCannotbeNegativeException(String message) {
        super(message);
    }
}
