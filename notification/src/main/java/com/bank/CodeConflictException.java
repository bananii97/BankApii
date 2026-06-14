package com.bank;

import com.bank.handler.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class CodeConflictException extends BaseBusinessException {
    public CodeConflictException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
