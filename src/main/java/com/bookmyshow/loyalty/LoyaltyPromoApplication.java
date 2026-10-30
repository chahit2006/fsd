package com.bookmyshow.loyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoyaltyPromoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoyaltyPromoApplication.class, args);
    }
}
