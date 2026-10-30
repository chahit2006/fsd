package com.bookmyshow.loyalty.service.impl;

import com.bookmyshow.loyalty.dto.request.LoyaltyTransactionRequest;
import com.bookmyshow.loyalty.dto.response.LoyaltyTransactionResponse;
import com.bookmyshow.loyalty.entity.LoyaltyAccount;
import com.bookmyshow.loyalty.entity.LoyaltyTransaction;
import com.bookmyshow.loyalty.exception.ResourceNotFoundException;
import com.bookmyshow.loyalty.mapper.LoyaltyTransactionMapper;
import com.bookmyshow.loyalty.repository.LoyaltyAccountRepository;
import com.bookmyshow.loyalty.repository.LoyaltyTransactionRepository;
import com.bookmyshow.loyalty.service.LoyaltyTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoyaltyTransactionServiceImpl implements LoyaltyTransactionService {

    private final LoyaltyTransactionRepository repository;
    private final LoyaltyAccountRepository accountRepository;
    private final LoyaltyTransactionMapper mapper;

    @Override
    @Transactional
    public LoyaltyTransactionResponse create(LoyaltyTransactionRequest request) {
        LoyaltyAccount account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "accountId", request.getAccountId()));
        LoyaltyTransaction txn = LoyaltyTransaction.builder()
                .account(account)
                .bookingId(request.getBookingId())
                .txnType(request.getTxnType())
                .pointsDelta(request.getPointsDelta())
                .description(request.getDescription())
                .build();
        txn = repository.save(txn);
        log.info("Created loyalty transaction: id={}, type={}, delta={}", txn.getTxnId(), txn.getTxnType(), txn.getPointsDelta());
        return mapper.toResponse(txn);
    }

    @Override
    @Transactional(readOnly = true)
    public LoyaltyTransactionResponse getById(Integer id) {
        return mapper.toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoyaltyTransactionResponse> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public LoyaltyTransactionResponse update(Integer id, LoyaltyTransactionRequest request) {
        LoyaltyTransaction txn = findById(id);
        LoyaltyAccount account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "accountId", request.getAccountId()));
        txn.setAccount(account);
        txn.setBookingId(request.getBookingId());
        txn.setTxnType(request.getTxnType());
        txn.setPointsDelta(request.getPointsDelta());
        txn.setDescription(request.getDescription());
        txn = repository.save(txn);
        log.info("Updated loyalty transaction: {}", txn.getTxnId());
        return mapper.toResponse(txn);
    }

    @Override
    @Transactional
    public LoyaltyTransactionResponse patch(Integer id, LoyaltyTransactionRequest request) {
        LoyaltyTransaction txn = findById(id);
        if (request.getAccountId() != null) {
            LoyaltyAccount account = accountRepository.findById(request.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("LoyaltyAccount", "accountId", request.getAccountId()));
            txn.setAccount(account);
        }
        if (request.getBookingId() != null) txn.setBookingId(request.getBookingId());
        if (request.getTxnType() != null) txn.setTxnType(request.getTxnType());
        if (request.getPointsDelta() != null) txn.setPointsDelta(request.getPointsDelta());
        if (request.getDescription() != null) txn.setDescription(request.getDescription());
        txn = repository.save(txn);
        log.info("Patched loyalty transaction: {}", txn.getTxnId());
        return mapper.toResponse(txn);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        LoyaltyTransaction txn = findById(id);
        repository.delete(txn);
        log.info("Deleted loyalty transaction: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoyaltyTransactionResponse> getByUserId(Integer userId, Pageable pageable) {
        return repository.findByAccountUserId(userId, pageable).map(mapper::toResponse);
    }

    private LoyaltyTransaction findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoyaltyTransaction", "txnId", id));
    }
}
