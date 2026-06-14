package com.bank;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class OtpUsedException extends BaseBusinessException {
    public OtpUsedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
