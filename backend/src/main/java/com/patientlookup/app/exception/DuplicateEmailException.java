package com.patientlookup.app.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Email is already in use");
    }
}