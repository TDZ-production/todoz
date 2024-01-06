package com.example.todoz.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
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

    public Integer getDueDateWeek() {
        if (dueDate != null) {
            return DateManager.formatWeek(this.dueDate);
        } else {
            return null;
        }
    }

    public String getDueDateFormat() {
        if (dueDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return dueDate.format(formatter);
        } else {
            return null;
        }
    }

    public String getDueDateDayOfWeek() {
        return this.dueDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    public String getRemainingDays() {
        if (this.getDueDate() == null) {
            return null;
        } else {
            Duration duration = Duration.between(LocalDateTime.now(), this.getDueDate());
            if (duration.toDays() == 1) {
                return "Tomorrow";
            }
            else if (duration.toDays() == 0) {
                return "Today";
            }
            else if (duration.toDays() == -1) {
                return "Yesterday";
            }
            else if (duration.toDays() < -1) {
                return Math.abs(duration.toDays()) + " days ago";
            }
            else{
                return duration.toDays() + " days";
            }
        }
    }

}
