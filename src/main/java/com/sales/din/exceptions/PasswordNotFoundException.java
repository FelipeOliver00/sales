package com.sales.din.exceptions;

public class PasswordNotFoundException extends RuntimeException{

    public PasswordNotFoundException(String message) {
        super(message);
    }
}
