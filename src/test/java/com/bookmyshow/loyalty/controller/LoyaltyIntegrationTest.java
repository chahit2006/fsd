package com.bookmyshow.loyalty.controller;

import com.bookmyshow.loyalty.dto.request.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoyaltyIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    // --- Tier Config CRUD ---
    @Test @Order(1)
    void createTier_returns201() throws Exception {
        LoyaltyTierConfigRequest request = LoyaltyTierConfigRequest.builder()
                .tierName("BRONZE").minPoints(0).maxPoints(499)
                .earnMultiplier(BigDecimal.valueOf(1.00)).expiryDays(365).build();

        mockMvc.perform(post("/api/v1/loyalty/tiers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tierName").value("BRONZE"));
    }

    @Test @Order(2)
    void createDuplicateTier_returns409() throws Exception {
        LoyaltyTierConfigRequest request = LoyaltyTierConfigRequest.builder()
                .tierName("BRONZE").minPoints(0).maxPoints(499)
                .earnMultiplier(BigDecimal.valueOf(1.00)).expiryDays(365).build();

        mockMvc.perform(post("/api/v1/loyalty/tiers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test @Order(3)
    void getTier_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty/tiers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tierName").value("BRONZE"));
    }

    @Test @Order(4)
    void getAllTiers_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty/tiers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test @Order(5)
    void updateTier_returns200() throws Exception {
        LoyaltyTierConfigRequest request = LoyaltyTierConfigRequest.builder()
                .tierName("BRONZE").minPoints(0).maxPoints(599)
                .earnMultiplier(BigDecimal.valueOf(1.00)).expiryDays(365).build();

        mockMvc.perform(put("/api/v1/loyalty/tiers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.maxPoints").value(599));
    }

    @Test @Order(6)
    void patchTier_returns200() throws Exception {
        mockMvc.perform(patch("/api/v1/loyalty/tiers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"expiryDays\": 400}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expiryDays").value(400));
    }

    @Test @Order(7)
    void getTierNotFound_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty/tiers/999"))
                .andExpect(status().isNotFound());
    }

    // --- Loyalty Account CRUD ---
    @Test @Order(10)
    void createAccount_returns201() throws Exception {
        LoyaltyAccountRequest request = LoyaltyAccountRequest.builder()
                .userId(10).totalPoints(0).redeemablePoints(0).tier("BRONZE").build();

        mockMvc.perform(post("/api/v1/loyalty/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(10));
    }

    @Test @Order(11)
    void createDuplicateAccount_returns409() throws Exception {
        LoyaltyAccountRequest request = LoyaltyAccountRequest.builder().userId(10).build();

        mockMvc.perform(post("/api/v1/loyalty/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test @Order(12)
    void getAccount_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty/accounts/1"))
                .andExpect(status().isOk());
    }

    @Test @Order(13)
    void deleteAccount_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/loyalty/accounts/1"))
                .andExpect(status().isNoContent());
    }

    // --- Coupon CRUD ---
    @Test @Order(20)
    void createCoupon_returns201() throws Exception {
        CouponRequest request = CouponRequest.builder()
                .couponCode("TEST50").discountType("FLAT").discountAmount(BigDecimal.valueOf(50))
                .minBookingAmount(BigDecimal.valueOf(200)).validFrom(LocalDate.of(2024, 1, 1))
                .validTo(LocalDate.of(2027, 12, 31)).usageLimit(10).couponStatus("ACTIVE").build();

        mockMvc.perform(post("/api/v1/promos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.couponCode").value("TEST50"));
    }

    @Test @Order(21)
    void createDuplicateCoupon_returns409() throws Exception {
        CouponRequest request = CouponRequest.builder()
                .couponCode("TEST50").discountType("FLAT").discountAmount(BigDecimal.valueOf(50))
                .minBookingAmount(BigDecimal.valueOf(200)).validFrom(LocalDate.of(2024, 1, 1))
                .validTo(LocalDate.of(2027, 12, 31)).usageLimit(10).build();

        mockMvc.perform(post("/api/v1/promos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test @Order(22)
    void getCoupon_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/promos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponCode").value("TEST50"));
    }

    @Test @Order(23)
    void patchCoupon_returns200() throws Exception {
        mockMvc.perform(patch("/api/v1/promos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usageLimit\": 20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usageLimit").value(20));
    }

    @Test @Order(24)
    void deleteCoupon_softDeletes_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/promos/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/promos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponStatus").value("INACTIVE"));
    }

    // --- Promo Validate/Apply ---
    @Test @Order(30)
    void createActiveCoupon_forBusinessTests() throws Exception {
        CouponRequest request = CouponRequest.builder()
                .couponCode("BIZ100").discountType("FLAT").discountAmount(BigDecimal.valueOf(100))
                .minBookingAmount(BigDecimal.valueOf(300)).validFrom(LocalDate.of(2024, 1, 1))
                .validTo(LocalDate.of(2027, 12, 31)).usageLimit(5).couponStatus("ACTIVE").build();

        mockMvc.perform(post("/api/v1/promos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test @Order(31)
    void validatePromo_valid_returns200() throws Exception {
        PromoValidateRequest request = PromoValidateRequest.builder()
                .couponCode("BIZ100").bookingAmount(BigDecimal.valueOf(500)).userId(1).build();

        mockMvc.perform(post("/api/v1/promos/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.finalDiscount").value(100.00));
    }

    @Test @Order(32)
    void validatePromo_belowMin_returnsInvalid() throws Exception {
        PromoValidateRequest request = PromoValidateRequest.builder()
                .couponCode("BIZ100").bookingAmount(BigDecimal.valueOf(100)).build();

        mockMvc.perform(post("/api/v1/promos/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

    @Test @Order(33)
    void applyPromo_success_returns200() throws Exception {
        PromoApplyRequest request = PromoApplyRequest.builder()
                .couponCode("BIZ100").userId(1).bookingId(501)
                .bookingAmount(BigDecimal.valueOf(500)).build();

        mockMvc.perform(post("/api/v1/promos/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applied").value(true));
    }

    @Test @Order(34)
    void applyPromo_alreadyUsedByUser_returns400() throws Exception {
        PromoApplyRequest request = PromoApplyRequest.builder()
                .couponCode("BIZ100").userId(1).bookingId(502)
                .bookingAmount(BigDecimal.valueOf(500)).build();

        mockMvc.perform(post("/api/v1/promos/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // --- Loyalty Business Endpoints ---
    @Test @Order(40)
    void earnPoints_returns200() throws Exception {
        EarnPointsRequest request = EarnPointsRequest.builder()
                .userId(20).bookingId(601).bookingAmount(BigDecimal.valueOf(500)).build();

        mockMvc.perform(post("/api/v1/loyalty/earn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pointsEarned").value(50));
    }

    @Test @Order(41)
    void getBalance_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty/20/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPoints").value(50));
    }

    @Test @Order(42)
    void getUserTransactions_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty/20/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test @Order(43)
    void redeemPoints_success_returns200() throws Exception {
        RedeemPointsRequest request = RedeemPointsRequest.builder()
                .userId(20).bookingId(602).pointsToRedeem(10)
                .bookingAmount(BigDecimal.valueOf(500)).build();

        mockMvc.perform(post("/api/v1/loyalty/redeem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pointsRedeemed").value(10));
    }

    @Test @Order(44)
    void redeemPoints_insufficient_returns400() throws Exception {
        RedeemPointsRequest request = RedeemPointsRequest.builder()
                .userId(20).bookingId(603).pointsToRedeem(99999)
                .bookingAmount(BigDecimal.valueOf(500000)).build();

        mockMvc.perform(post("/api/v1/loyalty/redeem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test @Order(45)
    void reverseBooking_returns200() throws Exception {
        ReverseRequest request = ReverseRequest.builder().userId(20).bookingId(601).build();

        mockMvc.perform(post("/api/v1/loyalty/reverse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pointsReversed").value(50));
    }

    @Test @Order(46)
    void getBalanceNotFound_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty/9999/balance"))
                .andExpect(status().isNotFound());
    }

    // --- Validation error ---
    @Test @Order(50)
    void invalidBody_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/promos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // --- Coupon Applicability CRUD ---
    @Test @Order(60)
    void createApplicability_returns201() throws Exception {
        CouponApplicabilityRequest request = CouponApplicabilityRequest.builder()
                .couponId(2).movieId(1).build();

        mockMvc.perform(post("/api/v1/promos/applicability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test @Order(61)
    void getApplicability_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/promos/applicability/1"))
                .andExpect(status().isOk());
    }

    @Test @Order(62)
    void deleteApplicability_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/promos/applicability/1"))
                .andExpect(status().isNoContent());
    }

    // --- Coupon Usage CRUD ---
    @Test @Order(70)
    void createUsage_returns201() throws Exception {
        CouponUsageRequest request = CouponUsageRequest.builder()
                .couponId(2).userId(5).bookingId(701).build();

        mockMvc.perform(post("/api/v1/promos/usage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test @Order(71)
    void getUsage_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/promos/usage/1"))
                .andExpect(status().isOk());
    }

    @Test @Order(72)
    void deleteUsage_returns204() throws Exception {
        mockMvc.perform(delete("/api/v1/promos/usage/1"))
                .andExpect(status().isNoContent());
    }

    // --- Transaction CRUD ---
    @Test @Order(80)
    void createAccountForTxnTest() throws Exception {
        LoyaltyAccountRequest accReq = LoyaltyAccountRequest.builder()
                .userId(80).totalPoints(100).redeemablePoints(100).tier("BRONZE").build();

        mockMvc.perform(post("/api/v1/loyalty/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accReq)))
                .andExpect(status().isCreated());
    }

    @Test @Order(81)
    void createTransaction_returns201() throws Exception {
        // account created in order 80 will have a known ID; need to find it
        MvcResult result = mockMvc.perform(get("/api/v1/loyalty/80/balance"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        int accountId = objectMapper.readTree(body).get("accountId").asInt();

        LoyaltyTransactionRequest request = LoyaltyTransactionRequest.builder()
                .accountId(accountId).bookingId(801).txnType("EARN").pointsDelta(25)
                .description("Test transaction").build();

        mockMvc.perform(post("/api/v1/loyalty/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test @Order(82)
    void getAllTransactions_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // --- Redemption CRUD ---
    @Test @Order(90)
    void createRedemption_returns201() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/loyalty/80/balance"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        int accountId = objectMapper.readTree(body).get("accountId").asInt();

        LoyaltyRedemptionRequest request = LoyaltyRedemptionRequest.builder()
                .accountId(accountId).bookingId(901).pointsUsed(10)
                .discountGiven(BigDecimal.valueOf(10)).build();

        mockMvc.perform(post("/api/v1/loyalty/redemptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test @Order(91)
    void getAllRedemptions_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/loyalty/redemptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
