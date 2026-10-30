package com.bookmyshow.loyalty.scheduler;

import com.bookmyshow.loyalty.entity.LoyaltyAccount;
import com.bookmyshow.loyalty.entity.LoyaltyTransaction;
import com.bookmyshow.loyalty.repository.LoyaltyAccountRepository;
import com.bookmyshow.loyalty.repository.LoyaltyTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointsExpiryScheduler {

    private final LoyaltyTransactionRepository transactionRepository;
    private final LoyaltyAccountRepository accountRepository;

    @Value("${loyalty.points.expiry-days:365}")
    private int expiryDays;

    @Scheduled(cron = "${loyalty.scheduler.expiry-cron:0 0 1 * * ?}")
    @Transactional
    public void expirePoints() {
        log.info("Starting points expiry job...");
        LocalDateTime cutoff = LocalDateTime.now().minusDays(expiryDays);

        List<LoyaltyTransaction> expirable = transactionRepository.findExpirableTransactions(cutoff);
        int totalExpired = 0;

        for (LoyaltyTransaction txn : expirable) {
            LoyaltyAccount account = txn.getAccount();
            int pointsToExpire = txn.getPointsDelta();

            LoyaltyTransaction expiryTxn = LoyaltyTransaction.builder()
                    .account(account)
                    .bookingId(txn.getBookingId())
                    .txnType("EXPIRE")
                    .pointsDelta(-pointsToExpire)
                    .description("Points expired (earned on " + txn.getCreatedAt().toLocalDate() + ")")
                    .build();
            transactionRepository.save(expiryTxn);

            // Mark original as processed by setting delta to 0
            txn.setPointsDelta(0);
            txn.setDescription(txn.getDescription() + " [EXPIRED]");
            transactionRepository.save(txn);

            account.setRedeemablePoints(Math.max(0, account.getRedeemablePoints() - pointsToExpire));
            accountRepository.save(account);

            totalExpired += pointsToExpire;
        }

        log.info("Points expiry job completed. Expired {} points across {} transactions.", totalExpired, expirable.size());
    }
}
