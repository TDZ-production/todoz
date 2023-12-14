package com.example.todoz.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    String title;
    String description;
    Long lastingDays;

    public Notification(String title, String description) {
        this.title = title;
        this.description = description;
    }


    public Notification() {

    }
}
