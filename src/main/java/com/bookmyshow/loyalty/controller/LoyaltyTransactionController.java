package com.bookmyshow.loyalty.controller;

import com.bookmyshow.loyalty.dto.request.LoyaltyTransactionRequest;
import com.bookmyshow.loyalty.dto.response.ErrorResponse;
import com.bookmyshow.loyalty.dto.response.LoyaltyTransactionResponse;
import com.bookmyshow.loyalty.service.LoyaltyTransactionService;
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
@RequestMapping("/api/v1/loyalty/transactions")
@RequiredArgsConstructor
@Tag(name = "Loyalty Transaction", description = "CRUD operations for loyalty transactions")
public class LoyaltyTransactionController {

    private final LoyaltyTransactionService service;

    @PostMapping
    @Operation(summary = "Create a new transaction", responses = {
            @ApiResponse(responseCode = "201", description = "Transaction created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LoyaltyTransactionResponse> create(@Valid @RequestBody LoyaltyTransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<LoyaltyTransactionResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all transactions (paginated)")
    public ResponseEntity<Page<LoyaltyTransactionResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Full update of a transaction", responses = {
            @ApiResponse(responseCode = "200", description = "Transaction updated"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<LoyaltyTransactionResponse> update(@PathVariable Integer id, @Valid @RequestBody LoyaltyTransactionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partial update of a transaction")
    public ResponseEntity<LoyaltyTransactionResponse> patch(@PathVariable Integer id, @RequestBody LoyaltyTransactionRequest request) {
        return ResponseEntity.ok(service.patch(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction", responses = {
            @ApiResponse(responseCode = "204", description = "Transaction deleted"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
