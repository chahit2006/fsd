package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request body for creating a loyalty transaction")
public class LoyaltyTransactionRequest {

    @NotNull(message = "Account ID is required")
    @Schema(example = "1")
    private Integer accountId;

    @Schema(example = "101")
    private Integer bookingId;

    @NotBlank(message = "Transaction type is required")
    @Schema(example = "EARN", allowableValues = {"EARN", "REDEEM", "EXPIRE", "REVERSE"})
    private String txnType;

    @NotNull(message = "Points delta is required")
    @Schema(example = "50")
    private Integer pointsDelta;

    @Size(max = 255, message = "Description must be at most 255 characters")
    @Schema(example = "Points earned for booking #101")
    private String description;
}
