package com.example.todoz.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private Integer priority;
    private LocalDate createdAt;
    private LocalDate dueDate;
    private boolean done;
    @ManyToOne
    private Week week;
    @ManyToOne
    private User user;

    public Task() {
        this.createdAt = LocalDate.now();
    }
}
