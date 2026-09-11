package com.patientlookup.app.service;

import com.patientlookup.app.dto.PatientRequest;
import com.patientlookup.app.dto.PatientResponse;
import com.patientlookup.app.entity.Patient;
import com.patientlookup.app.exception.PatientNotFoundException;
import com.patientlookup.app.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> findAll(String name) {
        List<Patient> patients = (name == null || name.isBlank())
                ? repository.findAll()
                : repository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return patients.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PatientResponse findById(Long id) {
        return toResponse(repository.findById(id).orElseThrow(() -> new PatientNotFoundException(id)));
    }

    public PatientResponse create(PatientRequest request) {
        Patient p = new Patient();
        apply(p, request);
        return toResponse(repository.save(p));
    }

    public PatientResponse update(Long id, PatientRequest request) {
        Patient p = repository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
        apply(p, request);
        return toResponse(repository.save(p));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new PatientNotFoundException(id);
        repository.deleteById(id);
    }

    private void apply(Patient p, PatientRequest r) {
        p.setFirstName(r.firstName().trim());
        p.setLastName(r.lastName().trim());
        p.setDateOfBirth(r.dateOfBirth());
        p.setEmail(blankToNull(r.email()));
        p.setPhone(blankToNull(r.phone()));
        p.setAddress(blankToNull(r.address()));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private PatientResponse toResponse(Patient p) {
        return new PatientResponse(p.getPatientId(), p.getFirstName(), p.getLastName(), p.getDateOfBirth(),
                p.getEmail(), p.getPhone(), p.getAddress());
    }
}