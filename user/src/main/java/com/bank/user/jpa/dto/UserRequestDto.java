package com.bank.user.jpa.dto;

import com.bank.address.dto.AddressRequestDto;
import com.bank.identity_card.dto.IdentityCardRequestDto;
import com.bank.user.enums.Gender;
import com.bank.user.enums.UserStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserRequestDto(
        @NotNull
        @Size(min = 3, max = 30)
        String firstName,
        @Size(min = 3, max = 40)
        @NotNull
        String lastName,
        @Email
        @NotNull
        String email,
        String password,
        @Size(min = 11, max = 11)
        String nationalNumber,
        @NotNull
        @Past(message = "incorrect date")
        LocalDate birthDate,
        @NotNull
        @Size(min = 9, max = 9)
        String phoneNumber,
        Gender gender,
        UserStatus status,
        @Valid
        AddressRequestDto address,
        @Valid
        IdentityCardRequestDto identityCard
) {
}
