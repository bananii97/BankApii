package com.bank.rabbit;

import com.bank.OtpVerifiedEvent;
import com.bank.config.RabbitMqUserConfig;
import com.bank.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Listener {

    private final UserService userService;

    @RabbitListener(queues = RabbitMqUserConfig.OTP_VERIFIED_QUEUE)
    public void handleOtpVerified(OtpVerifiedEvent event) {
        userService.activateUser(event.email());
    }
}
