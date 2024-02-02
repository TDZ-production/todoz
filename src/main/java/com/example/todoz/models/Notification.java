package com.example.todoz.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Long remainingDays;

    public Notification(Long remainingDays, String title, String description) {
        this.remainingDays = remainingDays;
        this.title = title;
        this.description = description;
    }
}
