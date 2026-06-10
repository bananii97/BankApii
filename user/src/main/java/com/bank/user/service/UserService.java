package com.bank.user.service;

import com.bank.exceptions.InvalidNationalNumberExceptions;
import com.bank.exceptions.UserAlreadyException;
import com.bank.user.UserMapper;
import com.bank.user.enums.Gender;
import com.bank.user.enums.UserStatus;
import com.bank.user.jpa.User;
import com.bank.user.jpa.UserRepository;
import com.bank.user.jpa.dto.UserRequestDto;
import com.bank.user.jpa.dto.UserResponseDto;
import com.bank.verification.NationalNumberValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final NationalNumberValidator nationalNumberValidator;

    @Transactional
    public UserResponseDto registry(UserRequestDto dto) {
        log.info("Validation: process starting for user with email: {}", dto.email());
        User user = UserMapper.fromRequest(dto);
        user.setGender(checkGender(dto.nationalNumber()));

        user.setStatus(UserStatus.PENDING_VERIFICATION);
        validateEmailNadNationalNumber(dto);

        userRepository.save(user);
        log.info("Validation successful: process finished successfully for email: {} ", dto.email());
        return new UserResponseDto();
    }

    public Gender checkGender(String nationalNumber) {
        int genderDigit = nationalNumber.charAt(9);
        return (genderDigit % 2 == 0) ? Gender.FEMALE : Gender.MALE;
    }

    public void validateEmailNadNationalNumber(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            log.warn("Validation: email already exists for user with email: {} ", dto.email());
            throw new UserAlreadyException("User with this email: " + dto.email() + " already exists", HttpStatus.CONFLICT);
        }

        if (!nationalNumberValidator.passed(dto)) {
            log.warn("Validation: national number is incorrect");
            throw new InvalidNationalNumberExceptions("National number is incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    public UserResponseDto getPersonalData(long id){
        log.info("Get personal data for user with id: {} ", id);
        User user = userRepository.findByIdOrThrowWhenIdNotExists(id);
        UserResponseDto userResponseDto = UserMapper.fromEntityToPersonalData(user);
        log.info("Get personal data for user with id: {} correct ", id);
        return userResponseDto;
    }
}
