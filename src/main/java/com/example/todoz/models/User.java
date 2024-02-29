package com.example.todoz.models;

import com.example.todoz.prtoken.PasswordResetToken;
import com.example.todoz.services.DateManager;
import com.example.todoz.services.Language;
import com.example.todoz.services.MessageManager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OrderBy("id DESC")
    private List<Week> weeks;
    @OneToMany(mappedBy = "user")
    List<Task> tasks = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<PasswordResetToken> pRTokens;
    @OneToMany(mappedBy= "user")
    private List<UserSubscription> userSubscription;
    public static final int MINIMAL_PASSWORD_LENGTH = 5;

    public String getText(String key) {
        return MessageManager.getString(key, Language.ENG, pussyMeter);
    }

    public User(String username, String password, Integer pussyMeter) {
        this();
        this.username = username;
        this.password = password;
        this.pussyMeter = pussyMeter;
    }

    public User() {
        this.createdAt = DateManager.now();
    }
}
