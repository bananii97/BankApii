package com.bank.address.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AddressResponseDto(
        String city,
        String postalCode,
        String street,
        String apartmentNumber,
        LocalDateTime updatedAt
) {
}
