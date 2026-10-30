package com.bookmyshow.loyalty.service.impl;

import com.bookmyshow.loyalty.dto.request.LoyaltyRedemptionRequest;
import com.bookmyshow.loyalty.dto.response.LoyaltyRedemptionResponse;
import com.bookmyshow.loyalty.entity.LoyaltyAccount;
import com.bookmyshow.loyalty.entity.LoyaltyRedemption;
import com.bookmyshow.loyalty.exception.ResourceNotFoundException;
import com.bookmyshow.loyalty.mapper.LoyaltyRedemptionMapper;
import com.bookmyshow.loyalty.repository.LoyaltyAccountRepository;
import com.bookmyshow.loyalty.repository.LoyaltyRedemptionRepository;
import com.bookmyshow.loyalty.service.LoyaltyRedemptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoyaltyRedemptionServiceImpl implements LoyaltyRedemptionService {

    private final LoyaltyRedemptionRepository repository;
    private final LoyaltyAccountRepository accountRepository;
    private final LoyaltyRedemptionMapper mapper;

    @Override
    @Transactional
    public LoyaltyRedemptionResponse create(LoyaltyRedemptionRequest request) {
        LoyaltyAccount account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "accountId", request.getAccountId()));
        LoyaltyRedemption redemption = LoyaltyRedemption.builder()
                .account(account)
                .bookingId(request.getBookingId())
                .pointsUsed(request.getPointsUsed())
                .discountGiven(request.getDiscountGiven())
                .build();
        redemption = repository.save(redemption);
        log.info("Created loyalty redemption: id={}, points={}", redemption.getRedemptionId(), redemption.getPointsUsed());
        return mapper.toResponse(redemption);
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyRedemptionResponse getById(Integer id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoyaltyRedemptionResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public LoyaltyRedemptionResponse update(Integer id, LoyaltyRedemptionRequest request) {
        LoyaltyRedemption redemption = findById(id);
        LoyaltyAccount account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "accountId", request.getAccountId()));
        redemption.setAccount(account);
        redemption.setBookingId(request.getBookingId());
        redemption.setPointsUsed(request.getPointsUsed());
        redemption.setDiscountGiven(request.getDiscountGiven());
        redemption = repository.save(redemption);
        log.info("Updated loyalty redemption: {}", redemption.getRedemptionId());
        return mapper.toResponse(redemption);
    }

    @Override
    @Transactional
    public LoyaltyRedemptionResponse patch(Integer id, LoyaltyRedemptionRequest request) {
        LoyaltyRedemption redemption = findById(id);
        if (request.getAccountId() != null) {
            LoyaltyAccount account = accountRepository.findById(request.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "accountId", request.getAccountId()));
            redemption.setAccount(account);
        }
        if (request.getBookingId() != null) redemption.setBookingId(request.getBookingId());
        if (request.getPointsUsed() != null) redemption.setPointsUsed(request.getPointsUsed());
        if (request.getDiscountGiven() != null) redemption.setDiscountGiven(request.getDiscountGiven());
        redemption = repository.save(redemption);
        log.info("Patched loyalty redemption: {}", redemption.getRedemptionId());
        return mapper.toResponse(redemption);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        LoyaltyRedemption redemption = findById(id);
        repository.delete(redemption);
        log.info("Deleted loyalty redemption: {}", id);
    }

    private LoyaltyRedemption findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyRedemption", "redemptionId", id));
    }
}
