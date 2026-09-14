package com.patientlookup.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "patients",
        indexes = {
                @Index(
                        name = "idx_patients_last_name",
                        columnList = "last_name"
                ),
                @Index(
                        name = "idx_patients_first_name",
                        columnList = "first_name"
                )
        }
)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    private String lastName;

    @Column(
            name = "date_of_birth",
            nullable = false
    )
    private LocalDate dateOfBirth;

    @Column(
            name = "email",
            length = 200,
            unique = true
    )
    private String email;

    @Column(
            name = "phone",
            length = 40
    )
    private String phone;

    @Column(
            name = "address",
            length = 300
    )
    private String address;
}