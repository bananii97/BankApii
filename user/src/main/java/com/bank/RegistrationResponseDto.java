package com.bank;

import com.bank.user.enums.UserStatus;

public record RegistrationResponseDto(
        String email,
        UserStatus status,
        String message
) {
}
