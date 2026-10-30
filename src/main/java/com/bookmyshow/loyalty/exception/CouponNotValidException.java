package com.bookmyshow.loyalty.exception;

public class CouponNotValidException extends BusinessException {

    public CouponNotValidException(String reason) {
        super("Coupon is not valid: " + reason);
    }
}
