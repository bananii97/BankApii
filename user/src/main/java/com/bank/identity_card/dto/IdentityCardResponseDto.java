package com.bank.identity_card.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record IdentityCardResponseDto(
        String identityCardNumber,
        String issuePlace,
        String issuingAuthority,
        LocalDate validUtil
) {
}
