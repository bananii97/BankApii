package com.bank;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class OtpNotFoundException extends BaseBusinessException {
    public OtpNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
