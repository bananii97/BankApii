package com.bank.identity_card;

import com.bank.identity_card.dto.IdentityCardRequestDto;
import com.bank.identity_card.dto.IdentityCardResponseDto;
import com.bank.user.jpa.model.FieldChangeRequest;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

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

    public List<FieldChangeRequest> getChanges(IdentityCard identityCard, IdentityCardRequestDto dto) {
        if(dto == null) {
            List.of();
        }
        return List.of(
                new FieldChangeRequest("Identity card number ", identityCard.getIdentityCardNumber(),
                        dto.identityCardNumber()),
                new FieldChangeRequest("Issue place " , identityCard.getIssuePlace(),
                        dto.issuePlace()),
                new FieldChangeRequest("Issuing authority ", identityCard.getIssuingAuthority(),
                        dto.issuingAuthority()),
                new FieldChangeRequest("Valid util ", identityCard.getValidUtil().toString(),
                        dto.validUtil().toString())
        );
    }
}
