package com.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RabbitTemplate rabbitTemplate;
    private final SecureRandom secureRandom;

    public void generateAndSendOtp(String email) {

    }
}
