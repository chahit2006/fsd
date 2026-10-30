package com.bookmyshow.loyalty.repository;

import com.bookmyshow.loyalty.entity.LoyaltyRedemption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoyaltyRedemptionRepository extends JpaRepository<LoyaltyRedemption, Integer> {

    Page<LoyaltyRedemption> findByAccountAccountId(Integer accountId, Pageable pageable);

    List<LoyaltyRedemption> findByBookingId(Integer bookingId);
}
