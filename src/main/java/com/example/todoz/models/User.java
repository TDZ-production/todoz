package com.example.todoz.models;

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
    private boolean morningNotificationSingleTask = true;
    @ManyToMany
    private Set<Role> roles;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Week> week;
    @OneToMany(mappedBy = "user")
    List<Task> tasks;

    @OneToMany(mappedBy= "user")
    private List<UserSubscription> userSubscription;
}
