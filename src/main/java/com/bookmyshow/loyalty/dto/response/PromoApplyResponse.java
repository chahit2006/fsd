package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Result of applying a promo code")
public class PromoApplyResponse {
    private boolean applied;
    private Integer usageId;
    private String couponCode;
    private BigDecimal discountApplied;
    private String message;
}
