package com.example.todoz.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue
    private Long id;
    private String userName;
    private String password;
    private Integer pussyMeter;
    private Role role = Role.USER;
    @OneToOne (cascade = CascadeType.ALL, mappedBy = "appUser")
    private TodoWeek todoWeek;
}
