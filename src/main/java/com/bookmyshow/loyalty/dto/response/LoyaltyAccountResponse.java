package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Loyalty account response")
public class LoyaltyAccountResponse {
    private Integer accountId;
    private Integer userId;
    private Integer totalPoints;
    private Integer redeemablePoints;
    private String tier;
    private LocalDateTime lastUpdated;
}
