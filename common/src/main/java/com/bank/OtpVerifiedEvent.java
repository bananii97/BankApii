package com.bank;

import java.io.Serializable;

public record OtpVerifiedEvent(String email) implements Serializable {
}
