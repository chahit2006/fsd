package com.bookmyshow.loyalty.service.impl;

import com.bookmyshow.loyalty.dto.request.CouponUsageRequest;
import com.bookmyshow.loyalty.dto.response.CouponUsageResponse;
import com.bookmyshow.loyalty.entity.Coupon;
import com.bookmyshow.loyalty.entity.CouponUsage;
import com.bookmyshow.loyalty.exception.ResourceNotFoundException;
import com.bookmyshow.loyalty.mapper.CouponUsageMapper;
import com.bookmyshow.loyalty.repository.CouponRepository;
import com.bookmyshow.loyalty.repository.CouponUsageRepository;
import com.bookmyshow.loyalty.service.CouponUsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponUsageServiceImpl implements CouponUsageService {

    private final CouponUsageRepository repository;
    private final CouponRepository couponRepository;
    private final CouponUsageMapper mapper;

    @Override
    @Transactional
    public CouponUsageResponse create(CouponUsageRequest request) {
        Coupon coupon = couponRepository.findById(request.getCouponId())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponId", request.getCouponId()));
        CouponUsage usage = CouponUsage.builder()
                .coupon(coupon)
                .userId(request.getUserId())
                .bookingId(request.getBookingId())
                .build();
        usage = repository.save(usage);
        log.info("Created coupon usage: id={}, couponId={}, userId={}", usage.getUsageId(), coupon.getCouponId(), usage.getUserId());
        return mapper.toResponse(usage);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponUsageResponse getById(Integer id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponUsageResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public CouponUsageResponse update(Integer id, CouponUsageRequest request) {
        CouponUsage usage = findById(id);
        Coupon coupon = couponRepository.findById(request.getCouponId())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponId", request.getCouponId()));
        usage.setCoupon(coupon);
        usage.setUserId(request.getUserId());
        usage.setBookingId(request.getBookingId());
        usage = repository.save(usage);
        log.info("Updated coupon usage: {}", usage.getUsageId());
        return mapper.toResponse(usage);
    }

    @Override
    @Transactional
    public CouponUsageResponse patch(Integer id, CouponUsageRequest request) {
        CouponUsage usage = findById(id);
        if (request.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponId", request.getCouponId()));
            usage.setCoupon(coupon);
        }
        if (request.getUserId() != null) usage.setUserId(request.getUserId());
        if (request.getBookingId() != null) usage.setBookingId(request.getBookingId());
        usage = repository.save(usage);
        log.info("Patched coupon usage: {}", usage.getUsageId());
        return mapper.toResponse(usage);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        CouponUsage usage = findById(id);
        repository.delete(usage);
        log.info("Deleted coupon usage: {}", id);
    }

    private CouponUsage findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CouponUsage", "usageId", id));
    }
}
