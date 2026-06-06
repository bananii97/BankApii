package com.bank.user.jpa;

import com.bank.address.Address;
import com.bank.base.AuditableEntity;
import com.bank.identity_card.IdentityCard;
import com.bank.user.enums.Gender;
import com.bank.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "person")
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String nationalNumber;
    private LocalDate birthDate;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id", unique = true)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "identity_card", referencedColumnName = "id", unique = true)
    private IdentityCard identityCard;
}
