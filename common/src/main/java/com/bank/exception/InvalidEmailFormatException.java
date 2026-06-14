package com.bank.exception;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class InvalidEmailFormatException extends BaseBusinessException {
    public InvalidEmailFormatException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
