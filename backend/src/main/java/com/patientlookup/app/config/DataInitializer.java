package com.patientlookup.app.config;

import com.patientlookup.app.entity.AppUser;
import com.patientlookup.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByUsername("admin")) {

                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );
                admin.setRole("ADMIN");

                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("employee")) {

                AppUser employee = new AppUser();
                employee.setUsername("employee");
                employee.setPassword(
                        passwordEncoder.encode("employee123")
                );
                employee.setRole("EMPLOYEE");

                userRepository.save(employee);
            }
        };
    }
}