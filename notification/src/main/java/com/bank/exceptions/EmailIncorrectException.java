package com.bank.exceptions;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class EmailIncorrectException extends BaseBusinessException {
    public EmailIncorrectException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
