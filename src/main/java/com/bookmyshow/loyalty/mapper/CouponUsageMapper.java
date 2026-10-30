package com.bookmyshow.loyalty.mapper;

import com.bookmyshow.loyalty.dto.response.CouponUsageResponse;
import com.bookmyshow.loyalty.entity.CouponUsage;
import org.springframework.stereotype.Component;

@Component
public class CouponUsageMapper {

    public CouponUsageResponse toResponse(CouponUsage entity) {
        return CouponUsageResponse.builder()
                .usageId(entity.getUsageId())
                .couponId(entity.getCoupon().getCouponId())
                .userId(entity.getUserId())
                .bookingId(entity.getBookingId())
                .usedAt(entity.getUsedAt())
                .build();
    }
}
