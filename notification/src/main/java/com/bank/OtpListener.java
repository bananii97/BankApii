package com.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class OtpListener {

    private final OtpService otpService;

    @RabbitListener(queues = RabbitMqOtpConfig.USER_REGISTERED_QUEUE)
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Received user registered event: {}", event.email());
        otpService.generateAndSendOtp(event.email());
        log.info("Registration event complete: {}", event.email());
    }
}
