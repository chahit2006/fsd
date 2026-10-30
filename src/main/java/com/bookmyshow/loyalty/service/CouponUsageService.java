package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.CouponUsageRequest;
import com.bookmyshow.loyalty.dto.response.CouponUsageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponUsageService {
    CouponUsageResponse create(CouponUsageRequest request);
    CouponUsageResponse getById(Integer id);
    Page<CouponUsageResponse> getAll(Pageable pageable);
    CouponUsageResponse update(Integer id, CouponUsageRequest request);
    CouponUsageResponse patch(Integer id, CouponUsageRequest request);
    void delete(Integer id);
}
