package com.bank;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class OtpConfig {

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }
}
