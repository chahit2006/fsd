package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Result of redeeming points")
public class RedeemPointsResponse {
    private Integer userId;
    private Integer pointsRedeemed;
    private BigDecimal discountGiven;
    private Integer remainingRedeemablePoints;
    private String message;
}
