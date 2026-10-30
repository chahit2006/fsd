package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request to reverse loyalty points and promo usage on booking cancellation")
public class ReverseRequest {

    @NotNull(message = "User ID is required")
    @Schema(example = "1")
    private Integer userId;

    @NotNull(message = "Booking ID is required")
    @Schema(example = "101")
    private Integer bookingId;
}
