package com.bank.identity_card.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record IdentityCardRequestDto(
        @Pattern(regexp = "^[A-Za-z]{3}\\d{6}$", message = "incorrect code from ID card")
        String identityCardNumber,
        @NotNull
        @Size(min = 3, max = 30)
        String issuePlace,
        @NotNull
        @Size(min = 3, max = 30)
        String issuingAuthority,
        @NotNull
        @Future(message = "incorrect date")
        LocalDate validUtil
) {
}
