package com.bookmyshow.loyalty.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Request body for creating/updating a coupon")
public class CouponRequest {

    @NotBlank(message = "Coupon code is required")
    @Size(max = 50, message = "Coupon code must be at most 50 characters")
    @Schema(example = "SUMMER25")
    private String couponCode;

    @NotBlank(message = "Discount type is required")
    @Schema(example = "PERCENTAGE", allowableValues = {"FLAT", "PERCENTAGE"})
    private String discountType;

    @NotNull(message = "Discount amount is required")
    @DecimalMin(value = "0.01", message = "Discount amount must be > 0")
    @Schema(example = "25.00")
    private BigDecimal discountAmount;

    @NotNull(message = "Min booking amount is required")
    @DecimalMin(value = "0.00", message = "Min booking amount must be >= 0")
    @Schema(example = "300.00")
    private BigDecimal minBookingAmount;

    @NotNull(message = "Valid from date is required")
    @Schema(example = "2024-01-01")
    private LocalDate validFrom;

    @NotNull(message = "Valid to date is required")
    @Schema(example = "2025-12-31")
    private LocalDate validTo;

    @NotNull(message = "Usage limit is required")
    @Min(value = 1, message = "Usage limit must be >= 1")
    @Schema(example = "5")
    private Integer usageLimit;

    @Schema(example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "EXPIRED"})
    private String couponStatus;
}
