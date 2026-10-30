package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.EarnPointsRequest;
import com.bookmyshow.loyalty.dto.request.RedeemPointsRequest;
import com.bookmyshow.loyalty.dto.request.ReverseRequest;
import com.bookmyshow.loyalty.dto.response.EarnPointsResponse;
import com.bookmyshow.loyalty.dto.response.RedeemPointsResponse;
import com.bookmyshow.loyalty.dto.response.ReverseResponse;
import com.bookmyshow.loyalty.entity.CouponUsage;
import com.bookmyshow.loyalty.entity.LoyaltyAccount;
import com.bookmyshow.loyalty.entity.LoyaltyRedemption;
import com.bookmyshow.loyalty.entity.LoyaltyTransaction;
import com.bookmyshow.loyalty.exception.BusinessException;
import com.bookmyshow.loyalty.exception.InsufficientPointsException;
import com.bookmyshow.loyalty.exception.ResourceNotFoundException;
import com.bookmyshow.loyalty.repository.*;
import com.bookmyshow.loyalty.service.impl.LoyaltyPointsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoyaltyPointsServiceTest {

    @Mock private LoyaltyAccountRepository accountRepository;
    @Mock private LoyaltyTransactionRepository transactionRepository;
    @Mock private LoyaltyRedemptionRepository redemptionRepository;
    @Mock private CouponUsageRepository couponUsageRepository;
    @Mock private LoyaltyTierConfigService tierConfigService;
    @InjectMocks private LoyaltyPointsServiceImpl service;

    private LoyaltyAccount account;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "earnRate", 1);
        ReflectionTestUtils.setField(service, "earnPerAmount", 10);
        ReflectionTestUtils.setField(service, "redeemConversion", 1.0);
        ReflectionTestUtils.setField(service, "maxRedeemPercentage", 50);

        account = LoyaltyAccount.builder()
                .accountId(1).userId(1).totalPoints(1000).redeemablePoints(800)
                .tier("SILVER").version(0).build();
    }

    @Test
    void earn_existingAccount_success() {
        EarnPointsRequest request = EarnPointsRequest.builder()
                .userId(1).bookingId(101).bookingAmount(BigDecimal.valueOf(500)).build();

        when(accountRepository.findByUserId(1)).thenReturn(Optional.of(account));
        when(tierConfigService.determineTier(anyInt())).thenReturn("SILVER");
        when(accountRepository.save(any())).thenReturn(account);
        when(transactionRepository.save(any())).thenReturn(LoyaltyTransaction.builder().txnId(1).build());

        EarnPointsResponse result = service.earn(request);
        assertThat(result.getPointsEarned()).isEqualTo(50);
        assertThat(result.getUserId()).isEqualTo(1);
    }

    @Test
    void earn_newAccount_createsAccount() {
        EarnPointsRequest request = EarnPointsRequest.builder()
                .userId(99).bookingId(201).bookingAmount(BigDecimal.valueOf(200)).build();

        LoyaltyAccount newAccount = LoyaltyAccount.builder()
                .accountId(2).userId(99).totalPoints(0).redeemablePoints(0).tier("BRONZE").build();

        when(accountRepository.findByUserId(99)).thenReturn(Optional.empty());
        when(accountRepository.save(any())).thenReturn(newAccount);
        when(tierConfigService.determineTier(anyInt())).thenReturn("BRONZE");
        when(transactionRepository.save(any())).thenReturn(LoyaltyTransaction.builder().txnId(1).build());

        EarnPointsResponse result = service.earn(request);
        assertThat(result.getPointsEarned()).isEqualTo(20);
    }

    @Test
    void redeem_success() {
        RedeemPointsRequest request = RedeemPointsRequest.builder()
                .userId(1).bookingId(101).pointsToRedeem(100)
                .bookingAmount(BigDecimal.valueOf(500)).build();

        when(accountRepository.findByUserId(1)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);
        when(transactionRepository.save(any())).thenReturn(LoyaltyTransaction.builder().txnId(1).build());
        when(redemptionRepository.save(any())).thenReturn(LoyaltyRedemption.builder().redemptionId(1).build());

        RedeemPointsResponse result = service.redeem(request);
        assertThat(result.getPointsRedeemed()).isEqualTo(100);
        assertThat(result.getDiscountGiven()).isEqualByComparingTo(BigDecimal.valueOf(100.0));
    }

    @Test
    void redeem_insufficientPoints_throws() {
        RedeemPointsRequest request = RedeemPointsRequest.builder()
                .userId(1).bookingId(101).pointsToRedeem(900)
                .bookingAmount(BigDecimal.valueOf(5000)).build();

        when(accountRepository.findByUserId(1)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> service.redeem(request)).isInstanceOf(InsufficientPointsException.class);
    }

    @Test
    void redeem_exceedsMaxPercentage_throws() {
        RedeemPointsRequest request = RedeemPointsRequest.builder()
                .userId(1).bookingId(101).pointsToRedeem(400)
                .bookingAmount(BigDecimal.valueOf(500)).build();

        when(accountRepository.findByUserId(1)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> service.redeem(request)).isInstanceOf(BusinessException.class);
    }

    @Test
    void redeem_accountNotFound_throws() {
        RedeemPointsRequest request = RedeemPointsRequest.builder()
                .userId(99).bookingId(101).pointsToRedeem(10)
                .bookingAmount(BigDecimal.valueOf(500)).build();

        when(accountRepository.findByUserId(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.redeem(request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void reverse_success() {
        ReverseRequest request = ReverseRequest.builder().userId(1).bookingId(101).build();

        LoyaltyTransaction earnTxn = LoyaltyTransaction.builder()
                .txnId(1).account(account).bookingId(101).txnType("EARN").pointsDelta(50).build();

        LoyaltyRedemption redemption = LoyaltyRedemption.builder()
                .redemptionId(1).account(account).bookingId(101).pointsUsed(30)
                .discountGiven(BigDecimal.valueOf(30)).build();

        when(accountRepository.findByUserId(1)).thenReturn(Optional.of(account));
        when(transactionRepository.findByBookingId(101)).thenReturn(List.of(earnTxn));
        when(redemptionRepository.findByBookingId(101)).thenReturn(List.of(redemption));
        when(couponUsageRepository.findByBookingId(101)).thenReturn(Collections.emptyList());
        when(tierConfigService.determineTier(anyInt())).thenReturn("SILVER");
        when(accountRepository.save(any())).thenReturn(account);
        when(transactionRepository.save(any())).thenReturn(LoyaltyTransaction.builder().txnId(2).build());

        ReverseResponse result = service.reverse(request);
        assertThat(result.getPointsReversed()).isEqualTo(50);
        assertThat(result.getPromosReversed()).isEqualTo(0);
    }
}
