package com.patientlookup.app.dto;

public record LoginRequest(
        String username,
        String password
) {}