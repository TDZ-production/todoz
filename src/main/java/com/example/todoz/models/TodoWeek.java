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
public class TodoWeek {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private AppUser appUser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "todoWeek")
    private List<Task> tasks;
}
