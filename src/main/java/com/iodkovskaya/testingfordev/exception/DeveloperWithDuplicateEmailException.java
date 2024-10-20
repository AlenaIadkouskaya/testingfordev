package com.iodkovskaya.testingfordev.exception;

public class DeveloperWithDuplicateEmailException extends RuntimeException {
    public DeveloperWithDuplicateEmailException(String message) {
        super(message);
    }
}
