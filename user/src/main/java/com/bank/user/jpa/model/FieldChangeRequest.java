package com.bank.user.jpa.model;

public record FieldChangeRequest(
        String fieldName,
        String oldValue,
        String newValue
) {
}
