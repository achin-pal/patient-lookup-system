package com.patientlookup.app.dto;

public record SignupRequest(
        String username,
        String password,
        String role
) {
}