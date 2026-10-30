package com.bookmyshow.loyalty.service.impl;

import com.bookmyshow.loyalty.dto.request.LoyaltyTierConfigRequest;
import com.bookmyshow.loyalty.dto.response.LoyaltyTierConfigResponse;
import com.bookmyshow.loyalty.entity.LoyaltyTierConfig;
import com.bookmyshow.loyalty.exception.DuplicateResourceException;
import com.bookmyshow.loyalty.exception.ResourceNotFoundException;
import com.bookmyshow.loyalty.mapper.LoyaltyTierConfigMapper;
import com.bookmyshow.loyalty.repository.LoyaltyTierConfigRepository;
import com.bookmyshow.loyalty.service.LoyaltyTierConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoyaltyTierConfigServiceImpl implements LoyaltyTierConfigService {

    private final LoyaltyTierConfigRepository repository;
    private final LoyaltyTierConfigMapper mapper;

    @Override
    @Transactional
    public LoyaltyTierConfigResponse create(LoyaltyTierConfigRequest request) {
        if (repository.existsByTierName(request.getTierName())) {
            throw new DuplicateResourceException("Tier", "tierName", request.getTierName());
        }
        LoyaltyTierConfig entity = mapper.toEntity(request);
        entity = repository.save(entity);
        log.info("Created loyalty tier: {}", entity.getTierName());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyTierConfigResponse getById(Integer id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoyaltyTierConfigResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public LoyaltyTierConfigResponse update(Integer id, LoyaltyTierConfigRequest request) {
        LoyaltyTierConfig entity = findById(id);
        if (!entity.getTierName().equals(request.getTierName()) && repository.existsByTierName(request.getTierName())) {
            throw new DuplicateResourceException("Tier", "tierName", request.getTierName());
        }
        mapper.updateEntity(entity, request);
        entity = repository.save(entity);
        log.info("Updated loyalty tier: {}", entity.getTierName());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public LoyaltyTierConfigResponse patch(Integer id, LoyaltyTierConfigRequest request) {
        LoyaltyTierConfig entity = findById(id);
        if (request.getTierName() != null) {
            if (!entity.getTierName().equals(request.getTierName()) && repository.existsByTierName(request.getTierName())) {
                throw new DuplicateResourceException("Tier", "tierName", request.getTierName());
            }
            entity.setTierName(request.getTierName());
        }
        if (request.getMinPoints() != null) entity.setMinPoints(request.getMinPoints());
        if (request.getMaxPoints() != null) entity.setMaxPoints(request.getMaxPoints());
        if (request.getEarnMultiplier() != null) entity.setEarnMultiplier(request.getEarnMultiplier());
        if (request.getExpiryDays() != null) entity.setExpiryDays(request.getExpiryDays());
        entity = repository.save(entity);
        log.info("Patched loyalty tier: {}", entity.getTierName());
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        LoyaltyTierConfig entity = findById(id);
        repository.delete(entity);
        log.info("Deleted loyalty tier: {}", entity.getTierName());
    }

    @Override
    @Transactional(readOnly = true)
    public String determineTier(int totalPoints) {
        return repository.findByMinPointsLessThanEqualAndMaxPointsGreaterThanEqual(totalPoints, totalPoints)
                .map(LoyaltyTierConfig::getTierName)
                .orElse("BRONZE");
    }

    private LoyaltyTierConfig findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tier", "tierId", id));
    }
}
