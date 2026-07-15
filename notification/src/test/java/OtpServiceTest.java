import com.bank.email.EmailSender;
import com.bank.exceptions.EmailIncorrectException;
import com.bank.jpa.OneTimePassword;
import com.bank.jpa.OtpRepository;
import com.bank.service.OtpService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OtpServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private SecureRandom  secureRandom;

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private EmailSender  emailSender;

    @Captor
    private ArgumentCaptor<OneTimePassword> otpCaptor;

    @InjectMocks
    private OtpService otpService;

    private static final String EMAIL = "test@gmail.com";

    @Test
    void shouldGenerateAndSendOtp_WhenEmailIsProvided() {
        //given
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiryAt = createdAt.now().plusMinutes(15);
        when(secureRandom.nextInt(900000)).thenReturn(123123);
        when(otpRepository.save(any(OneTimePassword.class))).thenAnswer(args -> {
            OneTimePassword otp = args.getArgument(0);
            return otp;
        });

        //when
        otpService.generateAndSendOtp(EMAIL);

        //then
        verify(otpRepository).save(otpCaptor.capture());
        OneTimePassword otp = otpCaptor.getValue();
        assertThat(otp.getCode()).isEqualTo("223123");
        assertThat(otp.getEmail()).isEqualTo(EMAIL);
        assertThat(otp.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(otp.getExpiresAt()).isAfter(expiryAt);
        verify(emailSender).sendMail(EMAIL,anyString(),"223123");
    }

    @ParameterizedTest
    @CsvSource( value = {
            "null, "
    },
            nullValues = {"null"})
    void shouldThrowEmailIncorrectException_whenEmailIsNullOrEmpty() {
        //given
        String email = null;
        String message = "Email is null or empty";

        //when then
        assertThatExceptionOfType(EmailIncorrectException.class)
                .isThrownBy(() -> otpService.generateAndSendOtp(email))
                .withMessageContaining(message);

        verifyNoInteractions(otpRepository);
        verifyNoInteractions(emailSender);
    }
}
