package com.bookmyshow.loyalty.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "loyalty_tier_config")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoyaltyTierConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tier_id")
    private Integer tierId;

    @Column(name = "tier_name", nullable = false, unique = true, length = 50)
    private String tierName;

    @Column(name = "min_points", nullable = false)
    private Integer minPoints;

    @Column(name = "max_points", nullable = false)
    private Integer maxPoints;

    @Column(name = "earn_multiplier", nullable = false, precision = 4, scale = 2)
    private BigDecimal earnMultiplier;

    @Column(name = "expiry_days", nullable = false)
    private Integer expiryDays;
}
