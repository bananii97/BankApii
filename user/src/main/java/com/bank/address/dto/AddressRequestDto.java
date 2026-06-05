package com.bank.address.dto;

public record AddressRequestDto(
        String city,
        String postalCode,
        String street,
        String apartmentNumber
) {
}
