package com.bank.exceptions;

import com.bank.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class UserAlreadyException extends BaseBusinessException {
    public UserAlreadyException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
