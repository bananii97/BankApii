package com.bank.identity_card;

import com.bank.identity_card.dto.IdentityCardRequestDto;
import com.bank.identity_card.dto.IdentityCardResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IdentityCardMapper {

    public static IdentityCard fromRequest(IdentityCardRequestDto dto) {
        return IdentityCard.builder()
                .identityCardNumber(dto.identityCardNumber())
                .issuePlace(dto.issuePlace())
                .issuingAuthority(dto.issuingAuthority())
                .validUtil(dto.validUtil())
                .build();
    }

    public static IdentityCardResponseDto toResponse(IdentityCard identityCard) {
        return IdentityCardResponseDto.builder()
                .identityCardNumber(identityCard.getIdentityCardNumber())
                .issuePlace(identityCard.getIssuePlace())
                .issuingAuthority(identityCard.getIssuingAuthority())
                .validUtil(identityCard.getValidUtil())
                .build();
    }
}
