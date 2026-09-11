package com.patientlookup.app.controller;

import com.patientlookup.app.dto.PatientRequest;
import com.patientlookup.app.dto.PatientResponse;
import com.patientlookup.app.service.PatientService;
import com.patientlookup.app.service.PatientServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientServiceImpl serviceImpl;

    public PatientController(PatientServiceImpl serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    @GetMapping
    @Operation(summary = "Get all patients or search by name")
    public List<PatientResponse> getPatients(@RequestParam(required = false) String name) {
        return serviceImpl.findAll(name);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a patient by patient ID")
    public PatientResponse getPatient(@PathVariable Long id) {
        return serviceImpl.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a patient")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceImpl.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a patient")
    public PatientResponse updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return serviceImpl.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        serviceImpl.delete(id);
        return ResponseEntity.noContent().build();
    }
}
