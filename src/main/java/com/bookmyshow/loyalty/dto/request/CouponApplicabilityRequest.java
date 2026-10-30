package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request body for creating/updating coupon applicability")
public class CouponApplicabilityRequest {

    @NotNull(message = "Coupon ID is required")
    @Schema(example = "1")
    private Integer couponId;

    @Schema(example = "1")
    private Integer movieId;

    @Schema(example = "1")
    private Integer hostingEventId;
}
