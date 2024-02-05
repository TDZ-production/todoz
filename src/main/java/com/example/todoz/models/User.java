package com.example.todoz.models;

import com.example.todoz.prtoken.PasswordResetToken;
import com.example.todoz.services.DateManager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private Integer pussyMeter = 1;
    private LocalDate createdAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Week> weeks;
    @OneToMany(mappedBy = "user")
    List<Task> tasks;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<PasswordResetToken> pRTokens;
    public static final int MINIMAL_PASSWORD_LENGTH = 5;

    public User(String username, String password, Integer pussyMeter) {
        this();
        this.username = username;
        this.password = password;
        this.pussyMeter = pussyMeter;
    }

    public User() {
        this.createdAt = DateManager.now().toLocalDate();
    }
}
