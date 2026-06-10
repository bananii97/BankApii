package com.bank.user;

import com.bank.address.AddressMapper;
import com.bank.identity_card.IdentityCardMapper;
import com.bank.user.jpa.User;
import com.bank.user.jpa.dto.UserRequestDto;
import com.bank.user.jpa.dto.UserResponseDto;
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

    public static UserResponseDto fromEntityToPersonalData(User user){
        return UserResponseDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .gender(user.getGender())
                .build();
    }
}
