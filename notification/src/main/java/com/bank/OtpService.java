package com.bank;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RabbitTemplate rabbitTemplate;
    private final SecureRandom secureRandom;
    private final OtpRepository otpRepository;

    public void generateAndSendOtp(String email) {
        int code = 100000 + secureRandom.nextInt(900000);
        String stringCode = String.valueOf(code);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        OneTimePassword otp = OneTimePassword.builder()
                .code(stringCode)
                .email(email)
                .otpType(OtpType.REGISTRATION)
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .build();

        otpRepository.save(otp);
    }

    public void verifyEmail(VerifyOtpRequest verifyOtpRequest) {
        String email = verifyOtpRequest.getEmail();
        String code = verifyOtpRequest.getCode();
        OtpType type = verifyOtpRequest.getOtp();

        OneTimePassword otp = otpRepository.getRequiredOtpOrThrowOtpException(email, code, type);
        validate(otp);
        otp.setUsed(true);

        otpRepository.save(otp);
        activeEmail(email);
    }

    public boolean validate(OneTimePassword otp) {
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CodeExpiredException("Code expired", HttpStatus.GONE);
        }

        if (otp.isUsed()) {
            throw new OtpUsedException("Otp is already used", HttpStatus.GONE);
        }
        return true;
    }

    public void activeEmail(String email) {
        OtpVerifiedEvent otpVerifiedEvent = new OtpVerifiedEvent(email);
        rabbitTemplate.convertAndSend(
                RabbitMqOtpConfig.EXCHANGE_NAME,
                RabbitMqOtpConfig.ROUTING_OTP_VERIFIED,
                otpVerifiedEvent
        );
    }
}
