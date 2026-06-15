package com.bank.exceptions;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class InvalidOtpException extends BaseBusinessException {
    public InvalidOtpException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
