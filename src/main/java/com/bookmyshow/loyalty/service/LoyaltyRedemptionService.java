package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.LoyaltyRedemptionRequest;
import com.bookmyshow.loyalty.dto.response.LoyaltyRedemptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoyaltyRedemptionService {
    LoyaltyRedemptionResponse create(LoyaltyRedemptionRequest request);
    LoyaltyRedemptionResponse getById(Integer id);
    Page<LoyaltyRedemptionResponse> getAll(Pageable pageable);
    LoyaltyRedemptionResponse update(Integer id, LoyaltyRedemptionRequest request);
    LoyaltyRedemptionResponse patch(Integer id, LoyaltyRedemptionRequest request);
    void delete(Integer id);
}
