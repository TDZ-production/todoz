package com.example.todoz.models;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue
    private Long id;
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public PasswordResetToken(User user) {
        this.token = UUID.randomUUID().toString();
        this.user = user;
    }
}
