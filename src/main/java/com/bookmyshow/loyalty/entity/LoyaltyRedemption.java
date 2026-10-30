package com.bookmyshow.loyalty.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_redemption")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoyaltyRedemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "redemption_id")
    private Integer redemptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private LoyaltyAccount account;

    @Column(name = "booking_id")
    private Integer bookingId;

    @Column(name = "points_used", nullable = false)
    private Integer pointsUsed;

    @Column(name = "discount_given", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountGiven;

    @Column(name = "redeemed_at", nullable = false)
    private LocalDateTime redeemedAt;

    @PrePersist
    public void prePersist() {
        if (this.redeemedAt == null) {
            this.redeemedAt = LocalDateTime.now();
        }
    }
}
