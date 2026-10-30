package com.bookmyshow.loyalty.controller;

import com.bookmyshow.loyalty.dto.request.LoyaltyTierConfigRequest;
import com.bookmyshow.loyalty.dto.response.ErrorResponse;
import com.bookmyshow.loyalty.dto.response.LoyaltyTierConfigResponse;
import com.bookmyshow.loyalty.service.LoyaltyTierConfigService;
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
@RequestMapping("/api/v1/loyalty/tiers")
@RequiredArgsConstructor
@Tag(name = "Loyalty Tier Config", description = "CRUD operations for loyalty tier configuration")
public class LoyaltyTierConfigController {

    private final LoyaltyTierConfigService service;

    @PostMapping
    @Operation(summary = "Create a new tier", responses = {
            @ApiResponse(responseCode = "201", description = "Tier created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate tier name", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoyaltyTierConfigResponse> create(@Valid @RequestBody LoyaltyTierConfigRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tier by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Tier found"),
            @ApiResponse(responseCode = "404", description = "Tier not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoyaltyTierConfigResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all tiers (paginated)")
    public ResponseEntity<Page<LoyaltyTierConfigResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Full update of a tier", responses = {
            @ApiResponse(responseCode = "200", description = "Tier updated"),
            @ApiResponse(responseCode = "404", description = "Tier not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate tier name")
    })
    public ResponseEntity<LoyaltyTierConfigResponse> update(@PathVariable Integer id, @Valid @RequestBody LoyaltyTierConfigRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partial update of a tier", responses = {
            @ApiResponse(responseCode = "200", description = "Tier patched"),
            @ApiResponse(responseCode = "404", description = "Tier not found")
    })
    public ResponseEntity<LoyaltyTierConfigResponse> patch(@PathVariable Integer id, @RequestBody LoyaltyTierConfigRequest request) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tier", responses = {
            @ApiResponse(responseCode = "204", description = "Tier deleted"),
            @ApiResponse(responseCode = "404", description = "Tier not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
