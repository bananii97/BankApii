package com.bank.jpa;

import com.bank.enums.OtpType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "one_time_password")
@Entity
public class OneTimePassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String email;

    @Enumerated(EnumType.STRING)
    private OtpType otpType;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    private boolean used = false;

    @Builder
    private OneTimePassword(String code, String email, OtpType otpType,
                            LocalDateTime createdAt,
                            LocalDateTime expiresAt) {
        this.code = code;
        this.email = email;
        this.otpType = otpType;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
