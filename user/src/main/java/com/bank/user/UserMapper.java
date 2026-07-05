package com.bank.user;

import com.bank.address.AddressMapper;
import com.bank.identity_card.IdentityCardMapper;
import com.bank.user.jpa.dto.UserUpdateRequestDto;
import com.bank.user.jpa.model.FieldChangeRequest;
import com.bank.user.jpa.model.User;
import com.bank.user.jpa.dto.UserRequestDto;
import com.bank.user.jpa.dto.UserResponseDto;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

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
                .gender(dto.gender())
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

    public List<FieldChangeRequest> getChanges(User user, UserUpdateRequestDto dto){
        List<FieldChangeRequest> changes = new ArrayList<>();
        if(dto.email() != null && !dto.email().isEmpty()){
            changes.add(new  FieldChangeRequest("Email", dto.email(), dto.email()));
        }
        if(dto.phoneNumber() != null && !dto.phoneNumber().isEmpty()){
            changes.add(new FieldChangeRequest("Phone number ", dto.phoneNumber(), dto.phoneNumber()));
        }
        return changes;
    }
}
