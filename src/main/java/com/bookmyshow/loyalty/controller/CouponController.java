package com.bookmyshow.loyalty.controller;

import com.bookmyshow.loyalty.dto.request.CouponRequest;
import com.bookmyshow.loyalty.dto.request.PromoApplyRequest;
import com.bookmyshow.loyalty.dto.request.PromoValidateRequest;
import com.bookmyshow.loyalty.dto.response.*;
import com.bookmyshow.loyalty.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/promos")
@RequiredArgsConstructor
@Tag(name = "Coupon / Promo Code", description = "CRUD and business operations for coupons")
public class CouponController {

    private final CouponService service;

    @PostMapping
    @Operation(summary = "Create a new coupon", responses = {
            @ApiResponse(responseCode = "201", description = "Coupon created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate coupon code", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CouponRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coupon by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Coupon found"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<CouponResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all coupons (paginated)")
    public ResponseEntity<Page<CouponResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Full update of a coupon", responses = {
            @ApiResponse(responseCode = "200", description = "Coupon updated"),
            @ApiResponse(responseCode = "404", description = "Coupon not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate coupon code")
    })
    public ResponseEntity<CouponResponse> update(@PathVariable Integer id, @Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partial update of a coupon (including status toggle)")
    public ResponseEntity<CouponResponse> patch(@PathVariable Integer id, @RequestBody CouponRequest request) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete (deactivate) a coupon", responses = {
            @ApiResponse(responseCode = "204", description = "Coupon deactivated"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate a promo code against a booking amount", responses = {
            @ApiResponse(responseCode = "200", description = "Validation result returned")
    })
    public ResponseEntity<PromoValidateResponse> validate(@Valid @RequestBody PromoValidateRequest request) {
        return ResponseEntity.ok(service.validate(request));
    }

    @PostMapping("/apply")
    @Operation(summary = "Apply a promo code to a booking", responses = {
            @ApiResponse(responseCode = "200", description = "Promo applied"),
            @ApiResponse(responseCode = "400", description = "Promo not valid", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<PromoApplyResponse> apply(@Valid @RequestBody PromoApplyRequest request) {
        return ResponseEntity.ok(service.apply(request));
    }
}
