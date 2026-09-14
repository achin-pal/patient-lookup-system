package com.patientlookup.app.service;

import com.patientlookup.app.dto.PatientRequest;
import com.patientlookup.app.dto.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {

    Page<PatientResponse> findAll(String name, Pageable pageable);

    PatientResponse findById(Long id);

    PatientResponse create(PatientRequest request);

    PatientResponse update(Long id, PatientRequest request);

    void delete(Long id);
}