package com.patientlookup.app;

import com.patientlookup.app.config.SecurityConfig;
import com.patientlookup.app.controller.AuthController;
import com.patientlookup.app.entity.AppUser;
import com.patientlookup.app.repository.UserRepository;
import com.patientlookup.app.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void signupCreatesEmployeeSuccessfully() throws Exception {

        when(userRepository.existsByUsername("john"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        when(userRepository.save(any(AppUser.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "username": "john",
                                          "password": "password123"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "User registered successfully"
                ));

        ArgumentCaptor<AppUser> userCaptor =
                ArgumentCaptor.forClass(AppUser.class);

        verify(userRepository)
                .save(userCaptor.capture());

        AppUser savedUser = userCaptor.getValue();

        assertEquals(
                "john",
                savedUser.getUsername()
        );

        assertEquals(
                "EMPLOYEE",
                savedUser.getRole()
        );

        assertEquals(
                "encoded-password",
                savedUser.getPassword()
        );

        assertNotEquals(
                "password123",
                savedUser.getPassword()
        );
    }

    @Test
    void duplicateUsernameReturnsBadRequest() throws Exception {

        when(userRepository.existsByUsername("john"))
                .thenReturn(true);

        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "username": "john",
                                          "password": "password123"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Username already exists"
                ));

        verify(userRepository, never())
                .save(any(AppUser.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }
}