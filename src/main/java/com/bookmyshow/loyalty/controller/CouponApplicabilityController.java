package com.bookmyshow.loyalty.controller;

import com.bookmyshow.loyalty.dto.request.CouponApplicabilityRequest;
import com.bookmyshow.loyalty.dto.response.CouponApplicabilityResponse;
import com.bookmyshow.loyalty.dto.response.ErrorResponse;
import com.bookmyshow.loyalty.service.CouponApplicabilityService;
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
@RequestMapping("/api/v1/promos/applicability")
@RequiredArgsConstructor
@Tag(name = "Coupon Applicability", description = "CRUD operations for coupon applicability rules")
public class CouponApplicabilityController {

    private final CouponApplicabilityService service;

    @PostMapping
    @Operation(summary = "Create a new applicability rule", responses = {
            @ApiResponse(responseCode = "201", description = "Applicability created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CouponApplicabilityResponse> create(@Valid @RequestBody CouponApplicabilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get applicability by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<CouponApplicabilityResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all applicability rules (paginated)")
    public ResponseEntity<Page<CouponApplicabilityResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Full update of an applicability rule")
    public ResponseEntity<CouponApplicabilityResponse> update(@PathVariable Integer id, @Valid @RequestBody CouponApplicabilityRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partial update of an applicability rule")
    public ResponseEntity<CouponApplicabilityResponse> patch(@PathVariable Integer id, @RequestBody CouponApplicabilityRequest request) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an applicability rule", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
