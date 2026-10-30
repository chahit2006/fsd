package com.bookmyshow.loyalty.mapper;

import com.bookmyshow.loyalty.dto.request.CouponRequest;
import com.bookmyshow.loyalty.dto.response.CouponResponse;
import com.bookmyshow.loyalty.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public Coupon toEntity(CouponRequest request) {
        return Coupon.builder()
                .couponCode(request.getCouponCode().toUpperCase())
                .discountType(request.getDiscountType())
                .discountAmount(request.getDiscountAmount())
                .minBookingAmount(request.getMinBookingAmount())
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .usageLimit(request.getUsageLimit())
                .couponStatus(request.getCouponStatus() != null ? request.getCouponStatus() : "ACTIVE")
                .build();
    }

    public CouponResponse toResponse(Coupon entity) {
        return CouponResponse.builder()
                .couponId(entity.getCouponId())
                .couponCode(entity.getCouponCode())
                .discountType(entity.getDiscountType())
                .discountAmount(entity.getDiscountAmount())
                .minBookingAmount(entity.getMinBookingAmount())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .usageLimit(entity.getUsageLimit())
                .couponStatus(entity.getCouponStatus())
                .build();
    }

    public void updateEntity(Coupon entity, CouponRequest request) {
        entity.setCouponCode(request.getCouponCode().toUpperCase());
        entity.setDiscountType(request.getDiscountType());
        entity.setDiscountAmount(request.getDiscountAmount());
        entity.setMinBookingAmount(request.getMinBookingAmount());
        entity.setValidFrom(request.getValidFrom());
        entity.setValidTo(request.getValidTo());
        entity.setUsageLimit(request.getUsageLimit());
        if (request.getCouponStatus() != null) entity.setCouponStatus(request.getCouponStatus());
    }
}
