package com.bank.exceptions;

public class EmailNotFoundExceptions extends RuntimeException {
    public EmailNotFoundExceptions(String message) {
        super(message);
    }
}
