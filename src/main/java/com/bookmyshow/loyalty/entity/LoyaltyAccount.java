package com.bookmyshow.loyalty.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_account")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoyaltyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @Column(name = "redeemable_points", nullable = false)
    private Integer redeemablePoints;

    @Column(name = "tier", nullable = false, length = 50)
    private String tier;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
