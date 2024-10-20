package com.iodkovskaya.testingfordev.exception;

public class DeveloperNotFoundException extends RuntimeException{
    public DeveloperNotFoundException(String message) {
        super(message);
    }
}
