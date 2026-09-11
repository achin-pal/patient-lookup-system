package com.patientlookup.app.dto;

import java.time.LocalDate;

public record PatientResponse(
        Long patientId,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String email,
        String phone,
        String address
) {}
