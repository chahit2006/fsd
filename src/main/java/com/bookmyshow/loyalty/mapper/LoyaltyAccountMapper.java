package com.bookmyshow.loyalty.mapper;

import com.bookmyshow.loyalty.dto.request.LoyaltyAccountRequest;
import com.bookmyshow.loyalty.dto.response.BalanceResponse;
import com.bookmyshow.loyalty.dto.response.LoyaltyAccountResponse;
import com.bookmyshow.loyalty.entity.LoyaltyAccount;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyAccountMapper {

    public LoyaltyAccount toEntity(LoyaltyAccountRequest request) {
        return LoyaltyAccount.builder()
                .userId(request.getUserId())
                .totalPoints(request.getTotalPoints() != null ? request.getTotalPoints() : 0)
                .redeemablePoints(request.getRedeemablePoints() != null ? request.getRedeemablePoints() : 0)
                .tier(request.getTier() != null ? request.getTier() : "BRONZE")
                .build();
    }

    public LoyaltyAccountResponse toResponse(LoyaltyAccount entity) {
        return LoyaltyAccountResponse.builder()
                .accountId(entity.getAccountId())
                .userId(entity.getUserId())
                .totalPoints(entity.getTotalPoints())
                .redeemablePoints(entity.getRedeemablePoints())
                .tier(entity.getTier())
                .lastUpdated(entity.getLastUpdated())
                .build();
    }

    public BalanceResponse toBalanceResponse(LoyaltyAccount entity) {
        return BalanceResponse.builder()
                .userId(entity.getUserId())
                .accountId(entity.getAccountId())
                .totalPoints(entity.getTotalPoints())
                .redeemablePoints(entity.getRedeemablePoints())
                .tier(entity.getTier())
                .build();
    }

    public void updateEntity(LoyaltyAccount entity, LoyaltyAccountRequest request) {
        if (request.getTotalPoints() != null) entity.setTotalPoints(request.getTotalPoints());
        if (request.getRedeemablePoints() != null) entity.setRedeemablePoints(request.getRedeemablePoints());
        if (request.getTier() != null) entity.setTier(request.getTier());
    }
}
