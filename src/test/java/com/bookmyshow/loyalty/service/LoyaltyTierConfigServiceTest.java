package com.bookmyshow.loyalty.service;

import com.bookmyshow.loyalty.dto.request.LoyaltyTierConfigRequest;
import com.bookmyshow.loyalty.dto.response.LoyaltyTierConfigResponse;
import com.bookmyshow.loyalty.entity.LoyaltyTierConfig;
import com.bookmyshow.loyalty.exception.DuplicateResourceException;
import com.bookmyshow.loyalty.exception.ResourceNotFoundException;
import com.bookmyshow.loyalty.mapper.LoyaltyTierConfigMapper;
import com.bookmyshow.loyalty.repository.LoyaltyTierConfigRepository;
import com.bookmyshow.loyalty.service.impl.LoyaltyTierConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoyaltyTierConfigServiceTest {

    @Mock private LoyaltyTierConfigRepository repository;
    @Mock private LoyaltyTierConfigMapper mapper;
    @InjectMocks private LoyaltyTierConfigServiceImpl service;

    private LoyaltyTierConfig entity;
    private LoyaltyTierConfigRequest request;
    private LoyaltyTierConfigResponse response;

    @BeforeEach
    void setUp() {
        entity = LoyaltyTierConfig.builder()
                .tierId(1).tierName("GOLD").minPoints(2000).maxPoints(4999)
                .earnMultiplier(BigDecimal.valueOf(1.50)).expiryDays(540).build();
        request = LoyaltyTierConfigRequest.builder()
                .tierName("GOLD").minPoints(2000).maxPoints(4999)
                .earnMultiplier(BigDecimal.valueOf(1.50)).expiryDays(540).build();
        response = LoyaltyTierConfigResponse.builder()
                .tierId(1).tierName("GOLD").minPoints(2000).maxPoints(4999)
                .earnMultiplier(BigDecimal.valueOf(1.50)).expiryDays(540).build();
    }

    @Test
    void create_success() {
        when(repository.existsByTierName("GOLD")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        LoyaltyTierConfigResponse result = service.create(request);

        assertThat(result.getTierName()).isEqualTo("GOLD");
        verify(repository).save(entity);
    }

    @Test
    void create_duplicate_throws() {
        when(repository.existsByTierName("GOLD")).thenReturn(true);
        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void getById_success() {
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        LoyaltyTierConfigResponse result = service.getById(1);
        assertThat(result.getTierId()).isEqualTo(1);
    }

    @Test
    void getById_notFound_throws() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAll_returnsPaged() {
        Page<LoyaltyTierConfig> page = new PageImpl<>(List.of(entity));
        when(repository.findAll(any(PageRequest.class))).thenReturn(page);
        when(mapper.toResponse(entity)).thenReturn(response);

        Page<LoyaltyTierConfigResponse> result = service.getAll(PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void update_success() {
        LoyaltyTierConfigRequest renameRequest = LoyaltyTierConfigRequest.builder()
                .tierName("PLATINUM").minPoints(2000).maxPoints(4999)
                .earnMultiplier(BigDecimal.valueOf(1.50)).expiryDays(540).build();
        LoyaltyTierConfigResponse renameResponse = LoyaltyTierConfigResponse.builder()
                .tierId(1).tierName("PLATINUM").minPoints(2000).maxPoints(4999)
                .earnMultiplier(BigDecimal.valueOf(1.50)).expiryDays(540).build();

        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.existsByTierName("PLATINUM")).thenReturn(false);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(renameResponse);

        LoyaltyTierConfigResponse result = service.update(1, renameRequest);
        assertThat(result.getTierName()).isEqualTo("PLATINUM");
        verify(mapper).updateEntity(entity, renameRequest);
    }

    @Test
    void delete_success() {
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        service.delete(1);
        verify(repository).delete(entity);
    }

    @Test
    void determineTier_found() {
        when(repository.findByMinPointsLessThanEqualAndMaxPointsGreaterThanEqual(3000, 3000))
                .thenReturn(Optional.of(entity));
        assertThat(service.determineTier(3000)).isEqualTo("GOLD");
    }

    @Test
    void determineTier_notFound_defaultBronze() {
        when(repository.findByMinPointsLessThanEqualAndMaxPointsGreaterThanEqual(anyInt(), anyInt()))
                .thenReturn(Optional.empty());
        assertThat(service.determineTier(999999)).isEqualTo("BRONZE");
    }
}
