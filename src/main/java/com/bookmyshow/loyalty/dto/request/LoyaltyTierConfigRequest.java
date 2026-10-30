package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request body for creating/updating a loyalty tier")
public class LoyaltyTierConfigRequest {

    @NotBlank(message = "Tier name is required")
    @Size(max = 50, message = "Tier name must be at most 50 characters")
    @Schema(example = "GOLD")
    private String tierName;

    @NotNull(message = "Min points is required")
    @Min(value = 0, message = "Min points must be >= 0")
    @Schema(example = "2000")
    private Integer minPoints;

    @NotNull(message = "Max points is required")
    @Min(value = 1, message = "Max points must be >= 1")
    @Schema(example = "4999")
    private Integer maxPoints;

    @NotNull(message = "Earn multiplier is required")
    @DecimalMin(value = "0.01", message = "Earn multiplier must be > 0")
    @Schema(example = "1.50")
    private BigDecimal earnMultiplier;

    @NotNull(message = "Expiry days is required")
    @Min(value = 1, message = "Expiry days must be >= 1")
    @Schema(example = "540")
    private Integer expiryDays;
}
