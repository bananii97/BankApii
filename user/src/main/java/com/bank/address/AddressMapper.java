package com.bank.address;

import com.bank.address.dto.AddressRequestDto;
import com.bank.address.dto.AddressResponseDto;
import com.bank.user.jpa.model.FieldChangeRequest;
import lombok.experimental.UtilityClass;

import java.util.List;

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

    public List<FieldChangeRequest> getChange(Address address, AddressRequestDto dto) {
        if(dto == null){

        }
        return List.of(
                new FieldChangeRequest("City ", address.getCity(), dto.city()),
                new FieldChangeRequest("Posal code ", address.getPostalCode(), dto.postalCode()),
                new FieldChangeRequest("Street ", address.getStreet(), address.getStreet()),
                new FieldChangeRequest("Apartment number ", address.getApartmentNumber(), dto.apartmentNumber())
        );
    }
}
