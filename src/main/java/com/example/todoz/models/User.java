package com.example.todoz.models;

import com.example.todoz.prtoken.PasswordResetToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
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
    private boolean notificationSingleTask = true;
    @ManyToMany
    private Set<Role> roles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OrderBy("id DESC")
    private List<Week> weeks;
    @OneToMany(mappedBy = "user")
    List<Task> tasks;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<PasswordResetToken> pRToken;
    @OneToMany(mappedBy= "user")
    private List<UserSubscription> userSubscription;
    public static final int MINIMAL_PASSWORD_LENGTH = 5;


//    public List<Task> getCurrentTasks() {
//        return getLastWeek().getSortedTasks();
//    }
//    public Week getLastWeek() {
//        return weeks.get(0);
//    }

//    public List<Task> getCurrentTasks() {
//        return List.of(new Task(...));
//    }
//
//
//    public Optional<Task> getTopTask() {
//        return getCurrentTasks().get(0);
//    }
    public User(String username, String password, Integer pussyMeter) {
        this.username = username;
        this.password = password;
        this.pussyMeter = pussyMeter;
    }
}
