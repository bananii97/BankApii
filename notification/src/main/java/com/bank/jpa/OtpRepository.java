package com.bank.jpa;

import com.bank.enums.OtpType;
import com.bank.exceptions.OtpNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<OneTimePassword, Long> {

    Optional<OneTimePassword> findByEmailAndOtpTypeAndUsedFalse(String email, OtpType otpType);

    default OneTimePassword findByEmailAndOtpTypeOrElseThrow (String email, OtpType otpType){
        return findByEmailAndOtpTypeAndUsedFalse(email,otpType)
                .orElseThrow(() -> new OtpNotFoundException("Otp not found for email: " + email,  HttpStatus.NOT_FOUND));
    }
}
