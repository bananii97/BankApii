package com.bank.service;

import com.bank.OtpVerifiedEvent;
import com.bank.email.EmailSender;
import com.bank.enums.OtpType;
import com.bank.exceptions.CodeExpiredException;
import com.bank.exceptions.InvalidOtpException;
import com.bank.exceptions.OtpFailedAttemptsExceptions;
import com.bank.exceptions.OtpUsedException;
import com.bank.jpa.OneTimePassword;
import com.bank.jpa.OtpRepository;
import com.bank.jpa.dto.VerifyOtpRequest;
import com.bank.rabbit.config.RabbitMqOtpConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class OtpService {

    private final RabbitTemplate rabbitTemplate;
    private final SecureRandom secureRandom;
    private final OtpRepository otpRepository;
    private final EmailSender emailSender;

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

        emailSender.sendMail(email,"a",String.valueOf(code));


        otpRepository.save(otp);
    }

    public void verifyEmail(VerifyOtpRequest verifyOtpRequest) {
        log.info("Verify email request received for email: {}" , verifyOtpRequest.email());
        String email = verifyOtpRequest.email();
        String code = verifyOtpRequest.code();
        OtpType type = verifyOtpRequest.otp();


        OneTimePassword otp = otpRepository.findByEmailAndOtpTypeOrElseThrow(email,type);
        validate(otp,code);
        otp.setUsed(true);

        otpRepository.save(otp);
        activeEmail(email);
        log.info("Email verified successfully for email: {}", email);
    }

    public void registerFailedAttempts(OneTimePassword otp) {
        log.info("Email verification code check for email: {}", otp.getEmail() );
        otp.setFailedAttempts(otp.getFailedAttempts() + 1);
        if(otp.getFailedAttempts() >= 3) {
            otp.setBlockedAt(LocalDateTime.now());
            log.info("The code has been blocked");
        }
        otpRepository.save(otp);
        log.info("Code is correct for  email: {}", otp.getEmail());
    }

    public void validate(OneTimePassword otp, String code) {
        log.info("Validating OTP request received for email: {}", otp.getEmail());
        if(otp.getBlockedAt() != null){
            throw new OtpFailedAttemptsExceptions("code blocked", HttpStatus.BAD_REQUEST);
        }
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CodeExpiredException("Code expired", HttpStatus.GONE);
        }

        if (otp.isUsed()) {
            throw new OtpUsedException("Otp is already used", HttpStatus.GONE);
        }

        if(!Objects.equals(otp.getCode(),code)) {
            registerFailedAttempts(otp);
            throw new InvalidOtpException("Code is incorrect", HttpStatus.CONFLICT);
        }
        log.info("OTP validated successfully for  email: {}", otp.getEmail());
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
