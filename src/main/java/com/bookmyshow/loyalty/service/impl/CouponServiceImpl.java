package com.bookmyshow.loyalty.service.impl;

import com.bookmyshow.loyalty.dto.request.CouponRequest;
import com.bookmyshow.loyalty.dto.request.PromoApplyRequest;
import com.bookmyshow.loyalty.dto.request.PromoValidateRequest;
import com.bookmyshow.loyalty.dto.response.CouponResponse;
import com.bookmyshow.loyalty.dto.response.PromoApplyResponse;
import com.bookmyshow.loyalty.dto.response.PromoValidateResponse;
import com.bookmyshow.loyalty.entity.Coupon;
import com.bookmyshow.loyalty.entity.CouponUsage;
import com.bookmyshow.loyalty.exception.CouponNotValidException;
import com.bookmyshow.loyalty.exception.DuplicateResourceException;
import com.bookmyshow.loyalty.exception.ResourceNotFoundException;
import com.bookmyshow.loyalty.mapper.CouponMapper;
import com.bookmyshow.loyalty.repository.CouponRepository;
import com.bookmyshow.loyalty.repository.CouponUsageRepository;
import com.bookmyshow.loyalty.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository repository;
    private final CouponUsageRepository usageRepository;
    private final CouponMapper mapper;

    @Override
    @Transactional
    public CouponResponse create(CouponRequest request) {
        if (repository.existsByCouponCode(request.getCouponCode().toUpperCase())) {
            throw new DuplicateResourceException("Coupon", "couponCode", request.getCouponCode());
        }
        Coupon entity = mapper.toEntity(request);
        entity = repository.save(entity);
        log.info("Created coupon: {}", entity.getCouponCode());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponse getById(Integer id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public CouponResponse update(Integer id, CouponRequest request) {
        Coupon entity = findById(id);
        String newCode = request.getCouponCode().toUpperCase();
        if (!entity.getCouponCode().equals(newCode) && repository.existsByCouponCode(newCode)) {
            throw new DuplicateResourceException("Coupon", "couponCode", newCode);
        }
        mapper.updateEntity(entity, request);
        entity = repository.save(entity);
        log.info("Updated coupon: {}", entity.getCouponCode());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public CouponResponse patch(Integer id, CouponRequest request) {
        Coupon entity = findById(id);
        if (request.getCouponCode() != null) {
            String newCode = request.getCouponCode().toUpperCase();
            if (!entity.getCouponCode().equals(newCode) && repository.existsByCouponCode(newCode)) {
                throw new DuplicateResourceException("Coupon", "couponCode", newCode);
            }
            entity.setCouponCode(newCode);
        }
        if (request.getDiscountType() != null) entity.setDiscountType(request.getDiscountType());
        if (request.getDiscountAmount() != null) entity.setDiscountAmount(request.getDiscountAmount());
        if (request.getMinBookingAmount() != null) entity.setMinBookingAmount(request.getMinBookingAmount());
        if (request.getValidFrom() != null) entity.setValidFrom(request.getValidFrom());
        if (request.getValidTo() != null) entity.setValidTo(request.getValidTo());
        if (request.getUsageLimit() != null) entity.setUsageLimit(request.getUsageLimit());
        if (request.getCouponStatus() != null) entity.setCouponStatus(request.getCouponStatus());
        entity = repository.save(entity);
        log.info("Patched coupon: {}", entity.getCouponCode());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Coupon entity = findById(id);
        entity.setCouponStatus("INACTIVE");
        repository.save(entity);
        log.info("Soft-deleted (deactivated) coupon: {}", entity.getCouponCode());
    }

    @Override
    @Transactional(readOnly = true)
    public PromoValidateResponse validate(PromoValidateRequest request) {
        Coupon coupon = repository.findByCouponCode(request.getCouponCode().toUpperCase())
                .orElse(null);

        if (coupon == null) {
            return PromoValidateResponse.builder()
                    .valid(false).couponCode(request.getCouponCode())
                    .message("Coupon code not found").build();
        }

        String reason = validateCoupon(coupon, request.getBookingAmount(), request.getUserId());
        if (reason != null) {
            return PromoValidateResponse.builder()
                    .valid(false).couponCode(coupon.getCouponCode())
                    .message(reason).build();
        }

        BigDecimal discount = calculateDiscount(coupon, request.getBookingAmount());
        return PromoValidateResponse.builder()
                .valid(true)
                .couponCode(coupon.getCouponCode())
                .discountType(coupon.getDiscountType())
                .discountAmount(coupon.getDiscountAmount())
                .finalDiscount(discount)
                .message("Coupon is valid")
                .build();
    }

    @Override
    @Transactional
    public PromoApplyResponse apply(PromoApplyRequest request) {
        Coupon coupon = repository.findByCouponCode(request.getCouponCode().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponCode", request.getCouponCode()));

        String reason = validateCoupon(coupon, request.getBookingAmount(), request.getUserId());
        if (reason != null) {
            throw new CouponNotValidException(reason);
        }

        BigDecimal discount = calculateDiscount(coupon, request.getBookingAmount());

        CouponUsage usage = CouponUsage.builder()
                .coupon(coupon)
                .userId(request.getUserId())
                .bookingId(request.getBookingId())
                .build();
        usage = usageRepository.save(usage);

        log.info("Applied coupon {} for user {} on booking {}, discount={}",
                coupon.getCouponCode(), request.getUserId(), request.getBookingId(), discount);

        return PromoApplyResponse.builder()
                .applied(true)
                .usageId(usage.getUsageId())
                .couponCode(coupon.getCouponCode())
                .discountApplied(discount)
                .message("Coupon applied successfully")
                .build();
    }

    private String validateCoupon(Coupon coupon, BigDecimal bookingAmount, Integer userId) {
        if (!"ACTIVE".equals(coupon.getCouponStatus())) {
            return "Coupon is not active";
        }
        LocalDate today = LocalDate.now();
        if (today.isBefore(coupon.getValidFrom()) || today.isAfter(coupon.getValidTo())) {
            return "Coupon has expired or is not yet valid";
        }
        if (bookingAmount.compareTo(coupon.getMinBookingAmount()) < 0) {
            return "Booking amount does not meet minimum requirement of " + coupon.getMinBookingAmount();
        }
        long totalUsage = usageRepository.countByCouponCouponId(coupon.getCouponId());
        if (totalUsage >= coupon.getUsageLimit()) {
            return "Coupon usage limit exceeded";
        }
        if (userId != null) {
            long userUsage = usageRepository.countByCouponCouponIdAndUserId(coupon.getCouponId(), userId);
            if (userUsage > 0) {
                return "User has already used this coupon";
            }
        }
        return null;
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal bookingAmount) {
        if ("PERCENTAGE".equals(coupon.getDiscountType())) {
            return bookingAmount.multiply(coupon.getDiscountAmount())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        return coupon.getDiscountAmount().min(bookingAmount);
    }

    private Coupon findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon", "couponId", id));
    }
}
