package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request to redeem loyalty points for a discount")
public class RedeemPointsRequest {

    @NotNull(message = "User ID is required")
    @Schema(example = "1")
    private Integer userId;

    @NotNull(message = "Booking ID is required")
    @Schema(example = "101")
    private Integer bookingId;

    @NotNull(message = "Points to redeem is required")
    @Min(value = 1, message = "Points to redeem must be > 0")
    @Schema(example = "100")
    private Integer pointsToRedeem;

    @NotNull(message = "Booking amount is required")
    @DecimalMin(value = "0.01", message = "Booking amount must be > 0")
    @Schema(example = "500.00")
    private BigDecimal bookingAmount;
}
