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
public class Todo {

    @Id
    @OneToOne
    private User user;
    @OneToMany(mappedBy = "todo")
    private List<Task> tasks;
}
