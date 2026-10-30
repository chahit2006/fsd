package com.bookmyshow.loyalty.repository;

import com.bookmyshow.loyalty.entity.LoyaltyTierConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyTierConfigRepository extends JpaRepository<LoyaltyTierConfig, Integer> {

    Optional<LoyaltyTierConfig> findByTierName(String tierName);

    Optional<LoyaltyTierConfig> findByMinPointsLessThanEqualAndMaxPointsGreaterThanEqual(
            Integer points1, Integer points2);

    boolean existsByTierName(String tierName);
}
