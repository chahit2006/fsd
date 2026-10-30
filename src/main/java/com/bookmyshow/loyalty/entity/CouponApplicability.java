package com.bookmyshow.loyalty.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupon_applicability")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CouponApplicability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicability_id")
    private Integer applicabilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "movie_id")
    private Integer movieId;

    @Column(name = "hosting_event_id")
    private Integer hostingEventId;
}
