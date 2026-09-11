package com.patientlookup.app.service;

import com.patientlookup.app.dto.PatientRequest;
import com.patientlookup.app.dto.PatientResponse;

import java.util.List;

public interface PatientService {

    List<PatientResponse> findAll(String name);

    PatientResponse findById(Long id);

    PatientResponse create(PatientRequest request);

    PatientResponse update(Long id, PatientRequest request);

    void delete(Long id);
}