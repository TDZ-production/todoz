package com.example.todoz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private String password;
    private Integer pussyMeter;
    private Role role = Role.USER;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Week> week;
    @OneToMany(mappedBy = "user")
    List<Task> tasks;
}
