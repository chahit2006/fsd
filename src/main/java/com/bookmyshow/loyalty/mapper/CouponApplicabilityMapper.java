package com.bookmyshow.loyalty.mapper;

import com.bookmyshow.loyalty.dto.request.CouponApplicabilityRequest;
import com.bookmyshow.loyalty.dto.response.CouponApplicabilityResponse;
import com.bookmyshow.loyalty.entity.Coupon;
import com.bookmyshow.loyalty.entity.CouponApplicability;
import org.springframework.stereotype.Component;

@Component
public class CouponApplicabilityMapper {

    public CouponApplicability toEntity(CouponApplicabilityRequest request, Coupon coupon) {
        return CouponApplicability.builder()
                .coupon(coupon)
                .movieId(request.getMovieId())
                .hostingEventId(request.getHostingEventId())
                .build();
    }

    public CouponApplicabilityResponse toResponse(CouponApplicability entity) {
        return CouponApplicabilityResponse.builder()
                .applicabilityId(entity.getApplicabilityId())
                .couponId(entity.getCoupon().getCouponId())
                .movieId(entity.getMovieId())
                .hostingEventId(entity.getHostingEventId())
                .build();
    }

    public void updateEntity(CouponApplicability entity, CouponApplicabilityRequest request, Coupon coupon) {
        entity.setCoupon(coupon);
        entity.setMovieId(request.getMovieId());
        entity.setHostingEventId(request.getHostingEventId());
    }
}
