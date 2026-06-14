package com.bank.exceptions;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class CodeExpiredException extends BaseBusinessException {
    public CodeExpiredException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
