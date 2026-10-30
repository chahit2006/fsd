package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.CouponApplicabilityRequest;
import com.bookmyshow.loyalty.dto.response.CouponApplicabilityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponApplicabilityService {
    CouponApplicabilityResponse create(CouponApplicabilityRequest request);
    CouponApplicabilityResponse getById(Integer id);
    Page<CouponApplicabilityResponse> getAll(Pageable pageable);
    CouponApplicabilityResponse update(Integer id, CouponApplicabilityRequest request);
    CouponApplicabilityResponse patch(Integer id, CouponApplicabilityRequest request);
    void delete(Integer id);
}
