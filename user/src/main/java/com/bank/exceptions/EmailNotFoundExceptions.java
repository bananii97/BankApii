package com.bank.exceptions;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class EmailNotFoundExceptions extends BaseBusinessException {
    public EmailNotFoundExceptions(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
