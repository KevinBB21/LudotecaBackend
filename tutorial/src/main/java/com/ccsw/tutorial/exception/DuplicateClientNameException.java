package com.ccsw.tutorial.exception;

public class DuplicateClientNameException extends RuntimeException {
    public DuplicateClientNameException(String message) {
        super(message);
    }

}