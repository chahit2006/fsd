package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.LoyaltyTransactionRequest;
import com.bookmyshow.loyalty.dto.response.LoyaltyTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoyaltyTransactionService {
    LoyaltyTransactionResponse create(LoyaltyTransactionRequest request);
    LoyaltyTransactionResponse getById(Integer id);
    Page<LoyaltyTransactionResponse> getAll(Pageable pageable);
    LoyaltyTransactionResponse update(Integer id, LoyaltyTransactionRequest request);
    LoyaltyTransactionResponse patch(Integer id, LoyaltyTransactionRequest request);
    void delete(Integer id);
    Page<LoyaltyTransactionResponse> getByUserId(Integer userId, Pageable pageable);
}
