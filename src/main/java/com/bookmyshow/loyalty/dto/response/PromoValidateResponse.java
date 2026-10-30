package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Promo code validation result")
public class PromoValidateResponse {
    private boolean valid;
    private String couponCode;
    private String discountType;
    private BigDecimal discountAmount;
    private BigDecimal finalDiscount;
    private String message;
}
