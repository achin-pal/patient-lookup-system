package com.patientlookup.app.controller;

import com.patientlookup.app.dto.PatientRequest;
import com.patientlookup.app.dto.PatientResponse;
import com.patientlookup.app.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all patients or search by name")
    public Page<PatientResponse> getPatients(
            @RequestParam(required = false) String name,
            @ParameterObject
            @PageableDefault(size = 5, sort = "patientId")
            Pageable pageable) {

        return service.findAll(name, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a patient by patient ID")
    public PatientResponse getPatient(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a patient")
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody PatientRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a patient")
    public PatientResponse updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {

        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}