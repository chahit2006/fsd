package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.LoyaltyAccountRequest;
import com.bookmyshow.loyalty.dto.response.BalanceResponse;
import com.bookmyshow.loyalty.dto.response.LoyaltyAccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoyaltyAccountService {
    LoyaltyAccountResponse create(LoyaltyAccountRequest request);
    LoyaltyAccountResponse getById(Integer id);
    Page<LoyaltyAccountResponse> getAll(Pageable pageable);
    LoyaltyAccountResponse update(Integer id, LoyaltyAccountRequest request);
    LoyaltyAccountResponse patch(Integer id, LoyaltyAccountRequest request);
    void delete(Integer id);
    BalanceResponse getBalanceByUserId(Integer userId);
}
