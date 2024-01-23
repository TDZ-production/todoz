package com.example.todoz.models;

import com.example.todoz.prtoken.PasswordResetToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private Integer pussyMeter = 1;
    @ManyToMany
    private Set<Role> roles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Week> week;
    @OneToMany(mappedBy = "user")
    List<Task> tasks;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<PasswordResetToken> pRToken;
    public static final int MINIMAL_PASSWORD_LENGTH = 5;

    public User(String username, String password, Integer pussyMeter) {
        this.username = username;
        this.password = password;
        this.pussyMeter = pussyMeter;
    }
}
