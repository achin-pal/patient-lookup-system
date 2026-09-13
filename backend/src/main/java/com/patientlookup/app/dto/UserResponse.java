package com.patientlookup.app.dto;

public record UserResponse(
        Long id,
        String username,
        String role
) {
}