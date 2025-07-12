package com.example.todoz.feedback;

import com.example.todoz.utility.DateManager;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue
    private Long id;

    private String description;
    private boolean resolved = false;
    private LocalDateTime createdAt;

    public Feedback(Feedback feedback){
        this.id = feedback.id;
        this.description = feedback.description;
        this.resolved = feedback.resolved;
        this.createdAt =  DateManager.now();
    }
}
