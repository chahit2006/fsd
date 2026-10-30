package com.bookmyshow.loyalty.controller;

import com.bookmyshow.loyalty.dto.request.LoyaltyAccountRequest;
import com.bookmyshow.loyalty.dto.response.ErrorResponse;
import com.bookmyshow.loyalty.dto.response.LoyaltyAccountResponse;
import com.bookmyshow.loyalty.service.LoyaltyAccountService;
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
@RequestMapping("/api/v1/loyalty/accounts")
@RequiredArgsConstructor
@Tag(name = "Loyalty Account", description = "CRUD operations for loyalty accounts")
public class LoyaltyAccountController {

    private final LoyaltyAccountService service;

    @PostMapping
    @Operation(summary = "Create a new loyalty account", responses = {
            @ApiResponse(responseCode = "201", description = "Account created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Duplicate userId", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoyaltyAccountResponse> create(@Valid @RequestBody LoyaltyAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<LoyaltyAccountResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all accounts (paginated)")
    public ResponseEntity<Page<LoyaltyAccountResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Full update of an account", responses = {
            @ApiResponse(responseCode = "200", description = "Account updated"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate userId")
    })
    public ResponseEntity<LoyaltyAccountResponse> update(@PathVariable Integer id, @Valid @RequestBody LoyaltyAccountRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partial update of an account", responses = {
            @ApiResponse(responseCode = "200", description = "Account patched"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<LoyaltyAccountResponse> patch(@PathVariable Integer id, @RequestBody LoyaltyAccountRequest request) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account", responses = {
            @ApiResponse(responseCode = "204", description = "Account deleted"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
