package com.patientlookup.app;

import com.patientlookup.app.controller.PatientController;
import com.patientlookup.app.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockBean PatientService patientService;

    @Test
    @WithMockUser(roles = "USER")
    void userCannotCreatePatient() throws Exception {
        mockMvc.perform(post("/api/patients")
                        .contentType("application/json")
                        .content("{\"firstName\":\"A\",\"lastName\":\"B\",\"dateOfBirth\":\"1990-01-01\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanReachCreateEndpoint() throws Exception {
        mockMvc.perform(post("/api/patients")
                        .contentType("application/json")
                        .content("{\"firstName\":\"A\",\"lastName\":\"B\",\"dateOfBirth\":\"1990-01-01\"}"))
                .andExpect(status().isCreated());
    }
}
