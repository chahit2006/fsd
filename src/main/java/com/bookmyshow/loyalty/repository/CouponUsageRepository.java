package com.bookmyshow.loyalty.repository;

import com.bookmyshow.loyalty.entity.CouponUsage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Integer> {

    long countByCouponCouponId(Integer couponId);

    long countByCouponCouponIdAndUserId(Integer couponId, Integer userId);

    List<CouponUsage> findByBookingId(Integer bookingId);

    Page<CouponUsage> findByCouponCouponId(Integer couponId, Pageable pageable);

    Page<CouponUsage> findByUserId(Integer userId, Pageable pageable);
}
