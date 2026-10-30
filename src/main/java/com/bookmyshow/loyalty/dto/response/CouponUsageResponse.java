package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Coupon usage response")
public class CouponUsageResponse {
    private Integer usageId;
    private Integer couponId;
    private Integer userId;
    private Integer bookingId;
    private LocalDateTime usedAt;
}
