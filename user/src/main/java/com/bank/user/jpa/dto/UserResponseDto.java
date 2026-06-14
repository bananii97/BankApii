package com.bank.user.jpa.dto;

import com.bank.user.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserResponseDto(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate birthDate,
        Gender gender
//        String maskedNationalNumber,
//        AddressDto address,
//        IdentityCardDto identityCard
) {
}
