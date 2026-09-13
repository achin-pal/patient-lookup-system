package com.patientlookup.app.controller;

import com.patientlookup.app.dto.UpdateRoleRequest;
import com.patientlookup.app.dto.UserResponse;
import com.patientlookup.app.entity.AppUser;
import com.patientlookup.app.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserRepository userRepository;

    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .toList();
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateRole(
            @PathVariable Long id,
            @RequestBody UpdateRoleRequest request) {

        AppUser user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        String role = request.role().toUpperCase();

        if (!role.equals("ADMIN") &&
                !role.equals("EMPLOYEE")) {
            return ResponseEntity.badRequest().build();
        }

        user.setRole(role);

        AppUser savedUser = userRepository.save(user);

        return ResponseEntity.ok(
                new UserResponse(
                        savedUser.getId(),
                        savedUser.getUsername(),
                        savedUser.getRole()
                )
        );
    }
}