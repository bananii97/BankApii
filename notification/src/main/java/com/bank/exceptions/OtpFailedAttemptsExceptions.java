package com.bank.exceptions;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class OtpFailedAttemptsExceptions extends BaseBusinessException {
    public OtpFailedAttemptsExceptions(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
