package com.bookmyshow.loyalty.exception;

public class InsufficientPointsException extends BusinessException {

    public InsufficientPointsException(int available, int requested) {
        super(String.format("Insufficient points: available=%d, requested=%d", available, requested));
    }
}
