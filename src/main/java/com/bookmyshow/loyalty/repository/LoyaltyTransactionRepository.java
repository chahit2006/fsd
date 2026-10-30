package com.bookmyshow.loyalty.repository;

import com.bookmyshow.loyalty.entity.LoyaltyTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Integer> {

    Page<LoyaltyTransaction> findByAccountAccountId(Integer accountId, Pageable pageable);

    Page<LoyaltyTransaction> findByAccountUserId(Integer userId, Pageable pageable);

    List<LoyaltyTransaction> findByBookingId(Integer bookingId);

    @Query("SELECT t FROM LoyaltyTransaction t WHERE t.txnType = 'EARN' AND t.createdAt < :cutoff AND t.pointsDelta > 0")
    List<LoyaltyTransaction> findExpirableTransactions(@Param("cutoff") LocalDateTime cutoff);
}
