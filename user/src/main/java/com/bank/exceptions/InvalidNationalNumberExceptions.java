package com.bank.exceptions;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class InvalidNationalNumberExceptions extends BaseBusinessException {
    public InvalidNationalNumberExceptions(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
