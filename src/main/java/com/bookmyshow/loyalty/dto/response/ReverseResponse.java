package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Result of reversing loyalty points and promo usage")
public class ReverseResponse {
    private Integer userId;
    private Integer bookingId;
    private Integer pointsReversed;
    private Integer promosReversed;
    private String message;
}
