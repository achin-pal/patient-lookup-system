package com.patientlookup.app;

import com.patientlookup.app.config.SecurityConfig;
import com.patientlookup.app.controller.PatientController;
import com.patientlookup.app.dto.PatientRequest;
import com.patientlookup.app.dto.PatientResponse;
import com.patientlookup.app.exception.DuplicateEmailException;
import com.patientlookup.app.exception.GlobalExceptionHandler;
import com.patientlookup.app.exception.PatientNotFoundException;
import com.patientlookup.app.security.JwtService;
import com.patientlookup.app.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@Import({
        SecurityConfig.class,
        GlobalExceptionHandler.class
})
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPatientReturnsCreated() throws Exception {

        PatientResponse response = new PatientResponse(
                1L,
                "John",
                "Smith",
                LocalDate.of(1990, 1, 1),
                "john@example.com",
                "1234567890",
                "Toronto"
        );

        when(patientService.create(any(PatientRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/patients")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "John",
                                          "lastName": "Smith",
                                          "dateOfBirth": "1990-01-01",
                                          "email": "john@example.com",
                                          "phone": "1234567890",
                                          "address": "Toronto"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void invalidPatientRequestReturnsBadRequest() throws Exception {

        mockMvc.perform(
                        post("/api/patients")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "",
                                          "lastName": "",
                                          "dateOfBirth": null
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void duplicateEmailReturnsConflict() throws Exception {

        when(patientService.create(any(PatientRequest.class)))
                .thenThrow(
                        new DuplicateEmailException(
                                "john@example.com"
                        )
                );

        mockMvc.perform(
                        post("/api/patients")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "John",
                                          "lastName": "Smith",
                                          "dateOfBirth": "1990-01-01",
                                          "email": "john@example.com"
                                        }
                                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getPatientByIdReturnsPatient() throws Exception {

        PatientResponse response = new PatientResponse(
                1L,
                "John",
                "Smith",
                LocalDate.of(1990, 1, 1),
                "john@example.com",
                "1234567890",
                "Toronto"
        );

        when(patientService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/patients/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void missingPatientReturnsNotFound() throws Exception {

        when(patientService.findById(eq(99L)))
                .thenThrow(new PatientNotFoundException(99L));

        mockMvc.perform(
                        get("/api/patients/99")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}