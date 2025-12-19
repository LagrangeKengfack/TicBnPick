package com.polytechnique.ticbnpick.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Represents a base person in the system.
 *
 * This class is the parent abstraction for client and deliveryPerson.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("persons")
public class Person {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("last_name")
    private String last_name;

    @NotNull
    @Column("first_name")
    private String first_name;

    @NotNull
    @Column("phone")
    private String phone;

    @NotNull
    @Column("email")
    private String email;

    @NotNull
    @Column("password")
    private String password;

    @NotNull
    @Column("national_id")
    private String nationalId;

    @NotNull
    @Column("photo_card")
    private String photoCard;

    @Column("criminal_record")
    private String criminalRecord;

    @Column("rating")
    private Double rating;

    @Column("total_deliveries")
    private Integer totalDeliveries;
}