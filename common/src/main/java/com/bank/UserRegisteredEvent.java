package com.bank;

import java.io.Serializable;

public record UserRegisteredEvent(String email) implements Serializable {
}
