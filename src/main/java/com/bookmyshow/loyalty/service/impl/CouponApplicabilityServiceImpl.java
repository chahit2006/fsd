package com.bookmyshow.loyalty.service.impl;

import com.bookmyshow.loyalty.dto.request.CouponApplicabilityRequest;
import com.bookmyshow.loyalty.dto.response.CouponApplicabilityResponse;
import com.bookmyshow.loyalty.entity.Coupon;
import com.bookmyshow.loyalty.entity.CouponApplicability;
import com.bookmyshow.loyalty.exception.ResourceNotFoundException;
import com.bookmyshow.loyalty.mapper.CouponApplicabilityMapper;
import com.bookmyshow.loyalty.repository.CouponApplicabilityRepository;
import com.bookmyshow.loyalty.repository.CouponRepository;
import com.bookmyshow.loyalty.service.CouponApplicabilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponApplicabilityServiceImpl implements CouponApplicabilityService {

    private final CouponApplicabilityRepository repository;
    private final CouponRepository couponRepository;
    private final CouponApplicabilityMapper mapper;

    @Override
    @Transactional
    public CouponApplicabilityResponse create(CouponApplicabilityRequest request) {
        Coupon coupon = couponRepository.findById(request.getCouponId())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponId", request.getCouponId()));
        CouponApplicability entity = mapper.toEntity(request, coupon);
        entity = repository.save(entity);
        log.info("Created coupon applicability: id={}, couponId={}", entity.getApplicabilityId(), coupon.getCouponId());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponApplicabilityResponse getById(Integer id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponApplicabilityResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public CouponApplicabilityResponse update(Integer id, CouponApplicabilityRequest request) {
        CouponApplicability entity = findById(id);
        Coupon coupon = couponRepository.findById(request.getCouponId())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponId", request.getCouponId()));
        mapper.updateEntity(entity, request, coupon);
        entity = repository.save(entity);
        log.info("Updated coupon applicability: {}", entity.getApplicabilityId());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public CouponApplicabilityResponse patch(Integer id, CouponApplicabilityRequest request) {
        CouponApplicability entity = findById(id);
        if (request.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(request.getCouponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponId", request.getCouponId()));
            entity.setCoupon(coupon);
        }
        if (request.getMovieId() != null) entity.setMovieId(request.getMovieId());
        if (request.getHostingEventId() != null) entity.setHostingEventId(request.getHostingEventId());
        entity = repository.save(entity);
        log.info("Patched coupon applicability: {}", entity.getApplicabilityId());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        CouponApplicability entity = findById(id);
        repository.delete(entity);
        log.info("Deleted coupon applicability: {}", id);
    }

    private CouponApplicability findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CouponApplicability", "applicabilityId", id));
    }
}
