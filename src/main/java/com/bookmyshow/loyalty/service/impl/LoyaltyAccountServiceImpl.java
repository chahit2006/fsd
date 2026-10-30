package com.bookmyshow.loyalty.service.impl;

import com.bookmyshow.loyalty.dto.request.LoyaltyAccountRequest;
import com.bookmyshow.loyalty.dto.response.BalanceResponse;
import com.bookmyshow.loyalty.dto.response.LoyaltyAccountResponse;
import com.bookmyshow.loyalty.entity.LoyaltyAccount;
import com.bookmyshow.loyalty.exception.DuplicateResourceException;
import com.bookmyshow.loyalty.exception.ResourceNotFoundException;
import com.bookmyshow.loyalty.mapper.LoyaltyAccountMapper;
import com.bookmyshow.loyalty.repository.LoyaltyAccountRepository;
import com.bookmyshow.loyalty.service.LoyaltyAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoyaltyAccountServiceImpl implements LoyaltyAccountService {

    private final LoyaltyAccountRepository repository;
    private final LoyaltyAccountMapper mapper;

    @Override
    @Transactional
    public LoyaltyAccountResponse create(LoyaltyAccountRequest request) {
        if (repository.existsByUserId(request.getUserId())) {
            throw new DuplicateResourceException("LoyaltyAccount", "userId", request.getUserId());
        }
        LoyaltyAccount entity = mapper.toEntity(request);
        entity = repository.save(entity);
        log.info("Created loyalty account for userId: {}", entity.getUserId());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyAccountResponse getById(Integer id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoyaltyAccountResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public LoyaltyAccountResponse update(Integer id, LoyaltyAccountRequest request) {
        LoyaltyAccount entity = findById(id);
        if (!entity.getUserId().equals(request.getUserId()) && repository.existsByUserId(request.getUserId())) {
            throw new DuplicateResourceException("LoyaltyAccount", "userId", request.getUserId());
        }
        entity.setUserId(request.getUserId());
        mapper.updateEntity(entity, request);
        entity = repository.save(entity);
        log.info("Updated loyalty account: {}", entity.getAccountId());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public LoyaltyAccountResponse patch(Integer id, LoyaltyAccountRequest request) {
        LoyaltyAccount entity = findById(id);
        if (request.getUserId() != null && !entity.getUserId().equals(request.getUserId())) {
            if (repository.existsByUserId(request.getUserId())) {
                throw new DuplicateResourceException("LoyaltyAccount", "userId", request.getUserId());
            }
            entity.setUserId(request.getUserId());
        }
        mapper.updateEntity(entity, request);
        entity = repository.save(entity);
        log.info("Patched loyalty account: {}", entity.getAccountId());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        LoyaltyAccount entity = findById(id);
        repository.delete(entity);
        log.info("Deleted loyalty account: {}", entity.getAccountId());
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getBalanceByUserId(Integer userId) {
        LoyaltyAccount account = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "userId", userId));
        return mapper.toBalanceResponse(account);
    }

    private LoyaltyAccount findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "accountId", id));
    }
}
