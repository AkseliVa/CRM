package org.example.crm.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.crm.enums.UserRole;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String passwordHash;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    public User() {};

    public User(String email, UserRole role, String passwordHash, LocalDateTime created_at, LocalDateTime updated_at) {
        this.email = email;
        this.role = role;
        this.passwordHash = passwordHash;
        this.created_at = created_at;
        this.updated_at = updated_at;
    };
}
