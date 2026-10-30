package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request to apply a promo code to a booking")
public class PromoApplyRequest {

    @NotBlank(message = "Coupon code is required")
    @Schema(example = "FLAT100")
    private String couponCode;

    @NotNull(message = "User ID is required")
    @Schema(example = "1")
    private Integer userId;

    @NotNull(message = "Booking ID is required")
    @Schema(example = "101")
    private Integer bookingId;

    @NotNull(message = "Booking amount is required")
    @DecimalMin(value = "0.01", message = "Booking amount must be > 0")
    @Schema(example = "750.00")
    private BigDecimal bookingAmount;
}
