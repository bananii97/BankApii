package com.bank.address.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AddressRequestDto(
        @NotNull
        @Size(min = 3, max = 50)
        String city,
        @NotNull
        @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Incorrect postal code")
        String postalCode,
        @NotNull
        @Size(min = 3, max = 50)
        String street,
        @NotNull
        @Size(min = 1, max = 50)
        String apartmentNumber
) {
}
