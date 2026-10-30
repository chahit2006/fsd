package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request to validate a promo code")
public class PromoValidateRequest {

    @NotBlank(message = "Coupon code is required")
    @Schema(example = "FLAT100")
    private String couponCode;

    @NotNull(message = "Booking amount is required")
    @DecimalMin(value = "0.01", message = "Booking amount must be > 0")
    @Schema(example = "750.00")
    private BigDecimal bookingAmount;

    @Schema(example = "1", description = "Optional user ID for per-user limit check")
    private Integer userId;
}
