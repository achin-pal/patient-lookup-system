package com.patientlookup.app.controller;

import com.patientlookup.app.dto.UpdateRoleRequest;
import com.patientlookup.app.dto.UserResponse;
import com.patientlookup.app.entity.AppUser;
import com.patientlookup.app.exception.UserNotFoundException;
import com.patientlookup.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

@Slf4j
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

        log.info("Admin requested application user list");

        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .toList();

        log.debug(
                "Returning {} application users",
                users.size()
        );

        return users;
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateRole(
            @PathVariable Long id,
            @RequestBody UpdateRoleRequest request,
            Authentication authentication) {

        log.info(
                "Role change requested for userId={}, requestedRole={}",
                id,
                request.role()
        );

        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (user.getUsername().equals(authentication.getName())) {

            log.warn(
                    "User {} attempted to change their own access level",
                    authentication.getName()
            );

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        if (request.role() == null || request.role().isBlank()) {

            log.warn(
                    "Role change rejected for userId={} because role was empty",
                    id
            );

            return ResponseEntity
                    .badRequest()
                    .build();
        }

        String role = request.role()
                .trim()
                .toUpperCase();

        if (!role.equals("ADMIN")
                && !role.equals("EMPLOYEE")) {

            log.warn(
                    "Invalid role change requested for userId={}, role={}",
                    id,
                    role
            );

            return ResponseEntity
                    .badRequest()
                    .build();
        }

        String previousRole = user.getRole();

        user.setRole(role);

        AppUser savedUser = userRepository.save(user);

        log.info(
                "User role updated successfully userId={}, previousRole={}, newRole={}",
                id,
                previousRole,
                role
        );

        return ResponseEntity.ok(
                new UserResponse(
                        savedUser.getId(),
                        savedUser.getUsername(),
                        savedUser.getRole()
                )
        );
    }
}