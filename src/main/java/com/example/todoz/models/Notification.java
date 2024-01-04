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
    private String type;
    private Integer pussyMeter;

    public Notification(String title, String type, Integer pussyMeter) {
        this.title = title;
        this.type = type;
        this.pussyMeter = pussyMeter;
    }
}
