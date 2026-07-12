import com.bank.UserRegisteredEvent;
import com.bank.address.Address;
import com.bank.address.dto.AddressRequestDto;
import com.bank.exceptions.EmailNotFoundExceptions;
import com.bank.exceptions.InvalidNationalNumberExceptions;
import com.bank.exceptions.UserAlreadyException;
import com.bank.exceptions.UserNotFoundException;
import com.bank.identity_card.IdentityCard;
import com.bank.identity_card.dto.IdentityCardRequestDto;
import com.bank.rabbit.RegistrationResponseDto;
import com.bank.user.UserMapper;
import com.bank.user.enums.Gender;
import com.bank.user.enums.UserStatus;
import com.bank.user.jpa.dto.UserRequestDto;
import com.bank.user.jpa.dto.UserResponseDto;
import com.bank.user.jpa.model.User;
import com.bank.user.jpa.repository.UserRepository;
import com.bank.user.service.UserService;
import com.bank.verification.NationalNumberValidator;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRegisteredEvent userRegisteredEvent;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private NationalNumberValidator nationalNumberValidator;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> argumentCaptor;

    private static User user;
    private static AddressRequestDto address;
    private static IdentityCardRequestDto identityCard;
    private static UserRequestDto dto;
    private static RegistrationResponseDto responseDto;

    private static final String EXCHANGE_NAME = "bank-events-exchange";
    private static final String ROUTING_USER_REGISTERED = "user.registered";

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "test@gamil.com";
    private static final Gender GENDER = Gender.MALE;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String PHONE_NUMBER = "123456789";
    private static final String NATIONAL_NUMBER = "98020176512";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1980, 1, 1);
    private static final UserStatus USER_STATUS = UserStatus.PENDING_VERIFICATION;
    private static final String MESSAGE = "A verification code has been sent to the email address provided.";

    @BeforeAll
    static void beforeAll() {
        address = AddressRequestDto.builder()
                .city("Warsaw")
                .street("New")
                .postalCode("12-120")
                .apartmentNumber("12")
                .build();

        identityCard = IdentityCardRequestDto.builder()
                .identityCardNumber("ABC123456")
                .issuePlace("Wasaw")
                .issuingAuthority("President")
                .validUtil(LocalDate.of(2028, 1, 1))
                .build();

        user = User.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .nationalNumber(NATIONAL_NUMBER)
                .birthDate(BIRTH_DATE)
                .phoneNumber(PHONE_NUMBER)
                .status(UserStatus.PENDING_VERIFICATION)
                .build();

        dto = UserRequestDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password("pass")
                .nationalNumber(NATIONAL_NUMBER)
                .birthDate(LocalDate.now().minusYears(25)) // Data z przeszłości
                .phoneNumber(PHONE_NUMBER)
                .gender(GENDER)
                .address(address)
                .identityCard(identityCard)
                .build();

        responseDto = new RegistrationResponseDto(EMAIL, USER_STATUS, MESSAGE);

    }

    @Test
    void shouldRegistryUserAndReturnResponseDto_whenAllValuesAreProvided() {
        //given
        when(nationalNumberValidator.passed(dto)).thenReturn(true);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(args -> {
            User savedUser = args.getArgument(0);
            savedUser.setId(USER_ID);
            return savedUser;
        });

        //when
        RegistrationResponseDto responseDto = userService.registry(dto);

        //then
        verify(userRepository, times(1)).save(argumentCaptor.capture());
        User createdUser = argumentCaptor.getValue();
        verify(nationalNumberValidator, times(1)).passed(dto);
        verify(userRepository, times(1)).findByEmail(EMAIL);

        ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(rabbitTemplate, times(1))
                .convertAndSend(
                        eq(EXCHANGE_NAME),
                        eq(ROUTING_USER_REGISTERED),
                        eventCaptor.capture());

        assertThat(createdUser.getEmail()).isEqualTo(EMAIL);
        assertThat(createdUser.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(createdUser.getLastName()).isEqualTo(LAST_NAME);

        assertThat(responseDto.email()).isEqualTo(EMAIL);
        assertThat(responseDto.message()).isEqualTo(MESSAGE);
        assertThat(responseDto.status()).isEqualTo(USER_STATUS);
    }

    @Test
    void shouldThrowUserAlreadyException_whenUserExists() {
        //given
        String message = "User with this email: " + EMAIL + " already exists";
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThatExceptionOfType(UserAlreadyException.class)
                .isThrownBy(() -> userService.registry(dto))
                .withMessageContaining(message);

        //when then
        verify(nationalNumberValidator, never()).passed(dto);
        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(userRegisteredEvent);
    }

    @Test
    void shouldThrowInvalidNationalNumberException_whenNumberIsInCorrect() {
        //given
        String message = "National number is incorrect";
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(nationalNumberValidator.passed(dto)).thenReturn(false);

        assertThatExceptionOfType(InvalidNationalNumberExceptions.class)
                .isThrownBy(() -> userService.registry(dto))
                .withMessageContaining(message);

        //when then
        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(userRepository, never()).save(any(User.class));
        verify(nationalNumberValidator, times(1)).passed(dto);
        verifyNoInteractions(userRegisteredEvent);
    }

    @Test
    void shouldActivateUser_whenEmailExists() {
        //given
        UserStatus status = UserStatus.ACTIVE;
        when(userRepository.findByEmailOrThrowWhenEmailNotExists(EMAIL))
                .thenReturn(user);

        //when
        userService.activateUser(EMAIL);

        //then
        verify(userRepository, times(1)).findByEmailOrThrowWhenEmailNotExists(EMAIL);
        assertThat(user.getStatus()).isEqualTo(status);
    }

    @Test
    void shouldThrowEmailNotFoundException_whenEmailNotExists() {
        //given
        String message = "Email: " + EMAIL + " not found.";
        when(userRepository.findByEmailOrThrowWhenEmailNotExists(EMAIL))
                .thenThrow(new EmailNotFoundExceptions(message, HttpStatus.NOT_FOUND));

        //when then
        assertThatExceptionOfType(EmailNotFoundExceptions.class)
                .isThrownBy(() -> userService.activateUser(EMAIL))
                .withMessageContaining(message);

        verify(userRepository, times(1)).findByEmailOrThrowWhenEmailNotExists(EMAIL);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldReturnRegistrationResponseDto_whenAllValuesAreProvided() {
        //given
        String message = "A verification code has been sent to the email address provided.";

        //when
        RegistrationResponseDto response = userService.returnRegistrationResponseDto(user);

        //then
        assertThat(response.status()).isEqualTo(USER_STATUS);
        assertThat(response.message()).isEqualTo(message);
        assertThat(response.email()).isEqualTo(EMAIL);
    }

    @Test
    void shouldReturnGenderFemale_whenNationalNumberIsProvided() {
        //when
        Gender returnedGender = userService.returnGender(NATIONAL_NUMBER);

        //then
        assertThat(returnedGender).isEqualTo(Gender.MALE);
    }

    @Test
    void shouldReturnGenderMale_whenNationalNumberIsProvided() {
        //given
        String nationalNumber = "98020176522";

        //when
        Gender returnedGender = userService.returnGender(nationalNumber);

        //then
        assertThat(returnedGender).isEqualTo(Gender.FEMALE);
    }

    @Test
    void shouldValidateEmailUniquesPass_whenEmailNotExists() {
        //given
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        //when
        userService.validateEmailUniques(dto);

        //then
        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test
    void shouldThrowUserAlreadyException_whenEmailExists() {
        //given
        String message = "User with this email: " + EMAIL + " already exists";
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThatExceptionOfType(UserAlreadyException.class)
                .isThrownBy(() -> userService.validateEmailUniques(dto))
                .withMessageContaining(message);

        //when then
        verify(userRepository, times(1)).findByEmail(EMAIL);
    }

    @Test
    void shouldThrowInvalidNationalNumberException_whenNationalNumberIsInCorrect() {
        //given
        String message = "National number is incorrect";
        when(nationalNumberValidator.passed(dto)).thenReturn(false);

        assertThatExceptionOfType(InvalidNationalNumberExceptions.class)
                .isThrownBy(() -> userService.validateNationalNumber(dto))
                .withMessageContaining(message);

        //when then
        verify(nationalNumberValidator, times(1)).passed(dto);
    }

    @Test
    void shouldReturnPersonalData_whenUserExists(){
        //given
        when(userRepository.findByIdOrThrowWhenIdNotExists(USER_ID)).thenReturn(user);

        //when
        UserResponseDto userResponse = userService.getPersonalData(USER_ID);

        //then
        assertThat(user.getId()).isEqualTo(USER_ID);
        verify(userRepository, times(1)).findByIdOrThrowWhenIdNotExists(USER_ID);
    }

    @Test
    void shouldThrowUserNotFoundException_whenUserNotExist(){
        //given
        Long userId = 999L;
        String message = "User with id: " + userId + " not found";
        when(userRepository.findByIdOrThrowWhenIdNotExists(userId))
                .thenThrow(new UserNotFoundException(message, HttpStatus.NOT_FOUND));

        //when
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.getPersonalData(userId))
                .withMessageContaining(message);

        //then
        verify(userRepository, times(1)).findByIdOrThrowWhenIdNotExists(userId);
    }
}
