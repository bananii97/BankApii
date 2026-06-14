package com.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OneTimePassword, Long> {

    Optional<OneTimePassword> findByEmailAndCodeAndOtpType(String email, String code, OtpType otpType);

    default OneTimePassword getRequiredOtpOrThrowOtpException(
            String email,
            String code,
            OtpType otpType
    ) {
        return findByEmailAndCodeAndOtpType(email, code, otpType)
                .orElseThrow(() -> new OtpNotFoundException("Email: " + email + " not found",
                        HttpStatus.BAD_REQUEST));
    }
}
