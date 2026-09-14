package com.patientlookup.app.service;

import com.patientlookup.app.dto.PatientRequest;
import com.patientlookup.app.dto.PatientResponse;
import com.patientlookup.app.entity.Patient;
import com.patientlookup.app.exception.DuplicateEmailException;
import com.patientlookup.app.exception.PatientNotFoundException;
import com.patientlookup.app.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientResponse> findAll(
            String name,
            Pageable pageable) {

        log.info(
                "Fetching patients - name={}, page={}, size={}",
                name,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Patient> patients =
                (name == null || name.isBlank())
                        ? repository.findAll(pageable)
                        : repository
                          .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                                  name,
                                  name,
                                  pageable
                          );

        log.debug(
                "Patient search completed - page={}, returned={}, totalElements={}",
                patients.getNumber(),
                patients.getNumberOfElements(),
                patients.getTotalElements()
        );

        return patients.map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse findById(Long id) {

        log.info("Fetching patient with id={}", id);

        Patient patient = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Patient not found with id={}", id);
                    return new PatientNotFoundException(id);
                });

        return toResponse(patient);
    }

    @Override
    @Transactional
    public PatientResponse create(PatientRequest request) {

        log.info("Creating new patient");

        Patient patient = new Patient();
        apply(patient, request);

        if (request.email() != null
                && !request.email().isBlank()
                && repository.existsByEmailIgnoreCase(request.email().trim())) {

            throw new DuplicateEmailException(request.email().trim());
        }

        Patient savedPatient = repository.save(patient);

        log.info(
                "Patient created successfully with id={}",
                savedPatient.getPatientId()
        );

        return toResponse(savedPatient);
    }

    @Override
    @Transactional
    public PatientResponse update(
            Long id,
            PatientRequest request) {

        log.info("Updating patient with id={}", id);

        Patient patient = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn(
                            "Cannot update patient. Patient with id={} was not found",
                            id
                    );

                    return new PatientNotFoundException(id);
                });

        apply(patient, request);

        Patient savedPatient = repository.save(patient);

        log.info(
                "Patient with id={} updated successfully",
                id
        );

        return toResponse(savedPatient);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        log.info("Deleting patient with id={}", id);

        if (!repository.existsById(id)) {
            log.warn(
                    "Cannot delete patient. Patient with id={} was not found",
                    id
            );

            throw new PatientNotFoundException(id);
        }

        repository.deleteById(id);

        log.info(
                "Patient with id={} deleted successfully",
                id
        );
    }

    private void apply(
            Patient patient,
            PatientRequest request) {

        patient.setFirstName(request.firstName().trim());
        patient.setLastName(request.lastName().trim());
        patient.setDateOfBirth(request.dateOfBirth());
        patient.setEmail(blankToNull(request.email()));
        patient.setPhone(blankToNull(request.phone()));
        patient.setAddress(blankToNull(request.address()));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank()
                ? null
                : value.trim();
    }

    private PatientResponse toResponse(Patient patient) {

        return new PatientResponse(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getAddress()
        );
    }
}