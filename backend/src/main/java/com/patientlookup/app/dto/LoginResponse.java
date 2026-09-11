package com.patientlookup.app.dto;

public record LoginResponse(
        String token,
        String username,
        String role
) {}