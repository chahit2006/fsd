package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.LoyaltyTierConfigRequest;
import com.bookmyshow.loyalty.dto.response.LoyaltyTierConfigResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoyaltyTierConfigService {
    LoyaltyTierConfigResponse create(LoyaltyTierConfigRequest request);
    LoyaltyTierConfigResponse getById(Integer id);
    Page<LoyaltyTierConfigResponse> getAll(Pageable pageable);
    LoyaltyTierConfigResponse update(Integer id, LoyaltyTierConfigRequest request);
    LoyaltyTierConfigResponse patch(Integer id, LoyaltyTierConfigRequest request);
    void delete(Integer id);
    String determineTier(int totalPoints);
}
