package com.bank;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping ("/otp")
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Verify accepted");
    }
}
