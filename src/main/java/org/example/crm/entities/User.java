package org.example.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.crm.enums.UserRole;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="app_user")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String passwordHash;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User() {};

    public User(String email, UserRole role, String passwordHash, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.email = email;
        this.role = role;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    };
}
