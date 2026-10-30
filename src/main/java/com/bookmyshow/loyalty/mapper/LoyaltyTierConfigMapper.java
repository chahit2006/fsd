package com.bookmyshow.loyalty.mapper;

import com.bookmyshow.loyalty.dto.request.LoyaltyTierConfigRequest;
import com.bookmyshow.loyalty.dto.response.LoyaltyTierConfigResponse;
import com.bookmyshow.loyalty.entity.LoyaltyTierConfig;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyTierConfigMapper {

    public LoyaltyTierConfig toEntity(LoyaltyTierConfigRequest request) {
        return LoyaltyTierConfig.builder()
                .tierName(request.getTierName())
                .minPoints(request.getMinPoints())
                .maxPoints(request.getMaxPoints())
                .earnMultiplier(request.getEarnMultiplier())
                .expiryDays(request.getExpiryDays())
                .build();
    }

    public LoyaltyTierConfigResponse toResponse(LoyaltyTierConfig entity) {
        return LoyaltyTierConfigResponse.builder()
                .tierId(entity.getTierId())
                .tierName(entity.getTierName())
                .minPoints(entity.getMinPoints())
                .maxPoints(entity.getMaxPoints())
                .earnMultiplier(entity.getEarnMultiplier())
                .expiryDays(entity.getExpiryDays())
                .build();
    }

    public void updateEntity(LoyaltyTierConfig entity, LoyaltyTierConfigRequest request) {
        entity.setTierName(request.getTierName());
        entity.setMinPoints(request.getMinPoints());
        entity.setMaxPoints(request.getMaxPoints());
        entity.setEarnMultiplier(request.getEarnMultiplier());
        entity.setExpiryDays(request.getExpiryDays());
    }
}
