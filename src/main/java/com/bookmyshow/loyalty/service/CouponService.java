package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.CouponRequest;
import com.bookmyshow.loyalty.dto.request.PromoApplyRequest;
import com.bookmyshow.loyalty.dto.request.PromoValidateRequest;
import com.bookmyshow.loyalty.dto.response.CouponResponse;
import com.bookmyshow.loyalty.dto.response.PromoApplyResponse;
import com.bookmyshow.loyalty.dto.response.PromoValidateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {
    CouponResponse create(CouponRequest request);
    CouponResponse getById(Integer id);
    Page<CouponResponse> getAll(Pageable pageable);
    CouponResponse update(Integer id, CouponRequest request);
    CouponResponse patch(Integer id, CouponRequest request);
    void delete(Integer id);
    PromoValidateResponse validate(PromoValidateRequest request);
    PromoApplyResponse apply(PromoApplyRequest request);
}
