package com.bank.identity_card;

import com.bank.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Table(name = "identity_card")
@Entity
public class IdentityCard extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identityCardNumber;
    private String issuePlace;
    private String issuingAuthority;
    private LocalDate validUtil;
}
