package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.EarnPointsRequest;
import com.bookmyshow.loyalty.dto.request.RedeemPointsRequest;
import com.bookmyshow.loyalty.dto.request.ReverseRequest;
import com.bookmyshow.loyalty.dto.response.EarnPointsResponse;
import com.bookmyshow.loyalty.dto.response.RedeemPointsResponse;
import com.bookmyshow.loyalty.dto.response.ReverseResponse;

public interface LoyaltyPointsService {
    EarnPointsResponse earn(EarnPointsRequest request);
    RedeemPointsResponse redeem(RedeemPointsRequest request);
    ReverseResponse reverse(ReverseRequest request);
}
