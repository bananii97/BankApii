package com.bank;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VerifyOtpRequest {

    @NotBlank(message = "Email is required for verification")
    @Email(message = "Incorrect email address format")
    private String email;

    @NotNull
    private OtpType otp;

    @NotBlank(message = "The code cannot be empty")
    @Size(min = 6, max = 6, message = "The code must be exactly 6 characters long")
    private String code;
}
