package com.patientlookup.app;

import com.patientlookup.app.config.SecurityConfig;
import com.patientlookup.app.controller.PatientController;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
@Import(SecurityConfig.class)
class PatientControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void employeeCannotCreatePatient() throws Exception {

        mockMvc.perform(
                        post("/api/patients")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "A",
                                          "lastName": "B",
                                          "dateOfBirth": "1990-01-01"
                                        }
                                        """)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanReachCreateEndpoint() throws Exception {

        mockMvc.perform(
                        post("/api/patients")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "A",
                                          "lastName": "B",
                                          "dateOfBirth": "1990-01-01"
                                        }
                                        """)
                )
                .andExpect(status().isCreated());
    }
}