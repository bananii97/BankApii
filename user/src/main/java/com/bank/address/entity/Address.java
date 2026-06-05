package com.bank.address.entity;

import com.bank.base.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.misc.NotNull;


@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Table(name = "address")
@Entity
public class Address extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String city;

    @NotNull
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Postal code must be format XX-XXX, np. 30-001")
    private String postalCode;

    @NotNull
    @Size(min = 3, max = 50)
    private String street;

    @NotNull
    @Size(min = 1, max = 50)
    private String apartmentNumber;
}
