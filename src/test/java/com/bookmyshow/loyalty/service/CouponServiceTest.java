package com.bookmyshow.loyalty.service;

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
import com.bookmyshow.loyalty.service.impl.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock private CouponRepository repository;
    @Mock private CouponUsageRepository usageRepository;
    @Mock private CouponMapper mapper;
    @InjectMocks private CouponServiceImpl service;

    private Coupon activeCoupon;
    private Coupon expiredCoupon;

    @BeforeEach
    void setUp() {
        activeCoupon = Coupon.builder()
                .couponId(1).couponCode("FLAT100").discountType("FLAT")
                .discountAmount(BigDecimal.valueOf(100)).minBookingAmount(BigDecimal.valueOf(500))
                .validFrom(LocalDate.of(2024, 1, 1)).validTo(LocalDate.of(2027, 12, 31))
                .usageLimit(5).couponStatus("ACTIVE").build();

        expiredCoupon = Coupon.builder()
                .couponId(2).couponCode("EXPIRED10").discountType("PERCENTAGE")
                .discountAmount(BigDecimal.valueOf(10)).minBookingAmount(BigDecimal.valueOf(100))
                .validFrom(LocalDate.of(2023, 1, 1)).validTo(LocalDate.of(2023, 12, 31))
                .usageLimit(10).couponStatus("EXPIRED").build();
    }

    @Test
    void create_success() {
        CouponRequest request = CouponRequest.builder()
                .couponCode("NEW50").discountType("FLAT").discountAmount(BigDecimal.valueOf(50))
                .minBookingAmount(BigDecimal.ZERO).validFrom(LocalDate.now()).validTo(LocalDate.now().plusMonths(6))
                .usageLimit(10).build();
        Coupon saved = Coupon.builder().couponId(3).couponCode("NEW50").build();
        CouponResponse resp = CouponResponse.builder().couponId(3).couponCode("NEW50").build();

        when(repository.existsByCouponCode("NEW50")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(saved);
        when(repository.save(saved)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(resp);

        CouponResponse result = service.create(request);
        assertThat(result.getCouponCode()).isEqualTo("NEW50");
    }

    @Test
    void create_duplicate_throws() {
        CouponRequest request = CouponRequest.builder().couponCode("FLAT100").build();
        when(repository.existsByCouponCode("FLAT100")).thenReturn(true);
        assertThatThrownBy(() -> service.create(request)).isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void validate_validCoupon_returnsValid() {
        PromoValidateRequest request = PromoValidateRequest.builder()
                .couponCode("FLAT100").bookingAmount(BigDecimal.valueOf(750)).userId(1).build();

        when(repository.findByCouponCode("FLAT100")).thenReturn(Optional.of(activeCoupon));
        when(usageRepository.countByCouponCouponId(1)).thenReturn(0L);
        when(usageRepository.countByCouponCouponIdAndUserId(1, 1)).thenReturn(0L);

        PromoValidateResponse result = service.validate(request);
        assertThat(result.isValid()).isTrue();
        assertThat(result.getFinalDiscount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void validate_expiredCoupon_returnsInvalid() {
        PromoValidateRequest request = PromoValidateRequest.builder()
                .couponCode("EXPIRED10").bookingAmount(BigDecimal.valueOf(500)).build();

        when(repository.findByCouponCode("EXPIRED10")).thenReturn(Optional.of(expiredCoupon));

        PromoValidateResponse result = service.validate(request);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("not active");
    }

    @Test
    void validate_belowMinBooking_returnsInvalid() {
        PromoValidateRequest request = PromoValidateRequest.builder()
                .couponCode("FLAT100").bookingAmount(BigDecimal.valueOf(100)).build();

        when(repository.findByCouponCode("FLAT100")).thenReturn(Optional.of(activeCoupon));

        PromoValidateResponse result = service.validate(request);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("minimum");
    }

    @Test
    void validate_usageLimitExceeded_returnsInvalid() {
        PromoValidateRequest request = PromoValidateRequest.builder()
                .couponCode("FLAT100").bookingAmount(BigDecimal.valueOf(750)).build();

        when(repository.findByCouponCode("FLAT100")).thenReturn(Optional.of(activeCoupon));
        when(usageRepository.countByCouponCouponId(1)).thenReturn(5L);

        PromoValidateResponse result = service.validate(request);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getMessage()).contains("usage limit");
    }

    @Test
    void apply_validCoupon_success() {
        PromoApplyRequest request = PromoApplyRequest.builder()
                .couponCode("FLAT100").userId(2).bookingId(201)
                .bookingAmount(BigDecimal.valueOf(750)).build();

        CouponUsage savedUsage = CouponUsage.builder().usageId(1).coupon(activeCoupon).userId(2).bookingId(201).build();

        when(repository.findByCouponCode("FLAT100")).thenReturn(Optional.of(activeCoupon));
        when(usageRepository.countByCouponCouponId(1)).thenReturn(0L);
        when(usageRepository.countByCouponCouponIdAndUserId(1, 2)).thenReturn(0L);
        when(usageRepository.save(any(CouponUsage.class))).thenReturn(savedUsage);

        PromoApplyResponse result = service.apply(request);
        assertThat(result.isApplied()).isTrue();
        assertThat(result.getDiscountApplied()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void apply_invalidCoupon_throws() {
        PromoApplyRequest request = PromoApplyRequest.builder()
                .couponCode("EXPIRED10").userId(1).bookingId(101)
                .bookingAmount(BigDecimal.valueOf(500)).build();

        when(repository.findByCouponCode("EXPIRED10")).thenReturn(Optional.of(expiredCoupon));
        assertThatThrownBy(() -> service.apply(request)).isInstanceOf(CouponNotValidException.class);
    }

    @Test
    void apply_notFound_throws() {
        PromoApplyRequest request = PromoApplyRequest.builder()
                .couponCode("NONEXIST").userId(1).bookingId(101)
                .bookingAmount(BigDecimal.valueOf(500)).build();

        when(repository.findByCouponCode("NONEXIST")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.apply(request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_softDeletes() {
        when(repository.findById(1)).thenReturn(Optional.of(activeCoupon));
        service.delete(1);
        assertThat(activeCoupon.getCouponStatus()).isEqualTo("INACTIVE");
        verify(repository).save(activeCoupon);
    }
}
