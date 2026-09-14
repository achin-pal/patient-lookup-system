package com.patientlookup.app;

import com.patientlookup.app.dto.PatientRequest;
import com.patientlookup.app.entity.Patient;
import com.patientlookup.app.exception.DuplicateEmailException;
import com.patientlookup.app.exception.PatientNotFoundException;
import com.patientlookup.app.repository.PatientRepository;
import com.patientlookup.app.service.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    private PatientRepository repository;
    private PatientServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PatientRepository.class);
        service = new PatientServiceImpl(repository);
    }

    @Test
    void createPatientSuccessfully() {
        PatientRequest request = new PatientRequest(
                "John",
                "Smith",
                LocalDate.of(1990, 1, 1),
                "john@example.com",
                "1234567890",
                "Toronto"
        );

        when(repository.existsByEmailIgnoreCase("john@example.com"))
                .thenReturn(false);

        when(repository.save(any(Patient.class)))
                .thenAnswer(invocation -> {
                    Patient patient = invocation.getArgument(0);
                    patient.setPatientId(1L);
                    return patient;
                });

        var response = service.create(request);

        assertNotNull(response);
        assertEquals(1L, response.patientId());
        assertEquals("John", response.firstName());
        assertEquals("Smith", response.lastName());

        verify(repository).save(any(Patient.class));
    }

    @Test
    void duplicateEmailThrowsException() {
        PatientRequest request = new PatientRequest(
                "John",
                "Smith",
                LocalDate.of(1990, 1, 1),
                "john@example.com",
                null,
                null
        );

        when(repository.existsByEmailIgnoreCase("john@example.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateEmailException.class,
                () -> service.create(request)
        );

        verify(repository, never())
                .save(any(Patient.class));
    }

    @Test
    void findPatientByIdSuccessfully() {
        Patient patient = createPatientEntity();

        when(repository.findById(1L))
                .thenReturn(Optional.of(patient));

        var response = service.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.patientId());
        assertEquals("John", response.firstName());
        assertEquals("Smith", response.lastName());

        verify(repository).findById(1L);
    }

    @Test
    void findMissingPatientThrowsException() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                PatientNotFoundException.class,
                () -> service.findById(99L)
        );
    }

    @Test
    void updatePatientSuccessfully() {
        Patient existingPatient = createPatientEntity();

        PatientRequest request = new PatientRequest(
                "Johnny",
                "Smith",
                LocalDate.of(1990, 1, 1),
                "johnny@example.com",
                "9999999999",
                "Ottawa"
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingPatient));

        when(repository.save(any(Patient.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        var response = service.update(1L, request);

        assertEquals("Johnny", response.firstName());
        assertEquals("johnny@example.com", response.email());
        assertEquals("Ottawa", response.address());

        verify(repository).save(existingPatient);
    }

    @Test
    void updateMissingPatientThrowsException() {
        PatientRequest request = new PatientRequest(
                "John",
                "Smith",
                LocalDate.of(1990, 1, 1),
                "john@example.com",
                null,
                null
        );

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                PatientNotFoundException.class,
                () -> service.update(99L, request)
        );

        verify(repository, never())
                .save(any(Patient.class));
    }

    @Test
    void deletePatientSuccessfully() {
        when(repository.existsById(1L))
                .thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteMissingPatientThrowsException() {
        when(repository.existsById(99L))
                .thenReturn(false);

        assertThrows(
                PatientNotFoundException.class,
                () -> service.delete(99L)
        );

        verify(repository, never())
                .deleteById(anyLong());
    }

    private Patient createPatientEntity() {
        Patient patient = new Patient();

        patient.setPatientId(1L);
        patient.setFirstName("John");
        patient.setLastName("Smith");
        patient.setDateOfBirth(
                LocalDate.of(1990, 1, 1)
        );
        patient.setEmail("john@example.com");
        patient.setPhone("1234567890");
        patient.setAddress("Toronto");

        return patient;
    }
}