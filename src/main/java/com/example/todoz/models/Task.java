package com.example.todoz.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private boolean done;
    @ManyToOne
    private Week week;
    @ManyToOne
    private User user;

    public Task() {
        this.createdAt = LocalDateTime.now();
    }

    public Integer getDueDateWeek(){
        return this.dueDate.get(WeekFields.SUNDAY_START.weekOfWeekBasedYear());
    }

    public Object getLastingDays() {
        if (this.getDueDate() == null) {
            return null;
        } else {
            Duration duration = Duration.between(this.getCreatedAt(),this.getDueDate());
            return duration.toDays();
        }
    }

}
