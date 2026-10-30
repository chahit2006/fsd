package com.bookmyshow.loyalty.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI loyaltyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookMyShow - Loyalty & Promo Code Module API")
                        .version("1.0.0")
                        .description("REST API for loyalty points management and promo code operations")
                        .contact(new Contact().name("BookMyShow Team")));
    }
}
