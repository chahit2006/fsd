package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Result of earning points")
public class EarnPointsResponse {
    private Integer userId;
    private Integer pointsEarned;
    private Integer totalPoints;
    private Integer redeemablePoints;
    private String tier;
    private String message;
}
