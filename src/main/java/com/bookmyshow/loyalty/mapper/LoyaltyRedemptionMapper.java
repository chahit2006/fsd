package com.bookmyshow.loyalty.mapper;

import com.bookmyshow.loyalty.dto.response.LoyaltyRedemptionResponse;
import com.bookmyshow.loyalty.entity.LoyaltyRedemption;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyRedemptionMapper {

    public LoyaltyRedemptionResponse toResponse(LoyaltyRedemption entity) {
        return LoyaltyRedemptionResponse.builder()
                .redemptionId(entity.getRedemptionId())
                .accountId(entity.getAccount().getAccountId())
                .bookingId(entity.getBookingId())
                .pointsUsed(entity.getPointsUsed())
                .discountGiven(entity.getDiscountGiven())
                .redeemedAt(entity.getRedeemedAt())
                .build();
    }
}
