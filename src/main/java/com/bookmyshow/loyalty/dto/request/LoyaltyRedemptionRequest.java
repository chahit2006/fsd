package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request body for creating a loyalty redemption")
public class LoyaltyRedemptionRequest {

    @NotNull(message = "Account ID is required")
    @Schema(example = "1")
    private Integer accountId;

    @Schema(example = "101")
    private Integer bookingId;

    @NotNull(message = "Points used is required")
    @Min(value = 1, message = "Points used must be > 0")
    @Schema(example = "100")
    private Integer pointsUsed;

    @NotNull(message = "Discount given is required")
    @DecimalMin(value = "0.01", message = "Discount given must be > 0")
    @Schema(example = "100.00")
    private BigDecimal discountGiven;
}
