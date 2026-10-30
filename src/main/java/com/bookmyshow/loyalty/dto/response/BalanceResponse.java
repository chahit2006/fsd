package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "User loyalty balance response")
public class BalanceResponse {
    private Integer userId;
    private Integer accountId;
    private Integer totalPoints;
    private Integer redeemablePoints;
    private String tier;
}
