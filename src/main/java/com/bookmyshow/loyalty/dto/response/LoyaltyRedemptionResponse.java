package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Loyalty redemption response")
public class LoyaltyRedemptionResponse {
    private Integer redemptionId;
    private Integer accountId;
    private Integer bookingId;
    private Integer pointsUsed;
    private BigDecimal discountGiven;
    private LocalDateTime redeemedAt;
}
