package com.bank.user.service;

import com.bank.address.AddressMapper;
import com.bank.identity_card.IdentityCardMapper;
import com.bank.rabbit.RegistrationResponseDto;
import com.bank.UserRegisteredEvent;
import com.bank.rabbit.config.RabbitMqUserConfig;
import com.bank.exceptions.InvalidNationalNumberExceptions;
import com.bank.exceptions.UserAlreadyException;
import com.bank.user.UserMapper;
import com.bank.user.enums.Gender;
import com.bank.user.enums.UserStatus;
import com.bank.user.jpa.model.FieldChangeRequest;
import com.bank.user.jpa.model.User;
import com.bank.user.jpa.model.UserDataHistory;
import com.bank.user.jpa.repository.UserDataHistoryRepository;
import com.bank.user.jpa.repository.UserRepository;
import com.bank.user.jpa.dto.UserRequestDto;
import com.bank.user.jpa.dto.UserResponseDto;
import com.bank.user.jpa.dto.UserUpdateRequestDto;
import com.bank.verification.NationalNumberValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final NationalNumberValidator nationalNumberValidator;
    private final RabbitTemplate rabbitTemplate;
    private final UserDataHistoryRepository userDataHistoryRepository;

    @Transactional
    public RegistrationResponseDto registry(UserRequestDto dto) {
        log.info("Validation: process starting for user with email: {}", dto.email());
        User user = UserMapper.fromRequest(dto);
        user.setGender(returnGender(dto.nationalNumber()));

        user.setStatus(UserStatus.PENDING_VERIFICATION);
        validateEmailUniques(dto);
        validateNationalNumber(dto);

        userRepository.save(user);

        UserRegisteredEvent event = new UserRegisteredEvent(user.getEmail());
        rabbitTemplate.convertAndSend(
                RabbitMqUserConfig.EXCHANGE_NAME,
                RabbitMqUserConfig.ROUTING_USER_REGISTERED,
                event
        );

        RegistrationResponseDto registrationResponseDto = returnRegistrationResponseDto(user);

        log.info("Validation successful: process finished successfully for email: {} ", dto.email());
        return registrationResponseDto;
    }

    public void activateUser(String email) {
        log.info("Activation user: process starting for user with email: {}", email);
        User user = userRepository.findByEmailOrThrowWhenEmailNotExists(email);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        log.info("Activation user successful: process finished successfully for email: {} ", email);
    }

    public RegistrationResponseDto returnRegistrationResponseDto(User user) {
        String email = user.getEmail();
        UserStatus status = UserStatus.PENDING_VERIFICATION;
        String message = "A verification code has been sent to the email address provided.";
        return new RegistrationResponseDto(email, status, message);
    }

    public Gender returnGender(String nationalNumber) {
        int genderDigit = nationalNumber.charAt(9);
        return (genderDigit % 2 == 0) ? Gender.FEMALE : Gender.MALE;
    }

    public void validateEmailUniques(UserRequestDto dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            log.warn("Validation: email already exists for user with email: {} ", dto.email());
            throw new UserAlreadyException(
                    "User with this email: " + dto.email() + " already exists", HttpStatus.CONFLICT);
        }
        log.info("Validation passed: email not exists: {} ", dto.email());
    }

    public void validateNationalNumber(UserRequestDto dto) {
        if (!nationalNumberValidator.passed(dto)) {
            log.warn("Validation: national number is incorrect");
            throw new InvalidNationalNumberExceptions("National number is incorrect", HttpStatus.BAD_REQUEST);
        }
        log.info("Validation passed: national number is correct");
    }

    public UserResponseDto getPersonalData(long id) {
        log.info("Get personal data for user with id: {} ", id);
        User user = userRepository.findByIdOrThrowWhenIdNotExists(id);
        UserResponseDto userResponseDto = UserMapper.fromEntityToPersonalData(user);
        log.info("Get personal data for user with id: {} correct ", id);
        return userResponseDto;
    }

    @Transactional
    public UserResponseDto updatePersonalData(long id, UserUpdateRequestDto dto) {
        log.info("Update personal data for user with id: {} ", id);
        User user = userRepository.findByIdOrThrowWhenIdNotExists(id);

        Optional.ofNullable(dto.email()).ifPresent(user::setEmail);
        Optional.ofNullable(dto.phoneNumber()).ifPresent(user::setPhoneNumber);

        Optional.ofNullable(dto.address())
                .map(AddressMapper::fromRequest)
                .ifPresent(user::setAddress);

        Optional.ofNullable(dto.identityCard())
                .map(IdentityCardMapper::fromRequest)
                .ifPresent(user::setIdentityCard);

        User updateUser = userRepository.save(user);

        saveFiledChanges(user.getId(), user, dto);

        log.info("Update personal data for user with id: {} correct ", id);
        return UserMapper.fromEntityToPersonalData(updateUser);
    }

    private void saveFiledChanges(Long userId, User user, UserUpdateRequestDto dto) {
        saveNewAndOldField(UserMapper.getChanges(user,dto), userId);
        saveNewAndOldField(AddressMapper.getChange(user.getAddress(), dto.address()), userId);
        saveNewAndOldField(IdentityCardMapper.getChanges(user.getIdentityCard(), dto.identityCard()), userId);
    }

    private void saveNewAndOldField(List<FieldChangeRequest> fieldChanges, Long userId) {
        if (!fieldChanges.isEmpty()) {
            fieldChanges
                    .forEach(changes -> {
                        UserDataHistory personalData = UserDataHistory.builder()
                                .fieldName(changes.fieldName())
                                .newValue(changes.newValue())
                                .oldValue(changes.oldValue())
                                .changedAt(LocalDateTime.now())
                                .userId(userId)
                                .build();

                        userDataHistoryRepository.save(personalData);
                    });
        }
    }
}
