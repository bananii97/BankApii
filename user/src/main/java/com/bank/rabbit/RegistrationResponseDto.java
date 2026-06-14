package com.bank.rabbit;

import com.bank.user.enums.UserStatus;

public record RegistrationResponseDto(
        String email,
        UserStatus status,
        String message
) {
}
