package com.bookmyshow.loyalty.controller;

import com.bookmyshow.loyalty.dto.request.CouponUsageRequest;
import com.bookmyshow.loyalty.dto.response.CouponUsageResponse;
import com.bookmyshow.loyalty.dto.response.ErrorResponse;
import com.bookmyshow.loyalty.service.CouponUsageService;
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
@RequestMapping("/api/v1/promos/usage")
@RequiredArgsConstructor
@Tag(name = "Coupon Usage", description = "CRUD operations for coupon usage records")
public class CouponUsageController {

    private final CouponUsageService service;

    @PostMapping
    @Operation(summary = "Create a new usage record", responses = {
            @ApiResponse(responseCode = "201", description = "Usage created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CouponUsageResponse> create(@Valid @RequestBody CouponUsageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get usage by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<CouponUsageResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all usage records (paginated)")
    public ResponseEntity<Page<CouponUsageResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Full update of a usage record")
    public ResponseEntity<CouponUsageResponse> update(@PathVariable Integer id, @Valid @RequestBody CouponUsageRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partial update of a usage record")
    public ResponseEntity<CouponUsageResponse> patch(@PathVariable Integer id, @RequestBody CouponUsageRequest request) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a usage record", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
