package com.bank.api;

import com.bank.service.OtpService;
import com.bank.jpa.dto.VerifyOtpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/otp")
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        otpService.verifyEmail(verifyOtpRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Verify accepted");
    }
}
