package com.bank.user;

import com.bank.address.AddressMapper;
import com.bank.identity_card.IdentityCardMapper;
import com.bank.user.jpa.User;
import com.bank.user.jpa.dto.UserRequestDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User fromRequest(UserRequestDto dto){
        return User.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .nationalNumber(dto.nationalNumber())
                .birthDate(dto.birthDate())
                .phoneNumber(dto.phoneNumber())
                .address(AddressMapper.fromRequest(dto.address()))
                .identityCard(IdentityCardMapper.fromRequest(dto.identityCard()))
                .build();
    }
}
