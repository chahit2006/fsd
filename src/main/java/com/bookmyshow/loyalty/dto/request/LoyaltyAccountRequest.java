package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request body for creating/updating a loyalty account")
public class LoyaltyAccountRequest {

    @NotNull(message = "User ID is required")
    @Schema(example = "1")
    private Integer userId;

    @Min(value = 0, message = "Total points must be >= 0")
    @Schema(example = "0")
    private Integer totalPoints;

    @Min(value = 0, message = "Redeemable points must be >= 0")
    @Schema(example = "0")
    private Integer redeemablePoints;

    @Size(max = 50, message = "Tier must be at most 50 characters")
    @Schema(example = "BRONZE")
    private String tier;
}
