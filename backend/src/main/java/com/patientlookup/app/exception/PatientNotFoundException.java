package com.patientlookup.app.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(Long patientId) {
        super("Patient not found: " + patientId);
    }
}
