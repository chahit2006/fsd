package com.bookmyshow.loyalty.controller;

import com.bookmyshow.loyalty.dto.request.EarnPointsRequest;
import com.bookmyshow.loyalty.dto.request.RedeemPointsRequest;
import com.bookmyshow.loyalty.dto.request.ReverseRequest;
import com.bookmyshow.loyalty.dto.response.*;
import com.bookmyshow.loyalty.service.LoyaltyPointsService;
import com.bookmyshow.loyalty.service.LoyaltyTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.bookmyshow.loyalty.service.LoyaltyAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loyalty")
@RequiredArgsConstructor
@Tag(name = "Loyalty Points Business", description = "Business endpoints for loyalty points: balance, earn, redeem, reverse")
public class LoyaltyPointsController {

    private final LoyaltyPointsService pointsService;
    private final LoyaltyAccountService accountService;
    private final LoyaltyTransactionService transactionService;

    @GetMapping("/{userId}/balance")
    @Operation(summary = "Get user's loyalty balance and tier", responses = {
            @ApiResponse(responseCode = "200", description = "Balance returned"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Integer userId) {
        return ResponseEntity.ok(accountService.getBalanceByUserId(userId));
    }

    @GetMapping("/{userId}/transactions")
    @Operation(summary = "Get user's transaction history (paginated)", responses = {
            @ApiResponse(responseCode = "200", description = "Transactions returned")
    })
    public ResponseEntity<Page<LoyaltyTransactionResponse>> getUserTransactions(
            @PathVariable Integer userId, Pageable pageable) {
        return ResponseEntity.ok(transactionService.getByUserId(userId, pageable));
    }

    @PostMapping("/earn")
    @Operation(summary = "Earn loyalty points for a booking", responses = {
            @ApiResponse(responseCode = "200", description = "Points earned"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<EarnPointsResponse> earn(@Valid @RequestBody EarnPointsRequest request) {
        return ResponseEntity.ok(pointsService.earn(request));
    }

    @PostMapping("/redeem")
    @Operation(summary = "Redeem loyalty points for a discount", responses = {
            @ApiResponse(responseCode = "200", description = "Points redeemed"),
            @ApiResponse(responseCode = "400", description = "Insufficient points or exceeds max cap", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<RedeemPointsResponse> redeem(@Valid @RequestBody RedeemPointsRequest request) {
        return ResponseEntity.ok(pointsService.redeem(request));
    }

    @PostMapping("/reverse")
    @Operation(summary = "Reverse points and promo usage on booking cancellation", responses = {
            @ApiResponse(responseCode = "200", description = "Reversal completed"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<ReverseResponse> reverse(@Valid @RequestBody ReverseRequest request) {
        return ResponseEntity.ok(pointsService.reverse(request));
    }
}
