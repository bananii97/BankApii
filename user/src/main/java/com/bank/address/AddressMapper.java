package com.bank.address;

import com.bank.address.dto.AddressRequestDto;
import com.bank.address.dto.AddressResponseDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AddressMapper {

    public static Address fromRequest(AddressRequestDto dto) {
        return Address.builder()
                .city(dto.city())
                .postalCode(dto.postalCode())
                .street(dto.street())
                .apartmentNumber(dto.apartmentNumber())
                .build();
    }

    public static AddressResponseDto toResponse(Address address) {
        return AddressResponseDto.builder()
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .street(address.getStreet())
                .apartmentNumber(address.getApartmentNumber())
                .updatedAt(address.getUpdatedAt())
                .build();
    }
}
