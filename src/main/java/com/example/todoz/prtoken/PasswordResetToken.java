package com.example.todoz.prtoken;

import com.example.todoz.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue
    private Long id;
    private String token;
    @ManyToOne
    private User user;
    private LocalDateTime expirationTime;
    private static final int EXPIRATION_TIME = 5;

    public PasswordResetToken(User user) {
        this();
        this.user = user;
    }

    public PasswordResetToken() {
        this.token = UUID.randomUUID().toString();
        this.expirationTime = LocalDateTime.now().plusMinutes(EXPIRATION_TIME);
    }
}
