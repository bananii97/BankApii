package com.bank.jpa.dto;

import com.bank.enums.OtpType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VerifyOtpRequest(
    @NotBlank(message = "Email is required for verification")
    @Email(message = "Incorrect email address format")
    String email,
    @NotNull
    OtpType otp,
    @NotBlank(message = "The code cannot be empty")
    @Size(min = 6, max = 6, message = "The code must be exactly 6 characters long")
    String code
    ){
}
