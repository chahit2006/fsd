package com.bookmyshow.loyalty.repository;

import com.bookmyshow.loyalty.entity.CouponApplicability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponApplicabilityRepository extends JpaRepository<CouponApplicability, Integer> {

    List<CouponApplicability> findByCouponCouponId(Integer couponId);

    Page<CouponApplicability> findByCouponCouponId(Integer couponId, Pageable pageable);
}
