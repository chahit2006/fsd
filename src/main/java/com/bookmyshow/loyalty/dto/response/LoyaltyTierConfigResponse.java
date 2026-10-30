package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Loyalty tier configuration response")
public class LoyaltyTierConfigResponse {
    private Integer tierId;
    private String tierName;
    private Integer minPoints;
    private Integer maxPoints;
    private BigDecimal earnMultiplier;
    private Integer expiryDays;
}
