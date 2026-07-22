package com.bank.user.jpa.dto;

import com.bank.address.dto.AddressRequestDto;
import com.bank.address.dto.AddressResponseDto;
import com.bank.identity_card.dto.IdentityCardRequestDto;
import com.bank.identity_card.dto.IdentityCardResponseDto;
import com.bank.user.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDate birthDate,
        Gender gender,
        AddressResponseDto addressDto,
        IdentityCardResponseDto identityCardDto
) {
}
