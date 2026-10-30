package com.bookmyshow.loyalty.controller;

import com.bookmyshow.loyalty.dto.request.LoyaltyRedemptionRequest;
import com.bookmyshow.loyalty.dto.response.ErrorResponse;
import com.bookmyshow.loyalty.dto.response.LoyaltyRedemptionResponse;
import com.bookmyshow.loyalty.service.LoyaltyRedemptionService;
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
@RequestMapping("/api/v1/loyalty/redemptions")
@RequiredArgsConstructor
@Tag(name = "Loyalty Redemption", description = "CRUD operations for loyalty redemptions")
public class LoyaltyRedemptionController {

    private final LoyaltyRedemptionService service;

    @PostMapping
    @Operation(summary = "Create a new redemption record", responses = {
            @ApiResponse(responseCode = "201", description = "Redemption created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoyaltyRedemptionResponse> create(@Valid @RequestBody LoyaltyRedemptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get redemption by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Redemption found"),
            @ApiResponse(responseCode = "404", description = "Redemption not found")
    })
    public ResponseEntity<LoyaltyRedemptionResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all redemptions (paginated)")
    public ResponseEntity<Page<LoyaltyRedemptionResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Full update of a redemption")
    public ResponseEntity<LoyaltyRedemptionResponse> update(@PathVariable Integer id, @Valid @RequestBody LoyaltyRedemptionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partial update of a redemption")
    public ResponseEntity<LoyaltyRedemptionResponse> patch(@PathVariable Integer id, @RequestBody LoyaltyRedemptionRequest request) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a redemption", responses = {
            @ApiResponse(responseCode = "204", description = "Redemption deleted"),
            @ApiResponse(responseCode = "404", description = "Redemption not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
