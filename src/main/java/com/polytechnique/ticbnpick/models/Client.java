package com.polytechnique.ticbnpick.models;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Represents a client in the system.
 * A client is a person who can post delivery announcements.
 *
 * @author Kengfack Lagrange
 * @date 17/12/2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table("clients")
public class Client {

    @Id
    @Column("id")
    private UUID id;

    @NotNull
    @Column("person_id")
    private UUID person_id;

    @NotNull
    @Column("loyalty_status")
    private String loyalty_status;
}