package com.example.todoz.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private Integer priority = 4;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private boolean checked;
    @ManyToOne
    private TodoWeek todoWeek;

    public Task(){
        this.createdAt = LocalDateTime.now();
    }
}
