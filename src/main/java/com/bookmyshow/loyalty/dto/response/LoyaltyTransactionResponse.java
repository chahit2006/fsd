package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Loyalty transaction response")
public class LoyaltyTransactionResponse {
    private Integer txnId;
    private Integer accountId;
    private Integer bookingId;
    private String txnType;
    private Integer pointsDelta;
    private String description;
    private LocalDateTime createdAt;
}
