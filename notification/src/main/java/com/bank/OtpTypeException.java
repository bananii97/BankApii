package com.bank;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class OtpTypeException extends BaseBusinessException {
    public OtpTypeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
