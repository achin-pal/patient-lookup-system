package com.patientlookup.app.controller;

import com.patientlookup.app.dto.LoginRequest;
import com.patientlookup.app.dto.LoginResponse;
import com.patientlookup.app.dto.SignupRequest;
import com.patientlookup.app.entity.AppUser;
import com.patientlookup.app.repository.UserRepository;
import com.patientlookup.app.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {

        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        log.info(
                "Login attempt for username={}",
                request.username()
        );

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        authentication.getName()
                );

        String token =
                jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("")
                .replace("ROLE_", "");

        log.info(
                "Login successful for username={}, role={}",
                userDetails.getUsername(),
                role
        );

        return new LoginResponse(
                token,
                userDetails.getUsername(),
                role
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignupRequest request) {

        log.info(
                "Signup request for username={}",
                request.username()
        );

        if (userRepository.existsByUsername(request.username())) {

            log.warn(
                    "Signup rejected because username={} already exists",
                    request.username()
            );

            return ResponseEntity
                    .badRequest()
                    .body("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setRole("EMPLOYEE");

        userRepository.save(user);

        log.info(
                "User registered successfully username={}, role=EMPLOYEE",
                request.username()
        );

        return ResponseEntity
                .ok("User registered successfully");
    }
}