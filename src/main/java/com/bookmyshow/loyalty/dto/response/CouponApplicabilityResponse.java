package com.bookmyshow.loyalty.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Coupon applicability response")
public class CouponApplicabilityResponse {
    private Integer applicabilityId;
    private Integer couponId;
    private Integer movieId;
    private Integer hostingEventId;
}
