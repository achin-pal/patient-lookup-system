package com.patientlookup.app;

import com.patientlookup.app.config.SecurityConfig;
import com.patientlookup.app.controller.AdminUserController;
import com.patientlookup.app.exception.GlobalExceptionHandler;
import com.patientlookup.app.repository.UserRepository;
import com.patientlookup.app.security.JwtService;
import com.patientlookup.app.entity.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminUserController.class)
@Import({
        SecurityConfig.class,
        GlobalExceptionHandler.class
})
class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(
            username = "admin",
            roles = "ADMIN"
    )
    void adminCanViewUsers() throws Exception {

        AppUser admin = createUser(
                1L,
                "admin",
                "ADMIN"
        );

        AppUser employee = createUser(
                2L,
                "employee",
                "EMPLOYEE"
        );

        when(userRepository.findAll())
                .thenReturn(
                        List.of(admin, employee)
                );

        mockMvc.perform(
                        get("/api/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("employee"))
                .andExpect(jsonPath("$[1].role").value("EMPLOYEE"));
    }

    @Test
    @WithMockUser(
            username = "employee",
            roles = "EMPLOYEE"
    )
    void employeeCannotViewUsers() throws Exception {

        mockMvc.perform(
                        get("/api/users")
                )
                .andExpect(status().isForbidden());

        verify(userRepository, never())
                .findAll();
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = "ADMIN"
    )
    void adminCanChangeAnotherUsersRole() throws Exception {

        AppUser employee = createUser(
                2L,
                "employee",
                "EMPLOYEE"
        );

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(employee));

        when(userRepository.save(any(AppUser.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        mockMvc.perform(
                        put("/api/users/2/role")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "role": "ADMIN"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("employee"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userRepository)
                .save(employee);
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = "ADMIN"
    )
    void adminCannotChangeOwnRole() throws Exception {

        AppUser admin = createUser(
                1L,
                "admin",
                "ADMIN"
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(admin));

        mockMvc.perform(
                        put("/api/users/1/role")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "role": "EMPLOYEE"
                                        }
                                        """)
                )
                .andExpect(status().isForbidden());

        verify(userRepository, never())
                .save(any(AppUser.class));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = "ADMIN"
    )
    void invalidRoleReturnsBadRequest() throws Exception {

        AppUser employee = createUser(
                2L,
                "employee",
                "EMPLOYEE"
        );

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(employee));

        mockMvc.perform(
                        put("/api/users/2/role")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "role": "SUPERUSER"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        verify(userRepository, never())
                .save(any(AppUser.class));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = "ADMIN"
    )
    void missingUserReturnsNotFound() throws Exception {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        put("/api/users/99/role")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "role": "ADMIN"
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(userRepository, never())
                .save(any(AppUser.class));
    }

    private AppUser createUser(
            Long id,
            String username,
            String role) {

        AppUser user = new AppUser();

        user.setId(id);
        user.setUsername(username);
        user.setPassword("encoded-password");
        user.setRole(role);

        return user;
    }
}