package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Coupon response")
public class CouponResponse {
    private Integer couponId;
    private String couponCode;
    private String discountType;
    private BigDecimal discountAmount;
    private BigDecimal minBookingAmount;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Integer usageLimit;
    private String couponStatus;
}
