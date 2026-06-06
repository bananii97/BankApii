package com.bank.user.service;

import com.bank.user.UserMapper;
import com.bank.user.enums.Gender;
import com.bank.user.enums.UserStatus;
import com.bank.user.jpa.User;
import com.bank.user.jpa.UserRepository;
import com.bank.user.jpa.dto.UserRequestDto;
import com.bank.user.jpa.dto.UserResponseDto;
import com.bank.verification.NationalNumberValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final NationalNumberValidator  nationalNumberValidator;

    public UserResponseDto registry(UserRequestDto dto) {
        log.info("Registration: process starting for user with email: {}", dto.email());
        User user = UserMapper.fromRequest(dto);
        nationalNumberValidator.passed(dto);
        user.setGender(changeGender(dto.nationalNumber()));
        user.setStatus(UserStatus.PENDING_VERIFICATION);

        userRepository.save(user);
        log.info("Registration successful: process finished successfully for email: {}", dto.email());
        return new UserResponseDto();
    }

    public Gender changeGender(String nationalNumber) {
        int genderDigit = nationalNumber.charAt(9);
        return (genderDigit % 2 == 0) ? Gender.FEMALE : Gender.MALE;
    }
}
