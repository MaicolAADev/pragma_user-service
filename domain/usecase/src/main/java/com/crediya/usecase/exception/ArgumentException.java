package com.crediya.usecase.exception;

public class ArgumentException extends IllegalArgumentException {
    public ArgumentException(String message) {
        super(message);
    }
}
