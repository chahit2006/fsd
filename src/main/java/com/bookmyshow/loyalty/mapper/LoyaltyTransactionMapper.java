package com.bookmyshow.loyalty.mapper;

import com.bookmyshow.loyalty.dto.response.LoyaltyTransactionResponse;
import com.bookmyshow.loyalty.entity.LoyaltyTransaction;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyTransactionMapper {

    public LoyaltyTransactionResponse toResponse(LoyaltyTransaction entity) {
        return LoyaltyTransactionResponse.builder()
                .txnId(entity.getTxnId())
                .accountId(entity.getAccount().getAccountId())
                .bookingId(entity.getBookingId())
                .txnType(entity.getTxnType())
                .pointsDelta(entity.getPointsDelta())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
