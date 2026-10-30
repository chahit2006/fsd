package com.bookmyshow.loyalty.service.impl;

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
import com.bookmyshow.loyalty.service.LoyaltyPointsService;
import com.bookmyshow.loyalty.service.LoyaltyTierConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoyaltyPointsServiceImpl implements LoyaltyPointsService {

    private final LoyaltyAccountRepository accountRepository;
    private final LoyaltyTransactionRepository transactionRepository;
    private final LoyaltyRedemptionRepository redemptionRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final LoyaltyTierConfigService tierConfigService;

    @Value("${loyalty.points.earn-rate:1}")
    private int earnRate;

    @Value("${loyalty.points.earn-per-amount:10}")
    private int earnPerAmount;

    @Value("${loyalty.points.redeem-conversion:1.0}")
    private double redeemConversion;

    @Value("${loyalty.points.max-redeem-percentage:50}")
    private int maxRedeemPercentage;

    @Override
    @Transactional
    public EarnPointsResponse earn(EarnPointsRequest request) {
        LoyaltyAccount account = accountRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    LoyaltyAccount newAccount = LoyaltyAccount.builder()
                            .userId(request.getUserId())
                            .totalPoints(0)
                            .redeemablePoints(0)
                            .tier("BRONZE")
                            .build();
                    return accountRepository.save(newAccount);
                });

        int pointsEarned = request.getBookingAmount()
                .divide(BigDecimal.valueOf(earnPerAmount), 0, RoundingMode.FLOOR)
                .intValue() * earnRate;

        account.setTotalPoints(account.getTotalPoints() + pointsEarned);
        account.setRedeemablePoints(account.getRedeemablePoints() + pointsEarned);

        String newTier = tierConfigService.determineTier(account.getTotalPoints());
        account.setTier(newTier);
        account = accountRepository.save(account);

        LoyaltyTransaction txn = LoyaltyTransaction.builder()
                .account(account)
                .bookingId(request.getBookingId())
                .txnType("EARN")
                .pointsDelta(pointsEarned)
                .description("Points earned for booking #" + request.getBookingId())
                .build();
        transactionRepository.save(txn);

        log.info("User {} earned {} points for booking {}", request.getUserId(), pointsEarned, request.getBookingId());

        return EarnPointsResponse.builder()
                .userId(request.getUserId())
                .pointsEarned(pointsEarned)
                .totalPoints(account.getTotalPoints())
                .redeemablePoints(account.getRedeemablePoints())
                .tier(account.getTier())
                .message(String.format("Earned %d points", pointsEarned))
                .build();
    }

    @Override
    @Transactional
    public RedeemPointsResponse redeem(RedeemPointsRequest request) {
        LoyaltyAccount account = accountRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "userId", request.getUserId()));

        if (account.getRedeemablePoints() < request.getPointsToRedeem()) {
            throw new InsufficientPointsException(account.getRedeemablePoints(), request.getPointsToRedeem());
        }

        BigDecimal discount = BigDecimal.valueOf(request.getPointsToRedeem() * redeemConversion);
        BigDecimal maxDiscount = request.getBookingAmount()
                .multiply(BigDecimal.valueOf(maxRedeemPercentage))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        if (discount.compareTo(maxDiscount) > 0) {
            throw new BusinessException(String.format(
                    "Discount %.2f exceeds max allowed %.2f (%d%% of booking amount)",
                    discount, maxDiscount, maxRedeemPercentage));
        }

        account.setRedeemablePoints(account.getRedeemablePoints() - request.getPointsToRedeem());
        account = accountRepository.save(account);

        LoyaltyTransaction txn = LoyaltyTransaction.builder()
                .account(account)
                .bookingId(request.getBookingId())
                .txnType("REDEEM")
                .pointsDelta(-request.getPointsToRedeem())
                .description("Points redeemed for booking #" + request.getBookingId())
                .build();
        transactionRepository.save(txn);

        LoyaltyRedemption redemption = LoyaltyRedemption.builder()
                .account(account)
                .bookingId(request.getBookingId())
                .pointsUsed(request.getPointsToRedeem())
                .discountGiven(discount)
                .build();
        redemptionRepository.save(redemption);

        log.info("User {} redeemed {} points for discount {} on booking {}",
                request.getUserId(), request.getPointsToRedeem(), discount, request.getBookingId());

        return RedeemPointsResponse.builder()
                .userId(request.getUserId())
                .pointsRedeemed(request.getPointsToRedeem())
                .discountGiven(discount)
                .remainingRedeemablePoints(account.getRedeemablePoints())
                .message(String.format("Redeemed %d points for discount of %.2f", request.getPointsToRedeem(), discount))
                .build();
    }

    @Override
    @Transactional
    public ReverseResponse reverse(ReverseRequest request) {
        LoyaltyAccount account = accountRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "userId", request.getUserId()));

        int pointsReversed = 0;

        List<LoyaltyTransaction> transactions = transactionRepository.findByBookingId(request.getBookingId());
        for (LoyaltyTransaction txn : transactions) {
            if ("EARN".equals(txn.getTxnType())) {
                pointsReversed += txn.getPointsDelta();

                LoyaltyTransaction reverseTxn = LoyaltyTransaction.builder()
                        .account(account)
                        .bookingId(request.getBookingId())
                        .txnType("REVERSE")
                        .pointsDelta(-txn.getPointsDelta())
                        .description("Reversal of earn for cancelled booking #" + request.getBookingId())
                        .build();
                transactionRepository.save(reverseTxn);
            }
        }

        List<LoyaltyRedemption> redemptions = redemptionRepository.findByBookingId(request.getBookingId());
        for (LoyaltyRedemption redemption : redemptions) {
            account.setRedeemablePoints(account.getRedeemablePoints() + redemption.getPointsUsed());

            LoyaltyTransaction reverseTxn = LoyaltyTransaction.builder()
                    .account(account)
                    .bookingId(request.getBookingId())
                    .txnType("REVERSE")
                    .pointsDelta(redemption.getPointsUsed())
                    .description("Reversal of redemption for cancelled booking #" + request.getBookingId())
                    .build();
            transactionRepository.save(reverseTxn);
        }

        account.setTotalPoints(account.getTotalPoints() - pointsReversed);
        account.setRedeemablePoints(account.getRedeemablePoints() - pointsReversed);
        if (account.getTotalPoints() < 0) account.setTotalPoints(0);
        if (account.getRedeemablePoints() < 0) account.setRedeemablePoints(0);

        String newTier = tierConfigService.determineTier(account.getTotalPoints());
        account.setTier(newTier);
        accountRepository.save(account);

        List<CouponUsage> couponUsages = couponUsageRepository.findByBookingId(request.getBookingId());
        int promosReversed = couponUsages.size();
        couponUsageRepository.deleteAll(couponUsages);

        log.info("Reversed {} points and {} promos for user {} booking {}",
                pointsReversed, promosReversed, request.getUserId(), request.getBookingId());

        return ReverseResponse.builder()
                .userId(request.getUserId())
                .bookingId(request.getBookingId())
                .pointsReversed(pointsReversed)
                .promosReversed(promosReversed)
                .message(String.format("Reversed %d points and %d promo usages", pointsReversed, promosReversed))
                .build();
    }
}
