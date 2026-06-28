package com.bank.user.jpa.dto;

import com.bank.address.dto.AddressRequestDto;
import com.bank.identity_card.dto.IdentityCardRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(
        @Email
        String email,
        @Size(min = 9, max = 9)
        String phoneNumber,
        @Valid
        AddressRequestDto address,
        @Valid
        IdentityCardRequestDto identityCard
) {
}
