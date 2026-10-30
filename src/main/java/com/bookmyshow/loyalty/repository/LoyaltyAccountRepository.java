package com.bookmyshow.loyalty.repository;

import com.bookmyshow.loyalty.entity.LoyaltyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, Integer> {

    Optional<LoyaltyAccount> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);
}
