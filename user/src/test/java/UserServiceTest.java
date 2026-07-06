import com.bank.UserRegisteredEvent;
import com.bank.address.Address;
import com.bank.address.dto.AddressRequestDto;
import com.bank.identity_card.IdentityCard;
import com.bank.identity_card.dto.IdentityCardRequestDto;
import com.bank.rabbit.RegistrationResponseDto;
import com.bank.user.enums.Gender;
import com.bank.user.enums.UserStatus;
import com.bank.user.jpa.dto.UserRequestDto;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
                .validUtil(LocalDate.of(2028,1,1))
                .build();

        user = User.builder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .nationalNumber(NATIONAL_NUMBER)
                .birthDate(BIRTH_DATE)
                .phoneNumber(PHONE_NUMBER)
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
    void shouldRegistryUserAndReturnResponseDto_whenAllValuesAreProvided(){
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
        User createdUser =  argumentCaptor.getValue();
        verify(nationalNumberValidator, times(1)).passed(dto);
        verify(userRepository, times(1)).findByEmail(EMAIL);

        assertThat(createdUser.getEmail()).isEqualTo(EMAIL);
        assertThat(createdUser.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(createdUser.getLastName()).isEqualTo(LAST_NAME);

        assertThat(responseDto.email()).isEqualTo(EMAIL);
        assertThat(responseDto.message()).isEqualTo(MESSAGE);
        assertThat(responseDto.status()).isEqualTo(USER_STATUS);
    }
}
